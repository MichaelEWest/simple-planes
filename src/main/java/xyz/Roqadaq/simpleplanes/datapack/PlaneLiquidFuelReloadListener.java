package xyz.roqadaq.simpleplanes.datapack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.level.material.Fluid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PlaneLiquidFuelReloadListener implements PreparableReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileToIdConverter LISTER = FileToIdConverter.json("plane_liquid_fuels");

    public static final Map<Fluid, Integer> fuelMap = new HashMap<>();

    @Override
    public CompletableFuture<Void> reload(SharedState currentReload, Executor backgroundExecutor, PreparationBarrier barrier, Executor gameExecutor) {
        ResourceManager manager = currentReload.resourceManager();
        return CompletableFuture.supplyAsync(() -> {
            Map<Identifier, JsonElement> map = new HashMap<>();
            for (Map.Entry<Identifier, Resource> entry : LISTER.listMatchingResources(manager).entrySet()) {
                Identifier id = LISTER.fileToId(entry.getKey());
                try (Reader reader = entry.getValue().openAsReader()) {
                    JsonElement element = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                    if (element != null) {
                        map.put(id, element);
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to load plane liquid fuel {}", entry.getKey(), e);
                }
            }
            return map;
        }, backgroundExecutor).thenCompose(barrier::wait).thenAcceptAsync(map -> {
            fuelMap.clear();
            for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
                try {
                    JsonObject jsonObject = GsonHelper.convertToJsonObject(entry.getValue(), "top element");
                    Fluid fluidType = BuiltInRegistries.FLUID.getOptional(Identifier.parse(jsonObject.get("fluid").getAsString())).orElse(null);
                    if (fluidType == null) {
                        continue;
                    }
                    int fuelPerMb = jsonObject.get("burn_time_per_mb").getAsInt();
                    fuelMap.put(fluidType, fuelPerMb);
                } catch (Exception e) {
                    LOGGER.error("Parsing error loading plane liquid fuel {}", entry.getKey(), e);
                }
            }
        }, gameExecutor);
    }
}
