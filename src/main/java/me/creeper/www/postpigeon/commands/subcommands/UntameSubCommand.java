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
import me.creeper.www.postpigeon.pigeon.Pigeon;
import me.creeper.www.postpigeon.pigeon.PigeonAction;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UntameSubCommand extends PostPigeonSubCommand {

    public UntameSubCommand(final PigeonManager pigeonManager) {
        super(pigeonManager);
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "untame";
    }

    @Override
    public @NotNull String getSubCommandUsage() {
        return "untame a pigeon and release it";
    }

    @Override
    public void onSubCommand(final CommandSender sender, final String[] args) {

        if(!isPlayer(sender)) {
            return;
        }

        final Entity en = (Entity) sender;

        Common.tell(sender, PPMessage.CLICK_TO_UNTAME.get());

        pigeonManager.queuePigeonAction(en.getUniqueId(), new PigeonAction() {
            @Override
            public void execute(final Player player, final Parrot parrot) {

                final UUID pigeonUUID = parrot.getUniqueId();

                final Pigeon pigeon = pigeonManager.getPigeon(pigeonUUID);

                if(pigeon == null) {
                    Common.tell(sender, PPMessage.NOT_TAMED.get());
                    return;
                }

                for(final ItemStack item : pigeon.getPigeonInventory()) {
                    if(item != null) {
                        parrot.getWorld().dropItemNaturally(parrot.getLocation(), item);
                    }
                }

                parrot.setOwner(null);
                pigeonManager.removePigeon(parrot.getUniqueId());

                Common.tell(player, PPMessage.UNTAMED_PIGEON.get());

            }
        });

    }
}
