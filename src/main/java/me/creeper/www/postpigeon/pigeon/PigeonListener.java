package me.creeper.www.postpigeon.pigeon;

import lombok.AllArgsConstructor;
import lombok.val;
import me.creeper.www.postpigeon.ConfigManager;
import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

@AllArgsConstructor
public class PigeonListener implements Listener {

    private final PigeonManager pigeonManager;
    private final NamespacedKey cornKey;
    private final ConfigManager configManager;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onRightClickEntity(final PlayerInteractEntityEvent e) {

        val entity = e.getRightClicked();

        if(!(entity instanceof Parrot)) {
            return;
        }

        val player = e.getPlayer();

        final PigeonAction pigeonAction = pigeonManager.pollPigeonAction(player.getUniqueId());

        if(pigeonAction != null) {
            pigeonAction.execute(player, (Parrot) entity);
            return;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack usedItem;

        if(e.getHand() == EquipmentSlot.HAND) {
            usedItem = inventory.getItemInMainHand();
        } else {
            usedItem = inventory.getItemInOffHand();
        }

        if(usedItem.hasItemMeta()) {

            //noinspection ConstantConditions
            if(usedItem.getItemMeta().getPersistentDataContainer().has(cornKey, PersistentDataType.BYTE)) {
                if(!pigeonManager.addPigeon(e.getRightClicked().getUniqueId())) {
                    Common.markError(player, PPMessage.PIGEON_ALREADY_TAMED.get());
                } else {

                    final Tameable parrot = (Tameable) entity;
                    parrot.setOwner(player);
                    Common.tell(player, PPMessage.TAMED_PIGEON.get());

                }
                return;
            }

        }


        val pigeon = pigeonManager.getPigeon(entity.getUniqueId());

        if(pigeon != null) {

            if(configManager.isPrivatePigeons()) {

                if(!player.equals(((Tameable) entity).getOwner())) {
                    Common.tell(player, PPMessage.DONT_OWN_PIGEON.get());
                    return;
                }

            }

            e.getPlayer().openInventory(pigeon.getPigeonInventory());
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryClick(final InventoryClickEvent e) {

        if(!configManager.isWhiteListInvEnabled()) {
            return;
        }

        if(e.getCurrentItem() == null) {
            return;
        }

        val inventory = e.getInventory();

        if(!pigeonManager.isPigeonInventory(inventory)) {
            return;
        }

        final boolean containsItem = configManager.getInvItemCollection().contains(e.getCurrentItem().getType());

        if(containsItem == configManager.isWhiteListIsBlackList()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent e) {

        final Entity en = e.getEntity();

        if(!(en instanceof Parrot)) {
            return;
        }

        final Pigeon pigeon = pigeonManager.getPigeon(en.getUniqueId());

        if(pigeon == null) {
            return;
        }

        val inventory = pigeon.getPigeonInventory();

        for(final ItemStack item : inventory) {
            if(item != null) {
                en.getWorld().dropItemNaturally(en.getLocation(), item);
            }
        }

        inventory.clear();

        inventory.getViewers().forEach(humanEntity -> {
            if(humanEntity instanceof Player) {
                ((Player) humanEntity).updateInventory();
            }
        });

    }

}
