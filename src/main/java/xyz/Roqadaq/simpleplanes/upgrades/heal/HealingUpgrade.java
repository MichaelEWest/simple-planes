package xyz.roqadaq.simpleplanes.upgrades.heal;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesItems;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.roqadaq.simpleplanes.upgrades.Upgrade;

public class HealingUpgrade extends Upgrade {

    public HealingUpgrade(PlaneEntity planeEntity) {
        super(SimplePlanesUpgrades.HEALING.get(), planeEntity);
    }
    private int cooldown = 10;

    @Override
    public Tag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putInt("cooldown", cooldown);
        return compoundNBT;
}
    @Override
    public void deserializeNBT(CompoundTag compoundNBT) {
        cooldown = compoundNBT.getInt("cooldown").orElse(0);
}
    @Override
    public void tick() {
        if (cooldown > 0) {
            --cooldown;
} else {
            remove();
}
}
    @Override
    public void render(PoseStack matrixStack, SubmitNodeCollector snc, int packedLight, PlaneRenderState state) {
}
    @Override
    public void onApply(ItemStack itemStack) {
        int health = planeEntity.getHealth();
        int m = planeEntity.getMaxHealth() * 2;
        if (health < m) {
            int heal = planeEntity.getOnGround() ? 2 : 1;
            planeEntity.setHealth(Math.min(health + heal, m));
}
        planeEntity.goldenHeartsTimeout = 0;
}
    @Override
    public void writePacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public void readPacket(RegistryFriendlyByteBuf buffer) {
}
    @Override
    public ItemStack getItemStack() {
        return SimplePlanesItems.HEALING.get().getDefaultInstance();
}
}