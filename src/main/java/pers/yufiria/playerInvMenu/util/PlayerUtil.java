package pers.yufiria.playerInvMenu.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isPlayerSurvival(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return false;
        }
        return !player.getGameMode().equals(GameMode.SPECTATOR);
    }

}
