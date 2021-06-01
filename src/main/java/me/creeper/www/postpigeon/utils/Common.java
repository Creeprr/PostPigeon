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

package me.creeper.www.postpigeon.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("NewClassNamingConvention")
@UtilityClass
public class Common {

    public String colorize(final String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public void tell(final CommandSender sender, final String msg) {
        sender.sendMessage(colorize(msg));
    }

    public void markError(final Player p, final String msg) {
        tell(p, msg);
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
    }

}
