package me.creeper.www.postpigeon.commands.subcommands;

import me.creeper.www.postpigeon.commands.PostPigeonSubCommand;
import me.creeper.www.postpigeon.language.PPMessage;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CornSubCommand extends PostPigeonSubCommand {

    public CornSubCommand(final PigeonManager pigeonManager) {
        super(pigeonManager);
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "corn";
    }

    @Override
    public @NotNull String getSubCommandUsage() {
        return "get a corn item to tame pigeons";
    }

    @Override
    public void onSubCommand(final CommandSender sender, final String[] args) {

        if(!isPlayer(sender)) {
            return;
        }

        final Player p = (Player) sender;

        final ItemStack corn = pigeonManager.getPostPigeon().getCorn().clone();

        if(args.length > 0) {

            try {
                corn.setAmount(Integer.parseInt(args[0]));
            } catch(final IllegalArgumentException e) {
                Common.tell(sender, PPMessage.NOT_A_VALID_NUMBER.get(args[0]));
                return;
            }

        }

        final Map<Integer, ItemStack> rest = p.getInventory().addItem(corn);

        if(!rest.isEmpty()) {
            rest.values().forEach(itemStack -> p.getWorld().dropItemNaturally(p.getLocation(), itemStack));
            Common.tell(sender, PPMessage.CORN_COULD_NOT_FIT.get());
            return;
        }

        Common.tell(sender, PPMessage.ADDED_CORN.get(String.valueOf(corn.getAmount())));

    }
}
