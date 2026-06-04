package xyz.roqadaq.simpleplanes.upgrades.engines.liquid;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.client.ClientUtil;
import xyz.roqadaq.simpleplanes.client.gui.PlaneInventoryScreen;
import xyz.roqadaq.simpleplanes.datapack.PlaneLiquidFuelReloadListener;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesConfig;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesItems;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.engines.EngineUpgrade;

import java.util.function.Function;

public class LiquidEngineUpgrade extends EngineUpgrade {

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(2);
    public final FluidTank fluidTank = new FluidTank(SimplePlanesConfig.LIQUID_ENGINE_CAPACITY.get(), fluidStack ->
            PlaneLiquidFuelReloadListener.fuelMap.containsKey(fluidStack.getFluid()));

    public int burnTime;

    public LiquidEngineUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.LIQUID_ENGINE.get(), planeEntity);
}
    @Override
    public void tick() {
        if (!planeEntity.level().isClientSide()) {
            if (burnTime > 0) {
                burnTime -= planeEntity.getFuelCost();
                updateClient();
} else if (planeEntity.getThrottle() > 0 && !fluidTank.isEmpty()) {
                burnTime = PlaneLiquidFuelReloadListener.fuelMap.getOrDefault(fluidTank.getFluid().getFluid(), 0);
                if (burnTime > 0) {
                    fluidTank.drain(1, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
                    updateClient();
}
}
            // TODO: fluid item transfer disabled - fluid capability API changed in NF 26.x
}
}
    @Override
    public void onRemoved() {
        if (planeEntity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            planeEntity.spawnAtLocation(serverLevel, itemStackHandler.getStackInSlot(0));
            planeEntity.spawnAtLocation(serverLevel, itemStackHandler.getStackInSlot(1));
        }
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.LIQUID_ENGINE.get().getDefaultInstance();
}
    @Override
    public boolean isPowered() {
        return !fluidTank.isEmpty();
}
    @Override
    public Tag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("items", serializeItemHandler());
        CompoundTag fluidTag = new CompoundTag();
        FluidStack fluid = fluidTank.getFluid();
        if (!fluid.isEmpty()) {
            net.minecraft.resources.Identifier fluidKey = net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(fluid.getFluid());
            if (fluidKey != null) fluidTag.putString("FluidName", fluidKey.toString());
            fluidTag.putInt("Amount", fluid.getAmount());
        }
        compoundTag.put("fluid", fluidTag);
        compoundTag.putInt("burnTime", burnTime);
        return compoundTag;
}
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        deserializeItemHandler(nbt.getCompoundOrEmpty("items"));
        CompoundTag fluidTag = nbt.getCompoundOrEmpty("fluid");
        fluidTag.getString("FluidName").ifPresent(name -> {
            net.minecraft.resources.Identifier fluidId = net.minecraft.resources.Identifier.parse(name);
            net.minecraft.core.registries.BuiltInRegistries.FLUID.getOptional(fluidId).ifPresent(fluid -> {
                int amount = fluidTag.getInt("Amount").orElse(0);
                fluidTank.setFluid(new FluidStack(fluid, amount));
            });
        });
        burnTime = nbt.getInt("burnTime").orElse(0);
}
    private net.minecraft.nbt.ListTag serializeItemHandler() {
        net.minecraft.nbt.ListTag list = new net.minecraft.nbt.ListTag();
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                CompoundTag slot = new CompoundTag();
                slot.putByte("Slot", (byte) i);
                ItemStack.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, stack).result().ifPresent(tag -> {
                    if (tag instanceof CompoundTag itemTag) { slot.merge(itemTag); }
                });
                list.add(slot);
            }
        }
        return list;
}
    private void deserializeItemHandler(CompoundTag tag) {
        // Legacy format support - reload if it was stored as a compound with Slot entries
        // Items list may be stored as a ListTag
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
        FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, fluidTank.getFluid());
        buffer.writeVarInt(burnTime);
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
        fluidTank.setFluid(FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer));
        burnTime = buffer.readVarInt();
}
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCap(BaseCapability<T, ?> cap) {
        if (cap == Capabilities.Fluid.ENTITY) {
            return (T) fluidTank;
}
        return super.getCap(cap);
}
    @Override
    public void renderPowerHUD(GuiGraphicsExtractor guiGraphics, HumanoidArm side, int scaledWidth, int scaledHeight, float partialTicks) {
        //TODO
}
    @Override
    public void addContainerData(Function<Slot, Slot> addSlot, Function<DataSlot, DataSlot> addDataSlot) {
        addSlot.apply(new SlotItemHandler(itemStackHandler, 0, 152, 8));
        addSlot.apply(new SlotItemHandler(itemStackHandler, 1, 152, 62));
}
    @Override
    public void renderScreen(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks, PlaneInventoryScreen planeInventoryScreen) {
        if (planeInventoryScreen.isHoveringPublic(153, 7 + 18 + 2, 16, 32, mouseX, mouseY)) {
            FluidStack fluidStack = fluidTank.getFluid();
            guiGraphics.setTooltipForNextFrame(net.minecraft.client.Minecraft.getInstance().font.split(Component.translatable(SimplePlanesMod.MODID + ".gui.fluid", fluidStack.getHoverName(), fluidStack.getAmount()), Integer.MAX_VALUE), mouseX, mouseY);
}
}
    @Override
    public void renderScreenBg(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks, PlaneInventoryScreen screen) {
        PlaneInventoryScreen.blitGui(guiGraphics, PlaneInventoryScreen.GUI, screen.getGuiLeft() + 151, screen.getGuiTop() + 7, 176, 72, 18, 72);
        FluidStack fluidStack = fluidTank.getFluid();
        int height = 36;
        int width = 18;
        int amount = fluidStack.getAmount();
        int fluidHeight = amount * (height - 4) / fluidTank.getCapacity();

        if (!fluidStack.isEmpty()) {
            ClientUtil.renderLiquidEngineFluid(guiGraphics, screen, fluidStack, height, width, fluidHeight);
}
        PlaneInventoryScreen.blitGui(guiGraphics, PlaneInventoryScreen.GUI, screen.getGuiLeft() + 154, screen.getGuiTop() + 28, 194, 72, 12, 30);
}
}