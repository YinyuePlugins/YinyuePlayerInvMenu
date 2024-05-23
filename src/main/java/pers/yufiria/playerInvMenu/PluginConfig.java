package pers.yufiria.playerInvMenu;

import crypticlib.config.ConfigHandler;
import crypticlib.config.entry.ConfigSectionConfigEntry;

@ConfigHandler(path = "config.yml")
public class PluginConfig {

    public static final ConfigSectionConfigEntry ITEMS = new ConfigSectionConfigEntry("items");

}
