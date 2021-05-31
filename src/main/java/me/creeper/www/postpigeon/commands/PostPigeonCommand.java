package me.creeper.www.postpigeon.commands;

import me.creeper.www.postpigeon.commands.subcommands.*;
import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class PostPigeonCommand implements CommandExecutor {

    private final Map<String, PostPigeonSubCommand> subCommandMap = new LinkedHashMap<>();

    public PostPigeonCommand(final PigeonManager pigeonManager) {

        addSubCommands(
                new PostSubCommand(pigeonManager),
                new UntameSubCommand(pigeonManager),
                new SpawnSubCommand(pigeonManager),
                new CornSubCommand(pigeonManager),
                new ReloadSubCommand(pigeonManager)
        );

    }

    private void addSubCommands(final PostPigeonSubCommand... commands) {

        for(final PostPigeonSubCommand cmd : commands) {
            subCommandMap.put(cmd.getSubCommandName(), cmd);
        }

    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {

        if(args.length < 1 || args[0].equalsIgnoreCase("help")) {

            Common.tell(sender, "&7  ---------- &aPostPigeon &7----------  ");
            subCommandMap.values().stream()
                    .filter(subCommand -> sender.hasPermission(subCommand.getPermission()))
                    .forEach(subCommand -> Common.tell(sender, "&a/pigeon " + subCommand.getSubCommandName() + "&7 - " + subCommand.getSubCommandUsage()));

            return true;
        }

        final PostPigeonSubCommand subCommand = subCommandMap.get(args[0]);

        if(subCommand != null) {
            subCommand.onSubCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            Common.tell(sender, PPMessage.UNKNOWN_COMMAND.get());
        }

        return true;
    }
}
