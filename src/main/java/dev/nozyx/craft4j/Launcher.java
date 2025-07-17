package dev.nozyx.craft4j;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.versions.IModLoaderVersion;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.fabric.FabricVersionBuilder;
import fr.flowarg.flowupdater.versions.fabric.QuiltVersionBuilder;
import fr.flowarg.flowupdater.versions.forge.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.neoforge.NeoForgeVersionBuilder;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;

import java.io.IOException;
import java.util.UUID;

/**
 * The {@code Launcher} class is part of the Craft4J library and provides an abstraction layer
 * to simplify authentication, game configuration, update, and launch processes for Minecraft launchers.
 *
 * <p>This class supports Microsoft authentication (via refresh token or webview),
 * offline authentication, version updating (vanilla and modded), and game launching using OpenLauncherLib and FlowUpdater.
 */
public class Launcher {

    /**
     * The name of the launcher (used for logging/debugging purposes).
     */
    private final String launcherName;

    private AuthInfos authInfos;
    private GameConfiguration gameConfiguration;

    /**
     * Constructs a new {@code Launcher} instance.
     *
     * @param launcherName The name of the launcher instance.
     */
    public Launcher(String launcherName) {
        System.out.println("[Craft4J] Initializing launcher: " + launcherName);

        this.launcherName = launcherName;
    }

    /**
     * Authenticates the launcher using a Microsoft refresh token.
     *
     * @param refreshToken The Microsoft refresh token.
     * @return The new refresh token returned by Microsoft.
     * @throws MicrosoftAuthenticationException If authentication fails.
     * @throws IllegalStateException If the launcher is already authenticated.
     */
    public String authWithRefreshToken(String refreshToken) throws MicrosoftAuthenticationException {
        if (authInfos != null) throw new IllegalStateException("Launcher is already authenticated!");

        System.out.println("[Craft4J] Authenticating with refresh token!");

        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        MicrosoftAuthResult response = authenticator.loginWithRefreshToken(refreshToken);

        System.out.println("[Craft4J] Auth success:\n- Username: " + response.getProfile().getName() + "\n- UUID: " + response.getProfile().getId());

        authInfos = new AuthInfos(
                response.getProfile().getName(),
                response.getAccessToken(),
                response.getProfile().getId(),
                response.getXuid(),
                response.getClientId()
        );

        return response.getRefreshToken();
    }

    /**
     * Authenticates the launcher using a Microsoft webview flow.
     *
     * @return The refresh token obtained after authentication.
     * @throws MicrosoftAuthenticationException If authentication fails.
     * @throws IllegalStateException If the launcher is already authenticated.
     */
    public String authWithWebview() throws MicrosoftAuthenticationException {
        if (authInfos != null) throw new IllegalStateException("Launcher is already authenticated!");

        System.out.println("[Craft4J] Authenticating with webview!");

        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        MicrosoftAuthResult response = authenticator.loginWithWebview();

        System.out.println("[Craft4J] Auth success:\n- Username: " + response.getProfile().getName() + "\n- UUID: " + response.getProfile().getId());

        authInfos = new AuthInfos(
                response.getProfile().getName(),
                response.getAccessToken(),
                response.getProfile().getId(),
                response.getXuid(),
                response.getClientId()
        );

        return response.getRefreshToken();
    }

    /**
     * Authenticates the launcher in offline mode using the provided username and UUID.
     *
     * @param username The offline username.
     * @param uuid     The user UUID.
     * @throws IllegalStateException If the launcher is already authenticated.
     */
    public void authOffline(String username, String uuid) {
        if (authInfos != null) throw new IllegalStateException("Launcher is already authenticated!");

        System.out.println("[Craft4J] Authenticating offline!");
        System.out.println("[Craft4J] Auth success:\n- Username: " + username + "\n- UUID: " + uuid);

        authInfos = new AuthInfos(
                username,
                UUID.randomUUID().toString(),
                uuid
        );
    }

