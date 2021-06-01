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

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LanguageManager {

    private static final LanguageManager INSTANCE = new LanguageManager();

    private final Map<PPMessage, String> translatedMap = new EnumMap<>(PPMessage.class);
    private final Logger logger = Bukkit.getLogger();

    public void loadLanguage(final Plugin plugin) {

        translatedMap.clear();

        loadPreCreatedLanguageFolders(plugin);

        final String fileName = plugin.getConfig().getString("language-file");

        final File directory = new File(plugin.getDataFolder() + "/languages");

        final File file = new File(directory, fileName + ".yml");

        if(!file.exists()) {
            try {
                //Ignored because we already know the file exists.
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }

        final FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

        final Set<String> keys = fc.getKeys(false);
        final PPMessage[] messages = PPMessage.values();

        final int keyAmount = keys.size();

        if(keyAmount < messages.length) {
            for(final PPMessage msg : messages) {
                if(!fc.contains(msg.toString())) {

                    final String fallBack = msg.getFallBack();

                    translatedMap.put(msg, fallBack);
                    fc.set(msg.toString(), fallBack);
                } else {
                    translatedMap.put(msg, fc.getString(msg.toString()));
                }
            }

            try {

                final LangFile langFile = LangFile.fromString(fileName + ".yml");

                if(langFile != null) {

                    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    final LocalDateTime now = LocalDateTime.now();

                    fc.options().header("Language translation by " + langFile.getAuthor() + ". Last update: " + dtf.format(now) + "\n"
                            + "This file has been updated with new messages and is therefore not\n"
                            + "up to date with translations. We will attempt to keep language files up to date\n"
                            + "But we must respect the time of the translators. :)");
                }

                fc.save(file);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        } else {
            for(final PPMessage msg : messages) {
                translatedMap.put(msg, fc.getString(msg.toString()));
            }
        }

    }

    private void loadPreCreatedLanguageFolders(final Plugin plugin) {

        final File outDir = new File(plugin.getDataFolder() + "/languages");

        if(!outDir.exists()) {
            if(!outDir.mkdirs()) {
                throw new IllegalArgumentException("Could not create directories for " + outDir.getPath());
            }
        }

        for(final LangFile lf : LangFile.values()) {

            final String path = lf.getPath();

            val file = new File(outDir, path);

            if(!file.exists()) {
                try {
                    savePPResource(path, plugin);
                } catch(final IOException e) {
                    logger.log(Level.SEVERE, "Could not save " + path, e);
                    e.printStackTrace();
                }
            }

        }


    }

    /**
     * Copied and edited from JavaPlugin#saveResource(...)
     */
    private void savePPResource(final String resourcePath, final Plugin plugin) throws IOException {
        final InputStream in = plugin.getResource("languages/" + resourcePath);

        if(in == null) {
            throw new IllegalArgumentException("Could not find a resource with the path 'languages/" + resourcePath + "'");
        }

        val outFile = new File(plugin.getDataFolder() + "/languages", resourcePath);

        final OutputStream out = new FileOutputStream(outFile);
        final byte[] buf = new byte[1024];
        int len;
        while((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }

    String get(final PPMessage msg) {
        return translatedMap.get(msg);
    }

    public static LanguageManager getInstance() {
        return INSTANCE;
    }
}
