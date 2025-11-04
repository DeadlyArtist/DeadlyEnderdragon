package deadlyenderdragon.neoforge;

import deadlyenderdragon.neoforge.utils.LoaderImpl;
import deadlyenderdragon.utils.Loader;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import deadlyenderdragon.ModEntry;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(ModEntry.MOD_ID)
public final class ModEntryNeoForge {
    public ModEntryNeoForge(IEventBus eventBus, ModContainer container) {
        //NeoForge.EVENT_BUS.register(this); // Only with @SubscribeEvent present

        ModEntry.init();
        RegistryInitManagerNeo.init();

        eventBus.addListener(this::setup);
        RegistryInitManagerNeo.register(eventBus);
    }

    public static void preInitialize() {
        Loader._impl = new LoaderImpl();
    }

    public void setup(final FMLCommonSetupEvent event) {

    }
}
