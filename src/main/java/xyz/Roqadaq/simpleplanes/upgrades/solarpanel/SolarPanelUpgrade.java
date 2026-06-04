package xyz.roqadaq.simpleplanes.upgrades.solarpanel;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.roqadaq.simpleplanes.client.render.UpgradesModels;
import xyz.roqadaq.simpleplanes.entities.LargePlaneEntity;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesEntities;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesItems;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.Upgrade;
import xyz.roqadaq.simpleplanes.upgrades.engines.electric.ElectricEngineUpgrade;

import javax.annotation.Nullable;

public class SolarPanelUpgrade extends Upgrade {

    private final short MAX_PER_TICK;

    public SolarPanelUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.SOLAR_PANEL.get(), planeEntity);
        if (planeEntity instanceof LargePlaneEntity) {
            MAX_PER_TICK = 10;
} else {
            MAX_PER_TICK = 5;
}
}
    @Override
    public void tick() {
        PlaneEntity entity = getPlaneEntity();
        Level world = entity.level();
        if (canSeeSun(world, entity.getOnPos().above())) {
            float brightness = MAX_PER_TICK * getSunBrightness(entity.level(), 1.0F);
            if (entity.engineUpgrade instanceof ElectricEngineUpgrade engine) {
                engine.energyStorage.receiveEnergy((int) brightness, false);
}
}
}
    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector snc, int packedLight, PlaneRenderState state) {
        EntityType<?> entityType = planeEntity.getType();
        UpgradesModels.ModelEntry modelEntry = UpgradesModels.MODEL_ENTRIES.get(getType());
        if (modelEntry == null) return;
        if (entityType == SimplePlanesEntities.PLANE.get()) {
            snc.submitModel(modelEntry.normal(), state, matrixStack, modelEntry.normalTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            snc.submitModel(modelEntry.large(), state, matrixStack, modelEntry.largeTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else if (entityType == SimplePlanesEntities.CARGO_PLANE.get()) {
            snc.submitModel(modelEntry.cargo(), state, matrixStack, modelEntry.cargoTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
}
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.SOLAR_PANEL.get().getDefaultInstance();
    }
    private static boolean canSeeSun(@Nullable Level level, BlockPos pos) {
        return level != null && level.dimensionType().hasSkyLight() && level.getSkyDarken() < 4 && level.canSeeSky(pos);
    }
    public static float getSunBrightness(Level world, float partialTicks) {
        float f = (world.getGameTime() % 24000L) / 24000.0f;
        float f1 = 1.0F - (Mth.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float) (f1 * (1.0D - world.getRainLevel(partialTicks) * 5.0F / 16.0D));
        f1 = (float) (f1 * (1.0D - world.getThunderLevel(partialTicks) * 5.0F / 16.0D));
        return f1 * 0.8F;
}
}