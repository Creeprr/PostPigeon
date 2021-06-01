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

package me.creeper.www.postpigeon.pigeon;

import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@SuppressWarnings("NewClassNamingConvention")
public class Pigeon {

    private final Inventory pigeonInventory;

    Pigeon(@Nullable final Map<Integer, ItemStack> itemMap, final int size) {

        pigeonInventory = Bukkit.createInventory(null, size * 9, Common.colorize(PPMessage.PIGEON_INVENTORY.get()));

        if(itemMap != null) {
            itemMap.forEach(pigeonInventory::setItem);
        }

    }

    public Inventory getPigeonInventory() {
        return pigeonInventory;
    }

}
