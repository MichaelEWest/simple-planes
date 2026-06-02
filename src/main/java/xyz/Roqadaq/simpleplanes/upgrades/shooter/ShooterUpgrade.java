package xyz.przemyk.simpleplanes.upgrades.shooter;

import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.joml.Vector3f;
import xyz.przemyk.simpleplanes.client.gui.PlaneInventoryScreen;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesItems;
import xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;

import java.util.function.Function;

public class ShooterUpgrade extends Upgrade {

    public final ItemStackHandler itemStackHandler = new ItemStackHandler();

    public ShooterUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.SHOOTER.get(), planeEntity);
    }
    public void use(Player player) {
        Vector3f motion1 = planeEntity.transformPos(new Vector3f(0, -0.25f, (float) (1 + planeEntity.getDeltaMovement().length())));
        Vec3 motion = new Vec3(motion1);
        Level level = player.level();
        RandomSource random = level.getRandom();

        Vector3f pos = planeEntity.transformPos(new Vector3f(0.0f, 1.8f, 2.0f));
        updateClient();

        double x = pos.x() + planeEntity.getX();
        double y = pos.y() + planeEntity.getY();
        double z = pos.z() + planeEntity.getZ();

        ItemStack itemStack = itemStackHandler.getStackInSlot(0);
        Item item = itemStack.getItem();

        if (item == Items.FIREWORK_ROCKET) {
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(level, itemStack, x, y, z, true);
            fireworkrocketentity.shoot(-motion.x, -motion.y, -motion.z, -(float) Math.max(0.5F, motion.length() * 1.5), 1.0F);
            level.addFreshEntity(fireworkrocketentity);
            if (!player.isCreative()) {
                itemStackHandler.extractItem(0, 1, false);
}
} // TODO: fire charge, arrow, ender eye shooting disabled - projectile API changed in MC 1.21.5
}
    @Override
    public Tag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        net.minecraft.nbt.Tag itemTag = ItemStack.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, itemStackHandler.getStackInSlot(0)).result().orElse(new CompoundTag());
        compoundTag.put("item", itemTag);
        return compoundTag;
}
    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        net.minecraft.nbt.Tag itemTag = compoundTag.get("item");
        if (itemTag != null) {
            itemStackHandler.setStackInSlot(0, ItemStack.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, itemTag).result().orElse(ItemStack.EMPTY));
        }
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, itemStackHandler.getStackInSlot(0));
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
        itemStackHandler.setStackInSlot(0, ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
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
    public void onRemoved() {
        if (planeEntity.level() instanceof ServerLevel serverLevel) {
            planeEntity.spawnAtLocation(serverLevel, itemStackHandler.getStackInSlot(0));
        }
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.SHOOTER.get().getDefaultInstance();
}
    @Override
    public void addContainerData(Function<Slot, Slot> addSlot, Function<DataSlot, DataSlot> addDataSlot) {
        addSlot.apply(new SlotItemHandler(itemStackHandler, 0, 134, 62));
}
    @Override
    public void renderScreenBg(GuiGraphicsExtractor guiGraphics, int x, int y, float partialTicks, PlaneInventoryScreen screen) {
        guiGraphics.blit(PlaneInventoryScreen.GUI, screen.getGuiLeft() + 133, screen.getGuiTop() + 61, 226, 0, 18, 18, 256, 256);
}
}