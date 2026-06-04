package xyz.roqadaq.simpleplanes.upgrades.jukebox;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.roqadaq.simpleplanes.client.MovingSound;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.network.JukeboxPacket;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.LargeUpgrade;

public class JukeboxUpgrade extends LargeUpgrade {

    private ItemStack record = ItemStack.EMPTY;

    public JukeboxUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.JUKEBOX.get(), planeEntity);
    }
    @Override
    public Tag serializeNBT() {
        return ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, record).result().orElse(new CompoundTag());
    }
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        record = ItemStack.CODEC.parse(NbtOps.INSTANCE, nbt).result().orElse(ItemStack.EMPTY);
    }
    @Override
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!planeEntity.level().isClientSide()) {
            Player player = event.getEntity();
            ItemStack itemStack = player.getItemInHand(event.getHand());
            if (!itemStack.is(record.getItem())) {
                ItemStack oldRecord = record;
                record = itemStack.copy();
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                if (!oldRecord.isEmpty()) {
                    player.addItem(oldRecord);
                }
                player.awardStat(Stats.PLAY_RECORD);
                PacketDistributor.sendToPlayersTrackingEntity(planeEntity, new JukeboxPacket(BuiltInRegistries.ITEM.getKey(itemStack.getItem()), planeEntity.getId()));
            }
        }
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
            planeEntity.spawnAtLocation(serverLevel, record);
        }
        if (planeEntity.level().isClientSide()) {
            MovingSound.remove(planeEntity);
        }
    }
    @Override
    public ItemStack getItemStack() {
        return Items.JUKEBOX.getDefaultInstance();
    }
}
