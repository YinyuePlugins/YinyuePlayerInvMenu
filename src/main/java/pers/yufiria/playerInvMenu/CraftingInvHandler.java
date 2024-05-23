package pers.yufiria.playerInvMenu;

import crypticlib.listener.BukkitListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

@BukkitListener
public class CraftingInvHandler implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.getType().equals(InventoryType.CRAFTING)) {
            topInventory.clear();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (!topInventory.getType().equals(InventoryType.CRAFTING)) {
            return;
        }
        if (topInventory != event.getClickedInventory()) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getSlot();
        if (ItemUtil.CRAFTING_INV_ACTIONS.containsKey(slot)) {
            ItemUtil.CRAFTING_INV_ACTIONS.get(slot).run((Player) event.getWhoClicked(), PlayerInvMenu.INSTANCE);
        }
    }

    //存在问题，会影响玩家背包拖动物品
//    @EventHandler
//    public void onDrag(InventoryDragEvent event) {
//        Inventory topInventory = event.getView().getTopInventory();
//        if (!topInventory.getType().equals(InventoryType.CRAFTING)) {
//            return;
//        }
//        if (topInventory != event.getInventory()) {
//            return;
//        }
//        event.setCancelled(true);
//    }

}
