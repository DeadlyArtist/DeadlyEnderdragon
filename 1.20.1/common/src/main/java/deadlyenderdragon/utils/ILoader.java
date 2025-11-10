package deadlyenderdragon.utils;

public interface ILoader {
    public static enum LoaderType {
        Fabric,
        Forge,
        NeoForge,
    }

    public LoaderType getLoaderType();

    public boolean isModLoaded(String id);
}
