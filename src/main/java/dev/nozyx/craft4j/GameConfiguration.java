package dev.nozyx.craft4j;

import java.nio.file.Path;
import java.util.List;

/**
 * Represents the configuration for a Minecraft game instance.
 * <p>
 * This class holds essential information such as the game directory,
 * Minecraft version, and optional JVM arguments.
 */
public class GameConfiguration {
    private final Path gameDir;
    private final String mcVersion;
    private final List<String> additionalVmArgs;

    /**
     * Constructs a new {@code GameConfiguration} instance.
     *
     * @param gameDir         The directory where the game files are located.
     * @param mcVersion       The Minecraft version to launch.
     * @param additionalVmArgs Additional JVM arguments to use when launching the game.
     */
    public GameConfiguration(Path gameDir, String mcVersion, List<String> additionalVmArgs) {
        this.gameDir = gameDir;
        this.mcVersion = mcVersion;
        this.additionalVmArgs = additionalVmArgs;
    }

    /**
     * Returns the game directory.
     *
     * @return The path to the game directory.
     */
    public Path getGameDir() {
        return gameDir;
    }

    /**
     * Returns the Minecraft version.
     *
     * @return The Minecraft version as a string.
     */
    public String getMcVersion() {
        return mcVersion;
    }

    /**
     * Returns the additional JVM arguments.
     *
     * @return A list of additional JVM arguments.
     */
    public List<String> getAdditionalVmArgs() {
        return additionalVmArgs;
    }
}
