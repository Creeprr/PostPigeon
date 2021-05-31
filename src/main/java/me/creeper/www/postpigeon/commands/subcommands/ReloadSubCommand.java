package me.creeper.www.postpigeon.commands.subcommands;

import me.creeper.www.postpigeon.commands.PostPigeonSubCommand;
import me.creeper.www.postpigeon.language.LanguageManager;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadSubCommand extends PostPigeonSubCommand {

    public ReloadSubCommand(final PigeonManager pigeonManager) {
        super(pigeonManager);
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "reload";
    }

    @Override
    public @NotNull String getSubCommandUsage() {
        return "reload the PostPigeon config";
    }

    @Override
    public void onSubCommand(final CommandSender sender, final String[] args) {
        pigeonManager.getConfigManager().reloadConfig();
        LanguageManager.getInstance().loadLanguage(pigeonManager.getPostPigeon());
        Common.tell(sender, "&aSuccessfully reloaded the config.");
    }
}
