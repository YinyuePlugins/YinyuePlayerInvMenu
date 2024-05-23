package pers.yufiria.playerInvMenu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import crypticlib.BukkitPlugin;
import crypticlib.CrypticLib;
import crypticlib.chat.TextProcessor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.comphenix.protocol.PacketType.Play.Server.WINDOW_ITEMS;

public class PlayerInvMenu extends BukkitPlugin {

    public static PlayerInvMenu INSTANCE;
    public boolean serverClosed = false;

    @Override
    public void enable() {
        serverClosed = false;
        INSTANCE = this;
        ItemUtil.reload();
        CrypticLib.platform().scheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)) {
                    return;
                }
                if (serverClosed) {
                    player.getOpenInventory().getTopInventory().clear();
                    return;
                }
                for (int i = 0; i < 5; i++) {
                    if (ItemUtil.CRAFTING_INV_ITEMS.get(i) == null) {
                        continue;
                    }
                    ItemStack item = ItemUtil.CRAFTING_INV_ITEMS.get(i).clone();
                    ItemMeta itemMeta = item.getItemMeta();
                    if (itemMeta.hasDisplayName()) {
                        itemMeta.setDisplayName(TextProcessor.color(TextProcessor.placeholder(player, itemMeta.getDisplayName())));
                    }
                    List<String> lore = itemMeta.getLore();
                    if (lore != null) {
                        lore.replaceAll(it -> TextProcessor.color(TextProcessor.placeholder(player, it)));
                    }
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    player.getOpenInventory().getTopInventory().setItem(i, item);
                }
            });
        }, 0, 1L);
    }

    @Override
    public void disable() {
        serverClosed = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory topInventory = player.getOpenInventory().getTopInventory();
            if (topInventory.getType().equals(InventoryType.CRAFTING)) {
                topInventory.clear();
            }
        }
    }

    public void reload() {
        reloadConfig();
        ItemUtil.reload();
    }

}