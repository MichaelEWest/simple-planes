package xyz.roqadaq.simpleplanes.upgrades.engines.furnace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import xyz.roqadaq.simpleplanes.client.ModBusClientEventHandler;
import xyz.roqadaq.simpleplanes.client.gui.PlaneInventoryScreen;
import xyz.roqadaq.simpleplanes.container.slots.FuelSlot;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesItems;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.engines.EngineUpgrade;

import java.util.function.Function;

public class FurnaceEngineUpgrade extends EngineUpgrade {

    public final ItemStackHandler itemStackHandler = new ItemStackHandler();
    public int burnTime;
    public int burnTimeTotal;

    public FurnaceEngineUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.FURNACE_ENGINE.get(), planeEntity);
}
    @Override
    public void tick() {
        if (burnTime > 0) {
            burnTime -= planeEntity.getFuelCost();
            updateClient();
} else if (planeEntity.getThrottle() > 0) {
            ItemStack itemStack = itemStackHandler.getStackInSlot(0);
            int itemBurnTime = itemStack.getBurnTime(RecipeType.SMELTING, planeEntity.level().fuelValues());
            if (itemBurnTime > 0) {
                burnTimeTotal = itemBurnTime;
                burnTime = itemBurnTime;
                itemStackHandler.extractItem(0, 1, false);
                // TODO: crafting remainder (bucket recovery) - getCraftingRemainingItem removed in MC 1.21.5
                updateClient();
}
}
}
    @Override
    public boolean isPowered() {
        return burnTime > 0;
}
    @Override
    public Tag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        ItemStack fuelStack = itemStackHandler.getStackInSlot(0);
        net.minecraft.nbt.Tag itemTag = ItemStack.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, fuelStack).result().orElse(new CompoundTag());
        compound.put("item", itemTag);
        compound.putInt("burnTime", burnTime);
        compound.putInt("burnTimeTotal", burnTimeTotal);
        return compound;
}
    @Override
    public void deserializeNBT(CompoundTag compound) {
        net.minecraft.nbt.Tag itemTag = compound.get("item");
        if (itemTag != null) {
            itemStackHandler.setStackInSlot(0, ItemStack.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, itemTag).result().orElse(ItemStack.EMPTY));
        }
        burnTime = compound.getInt("burnTime").orElse(0);
        burnTimeTotal = compound.getInt("burnTimeTotal").orElse(0);
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, itemStackHandler.getStackInSlot(0));
        buffer.writeVarInt(burnTime);
        buffer.writeVarInt(burnTimeTotal);
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
        itemStackHandler.setStackInSlot(0, ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
        burnTime = buffer.readVarInt();
        burnTimeTotal = buffer.readVarInt();
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
        if (planeEntity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            planeEntity.spawnAtLocation(serverLevel, itemStackHandler.getStackInSlot(0));
        }
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.FURNACE_ENGINE.get().getDefaultInstance();
}
    @Override
    public void renderPowerHUD(GuiGraphicsExtractor guiGraphics, HumanoidArm side, int scaledWidth, int scaledHeight, float partialTicks) {
        int i = scaledWidth / 2;
        Minecraft mc = Minecraft.getInstance();
        if (side == HumanoidArm.LEFT) {
            PlaneInventoryScreen.blitGui(guiGraphics, ModBusClientEventHandler.HUD_TEXTURE, i - 91 - 29, scaledHeight - 40, 0, 44, 22, 40);
        } else {
            PlaneInventoryScreen.blitGui(guiGraphics, ModBusClientEventHandler.HUD_TEXTURE, i + 91, scaledHeight - 40, 0, 44, 22, 40);
        }
        if (burnTime > 0) {
            int burnTimeTotal2 = burnTimeTotal == 0 ? 200 : burnTimeTotal;
            int burnLeftScaled = burnTime * 13 / burnTimeTotal2;
            if (side == HumanoidArm.LEFT) {
                PlaneInventoryScreen.blitGui(guiGraphics, ModBusClientEventHandler.HUD_TEXTURE, i - 91 - 29 + 4, scaledHeight - 40 + 16 - burnLeftScaled, 22, 56 - burnLeftScaled, 14, burnLeftScaled + 1);
            } else {
                PlaneInventoryScreen.blitGui(guiGraphics, ModBusClientEventHandler.HUD_TEXTURE, i + 91 + 4, scaledHeight - 40 + 16 - burnLeftScaled, 22, 56 - burnLeftScaled, 14, burnLeftScaled + 1);
            }
}
        ItemStack fuelStack = itemStackHandler.getStackInSlot(0);
        if (!fuelStack.isEmpty()) {
            int i2 = scaledHeight - 16 - 3;
            if (side == HumanoidArm.LEFT) {
                // TODO: renderItem/renderItemDecorations API changed in MC 1.21.5
                // guiGraphics.renderItem(fuelStack, i - 91 - 26, i2);
} else {
                // guiGraphics.renderItem(fuelStack, i + 91 + 3, i2);
}
}
}
    @Override
    public void addContainerData(Function<Slot, Slot> addSlot, Function<DataSlot, DataSlot> addDataSlot) {
        addSlot.apply(new FuelSlot(itemStackHandler, 0, 152, 62, planeEntity.level().fuelValues()));
}
    @Override
    public void renderScreenBg(GuiGraphicsExtractor guiGraphics, int x, int y, float partialTicks, PlaneInventoryScreen screen) {
        PlaneInventoryScreen.blitGui(guiGraphics, PlaneInventoryScreen.GUI, screen.getGuiLeft() + 151, screen.getGuiTop() + 44, 208, 0, 18, 35);

        if (burnTime > 0) {
            int burnLeftScaled = burnTime * 13 / (burnTimeTotal == 0 ? 200 : burnTimeTotal);
            PlaneInventoryScreen.blitGui(guiGraphics, PlaneInventoryScreen.GUI, screen.getGuiLeft() + 152, screen.getGuiTop() + 57 - burnLeftScaled, 208, 47 - burnLeftScaled, 14, burnLeftScaled + 1);
        }
}
}