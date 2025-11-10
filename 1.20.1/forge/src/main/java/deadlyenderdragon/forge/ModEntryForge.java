package deadlyenderdragon.forge;

import deadlyenderdragon.forge.utils.LoaderImpl;
import deadlyenderdragon.utils.Loader;
import net.minecraftforge.fml.common.Mod;

import deadlyenderdragon.ModEntry;

@Mod(ModEntry.MOD_ID)
public final class ModEntryForge {
    public ModEntryForge() {
        // Run our common setup.
        ModEntry.init();
    }

    public static void preInitialize() {
        Loader._impl = new LoaderImpl();
    }
}
