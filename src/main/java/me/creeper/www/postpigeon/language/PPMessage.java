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

package me.creeper.www.postpigeon.language;

import lombok.var;

public enum PPMessage {

    NOT_A_VALID_NUMBER("&c'%arg1%' is not a valid number!"),
    CORN_COULD_NOT_FIT("&cSome corn could not fit in your inventory, so it has been dropped on the ground."),
    ADDED_CORN("&aSuccessfully added '%arg1%' corn to your inventory."),
    TYPE_NAME_OF_PLAYER("&cPlease type the name of the player you wish to send this pigeon to."),
    NO_PLAYER_WITH_NAME("&cThere is no player online with the name '%arg1%'."),
    CLICK_ON_PIGEON_TO_SEND("&aClick on a pigeon to send it to '%arg1%'."),
    PLAYER_NO_LONGER_ONLINE("&cThe player '%arg1%' is no longer online."),
    SENDING_PIGEON_TO("&aSending the pigeon to %arg1%!"),
    RECEIVING_PIGEON_FROM("&aYou should receive a pigeon from %arg1% very soon!"),
    RELOADED_CONFIG("&aSuccessfully reloaded the config."),
    UNKNOWN_PIGEON_VARIANT("&c'%arg1%' is not a valid pigeon variant. Options are as following:"),
    CLICK_TO_UNTAME("&aClick on the pigeon you wish to untame."),
    NOT_TAMED("&cThe chosen pigeon is not tamed."),
    UNTAMED_PIGEON("&aUntamed the chosen pigeon!"),
    UNKNOWN_COMMAND("&cUnknown pigeon command, see /pigeon help for help."),
    ONLY_PLAYERS("&cOnly players can execute this command!"),
    PIGEON_INVENTORY("&2Pigeon Inventory"),
    PIGEON_ALREADY_TAMED("&cThat pigeon is already tamed!"),
    TAMED_PIGEON("&aSuccessfully tamed a pigeon!"),
    DONT_OWN_PIGEON("&cYou do not own this pigeon!");


    private final String fallBack;

    PPMessage(final String fallBack) {
        this.fallBack = fallBack;
    }

    public String get() {
        return LanguageManager.getInstance().get(this);
    }

    public String get(final String... args) {

        var msg = get();

        int count = 0;

        for(final String arg : args) {
            count++;
            msg = msg.replace("%arg" + count + "%", arg);
        }

        return msg;

    }

    public String getFallBack() {
        return fallBack;
    }


}
