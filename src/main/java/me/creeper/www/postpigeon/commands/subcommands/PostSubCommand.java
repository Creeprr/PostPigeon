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

package me.creeper.www.postpigeon.commands.subcommands;

import me.creeper.www.postpigeon.commands.PostPigeonSubCommand;
import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.pigeon.PigeonAction;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PostSubCommand extends PostPigeonSubCommand {

    public PostSubCommand(final PigeonManager pigeonManager) {
        super(pigeonManager);
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "post";
    }

    @Override
    public @NotNull String getSubCommandUsage() {
        return "send a pigeon to another player";
    }

    @Override
    public void onSubCommand(final CommandSender sender, final String[] args) {

        if(!isPlayer(sender)) {
            return;
        }

        if(args.length < 1) {
            Common.tell(sender, PPMessage.TYPE_NAME_OF_PLAYER.get());
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            Common.tell(sender, PPMessage.NO_PLAYER_WITH_NAME.get(args[0]));
            return;
        }

        final Entity en = (Entity) sender;

        Common.tell(sender, PPMessage.CLICK_ON_PIGEON_TO_SEND.get(args[0]));

        pigeonManager.queuePigeonAction(en.getUniqueId(), new PigeonAction() {
            @Override
            public void execute(final Player player, final Parrot pigeon) {

                if(!target.isOnline()) {
                    Common.tell(sender, PPMessage.PLAYER_NO_LONGER_ONLINE.get(args[0]));
                    return;
                }

                pigeon.setOwner(target);
                Common.tell(sender, PPMessage.SENDING_PIGEON_TO.get(target.getName()));
                Common.tell(target, PPMessage.RECEIVING_PIGEON_FROM.get(sender.getName()));

            }
        });
    }

}
