package xyz.przemyk.simpleplanes.client;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import com.mojang.math.Axis;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Quaternionf;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.misc.MathUtil;
import xyz.przemyk.simpleplanes.network.*;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {

    public static KeyMapping moveHeliUpKey;
    public static KeyMapping openPlaneInventoryKey;
    public static KeyMapping dropPayloadKey;
    public static KeyMapping throttleUp;
    public static KeyMapping throttleDown;
    public static KeyMapping pitchUp;
    public static KeyMapping pitchDown;
    public static KeyMapping yawRight;
    public static KeyMapping yawLeft;

    // TODO: RenderLivingEvent no longer exposes getEntity() in NeoForge 26.x - passenger rotation not implemented

    private static boolean oldMoveHeliUpState = false;
    private static boolean oldPitchUpState = false;
    private static boolean oldPitchDownState = false;
    private static boolean oldYawRightState = false;
    private static boolean oldYawLeftState = false;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientPlayerTick(PlayerTickEvent.Post event) {
        final Player player = event.getEntity();
        if (player instanceof LocalPlayer) {
            if (player.getVehicle() instanceof PlaneEntity planeEntity) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.options.getCameraType() != CameraType.FIRST_PERSON) {
                    planeEntity.applyYawToEntity(player);
}
                if (mc.screen == null && mc.getOverlay() == null && openPlaneInventoryKey.consumeClick()) {
                    ClientPacketDistributor.sendToServer(new OpenPlaneInventoryPacket());
} else if (dropPayloadKey.consumeClick()) {
                    planeEntity.dropPayload();
}
                if (throttleUp.consumeClick()) {
                    ClientPacketDistributor.sendToServer(new ChangeThrottlePacket(ChangeThrottlePacket.Direction.UP));
} else if (throttleDown.consumeClick()) {
                    ClientPacketDistributor.sendToServer(new ChangeThrottlePacket(ChangeThrottlePacket.Direction.DOWN));
}
                boolean isMoveHeliUp = moveHeliUpKey.isDown();
                boolean isPitchUp = pitchUp.isDown();
                boolean isPitchDown = pitchDown.isDown();
                boolean isYawRight = yawRight.isDown();
                boolean isYawLeft = yawLeft.isDown();

                if (isMoveHeliUp != oldMoveHeliUpState) {
                    ClientPacketDistributor.sendToServer(new MoveHeliUpPacket(isMoveHeliUp));
}
                if (isPitchUp != oldPitchUpState || isPitchDown != oldPitchDownState) {
                    ClientPacketDistributor.sendToServer(new PitchPacket((byte) Boolean.compare(isPitchUp, isPitchDown)));
}
                if (isYawRight != oldYawRightState || isYawLeft != oldYawLeftState) {
                    ClientPacketDistributor.sendToServer(new YawPacket((byte) Boolean.compare(isYawRight, isYawLeft)));
}
                oldMoveHeliUpState = isMoveHeliUp;
                oldPitchUpState = isPitchUp;
                oldPitchDownState = isPitchDown;
                oldYawRightState = isYawRight;
                oldYawLeftState = isYawLeft;
} else {
                oldMoveHeliUpState = false;
                oldPitchUpState = false;
                oldPitchDownState = false;
                oldYawRightState = false;
                oldYawLeftState = false;
}
}
    }
    //TODO: make it so player rotation variables correspond to what he is actually looking at, so that guns etc. shoot in the right direction
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Camera camera = event.getCamera();
        Entity player = camera.entity();
        if (player.getVehicle() instanceof PlaneEntity planeEntity) {
            if (!camera.isDetached()) {
                double partialTicks = event.getPartialTick();

                Quaternionf qPrev = planeEntity.getQ_Prev();
                Quaternionf qNow = planeEntity.getQ_Client();

                qPrev.mul(Axis.YP.rotationDegrees(player.yRotO));
                qPrev.mul(Axis.XP.rotationDegrees(event.getPitch()));
                MathUtil.EulerAngles eulerAnglesPrev = MathUtil.toEulerAngles(qPrev);

                qNow.mul(Axis.YP.rotationDegrees(player.getYRot()));
                qNow.mul(Axis.XP.rotationDegrees(event.getPitch()));
                MathUtil.EulerAngles eulerAnglesNow = MathUtil.toEulerAngles(qNow);

                event.setPitch(-(float) MathUtil.lerpAngle(partialTicks, eulerAnglesPrev.pitch, eulerAnglesNow.pitch));
                event.setYaw((float) MathUtil.lerpAngle(partialTicks, eulerAnglesPrev.yaw, eulerAnglesNow.yaw));
                event.setRoll(-(float) MathUtil.lerpAngle(partialTicks, eulerAnglesPrev.roll, eulerAnglesNow.roll));
}
}
}
    @SubscribeEvent
    public static void onCalculateDetachedCameraDistance(CalculateDetachedCameraDistanceEvent event) {
        if (event.getCamera().entity() != null && event.getCamera().entity().getVehicle() instanceof PlaneEntity planeEntity) {
            event.setDistance((float) (4.0 * planeEntity.getCameraDistanceMultiplayer()));
}
}
}