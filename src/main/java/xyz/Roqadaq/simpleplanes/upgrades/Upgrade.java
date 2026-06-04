package xyz.roqadaq.simpleplanes.upgrades;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xyz.roqadaq.simpleplanes.client.gui.PlaneInventoryScreen;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import xyz.roqadaq.simpleplanes.client.render.UpgradesModels;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesEntities;

import java.util.function.Function;

public abstract class Upgrade {

    private final UpgradeType type;
    protected final PlaneEntity planeEntity;
    public boolean updateClient = false;
    public boolean removed = false;

    public PlaneEntity getPlaneEntity() {
        return planeEntity;
    }
    public Upgrade(UpgradeType type, PlaneEntity planeEntity) {
        this.type = type;
        this.planeEntity = planeEntity;
    }
    protected void updateClient() {
        updateClient = true;
    }
    public void remove() {
        removed = true;
    }
    public final UpgradeType getType() {
        return type;
    }
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
    }
    public void tick() {
    }
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
} else {
            snc.submitModel(modelEntry.heli(), state, matrixStack, modelEntry.heliTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
}
    }
    public Tag serializeNBT() {
        return new CompoundTag();
    }
    public void deserializeNBT(CompoundTag nbt) {
    }
    public void onApply(ItemStack itemStack) {
    }
    public abstract void writePacket(RegistryFriendlyByteBuf buffer);

    public abstract void readPacket(RegistryFriendlyByteBuf buffer);

    public void onRemoved() {
    }
    public abstract ItemStack getItemStack();

    public boolean canBeDroppedAsPayload() {
        return false;
    }
    public void dropAsPayload() {
    }
    public void addContainerData(Function<Slot, Slot> addSlot, Function<DataSlot, DataSlot> addDataSlot) {
    }
    public void renderScreen(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks, PlaneInventoryScreen planeInventoryScreen) {
    }
    public void renderScreenBg(GuiGraphicsExtractor guiGraphics, int x, int y, float partialTicks, PlaneInventoryScreen screen) {
    }
    public <T> T getCap(BaseCapability<T, ?> cap) {
        return null;
}
}