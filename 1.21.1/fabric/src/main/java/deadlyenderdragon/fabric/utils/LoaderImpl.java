package deadlyenderdragon.fabric.utils;

import deadlyenderdragon.utils.ILoader;
import net.fabricmc.loader.api.FabricLoader;

public class LoaderImpl implements ILoader {
    @Override
    public ILoader.LoaderType getLoaderType() {
        return ILoader.LoaderType.Fabric;
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
