package me.creeper.www.postpigeon.pigeon;

import lombok.Getter;
import lombok.val;
import me.creeper.www.postpigeon.ConfigManager;
import me.creeper.www.postpigeon.PostPigeon;
import org.apache.logging.log4j.core.util.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class PigeonManager {

    private final Map<UUID, Pigeon> pigeonMap = new HashMap<>();
    private final Map<Inventory, UUID> inventoryUUIDMap = new HashMap<>();
    private final Logger logger = Bukkit.getLogger();
    private final File pigeonDataFolder;
    private final Map<UUID, PigeonAction> actionCache = new HashMap<>();
    @Getter
    private final PostPigeon postPigeon;
    @Getter
    private final ConfigManager configManager;

    public PigeonManager(final PostPigeon postPigeon) {
        this.postPigeon = postPigeon;
        pigeonDataFolder = new File(postPigeon.getDataFolder(), "pigeon-data");
        configManager = postPigeon.getConfigManager();
        loadPigeonData();
    }

    private void loadPigeonData() {
        if(!pigeonDataFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            pigeonDataFolder.mkdirs();
            return;
        }

        if(!pigeonDataFolder.isDirectory()) {
            Bukkit.getPluginManager().disablePlugin(postPigeon);
            throw new IllegalArgumentException("Pigeon datafolder is not a directory!");
        }

        //noinspection ConstantConditions
        for(final File file : pigeonDataFolder.listFiles()) {

            final String fileName = file.getName();

            final UUID uuid = UUID.fromString(fileName.substring(0, fileName.length() - 4));

            if(Bukkit.getEntity(uuid) == null) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
                continue;
            }

            final String content;

            try {
                content = IOUtils.toString(new FileReader(file));
            } catch(final IOException e) {
                e.printStackTrace();
                continue;
            }

            try {
                addPigeonToMaps(uuid, new Pigeon(itemsFromBase64(content), configManager.getInvSize()));
            } catch(final IOException | ClassNotFoundException e) {
                logger.warning("Could not load data for pigeon with UUID: " + uuid);
                e.printStackTrace();
            }

        }
    }

    /**
     * @param uuid The UUID of the pigeon entity which should be added as a pigeon
     * @return returns true if the pigeon was added, false if it already exists.
     */
    public boolean addPigeon(final UUID uuid) {

        if(!pigeonMap.containsKey(uuid)) {
            addPigeonToMaps(uuid, new Pigeon(null, configManager.getInvSize()));
            return true;
        }

        return false;

    }

    private void addPigeonToMaps(final UUID uuid, final Pigeon pigeon) {
        pigeonMap.put(uuid, pigeon);
        inventoryUUIDMap.put(pigeon.getPigeonInventory(), uuid);
    }

    /**
     * @param uuid the uuid of the pigeon entity which should be removed.
     */
    public void removePigeon(final @NotNull UUID uuid) {
        final Pigeon pigeon = pigeonMap.remove(uuid);

        if(pigeon != null) {
            inventoryUUIDMap.remove(pigeon.getPigeonInventory());
        }
    }

    @Nullable
    public Pigeon getPigeon(final UUID uuid) {
        return pigeonMap.get(uuid);
    }

    @Nullable
    public Pigeon getPigeon(final Inventory inventory) {
        final UUID uuid = getPigeonUUID(inventory);
        return uuid == null ? null : getPigeon(uuid);
    }

    public boolean isPigeonInventory(final Inventory inventory) {
        return inventoryUUIDMap.containsKey(inventory);
    }

    @Nullable
    public UUID getPigeonUUID(final Inventory inventory) {
        return inventoryUUIDMap.get(inventory);
    }

    @Nullable
    public PigeonAction pollPigeonAction(final UUID uuid) {
        return actionCache.remove(uuid);
    }

    public void disable() {

        try {
            FileUtils.cleanDirectory(pigeonDataFolder);
        } catch(final IOException e) {
            logger.warning("Could not clean pigeon datafolder!");
            e.printStackTrace();
        }

        for(final UUID uuid : pigeonMap.keySet()) {
            try {
                savePigeonData(uuid);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param uuid the UUID of the pigeon
     * @throws IllegalArgumentException if there is no pigeon with specified UUID
     */
    private void savePigeonData(@NotNull final UUID uuid) throws IOException {

        final Pigeon pigeon = pigeonMap.get(uuid);

        final File pigeonDataFile = new File(pigeonDataFolder, uuid + ".dat");

        if(!pigeonDataFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            pigeonDataFile.createNewFile();
        }

        final Inventory pigeonInventory = pigeon.getPigeonInventory();

        final Map<Integer, ItemStack> contentMap = new HashMap<>(pigeonInventory.getSize());

        for(int i = 0; i < pigeonInventory.getSize(); i++) {
            contentMap.put(i, pigeonInventory.getItem(i));
        }

        final String serializedInventory = itemsToBase64(contentMap);

        final FileWriter fileWriter = new FileWriter(pigeonDataFile);
        fileWriter.write(serializedInventory);
        fileWriter.close();

    }

    private String itemsToBase64(@NotNull final Map<Integer, ItemStack> itemMap) throws IOException {
        val byteArrayOutputStream = new ByteArrayOutputStream();
        val bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

        bukkitObjectOutputStream.writeInt(itemMap.size());

        for(final ItemStack item : itemMap.values()) {
            bukkitObjectOutputStream.writeObject(item);
        }

        bukkitObjectOutputStream.close();
        return Base64Coder.encodeLines(byteArrayOutputStream.toByteArray());
    }

    private Map<Integer, ItemStack> itemsFromBase64(final String data) throws IOException, ClassNotFoundException {
        val byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        val bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);

        final int size = bukkitObjectInputStream.readInt();

        val itemMap = new HashMap<Integer, ItemStack>(size);

        for(int i = 0; i < size; i++) {
            itemMap.put(i, (ItemStack) bukkitObjectInputStream.readObject());
        }

        bukkitObjectInputStream.close();
        return itemMap;
    }

    public void queuePigeonAction(final UUID uuid, final PigeonAction action) {
        actionCache.put(uuid, action);
    }
}
