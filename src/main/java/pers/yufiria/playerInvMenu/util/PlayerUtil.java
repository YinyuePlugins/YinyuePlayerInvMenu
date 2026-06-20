package pers.yufiria.playerInvMenu.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isPlayerSurvival(Player player) {
        GameMode gameMode = player.getGameMode();
        if (gameMode.equals(GameMode.SURVIVAL)) {
            return true;
        }
        return gameMode.equals(GameMode.ADVENTURE);
    }

}
