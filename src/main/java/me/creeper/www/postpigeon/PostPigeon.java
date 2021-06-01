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

import lombok.Getter;
import me.creeper.www.postpigeon.commands.PostPigeonCommand;
import me.creeper.www.postpigeon.language.LanguageManager;
import me.creeper.www.postpigeon.pigeon.PigeonListener;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class PostPigeon extends JavaPlugin {

    private PigeonManager pigeonManager;
    private NamespacedKey cornKey;
    private ItemStack corn;
    private ConfigManager configManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        LanguageManager.getInstance().loadLanguage(this);

        configManager = new ConfigManager(this);

        cornKey = new NamespacedKey(this, "postpigeon_corn");

        pigeonManager = new PigeonManager(this);

        initializeCorn();

        if(configManager.isCraftingEnabled()) {
            registerCornRecipe();
        }

        //noinspection ConstantConditions
        getCommand("pigeon").setExecutor(new PostPigeonCommand(pigeonManager));

        Bukkit.getPluginManager().registerEvents(new PigeonListener(pigeonManager, cornKey, configManager), this);

    }

    private void initializeCorn() {
        corn = new ItemStack(Material.PUMPKIN_SEEDS);
        final ItemMeta meta = corn.getItemMeta();
        //noinspection ConstantConditions
        meta.setDisplayName(Common.colorize(configManager.getCraftingName()));

        final List<String> lore = configManager.getCraftingLore();

        if(!lore.isEmpty()) {
            meta.setLore(lore);
        }

        meta.getPersistentDataContainer().set(cornKey, PersistentDataType.BYTE, (byte) 0);
        corn.setItemMeta(meta);
    }

    @Override
    public void onDisable() {
        pigeonManager.disable();
    }

    private void registerCornRecipe() {
        final ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, "postpigeon_corn_recipe"), corn);
        recipe.addIngredient(Material.WHEAT);
        Bukkit.addRecipe(recipe);

    }

}
