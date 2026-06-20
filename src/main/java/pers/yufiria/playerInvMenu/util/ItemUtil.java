package pers.yufiria.playerInvMenu.util;

import crypticlib.action.Action;
import crypticlib.action.ActionCompiler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pers.yufiria.playerInvMenu.PlayerInvMenu;
import pers.yufiria.playerInvMenu.PluginConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemUtil {

    public static final Map<Integer, ItemStack> CRAFTING_INV_ITEMS = new ConcurrentHashMap<>();
    public static final Map<Integer, Action> CRAFTING_INV_ACTIONS = new ConcurrentHashMap<>();

    public static void reload() {
        CRAFTING_INV_ITEMS.clear();
        ConfigurationSection itemsConfig = PluginConfig.ITEMS.value();
        for (String key : itemsConfig.getKeys(false)) {
            try {
                int slot = Integer.parseInt(key);
                if (slot < 0 || slot >= 5) {
                    throw new IllegalArgumentException("Slot " + key + " is out of range.");
                }
                ConfigurationSection config = itemsConfig.getConfigurationSection(key);
                List<String> actions = config.getStringList("actions");
                Action action = ActionCompiler.INSTANCE.compile(actions);
                CRAFTING_INV_ACTIONS.put(slot, action);
                if (!config.getKeys(false).contains("material")) {
                    continue;
                }
                Material material = Material.matchMaterial(config.getString("material", "air"));
                String name = config.getString("name");
                List<String> lore = config.getStringList("lore");
                int customModelData = config.getInt("model-data");
                ItemStack item = new ItemStack(material);
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(name);
                    itemMeta.setLore(lore);
                    itemMeta.setCustomModelData(customModelData);
                    item.setItemMeta(itemMeta);
                }
                putSymbol(item);
                CRAFTING_INV_ITEMS.put(slot, item);

            } catch (NumberFormatException ignored) {}
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static ItemStack putSymbol(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(PlayerInvMenu.INSTANCE, "player_inv_menu_item"), PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean isPlayerInvMenuItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return false;
        return itemMeta.getPersistentDataContainer().has(new NamespacedKey(PlayerInvMenu.INSTANCE, "player_inv_menu_item"));
    }

}
