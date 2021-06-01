/*
 *  Copyright (C) 2021 Creeprr
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/
 */

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
