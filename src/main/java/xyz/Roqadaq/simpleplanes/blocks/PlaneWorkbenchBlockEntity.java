package xyz.roqadaq.simpleplanes.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesBlocks;

public class PlaneWorkbenchBlockEntity extends BlockEntity {

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(2);
    public final DataSlot selectedRecipe = DataSlot.standalone();

    public PlaneWorkbenchBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SimplePlanesBlocks.PLANE_WORKBENCH_TILE.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput out) {
        super.saveAdditional(out);
        itemStackHandler.serialize(out.child("input"));
        out.putInt("selected_recipe", selectedRecipe.get());
    }

    @Override
    protected void loadAdditional(ValueInput in) {
        super.loadAdditional(in);
        itemStackHandler.deserialize(in.childOrEmpty("input"));
        selectedRecipe.set(in.getIntOr("selected_recipe", 0));
    }
}
