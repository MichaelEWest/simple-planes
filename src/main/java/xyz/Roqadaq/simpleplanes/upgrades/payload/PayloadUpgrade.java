package xyz.roqadaq.simpleplanes.upgrades.payload;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import xyz.roqadaq.simpleplanes.datapack.PayloadEntry;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesEntities;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.LargeUpgrade;

public class PayloadUpgrade extends LargeUpgrade {

    private PayloadEntry payloadEntry;

    public PayloadUpgrade(PlaneEntity planeEntity, PayloadEntry payloadEntry) {
        super(SimplePlanesUpgrades.PAYLOAD.get(), planeEntity);
        this.payloadEntry = payloadEntry;
    }

    public PayloadUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.PAYLOAD.get(), planeEntity);
    }

    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector snc, int packedLight, PlaneRenderState renderState) {
        // Block rendering in new API requires SubmitNodeCollector - simplified stub
    }

    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
        if (payloadEntry == null) return;
        buffer.writeIdentifier(BuiltInRegistries.ITEM.getKey(payloadEntry.item()));
        buffer.writeIdentifier(BuiltInRegistries.BLOCK.getKey(payloadEntry.renderBlock()));
        buffer.writeIdentifier(BuiltInRegistries.ENTITY_TYPE.getKey(payloadEntry.dropSpawnEntity()));
        buffer.writeNbt(payloadEntry.compoundTag());
    }

    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
        Item item = (Item) BuiltInRegistries.ITEM.getOptional(buffer.readIdentifier()).orElse(null);
        Block renderBlock = (Block) BuiltInRegistries.BLOCK.getOptional(buffer.readIdentifier()).orElse(null);
        @SuppressWarnings("unchecked") EntityType<?> dropSpawnEntity = (EntityType<?>) BuiltInRegistries.ENTITY_TYPE.getOptional(buffer.readIdentifier()).orElse(null);
        CompoundTag compoundTag = buffer.readNbt();
        payloadEntry = new PayloadEntry(item, renderBlock, dropSpawnEntity, compoundTag);
    }

    @Override
    public Tag serializeNBT() {
        if (payloadEntry == null) return new CompoundTag();
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("item", BuiltInRegistries.ITEM.getKey(payloadEntry.item()).toString());
        compoundTag.putString("block", BuiltInRegistries.BLOCK.getKey(payloadEntry.renderBlock()).toString());
        compoundTag.putString("entity", BuiltInRegistries.ENTITY_TYPE.getKey(payloadEntry.dropSpawnEntity()).toString());
        compoundTag.put("entityTag", payloadEntry.compoundTag());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Item item = (Item) BuiltInRegistries.ITEM.getOptional(Identifier.parse(nbt.getString("item").orElse(""))).orElse(null);
        Block renderBlock = (Block) BuiltInRegistries.BLOCK.getOptional(Identifier.parse(nbt.getString("block").orElse(""))).orElse(null);
        @SuppressWarnings("unchecked") EntityType<?> dropSpawnEntity = (EntityType<?>) BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse(nbt.getString("entity").orElse(""))).orElse(null);
        payloadEntry = new PayloadEntry(item, renderBlock, dropSpawnEntity, nbt.getCompoundOrEmpty("entityTag"));
    }

    @Override
    public ItemStack getItemStack() {
        return payloadEntry != null ? new ItemStack(payloadEntry.item()) : ItemStack.EMPTY;
    }

    @Override
    public boolean canBeDroppedAsPayload() {
        return true;
    }

    @Override
    public void dropAsPayload() {
        if (payloadEntry != null) {
            Entity entity = payloadEntry.dropSpawnEntity().create(planeEntity.level(), net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED);
            if (entity != null) {
                entity.setPos(planeEntity.position());
                entity.setDeltaMovement(planeEntity.getDeltaMovement());
                planeEntity.level().addFreshEntity(entity);
            }
        }
        remove();
    }
}
