package pers.yufiria.playerInvMenu;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import crypticlib.chat.TextProcessor;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过 packetevents 拦截 WINDOW_ITEMS 包，将玩家 2x2 合成格（slots 0-4）
 * 替换为配置中定义的虚拟菜单物品，实现客户端侧的自定义菜单显示。
 */
public class PlayerInvMenuPacketListener implements PacketListener {

    /**
     * 拦截服务端发往客户端的 Window Items 包。
     * 当玩家打开背包（CRAFTING 类型）时，服务端会发送该包。
     * 我们在此将 slot 0-4 的物品替换为菜单物品（仅客户端可见）。
     */
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.WINDOW_ITEMS) {
            return;
        }

        Player player = (Player) event.getPlayer();
        if (player == null) {
            return;
        }

        // 仅在玩家打开的是自带合成背包（CRAFTING）时修改
        if (!player.getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)) {
            return;
        }

        // 检查是否有菜单物品需要显示
        if (ItemUtil.CRAFTING_INV_ITEMS.isEmpty()) {
            return;
        }

        WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(event);
        List<ItemStack> items = packet.getItems();

        // 替换 slot 0-4 中有配置的菜单物品
        boolean modified = false;
        for (int i = 0; i < 5 && i < items.size(); i++) {
            org.bukkit.inventory.ItemStack bukkitItem = ItemUtil.CRAFTING_INV_ITEMS.get(i);
            if (bukkitItem != null) {
                items.set(i, convertAndProcessItem(bukkitItem, player));
                modified = true;
            }
        }

        if (modified) {
            packet.setItems(items);
            // 标记包需要重新编码，否则修改不会生效
            event.markForReEncode(true);
        }
    }

    /**
     * 将 Bukkit ItemStack 转换为 packetevents ItemStack，
     * 并处理占位符和颜色代码。
     */
    public static ItemStack convertAndProcessItem(org.bukkit.inventory.ItemStack bukkitItem, Player player) {
        org.bukkit.inventory.ItemStack item = bukkitItem.clone();
        org.bukkit.inventory.meta.ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(TextProcessor.color(TextProcessor.placeholder(player, itemMeta.getDisplayName())));
            }
            List<String> lore = itemMeta.getLore();
            if (lore != null) {
                lore.replaceAll(it -> TextProcessor.color(TextProcessor.placeholder(player, it)));
                itemMeta.setLore(lore);
            }
            item.setItemMeta(itemMeta);
        }
        return SpigotConversionUtil.fromBukkitItemStack(item);
    }

    /**
     * 将 Bukkit ItemStack 列表转换为 packetevents ItemStack 列表。
     * null 元素会被转换为 ItemStack.EMPTY。
     */
    public static List<ItemStack> convertItems(java.util.Map<Integer, ? extends org.bukkit.inventory.ItemStack> source) {
        List<ItemStack> result = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            org.bukkit.inventory.ItemStack bukkitItem = source.get(i);
            if (bukkitItem != null) {
                result.add(SpigotConversionUtil.fromBukkitItemStack(bukkitItem));
            } else {
                result.add(ItemStack.EMPTY);
            }
        }
        return result;
    }
}
