package pers.yufiria.playerInvMenu;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import crypticlib.BukkitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import pers.yufiria.playerInvMenu.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayerInvMenu extends BukkitPlugin {

    public static PlayerInvMenu INSTANCE;
    public boolean serverClosed = false;
    private PlayerInvMenuPacketListener packetListener;
    private PacketListenerCommon packetListenerCommon;

    @Override
    public void enable() {
        serverClosed = false;
        INSTANCE = this;

        // 注册 packetevents 包监听器
        packetListener = new PlayerInvMenuPacketListener();
        packetListenerCommon = PacketEvents.getAPI().getEventManager().registerListener(packetListener, PacketListenerPriority.NORMAL);

        // 加载配置
        ItemUtil.reload();
    }

    @Override
    public void disable() {
        serverClosed = true;

        // 注销 packetevents 监听器
        if (packetListener != null) {
            PacketEvents.getAPI().getEventManager().unregisterListener(packetListenerCommon);
            packetListener = null;
        }

        // 清理所有在线玩家的合成格显示
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

    /**
     * 通过 packetevents 向指定玩家发送修改后的 Window Items 包，
     * 将 2x2 合成格的 slot 0-4 替换为菜单物品。
     * 这是纯客户端侧操作，不修改服务端背包数据。
     */
    public void sendMenuItems(Player player) {
        // 获取玩家当前打开的合成背包的实际物品数据
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (!topInventory.getType().equals(InventoryType.CRAFTING)) {
            return;
        }

        // 构造 items 列表（slot 0-4 使用菜单物品，其余使用背包原始物品）
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < topInventory.getSize(); i++) {
            if (i < 5 && ItemUtil.CRAFTING_INV_ITEMS.containsKey(i)) {
                // 使用菜单物品（已处理占位符和颜色）
                items.add(PlayerInvMenuPacketListener.convertAndProcessItem(
                        ItemUtil.CRAFTING_INV_ITEMS.get(i), player));
            } else {
                // 使用背包原始物品
                org.bukkit.inventory.ItemStack bukkitItem = topInventory.getItem(i);
                if (bukkitItem != null) {
                    items.add(io.github.retrooper.packetevents.util.SpigotConversionUtil.fromBukkitItemStack(bukkitItem));
                } else {
                    items.add(ItemStack.EMPTY);
                }
            }
        }

        // 发送伪造的 Window Items 包给客户端
        // windowId = 0 表示玩家自己的背包窗口
        WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(
                0,    // windowId: 玩家自带背包
                0,    // stateId
                items,
                ItemStack.EMPTY  // carriedItem: 光标上的物品（无）
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}
