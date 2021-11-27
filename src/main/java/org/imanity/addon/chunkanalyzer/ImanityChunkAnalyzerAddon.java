package org.imanity.addon.chunkanalyzer;

import org.bukkit.plugin.java.JavaPlugin;
import org.imanity.addon.chunkanalyzer.command.ChunkAnalyzerCommand;
import org.imanity.addon.chunkanalyzer.listener.ChunkAnalyzerListener;
import org.imanity.addon.chunkanalyzer.manager.ChunkAnalyzerManager;
import org.imanity.addon.chunkanalyzer.util.menu.Menu;

public class ImanityChunkAnalyzerAddon extends JavaPlugin {

    private ChunkAnalyzerManager manager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        if (!isServerRunningImanitySpigot3()) {
            getLogger().warning("This server is not running ImanitySpigot3, ImanityChunkAnalyzerAddon need it to work! Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }
        this.manager = new ChunkAnalyzerManager();

        Menu.init(this);

        getCommand("chunkanalyzer").setExecutor(new ChunkAnalyzerCommand(this.manager));
        getServer().getPluginManager().registerEvents(new ChunkAnalyzerListener(this.manager), this);

        getLogger().info("ImanityChunkAnalyzerAddon has been successfully started in " + (System.currentTimeMillis() - start) + "ms.");
    }

    @Override
    public void onDisable() {
        Menu.destroy(this);
    }

    public ChunkAnalyzerManager getManager() {
        return this.manager;
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
