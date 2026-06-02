package xyz.przemyk.simpleplanes.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import xyz.przemyk.simpleplanes.SimplePlanesMod;
import xyz.przemyk.simpleplanes.container.PlaneInventoryContainer;
import xyz.przemyk.simpleplanes.network.CyclePlaneInventoryPacket;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;

public class PlaneInventoryScreen extends AbstractContainerScreen<PlaneInventoryContainer> {

    public static final Identifier GUI = Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "textures/gui/plane_inventory.png");
    public static final WidgetSprites LEFT_BUTTON_SPRITES = new WidgetSprites(
        Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "left"),
        Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "left_highlighted")
    );
    public static final WidgetSprites RIGHT_BUTTON_SPRITES = new WidgetSprites(
        Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "right"),
        Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "right_highlighted")
    );

    public PlaneInventoryScreen(PlaneInventoryContainer screenContainer, Inventory inventory, Component title) {
        super(screenContainer, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new ImageButton(leftPos + 8, topPos + 54, 10, 15, LEFT_BUTTON_SPRITES,
                button -> ClientPacketDistributor.sendToServer(new CyclePlaneInventoryPacket(CyclePlaneInventoryPacket.Direction.LEFT))));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        if (menu.planeEntity != null) {
            for (Upgrade upgrade : menu.planeEntity.upgrades.values()) {
                upgrade.renderScreen(guiGraphics, mouseX, mouseY, partialTicks, this);
            }
        }
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        blitGui(guiGraphics, GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (menu.planeEntity != null) {
            for (Upgrade upgrade : menu.planeEntity.upgrades.values()) {
                upgrade.renderScreenBg(guiGraphics, mouseX, mouseY, partialTick, this);
            }
        }
        super.extractContents(guiGraphics, mouseX, mouseY, partialTick);
    }

    public boolean isHoveringPublic(int x, int y, int width, int height, double mouseX, double mouseY) {
        return isHovering(x, y, width, height, mouseX, mouseY);
    }

    public static void blitGui(GuiGraphicsExtractor g, Identifier texture, int x, int y, int u, int v, int w, int h) {
        g.blit(texture, x, y, x + w, y + h, u / 256.0f, (u + w) / 256.0f, v / 256.0f, (v + h) / 256.0f);
    }
}
