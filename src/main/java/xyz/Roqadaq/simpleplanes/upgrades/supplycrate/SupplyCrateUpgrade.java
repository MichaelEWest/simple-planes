package xyz.roqadaq.simpleplanes.upgrades.supplycrate;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.items.ItemStackHandler;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.container.StorageContainer;
import xyz.roqadaq.simpleplanes.entities.ParachuteEntity;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesEntities;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesItems;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.LargeUpgrade;

public class SupplyCrateUpgrade extends LargeUpgrade {

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(27);

    public SupplyCrateUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.SUPPLY_CRATE.get(), planeEntity);
}
    @Override
    public Tag serializeNBT() {
        return new CompoundTag(); // TODO: migrate ItemStackHandler serialization to NF26 API
}
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // TODO: migrate ItemStackHandler deserialization to NF26 API
}
    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector snc, int packedLight, PlaneRenderState state) {
        // TODO: Block rendering removed in MC 1.21.5 - reimplement using new API
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public void onRemoved() {
        if (planeEntity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                ItemStack itemStack = itemStackHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    planeEntity.spawnAtLocation(serverLevel, itemStack);
}
}
}
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.SUPPLY_CRATE.get().getDefaultInstance();
}
    @Override
    public boolean hasStorage() {
        return true;
}
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCap(BaseCapability<T, ?> cap) {
        if (cap == Capabilities.Item.ENTITY) {
            return (T) itemStackHandler;
}
        return super.getCap(cap);
}
    @Override
    public void openStorageGui(Player player, int cycleableContainerID) {
        player.openMenu(new SimpleMenuProvider(
                (id, playerInventory, playerIn) -> new StorageContainer(id, playerInventory, itemStackHandler, BuiltInRegistries.ITEM.getKey(Items.BARREL).toString(), cycleableContainerID),
                Component.translatable(SimplePlanesMod.MODID + ":supply_crate")
        ), buffer -> {
            buffer.writeUtf(BuiltInRegistries.ITEM.getKey(Items.BARREL).toString());
            buffer.writeByte(cycleableContainerID);
});
}
    @Override
    public boolean canBeDroppedAsPayload() {
        return true;
}
    @Override
    public void dropAsPayload() {
        ParachuteEntity parachuteEntity = new ParachuteEntity(planeEntity.level(), itemStackHandler);
        parachuteEntity.setPos(planeEntity.position());
        parachuteEntity.setDeltaMovement(planeEntity.getDeltaMovement());
        planeEntity.level().addFreshEntity(parachuteEntity);
        remove();
}
}