package pers.yufiria.playerInvMenu;

import crypticlib.chat.MsgSender;
import crypticlib.command.CommandHandler;
import crypticlib.command.CommandInfo;
import crypticlib.command.SubcommandHandler;
import crypticlib.command.annotation.Command;
import crypticlib.command.annotation.Subcommand;
import crypticlib.perm.PermInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Command
public class PlayerInvMenuCommand extends CommandHandler {

    public PlayerInvMenuCommand() {
        super(new CommandInfo("player-inv-menu"));
    }

    @Subcommand
    SubcommandHandler reload = new SubcommandHandler("reload", new PermInfo("player-inv-menu.reload")) {
        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull List<String> args) {
            PlayerInvMenu.INSTANCE.reload();
            MsgSender.sendMsg(sender, "[PlayerInvMenu] Reloaded");
            return true;
        }
    };

}
