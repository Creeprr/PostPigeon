package me.creeper.www.postpigeon.commands;

import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PostPigeonSubCommand {

    protected PigeonManager pigeonManager;

    public PostPigeonSubCommand(final PigeonManager pigeonManager) {
        this.pigeonManager = pigeonManager;
    }

    @NotNull
    public abstract String getSubCommandName();

    @NotNull
    public abstract String getSubCommandUsage();

    @NotNull
    String getPermission() {
        return "postpigeon." + getSubCommandName();
    }

    public abstract void onSubCommand(final CommandSender sender, final String[] args);

    protected boolean isPlayer(final CommandSender sender) {

        if(!(sender instanceof Player)) {
            Common.tell(sender, PPMessage.ONLY_PLAYERS.get());
            return false;
        }

        return true;
    }

}
