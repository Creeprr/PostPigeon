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

import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

public abstract class PigeonAction {

    private final long creationTime;

    protected PigeonAction() {
        creationTime = System.currentTimeMillis();
    }

    public abstract void execute(Player player, Parrot pigeon);

    public long getCreationTime() {
        //TODO: Add creation time
        return creationTime;
    }
}
