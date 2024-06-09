package fusionmc.structure_music.client.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();

    public static void init() {
        File jsonConfig = new File(FabricLoader.getInstance().getConfigDir().resolve("structuremusic.config.json").toString());
        try {
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                Map<String, StructureMusicSound> defaultMap = getDefaults();
                String json = gson.toJson(defaultMap, new TypeToken<Map<String, StructureMusicSound>>(){}.getType());
                FileWriter writer = new FileWriter(jsonConfig);
                writer.write(json);
                writer.close();
            }

            registeredStructures = gson.fromJson(new FileReader(jsonConfig), new TypeToken<Map<String, StructureMusicSound>>(){}.getType());
        } catch (IOException e) {
            System.err.println("Error creating config file for structure music: " + e.getMessage());
        }
    }

    private static Map<String, StructureMusicSound> getDefaults() {
        Map<String, StructureMusicSound> ret = new HashMap<>();
        ret.put("minecraft:trial_chambers", new StructureMusicSound("structure_music:music.dungeon.trial_chambers", 6000, 18000, false));
        ret.put("minecraft:stronghold", new StructureMusicSound("structure_music:music.dungeon.stronghold", 6000, 18000, false));
        ret.put("nova_structures:lone_citadel", new StructureMusicSound("nova_structures:music.dungeon.lone_citadel", 6000, 18000, false));
        ret.put("nova_structures:toxic_lair", new StructureMusicSound("nova_structures:music.dungeon.toxic_lair", 6000, 18000, false));
        ret.put("nova_structures:undead_crypt", new StructureMusicSound("nova_structures:music.dungeon.crypt", 6000, 18000, false));
        ret.put("nova_structures:creeping_crypt", new StructureMusicSound("nova_structures:music.dungeon.crypt", 6000, 18000, false));
        return ret;
    }

    public static class StructureMusicSound {
        public final String id;
        public final int minDelay;
        public final int maxDelay;
        public final boolean replaceCurrentMusic;

        public StructureMusicSound(String id, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
            this.id = id;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
            this.replaceCurrentMusic = replaceCurrentMusic;
        }
    }
}