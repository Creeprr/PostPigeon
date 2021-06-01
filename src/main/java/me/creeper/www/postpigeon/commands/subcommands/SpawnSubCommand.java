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

import lombok.var;
import me.creeper.www.postpigeon.commands.PostPigeonSubCommand;
import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnSubCommand extends PostPigeonSubCommand {

    public SpawnSubCommand(final PigeonManager pigeonManager) {
        super(pigeonManager);
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "spawn";
    }

    @Override
    public @NotNull String getSubCommandUsage() {
        return "spawn a pigeon";
    }

    @Override
    public void onSubCommand(final CommandSender sender, final String[] args) {

        if(!isPlayer(sender)) {
            return;
        }

        var variant = Parrot.Variant.GRAY;

        if(args.length > 0) {

            try {
                variant = Parrot.Variant.valueOf(args[0].toUpperCase());
            } catch(final IllegalArgumentException e) {
                Common.tell(sender, PPMessage.UNKNOWN_PIGEON_VARIANT.get(args[0]));
                for(final Parrot.Variant value : Parrot.Variant.values()) {
                    Common.tell(sender, "&7 - &a" + value.name());
                }
                return;
            }

        }

        final Player p = (Player) sender;
        final Parrot parrot = (Parrot) p.getWorld().spawnEntity(p.getLocation(), EntityType.PARROT);
        parrot.setVariant(variant);
        parrot.setOwner(p);
        pigeonManager.addPigeon(parrot.getUniqueId());

    }
}
