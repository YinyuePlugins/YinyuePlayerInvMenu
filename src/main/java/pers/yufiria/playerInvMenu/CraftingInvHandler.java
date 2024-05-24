package pers.yufiria.playerInvMenu;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
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
        int slot = event.getSlot();
        if (ItemUtil.CRAFTING_INV_ACTIONS.containsKey(slot)) {
            event.setCancelled(true);
            ItemUtil.CRAFTING_INV_ACTIONS.get(slot).run((Player) event.getWhoClicked(), PlayerInvMenu.INSTANCE);
        }
    }

    @EventHandler
    public void onClickRecipeBook(PlayerRecipeBookClickEvent event) {
        Player player = event.getPlayer();
        InventoryType topInvType = player.getOpenInventory().getTopInventory().getType();
        if (topInvType.equals(InventoryType.CRAFTING)) {
            event.setCancelled(true);
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
