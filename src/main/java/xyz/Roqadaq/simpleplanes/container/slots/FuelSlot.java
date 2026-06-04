package xyz.roqadaq.simpleplanes.container.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FuelSlot extends SlotItemHandler {

    private final FuelValues fuelValues;

    public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, FuelValues fuelValues) {
        super(itemHandler, index, xPosition, yPosition);
        this.fuelValues = fuelValues;
    }
    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getBurnTime(RecipeType.SMELTING, fuelValues) > 0;
    }
}
