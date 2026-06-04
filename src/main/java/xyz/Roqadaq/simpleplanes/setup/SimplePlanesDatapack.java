package xyz.roqadaq.simpleplanes.setup;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.datapack.PlaneLiquidFuelReloadListener;
import xyz.roqadaq.simpleplanes.datapack.PlanePayloadReloadListener;

public class SimplePlanesDatapack {

    public static void init() {
        NeoForge.EVENT_BUS.addListener(SimplePlanesDatapack::addReloadListener);
    }
    private static void addReloadListener(AddServerReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "plane_payload"), new PlanePayloadReloadListener());
        event.addListener(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "plane_liquid_fuel"), new PlaneLiquidFuelReloadListener());
}
}