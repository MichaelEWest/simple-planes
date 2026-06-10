package xyz.roqadaq.simpleplanes.client;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Quaternionf;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.misc.MathUtil;
import xyz.roqadaq.simpleplanes.network.*;

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

    // Quaternion of the plane the local player is currently riding; null when not riding.
    private static Quaternionf riderQPrev = null;
    private static Quaternionf riderQClient = null;

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
                riderQPrev = planeEntity.getQ_Prev();
                riderQClient = planeEntity.getQ_Client();
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
                riderQPrev = null;
                riderQClient = null;
                oldMoveHeliUpState = false;
                oldPitchUpState = false;
                oldPitchDownState = false;
                oldYawRightState = false;
                oldYawLeftState = false;
}
}
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?, ?> event) {
        if (riderQClient == null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Identify this render as the local player by position proximity.
        var state = event.getRenderState();
        double dx = state.x - mc.player.getX();
        double dz = state.z - mc.player.getZ();
        if (dx * dx + dz * dz > 1.0) return;

        float partial = event.getPartialTick();
        Quaternionf q = MathUtil.lerpQ(partial, riderQPrev, riderQClient);

        // The plane renderer applies: scale(-1,-1,1) * Axis.YP(180°) * q  =  Axis.XP(180°) * q.
        // The character renderer applies (from vertex's view): scale(-1,-1,1) → R_y → q_ours.
        // For q_ours * R_y * scale(-1,-1,1) = Axis.XP(180°) * q  →  Axis.ZP(180°) = scale(-1,-1,1):
        //   q_ours = Axis.XP(180°) * q * Axis.ZP(180°) * R_y_inv
        float bodyRot = (float) MathUtil.toEulerAngles(q).yaw;
        Quaternionf qOurs = new Quaternionf(Axis.XP.rotationDegrees(180.0f))
                .mul(q)
                .mul(Axis.ZP.rotationDegrees(180.0f))
                .mul(Axis.YP.rotationDegrees(bodyRot - 180.0f));

        event.getPoseStack().mulPose(qOurs);
    }
    //TODO: make it so player rotation variables correspond to what he is actually looking at, so that guns etc. shoot in the right direction
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Camera camera = event.getCamera();
        Entity player = camera.entity();
        if (player.getVehicle() instanceof PlaneEntity planeEntity) {
            double partialTicks = event.getPartialTick();

            if (!camera.isDetached()) {
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
            } else {
                // 3rd person: let Minecraft control orbit yaw/pitch; only apply roll so the
                // horizon tilts with the plane and the character model appears correctly oriented.
                MathUtil.EulerAngles prev = MathUtil.toEulerAngles(planeEntity.getQ_Prev());
                MathUtil.EulerAngles now  = MathUtil.toEulerAngles(planeEntity.getQ_Client());
                event.setRoll(-(float) MathUtil.lerpAngle(partialTicks, prev.roll, now.roll));
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