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

package me.creeper.www.postpigeon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

@Getter
public class ConfigManager {

    @Getter(AccessLevel.NONE)
    private final Logger logger = Bukkit.getLogger();

    @Getter(AccessLevel.NONE)
    private final PostPigeon postPigeon;

    private boolean privatePigeons;

    private int invSize;
    private boolean whiteListInvEnabled;
    private boolean whiteListIsBlackList;
    private final Collection<Material> invItemCollection = EnumSet.noneOf(Material.class);

    private boolean craftingEnabled;
    private String craftingName;
    private final List<String> craftingLore = new ArrayList<>();

    ConfigManager(final PostPigeon postPigeon) {
        this.postPigeon = postPigeon;
        loadConfig();
    }

    public void reloadConfig() {
        postPigeon.reloadConfig();
        invItemCollection.clear();
        craftingLore.clear();
        loadConfig();
    }

    private void loadConfig() {
        final FileConfiguration config = postPigeon.getConfig();

        privatePigeons = config.getBoolean("private-pigeons");

        val pigeonInvPrefix = "pigeon-inventory.";
        val pigeonInvWhitelist = pigeonInvPrefix + "whitelist.";

        invSize = config.getInt(pigeonInvPrefix + "size");
        whiteListInvEnabled = config.getBoolean(pigeonInvWhitelist + "enabled");
        whiteListIsBlackList = config.getBoolean(pigeonInvWhitelist + "whitelist-is-blacklist");
        config.getStringList(pigeonInvWhitelist + "items").forEach(item -> {
            try {
                invItemCollection.add(Material.valueOf(item));
            } catch(final IllegalArgumentException e) {
                logger.warning(item + " is not a valid item in pigeon inventory whitelist! Skipping this item.");
            }
        });

        val tamingSection = config.getConfigurationSection("taming");

        if(tamingSection == null) {
            logger.warning("Taming section is null.");
            return;
        }

        craftingEnabled = tamingSection.getBoolean("crafting");
        craftingName = tamingSection.getString("name");

        tamingSection.getStringList("lore").forEach(s -> craftingLore.add(Common.colorize(s)));

    }


}
