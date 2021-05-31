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
