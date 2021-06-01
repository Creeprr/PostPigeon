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

package me.creeper.www.postpigeon.commands.subcommands;

import me.creeper.www.postpigeon.commands.PostPigeonSubCommand;
import me.creeper.www.postpigeon.language.LanguageManager;
import me.creeper.www.postpigeon.pigeon.PigeonManager;
import me.creeper.www.postpigeon.utils.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadSubCommand extends PostPigeonSubCommand {

    public ReloadSubCommand(final PigeonManager pigeonManager) {
        super(pigeonManager);
    }

    @Override
    public @NotNull String getSubCommandName() {
        return "reload";
    }

    @Override
    public @NotNull String getSubCommandUsage() {
        return "reload the PostPigeon config";
    }

    @Override
    public void onSubCommand(final CommandSender sender, final String[] args) {
        pigeonManager.getConfigManager().reloadConfig();
        LanguageManager.getInstance().loadLanguage(pigeonManager.getPostPigeon());
        Common.tell(sender, "&aSuccessfully reloaded the config.");
    }
}
