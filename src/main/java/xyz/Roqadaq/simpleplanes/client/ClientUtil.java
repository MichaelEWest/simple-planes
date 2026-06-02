package xyz.przemyk.simpleplanes.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import xyz.przemyk.simpleplanes.client.gui.PlaneInventoryScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ClientUtil {

    public static void renderTiledTextureAtlas(GuiGraphicsExtractor guiGraphics, AbstractContainerScreen<?> screen, TextureAtlasSprite sprite, int x, int y, int width, int height, int depth) {
        // Fluid texture tiling - simplified for MC 1.21.5 rendering API changes
        // Full implementation requires new rendering pipeline integration
    }

    public static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }
    public static int red(int c) {
        return (c >> 16) & 0xFF;
    }
    public static int green(int c) {
        return (c >> 8) & 0xFF;
    }
    public static int blue(int c) {
        return (c) & 0xFF;
    }

    public static void renderLiquidEngineFluid(GuiGraphicsExtractor guiGraphics, PlaneInventoryScreen screen, FluidStack fluidStack, int height, int width, int fluidHeight) {
        // Fluid rendering - simplified for MC 1.21.5 rendering API changes
        // TODO: implement with new rendering API
    }
}
