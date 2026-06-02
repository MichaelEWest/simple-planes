package xyz.przemyk.simpleplanes.upgrades.armor;
import xyz.przemyk.simpleplanes.client.render.PlaneRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import xyz.przemyk.simpleplanes.client.render.UpgradesModels;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.setup.SimplePlanesItems;
import xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;

public class ArmorUpgrade extends Upgrade {

    private int protectionLevel = 0;

    public ArmorUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.ARMOR.get(), planeEntity);
}
    @Override
    public void onApply(ItemStack itemStack) {
        planeEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.PROTECTION).ifPresent(enchant -> {
            protectionLevel = itemStack.getEnchantmentLevel(enchant);
});
}
    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector snc, int packedLight, PlaneRenderState state) {
        EntityType<?> entityType = planeEntity.getType();
        UpgradesModels.ModelEntry modelEntry = UpgradesModels.MODEL_ENTRIES.get(getType());
        if (modelEntry == null) return;
        if (entityType == SimplePlanesEntities.PLANE.get()) {
            snc.submitModel(modelEntry.normal(), state, matrixStack, modelEntry.normalTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
            snc.submitModel(UpgradesModels.ARMOR_WINDOW, state, matrixStack, modelEntry.normalTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            snc.submitModel(modelEntry.large(), state, matrixStack, modelEntry.largeTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else if (entityType == SimplePlanesEntities.CARGO_PLANE.get()) {
            snc.submitModel(modelEntry.cargo(), state, matrixStack, modelEntry.cargoTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
} else {
            snc.submitModel(modelEntry.heli(), state, matrixStack, modelEntry.heliTexture(), packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
}
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
        buffer.writeByte(protectionLevel);
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
        protectionLevel = buffer.readByte();
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.ARMOR.get().getDefaultInstance();
        // TODO: restore enchantment - ItemStack.enchant API changed in MC 1.21.5
    }
    public float getReducedDamage(float amount) {
        return amount * (1.0f - (0.04f * getArmorValue()));
    }
    public int getArmorValue() {
        return 15 + (protectionLevel * 2);
}
    @Override
    public Tag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putByte("protection", (byte) protectionLevel);
        return compoundTag;
}
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        protectionLevel = nbt.getByte("protection").orElse((byte) 0);
}
}