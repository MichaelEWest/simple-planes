package xyz.roqadaq.simpleplanes.client;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;
import xyz.roqadaq.simpleplanes.network.JukeboxPacket;

import java.util.Map;

public class MovingSound extends AbstractTickableSoundInstance {

    public static final Map<Entity, SoundInstance> playingRecords = Maps.newHashMap();

    private final Entity entity;

    public MovingSound(SoundEvent soundEvent, Entity entity) {
        super(soundEvent, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.entity = entity;
}
    @Override
    public void tick() {
        if (!entity.isAlive()) {
            stop();
} else {
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
}
    }
    public static void playRecord(JukeboxPacket jukeboxPacket) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.level.getEntity(jukeboxPacket.planeEntityID());
        ItemStack recordStack = BuiltInRegistries.ITEM.getOptional(jukeboxPacket.record())
            .map(ref -> new ItemStack(ref))
            .orElse(ItemStack.EMPTY);
        JukeboxPlayable playable = recordStack.get(DataComponents.JUKEBOX_PLAYABLE);
        if (playable == null) return;
        JukeboxSong jukeboxSong;
        try { jukeboxSong = playable.song().value(); } catch (Exception e) { return; }
        {
            SoundInstance soundInstance = playingRecords.get(entity);
            if (soundInstance != null) {
                minecraft.getSoundManager().stop(soundInstance);
                playingRecords.remove(entity);
}
            minecraft.gui.setNowPlaying(jukeboxSong.description());
            MovingSound movingSound = new MovingSound(jukeboxSong.soundEvent().value(), entity);
            playingRecords.put(entity, movingSound);
            minecraft.getSoundManager().play(movingSound);
}
    }
    public static void play(SoundEvent event, Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new MovingSound(event, entity));
    }
    public static void remove(Entity entity) {
        SoundInstance soundInstance = playingRecords.get(entity);
        if (soundInstance != null) {
            Minecraft.getInstance().getSoundManager().stop(soundInstance);
            playingRecords.remove(entity);
}
}
}