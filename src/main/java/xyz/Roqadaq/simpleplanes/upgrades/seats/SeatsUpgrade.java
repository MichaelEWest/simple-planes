package xyz.roqadaq.simpleplanes.upgrades.seats;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.client.render.UpgradesModels;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesEntities;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesItems;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.Upgrade;

public class SeatsUpgrade extends Upgrade {

    public static final Identifier TEXTURE = SimplePlanesMod.texture("seats.png");
    public static final Identifier LARGE_TEXTURE = SimplePlanesMod.texture("seats_large.png");
    public static final Identifier CARGO_TEXTURE = SimplePlanesMod.texture("cargo_plane_metal.png");
    public static final Identifier HELI_TEXTURE = SimplePlanesMod.texture("seats_heli.png");

    public SeatsUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.SEATS.get(), planeEntity);
}
    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector snc, int packedLight, PlaneRenderState state) {
        EntityType<?> entityType = planeEntity.getType();
        if (entityType == SimplePlanesEntities.PLANE.get()) {
            snc.submitModel(UpgradesModels.SEATS, state, matrixStack, TEXTURE, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
            snc.submitModel(UpgradesModels.WOODEN_SEATS, state, matrixStack, state.materialTexture, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            snc.submitModel(UpgradesModels.LARGE_SEATS, state, matrixStack, LARGE_TEXTURE, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else if (entityType == SimplePlanesEntities.CARGO_PLANE.get()) {
            snc.submitModel(UpgradesModels.CARGO_SEATS, state, matrixStack, CARGO_TEXTURE, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else {
            snc.submitModel(UpgradesModels.HELI_SEATS, state, matrixStack, HELI_TEXTURE, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
            snc.submitModel(UpgradesModels.WOODEN_HELI_SEATS, state, matrixStack, state.materialTexture, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
}
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public void onRemoved() {
        planeEntity.ejectPassengers();
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.SEATS.get().getDefaultInstance();
}
}