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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isPlayer(final CommandSender sender) {

        if(!(sender instanceof Player)) {
            Common.tell(sender, PPMessage.ONLY_PLAYERS.get());
            return false;
        }

        return true;
    }

}
