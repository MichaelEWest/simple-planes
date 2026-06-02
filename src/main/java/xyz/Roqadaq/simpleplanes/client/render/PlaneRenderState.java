package xyz.przemyk.simpleplanes.client.render;
import xyz.przemyk.simpleplanes.client.render.PlaneRenderState;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import xyz.przemyk.simpleplanes.upgrades.LargeUpgrade;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaneRenderState extends EntityRenderState {
    public Identifier materialTexture = PlaneRenderer.FALLBACK_TEXTURE;
    public float propellerRotation = 0;
    public int timeSinceHit = 0;
    public float damageTaken = 0;
    public org.joml.Quaternionf qClient = new org.joml.Quaternionf();
    public org.joml.Quaternionf qPrev = new org.joml.Quaternionf();
    public HashMap<Identifier, Upgrade> upgrades = new HashMap<>();
    public List<LargeUpgrade> largeUpgrades = new ArrayList<>();
    public boolean isCargo = false;
    public int entityId = 0;
}