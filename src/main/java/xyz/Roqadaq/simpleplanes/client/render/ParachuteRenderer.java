package xyz.roqadaq.simpleplanes.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import xyz.roqadaq.simpleplanes.client.render.models.ParachuteModel;
import xyz.roqadaq.simpleplanes.entities.ParachuteEntity;

public class ParachuteRenderer extends EntityRenderer<ParachuteEntity, ParachuteRenderState> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_wool.png");

    private final ParachuteModel parachuteModel;

    public ParachuteRenderer(EntityRendererProvider.Context context, ParachuteModel parachuteModel) {
        super(context);
        this.parachuteModel = parachuteModel;
}
    @Override
    public ParachuteRenderState createRenderState() {
        return new ParachuteRenderState();
}
    @Override
    public void extractRenderState(ParachuteEntity entity, ParachuteRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.hasStorageCrate = entity.hasStorageCrate();
}
    public Identifier getTextureLocation(ParachuteRenderState state) {
        return TEXTURE;
}
    @Override
    public void submit(ParachuteRenderState state, PoseStack poseStack, SubmitNodeCollector snc, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(0, state.hasStorageCrate ? -2.0 : -3.0, 0);
        snc.submitModel(parachuteModel, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, snc, camera);
}
}