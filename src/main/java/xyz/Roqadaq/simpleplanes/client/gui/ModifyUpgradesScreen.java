package xyz.przemyk.simpleplanes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.przemyk.simpleplanes.SimplePlanesMod;
import xyz.przemyk.simpleplanes.container.ModifyUpgradesContainer;
import xyz.przemyk.simpleplanes.entities.CargoPlaneEntity;

import javax.annotation.Nullable;

public class ModifyUpgradesScreen extends AbstractContainerScreen<ModifyUpgradesContainer> {

    public static final Identifier GUI = Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "textures/gui/modify_upgrades.png");
    public static final Component UPGRADES_TOOLTIP = Component.translatable(SimplePlanesMod.MODID + ".add_upgrades");
    public static final Component CARGO_TOOLTIP = Component.translatable(SimplePlanesMod.MODID + ".add_cargo");

    public ModifyUpgradesScreen(ModifyUpgradesContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 176, 184);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        if (hoveredSlot != null && hoveredSlot.getItem().isEmpty()) {
            if (hoveredSlot.index < 6) {
                guiGraphics.setTooltipForNextFrame(font.split(UPGRADES_TOOLTIP, 115), mouseX, mouseY);
            } else if (hoveredSlot.index < 14) {
                guiGraphics.setTooltipForNextFrame(font.split(CARGO_TOOLTIP, 115), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = this.leftPos;
        int j = this.topPos;
        PlaneInventoryScreen.blitGui(guiGraphics, GUI, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (menu.planeEntity != null) {
            int scale = menu.planeEntity instanceof CargoPlaneEntity ? 6 : 11;
            renderEntityInInventory(guiGraphics, i + 62, j + 8, i + 168, j + 69, scale, menu.planeEntity);
            if (menu.planeEntity instanceof CargoPlaneEntity) {
                PlaneInventoryScreen.blitGui(guiGraphics, GUI, i + 25, j + 74, 25, 101, 144, 18);
            }
        }

        if (menu.errorSlot != -1) {
            Slot slot = menu.slots.get(menu.errorSlot);
            PlaneInventoryScreen.blitGui(guiGraphics, GUI, i + slot.x - 1, j + slot.y - 1, 176, 76, 18, 18);
        }
        super.extractContents(guiGraphics, mouseX, mouseY, partialTick);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void renderEntityInInventory(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, int scale, Entity entity) {
        try {
            EntityRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
            EntityRenderState state = renderer.createRenderState(entity, 1.0f);
            state.outlineColor = 0;
            state.shadowPieces.clear();
            Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI);
            Quaternionf xRotation = new Quaternionf().rotateX(0.3f);
            rotation.mul(xRotation);
            Vector3f translation = new Vector3f(0.0f, state.boundingBoxHeight / 2.0f, 0.0f);
            graphics.entity(state, scale, translation, rotation, xRotation, x0, y0, x1, y1);
        } catch (Exception ignored) {
        }
    }
}
