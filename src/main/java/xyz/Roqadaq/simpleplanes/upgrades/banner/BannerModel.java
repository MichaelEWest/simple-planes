package xyz.przemyk.simpleplanes.upgrades.banner;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class BannerModel {

    public static void renderBanner(BannerUpgrade bannerUpgrade, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, ItemStack banner, int packedLight) {
        // Banner rendering API changed significantly in MC 1.21.5 - TODO: reimplement
    }
}
