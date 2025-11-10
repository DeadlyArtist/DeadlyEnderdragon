package deadlyenderdragon.forge.utils;

import deadlyenderdragon.utils.ILoader;
import net.minecraftforge.fml.loading.LoadingModList;

public class LoaderImpl implements ILoader {
    @Override
    public LoaderType getLoaderType() {
        return LoaderType.Fabric;
    }

    @Override
    public boolean isModLoaded(String id) {
        return LoadingModList.get().getModFileById(id) != null;
    }
}