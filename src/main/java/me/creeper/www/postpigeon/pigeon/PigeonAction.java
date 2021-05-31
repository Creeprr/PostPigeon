package me.creeper.www.postpigeon.pigeon;

import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

public abstract class PigeonAction {

    private final long creationTime;

    public PigeonAction() {
        creationTime = System.currentTimeMillis();
    }

    public abstract void execute(Player player, Parrot pigeon);

    public long getCreationTime() {
        return creationTime;
    }
}
