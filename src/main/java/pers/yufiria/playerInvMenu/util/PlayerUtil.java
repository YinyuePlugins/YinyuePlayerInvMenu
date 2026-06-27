package pers.yufiria.playerInvMenu.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isPlayerSurvival(Player player) {
        return isSurvivalMode(player.getGameMode());
    }

    public static boolean isSurvivalMode(GameMode gameMode) {
        return gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE;
    }

}
