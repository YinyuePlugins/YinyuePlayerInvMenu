package pers.yufiria.playerInvMenu;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
import crypticlib.CrypticLib;
import crypticlib.listener.BukkitListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import pers.yufiria.playerInvMenu.util.ItemUtil;
import pers.yufiria.playerInvMenu.util.PlayerUtil;

@BukkitListener
public class CraftingInvHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!PlayerUtil.isPlayerSurvival(player)) {
            return;
        }
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
            CrypticLib.platform().scheduler().runTask(PlayerInvMenu.INSTANCE, () -> {
                PlayerInvMenu.INSTANCE.sendMenuItems(player);
                ItemUtil.CRAFTING_INV_ACTIONS.get(slot).run(player, PlayerInvMenu.INSTANCE);
            });
        }
    }

    @EventHandler
    public void onClickRecipeBook(PlayerRecipeBookClickEvent event) {
        Player player = event.getPlayer();
        if (!PlayerUtil.isPlayerSurvival(player)) {
            return;
        }
        InventoryType topInvType = player.getOpenInventory().getTopInventory().getType();
        if (topInvType.equals(InventoryType.CRAFTING)) {
            event.setCancelled(true);
        }
    }

}
