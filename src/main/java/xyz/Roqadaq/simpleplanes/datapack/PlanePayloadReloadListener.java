package xyz.roqadaq.simpleplanes.datapack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PlanePayloadReloadListener implements PreparableReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileToIdConverter LISTER = FileToIdConverter.json("plane_payload");
    public static final Map<Item, PayloadEntry> payloadEntries = new HashMap<>();

    @Override
    public CompletableFuture<Void> reload(SharedState currentReload, Executor backgroundExecutor, PreparationBarrier barrier, Executor gameExecutor) {
        ResourceManager manager = currentReload.resourceManager();
        return CompletableFuture.supplyAsync(() -> {
            Map<Identifier, JsonElement> map = new HashMap<>();
            for (Map.Entry<Identifier, Resource> entry : LISTER.listMatchingResources(manager).entrySet()) {
                Identifier id = LISTER.fileToId(entry.getKey());
                try (Reader reader = entry.getValue().openAsReader()) {
                    JsonElement element = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                    if (element != null) map.put(id, element);
                } catch (Exception e) {
                    LOGGER.error("Failed to load plane payload {}", entry.getKey(), e);
                }
            }
            return map;
        }, backgroundExecutor).thenCompose(barrier::wait).thenAcceptAsync(map -> {
            payloadEntries.clear();
            for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
                try {
                    JsonObject jsonObject = GsonHelper.convertToJsonObject(entry.getValue(), "top element");
                    Item item = Objects.requireNonNull(BuiltInRegistries.ITEM.getOptional(Identifier.parse(jsonObject.get("item").getAsString())).orElse(null), "missing item");
                    Block renderBlock = Objects.requireNonNull(BuiltInRegistries.BLOCK.getOptional(Identifier.parse(jsonObject.get("block").getAsString())).orElse(null), "missing block");
                    EntityType<?> dropSpawnEntity = Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse(jsonObject.get("entity").getAsString())).orElse(null), "missing entity");
                    CompoundTag compoundTag;
                    if (jsonObject.has("entity_nbt")) {
                        String tag = GsonHelper.convertToString(jsonObject.get("entity_nbt"), "entity_nbt");
                        compoundTag = TagParser.parseCompoundFully(tag);
                    } else {
                        compoundTag = new CompoundTag();
                    }
                    payloadEntries.put(item, new PayloadEntry(item, renderBlock, dropSpawnEntity, compoundTag));
                } catch (Exception e) {
                    LOGGER.error("Parsing error loading plane payload {}", entry.getKey(), e);
                }
            }
        }, gameExecutor);
    }
}
