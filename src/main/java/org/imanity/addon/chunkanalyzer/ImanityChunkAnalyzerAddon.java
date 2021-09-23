package org.imanity.addon.chunkanalyzer;

import org.bukkit.plugin.java.JavaPlugin;
import org.imanity.addon.chunkanalyzer.command.ChunkAnalyzerCommand;
import org.imanity.addon.chunkanalyzer.util.menu.Menu;

public class ImanityChunkAnalyzerAddon extends JavaPlugin {

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        if (!isServerRunningImanitySpigot3()) {
            getLogger().warning("This server is not running ImanitySpigot3, ImanityChunkAnalyzerAddon need it to work! Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }
        Menu.init(this);
        getCommand("chunkanalyzer").setExecutor(new ChunkAnalyzerCommand());

        getLogger().info("ImanityChunkAnalyzerAddon has been successfully started in " + (System.currentTimeMillis() - start) + "ms.");
    }

    private boolean isServerRunningImanitySpigot3() {
        try {
            Class.forName("org.imanity.imanityspigot.ImanitySpigot");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }
}
