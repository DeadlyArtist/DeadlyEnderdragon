package deadlyenderdragon.utils;

public class Loader {
    public static ILoader _impl;

    public static ILoader.LoaderType getLoaderType() {
        return _impl.getLoaderType();
    }

    public static boolean isFabric() {
        return getLoaderType() == ILoader.LoaderType.Fabric;
    }

    public static boolean isForge() {
        return getLoaderType() == ILoader.LoaderType.Forge;
    }

    public static boolean isNeoForge() {
        return getLoaderType() == ILoader.LoaderType.NeoForge;
    }

    public static boolean isModLoaded(String id) {
        return _impl.isModLoaded(id);
    }
}
