package xyz.roqadaq.simpleplanes.upgrades.engines;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.HumanoidArm;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.upgrades.Upgrade;
import xyz.roqadaq.simpleplanes.upgrades.UpgradeType;

public abstract class EngineUpgrade extends Upgrade {

    public EngineUpgrade(UpgradeType type, PlaneEntity planeEntity) {
        super(type, planeEntity);
}
    @Override
    public void remove() {
        super.remove();
        planeEntity.engineUpgrade = null;
    }
    public abstract boolean isPowered();
    public abstract void renderPowerHUD(GuiGraphicsExtractor guiGraphics, HumanoidArm side, int scaledWidth, int scaledHeight, float partialTicks);
}