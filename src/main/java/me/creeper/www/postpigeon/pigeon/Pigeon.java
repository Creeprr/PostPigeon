package me.creeper.www.postpigeon.pigeon;

import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Pigeon {

    private final Inventory pigeonInventory;

    public Pigeon(@Nullable final Map<Integer, ItemStack> itemMap, final int size) {

        pigeonInventory = Bukkit.createInventory(null, size * 9, Common.colorize(PPMessage.PIGEON_INVENTORY.get()));

        if(itemMap != null) {
            itemMap.forEach(pigeonInventory::setItem);
        }

    }

    public Inventory getPigeonInventory() {
        return pigeonInventory;
    }

}
