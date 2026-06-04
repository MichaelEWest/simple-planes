package xyz.roqadaq.simpleplanes.client.render;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.model.data.ModelData;
import org.joml.Quaternionf;
import xyz.roqadaq.simpleplanes.entities.CargoPlaneEntity;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.misc.MathUtil;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesEntities;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesRegistries;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.LargeUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.Upgrade;
import xyz.roqadaq.simpleplanes.upgrades.storage.ChestUpgrade;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlaneRenderer<T extends PlaneEntity> extends EntityRenderer<T, PlaneRenderState> {

    protected final EntityModel<PlaneRenderState> propellerModel;
    protected final EntityModel<PlaneRenderState> planeEntityModel;
    protected final EntityModel<PlaneRenderState> planeMetalModel;
    protected final Identifier metalTexture;
    protected final Identifier propellerTexture;

    public PlaneRenderer(EntityRendererProvider.Context context, EntityModel<PlaneRenderState> planeModel, EntityModel<PlaneRenderState> planeMetalModel, EntityModel<PlaneRenderState> propellerModel, float shadowSize, Identifier metalTexture, Identifier propellerTexture) {
        super(context);
        this.propellerModel = propellerModel;
        this.planeEntityModel = planeModel;
        this.planeMetalModel = planeMetalModel;
        this.metalTexture = metalTexture;
        this.propellerTexture = propellerTexture;
        this.shadowRadius = shadowSize;
    }
    public static float getPropellerRotation(PlaneEntity entity, float partialTicks) {
        return Mth.lerp(partialTicks, entity.propellerRotationOld, entity.propellerRotationNew);
}
    @Override
    public PlaneRenderState createRenderState() {
        return new PlaneRenderState();
}
    @Override
    public void extractRenderState(T entity, PlaneRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.propellerRotation = getPropellerRotation(entity, partialTicks);
        state.timeSinceHit = entity.getTimeSinceHit();
        state.damageTaken = entity.getDamageTaken();
        state.qClient = entity.getQ_Client();
        state.qPrev = entity.getQ_Prev();
        state.materialTexture = getMaterialTexture(entity);
        state.upgrades = entity.upgrades;
        state.entityId = entity.getId();
        if (entity instanceof CargoPlaneEntity cargoPlane) {
            state.isCargo = true;
            state.largeUpgrades = cargoPlane.largeUpgrades;
} else {
            state.isCargo = false;
            state.largeUpgrades = Collections.emptyList();
}
}
    @Override
    public void submit(PlaneRenderState state, PoseStack poseStack, SubmitNodeCollector snc, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.375D, 0.0D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        Quaternionf q = MathUtil.lerpQ(state.partialTick, state.qPrev, state.qClient);
        poseStack.mulPose(q);
        EntityType<?> entityType = state.entityType;
        if (entityType == SimplePlanesEntities.PLANE.get()) {
            poseStack.translate(0, -0.5, -0.5);
} else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            poseStack.translate(0, -0.3, -1);
} else if (entityType == SimplePlanesEntities.CARGO_PLANE.get()) {
            poseStack.translate(0, -0.8, -1);
} else {
            poseStack.translate(0, 0, 0.9);
}
        float timeSinceHitWithPartial = (float) state.timeSinceHit - state.partialTick;
        if (timeSinceHitWithPartial > 0.0F) {
            float angle = Mth.clamp(timeSinceHitWithPartial / 10.0F, -30, 30);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(state.ageInTicks) * angle));
}
        poseStack.translate(0, -1.1, 0);

        snc.submitModel(planeEntityModel, state, poseStack, state.materialTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        snc.submitModel(propellerModel, state, poseStack, propellerTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        snc.submitModel(planeMetalModel, state, poseStack, metalTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

        for (Upgrade upgrade : state.upgrades.values()) {
            upgrade.render(poseStack, snc, state.lightCoords, state);
}
        if (state.isCargo) {
            if (!state.upgrades.containsKey(SimplePlanesRegistries.UPGRADE_TYPE.getKey(SimplePlanesUpgrades.SEATS.get()))) {
                snc.submitModel(UpgradesModels.WOODEN_CARGO_SEATS, state, poseStack, state.materialTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
}
            for (int i = 0; i < state.largeUpgrades.size(); i++) {
                LargeUpgrade upgrade = state.largeUpgrades.get(i);
                poseStack.pushPose();
                if (i < 6) {
                    poseStack.translate(i % 2 - 0.5, 0.3125, i / 2 + 0.5);
} else {
                    if (upgrade instanceof ChestUpgrade) {
                        poseStack.translate(0, -0.1, 0);
}
                    if (i == 6) {
                        poseStack.translate(2.875, 1.05, 2.1375);
} else {
                        poseStack.translate(-2.875, 1.05, 2.1375);
}
}
                upgrade.render(poseStack, snc, state.lightCoords, state);
                poseStack.popPose();
}
}
        poseStack.popPose();
        super.submit(state, poseStack, snc, camera);
}
    public Identifier getTextureLocation(PlaneRenderState state) {
        return state.materialTexture;
    }
    public static Identifier getMaterialTexture(PlaneEntity entity) {
        return getMaterialTextureForBlock(entity.getMaterial());
    }
    private static Identifier getMaterialTextureForBlock(Block block) {
        if (cachedTextures.containsKey(block)) {
            return cachedTextures.get(block);
        }
        // Block model texture lookup API changed in 26.1; return fallback
        cachedTextures.put(block, FALLBACK_TEXTURE);
        return FALLBACK_TEXTURE;
    }
    public static final Map<Block, Identifier> cachedTextures = new HashMap<>();
    public static final Identifier FALLBACK_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/block/oak_planks.png");
}