    /**
     * Updates the game files using FlowUpdater according to the current game configuration.
     * Supports both vanilla and modded Minecraft versions.
     *
     * @param callback The progress callback used during the update.
     * @throws Exception If the update fails or configuration is missing.
     * @throws IllegalStateException If the game configuration is not set.
     */
    public void update(IProgressCallback callback) throws Exception {
        if (gameConfiguration == null) throw new IllegalStateException("Launcher GameConfiguration not set!");

        System.out.println("[Craft4J] Starting update!");

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder()
                .withName(gameConfiguration.getMcVersion())
                .build();

        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                .withVanillaVersion(vanillaVersion)
                .withProgressCallback(callback)
                .build();

        if (gameConfiguration instanceof ModdedGameConfiguration) {
            IModLoaderVersion modLoaderVersion = null;

            switch (((ModdedGameConfiguration) gameConfiguration).getModLoader()) {
                case FORGE:
                    modLoaderVersion = new ForgeVersionBuilder()
                            .withForgeVersion(((ModdedGameConfiguration) gameConfiguration).getModLoaderVersion())
                            .withMods(((ModdedGameConfiguration) gameConfiguration).getMods())
                            .build();
                    break;
                case NEOFORGE:
                    modLoaderVersion = new NeoForgeVersionBuilder()
                            .withNeoForgeVersion(((ModdedGameConfiguration) gameConfiguration).getModLoaderVersion())
                            .withMods(((ModdedGameConfiguration) gameConfiguration).getMods())
                            .build();
                    break;
                case FABRIC:
                    modLoaderVersion = new FabricVersionBuilder()
                            .withFabricVersion(((ModdedGameConfiguration) gameConfiguration).getModLoaderVersion())
                            .withMods(((ModdedGameConfiguration) gameConfiguration).getMods())
                            .build();
                    break;
                case QUILT:
                    modLoaderVersion = new QuiltVersionBuilder()
                            .withQuiltVersion(((ModdedGameConfiguration) gameConfiguration).getModLoaderVersion())
                            .withMods(((ModdedGameConfiguration) gameConfiguration).getMods())
                            .build();
                    break;
            };

            updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanillaVersion)
                    .withModLoaderVersion(modLoaderVersion)
                    .withProgressCallback(callback)
                    .build();
        }

        updater.update(gameConfiguration.getGameDir());
    }

    /**
     * Launches the Minecraft game using OpenLauncherLib's NoFramework.
     *
     * @return The {@link Process} of the launched game.
     * @throws Exception If the game fails to launch.
     * @throws IllegalStateException If authentication or configuration is missing.
     */
    public Process startGame() throws Exception {
        if (authInfos == null) throw new IllegalStateException("Launcher is not authenticated!");
        if (gameConfiguration == null) throw new IllegalStateException("Launcher GameConfiguration not set!");

        System.out.println("[Craft4J] Starting game!");

        NoFramework noFramework = new NoFramework(
                gameConfiguration.getGameDir(),
                authInfos,
                GameFolder.FLOW_UPDATER
        );

        noFramework.getAdditionalVmArgs().addAll(gameConfiguration.getAdditionalVmArgs());

        String modLoaderVersion = gameConfiguration.getMcVersion();

        NoFramework.ModLoader modLoader = NoFramework.ModLoader.VANILLA;

        if (gameConfiguration instanceof ModdedGameConfiguration) {
            modLoaderVersion = ((ModdedGameConfiguration) gameConfiguration).getModLoaderVersion();

            switch (((ModdedGameConfiguration) gameConfiguration).getModLoader()) {
                case FORGE:
                    modLoader = NoFramework.ModLoader.FORGE;
                    break;
                case NEOFORGE:
                    modLoader = NoFramework.ModLoader.NEO_FORGE;
                    break;
                case FABRIC:
                    modLoader = NoFramework.ModLoader.FABRIC;
                    break;
                case QUILT:
                    modLoader = NoFramework.ModLoader.QUILT;
                    break;
            }
        }

        Process p = noFramework.launch(
                gameConfiguration.getMcVersion(),
                modLoaderVersion,
                modLoader
        );

        new Thread(() -> {
            int exitCode = 0;
            try {
                exitCode = p.waitFor();
            } catch (InterruptedException ignored) {}

            System.out.println("[Craft4J] Game stopped with exit code: " + exitCode);
        }, "Craft4J/GameStopListener").start();

        return p;
    }

    /**
     * Clears the current authentication information.
     */
    public void clearAuthInfos() {
        this.authInfos = null;
    }

    /**
     * Returns the current authentication information.
     *
     * @return The {@link AuthInfos} object, or {@code null} if not authenticated.
     */
    public AuthInfos getAuthInfos() {
        return authInfos;
    }

    /**
     * Sets the current game configuration to be used for updates and launching.
     *
     * @param gameConfiguration The {@link GameConfiguration} to set.
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /**
     * Returns the current game configuration.
     *
     * @return The {@link GameConfiguration}, or {@code null} if not set.
     */
    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    /**
     * Returns the name of the launcher.
     *
     * @return The launcher name.
     */
    public String getLauncherName() {
        return launcherName;
    }
}
