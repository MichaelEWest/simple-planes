package xyz.roqadaq.simpleplanes.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class DescriptionItem extends Item {

    private final Component component;

    public DescriptionItem(Properties properties, Component component) {
        super(properties);
        this.component = component;
    }
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, TooltipDisplay display, Consumer<Component> builder, TooltipFlag pTooltipFlag) {
        builder.accept(component);
    }
}
