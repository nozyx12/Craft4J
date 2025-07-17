package dev.nozyx.craft4j;

import fr.flowarg.flowupdater.download.json.Mod;

import java.nio.file.Path;
import java.util.List;

/**
 * Represents a modded Minecraft game configuration.
 * <p>
 * Extends {@link GameConfiguration} by adding mod loader information,
 * mod loader version, and a list of mods to be loaded.
 */
public class ModdedGameConfiguration extends GameConfiguration {
    private final ModLoader modLoader;
    private final String modLoaderVersion;
    private final List<Mod> mods;

    /**
     * Constructs a new {@code ModdedGameConfiguration} instance.
     *
     * @param gameDir          The directory where the game files are located.
     * @param mcVersion        The Minecraft version to launch.
     * @param modLoader        The mod loader type (e.g., Forge, Fabric, Quilt).
     * @param modLoaderVersion The version of the mod loader to use.
     * @param mods             The list of mods to include in the game.
     * @param additionalVmArgs Additional JVM arguments to use when launching the game.
     */
    public ModdedGameConfiguration(Path gameDir, String mcVersion, ModLoader modLoader, String modLoaderVersion, List<Mod> mods, List<String> additionalVmArgs) {
        super(gameDir, mcVersion, additionalVmArgs);

        this.modLoader = modLoader;
        this.modLoaderVersion = modLoaderVersion;
        this.mods = mods;
    }

    /**
     * Returns the mod loader type.
     *
     * @return The mod loader.
     */
    public ModLoader getModLoader() {
        return modLoader;
    }

    /**
     * Returns the version of the mod loader.
     *
     * @return The mod loader version.
     */
    public String getModLoaderVersion() {
        return modLoaderVersion;
    }

    /**
     * Returns the list of mods.
     *
     * @return The list of mods.
     */
    public List<Mod> getMods() {
        return mods;
    }
}
