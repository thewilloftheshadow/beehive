package com.shadowdev.beehive;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.apache.commons.io.FileUtils;

import com.shadowdev.beehive.listeners.ShopkeeperOpenUIEventListener;

import net.milkbowl.vault.permission.Permission;

public final class BeeHiveSMP extends JavaPlugin {
    public final Logger logger = this.getLogger();

    public static Permission perms = null;

    public BeeHiveSMP plugin;
    public final int currentConfig = 1;

    @Override
    public void onEnable() {

        Boolean configIsValid = checkConfig();
        if (configIsValid) {
            this.plugin = this;
            getConfig().options().copyDefaults();
            saveDefaultConfig();
            setupPermissions();
            new ShopkeeperOpenUIEventListener(this);
        }
    }

    public Boolean checkConfig() {
        int configVersion = this.getConfig().getInt("config-version");
        if (configVersion != this.currentConfig) {
            File oldConfigTo = new File(this.getDataFolder(), "config-old-" + configVersion + ".yml");
            File old = new File(this.getDataFolder(), "config.yml");
            try {
                FileUtils.moveFile(old, oldConfigTo);
                getConfig().options().copyDefaults();
                saveDefaultConfig();
                this.logger.severe("Your config is outdated. Your old config has been moved to " + oldConfigTo.getName()
                        + ", and the new version has been applied in its place.");
            } catch (Exception e) {
                File newConfig = new File(this.getDataFolder(), "config-new.yml");
                InputStream newConfigData = this.getResource("config.yml");
                try {
                    FileUtils.copyInputStreamToFile(newConfigData, newConfig);
                    this.logger.severe(
                            "Your config is outdated, but I was unable to replace your old config. Instead, the new config has been saved to "
                                    + newConfig.getName() + ".");
                } catch (Exception e1) {
                    this.logger.severe(
                            "Your config is outdated, but I could not move your old config to a backup or copy in the new config format.");
                }

            }

            this.logger.severe(
                    "The plugin will now disable, please migrate the values from your old config to the new one.");
            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        } else {
            File newConfig = new File(this.getDataFolder(), "config-new.yml");
            if (newConfig.exists())
                FileUtils.deleteQuietly(newConfig);
        }
        return true;
    }

    public void debug(String message) {
        if (this.getConfig().getBoolean("debug")) {
            this.logger.info(message);
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @Override
    public void onDisable() {
        logger.info("BeeHiveSMP has been disabled.");
    }
}