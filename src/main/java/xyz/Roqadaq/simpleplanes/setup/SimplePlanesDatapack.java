package xyz.przemyk.simpleplanes.setup;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import xyz.przemyk.simpleplanes.SimplePlanesMod;
import xyz.przemyk.simpleplanes.datapack.PlaneLiquidFuelReloadListener;
import xyz.przemyk.simpleplanes.datapack.PlanePayloadReloadListener;

public class SimplePlanesDatapack {

    public static void init() {
        NeoForge.EVENT_BUS.addListener(SimplePlanesDatapack::addReloadListener);
    }
    private static void addReloadListener(AddServerReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "plane_payload"), new PlanePayloadReloadListener());
        event.addListener(Identifier.fromNamespaceAndPath(SimplePlanesMod.MODID, "plane_liquid_fuel"), new PlaneLiquidFuelReloadListener());
}
}