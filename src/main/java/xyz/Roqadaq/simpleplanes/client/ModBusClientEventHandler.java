package xyz.roqadaq.simpleplanes.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.lwjgl.glfw.GLFW;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.client.gui.ModifyUpgradesScreen;
import xyz.roqadaq.simpleplanes.client.gui.PlaneInventoryScreen;
import xyz.roqadaq.simpleplanes.client.gui.PlaneWorkbenchScreen;
import xyz.roqadaq.simpleplanes.client.gui.StorageScreen;
import xyz.roqadaq.simpleplanes.client.render.PlaneItemColors;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesContainers;
import xyz.roqadaq.simpleplanes.upgrades.booster.BoosterUpgrade;

@EventBusSubscriber(value = Dist.CLIENT, modid = SimplePlanesMod.MODID)
public class ModBusClientEventHandler {

    public static final Identifier HUD_TEXTURE = Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "textures/gui/plane_hud.png");
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "key.category"));

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(SimplePlanesContainers.PLANE_WORKBENCH.get(), PlaneWorkbenchScreen::new);
        event.register(SimplePlanesContainers.UPGRADES_REMOVAL.get(), ModifyUpgradesScreen::new);
        event.register(SimplePlanesContainers.STORAGE.get(), StorageScreen::new);
        event.register(SimplePlanesContainers.PLANE_INVENTORY.get(), PlaneInventoryScreen::new);
    }

    @SubscribeEvent
    public static void reloadTextures(TextureAtlasStitchedEvent event) {
        PlaneItemColors.clearCache();
    }

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        ClientEventHandler.moveHeliUpKey = new KeyMapping("key.move_heli_up.desc", GLFW.GLFW_KEY_SPACE, CATEGORY);
        ClientEventHandler.openPlaneInventoryKey = new KeyMapping("key.plane_inventory_open.desc", GLFW.GLFW_KEY_X, CATEGORY);
        ClientEventHandler.dropPayloadKey = new KeyMapping("key.plane_drop_payload.desc", GLFW.GLFW_KEY_C, CATEGORY);
        ClientEventHandler.throttleUp = new KeyMapping("key.plane_throttle_up.desc", GLFW.GLFW_KEY_UP, CATEGORY);
        ClientEventHandler.throttleDown = new KeyMapping("key.plane_throttle_down.desc", GLFW.GLFW_KEY_DOWN, CATEGORY);
        ClientEventHandler.pitchUp = new KeyMapping("key.plane_pitch_up.desc", GLFW.GLFW_KEY_W, CATEGORY);
        ClientEventHandler.pitchDown = new KeyMapping("key.plane_pitch_down.desc", GLFW.GLFW_KEY_S, CATEGORY);
        ClientEventHandler.yawRight = new KeyMapping("key.plane_yaw_right.desc", GLFW.GLFW_KEY_RIGHT, CATEGORY);
        ClientEventHandler.yawLeft = new KeyMapping("key.plane_yaw_left.desc", GLFW.GLFW_KEY_LEFT, CATEGORY);
        event.register(ClientEventHandler.moveHeliUpKey);
        event.register(ClientEventHandler.openPlaneInventoryKey);
        event.register(ClientEventHandler.dropPayloadKey);
        event.register(ClientEventHandler.throttleUp);
        event.register(ClientEventHandler.throttleDown);
        event.register(ClientEventHandler.pitchUp);
        event.register(ClientEventHandler.pitchDown);
        event.register(ClientEventHandler.yawRight);
        event.register(ClientEventHandler.yawLeft);
    }

    @SubscribeEvent
    public static void registerHUDOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "plane_hud"), (guiGraphics, deltaTracker) -> {
            Minecraft mc = Minecraft.getInstance();
            int scaledWidth = mc.getWindow().getGuiScaledWidth();
            int scaledHeight = mc.getWindow().getGuiScaledHeight();
            if (mc.player != null && mc.player.getVehicle() instanceof PlaneEntity planeEntity) {
                int left_align = scaledWidth / 2 + 91;
                int health = planeEntity.getHealth();
                int hearts = Math.min((int) planeEntity.getMaxHealth(), 10);
                final int FULL = 0, EMPTY = 16, GOLD = 32, max_row_size = 5;
                for (int heart = 0; hearts > 0; heart += max_row_size) {
                    int top = scaledHeight - mc.gui.rightHeight;
                    int rowCount = Math.min(hearts, max_row_size);
                    hearts -= rowCount;
                    for (int i = 0; i < rowCount; ++i) {
                        int x = left_align - i * 16 - 16;
                        int vOffset = 35;
                        if (i + heart + 10 < health)
                            PlaneInventoryScreen.blitGui(guiGraphics, HUD_TEXTURE, x, top, GOLD, vOffset, 16, 9);
                        else if (i + heart < health)
                            PlaneInventoryScreen.blitGui(guiGraphics, HUD_TEXTURE, x, top, FULL, vOffset, 16, 9);
                        else
                            PlaneInventoryScreen.blitGui(guiGraphics, HUD_TEXTURE, x, top, EMPTY, vOffset, 16, 9);
                    }
                    mc.gui.rightHeight += 10;
                    PlaneInventoryScreen.blitGui(guiGraphics, HUD_TEXTURE, scaledWidth - 24, scaledHeight - 42, 0, 84, 22, 40);
                    int throttle = planeEntity.getThrottle();
                    if (throttle > 0) {
                        int ts = throttle * 28 / BoosterUpgrade.MAX_THROTTLE;
                        PlaneInventoryScreen.blitGui(guiGraphics, HUD_TEXTURE, scaledWidth - 14, scaledHeight - 42 + 6 + 28 - ts, 22, 90 + 28 - ts, 2, ts);
                    }
                    if (planeEntity.engineUpgrade != null) {
                        ItemStack offhandStack = mc.player.getOffhandItem();
                        HumanoidArm primaryHand = mc.player.getMainArm();
                        planeEntity.engineUpgrade.renderPowerHUD(guiGraphics, (primaryHand == HumanoidArm.LEFT || offhandStack.isEmpty()) ? HumanoidArm.LEFT : HumanoidArm.RIGHT, scaledWidth, scaledHeight, deltaTracker.getGameTimeDeltaPartialTick(false));
                    }
                }
            }
        });
    }
}
