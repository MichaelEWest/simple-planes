package xyz.roqadaq.simpleplanes.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Quaternionf;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.misc.MathUtil;

public record RotationPacket(Quaternionf quaternion) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RotationPacket> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "rotation"));

    private static final StreamCodec<ByteBuf, Quaternionf> QUATERNIONF_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, Quaternionf::x,
        ByteBufCodecs.FLOAT, Quaternionf::y,
        ByteBufCodecs.FLOAT, Quaternionf::z,
        ByteBufCodecs.FLOAT, Quaternionf::w,
        Quaternionf::new
    );

    public static final StreamCodec<ByteBuf, RotationPacket> STREAM_CODEC = StreamCodec.composite(
        QUATERNIONF_CODEC, RotationPacket::quaternion,
        RotationPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().getVehicle() instanceof PlaneEntity planeEntity) {
                planeEntity.setQ(quaternion);
                MathUtil.EulerAngles eulerAngles = MathUtil.toEulerAngles(quaternion);
                planeEntity.setYRot((float) eulerAngles.yaw);
                planeEntity.setXRot((float) eulerAngles.pitch);
                planeEntity.rotationRoll = (float) eulerAngles.roll;
                planeEntity.setQ_Client(quaternion);
}
});
}
}