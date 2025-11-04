package deadlyenderdragon.neoforge.utils;

import deadlyenderdragon.utils.ILoader;
import net.neoforged.fml.loading.LoadingModList;

public class LoaderImpl implements ILoader {
    @Override
    public ILoader.LoaderType getLoaderType() {
        return ILoader.LoaderType.Fabric;
    }

    @Override
    public boolean isModLoaded(String id) {
        return LoadingModList.get().getModFileById(id) != null;
    }
}