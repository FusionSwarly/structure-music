package fusionmc.structure_music.client.registry;

import fusionmc.structure_music.client.config.ModConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class StructureMusicSounds {
    public static Map<String, MusicSound> structures = new HashMap<>();
    private static MusicSound registerSound(String id, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        Identifier sound_id = Identifier.of(id);
        SoundEvent music = SoundEvent.of(sound_id);
        System.out.println("Registering structure music: "+id);
        return new MusicSound(Registry.registerReference(Registries.SOUND_EVENT, sound_id, music), minDelay, maxDelay, replaceCurrentMusic);
    }

    public static void registerMusic() {
        for(Map.Entry<String, ModConfig.StructureMusicSound> structure : ModConfig.registeredStructures.entrySet()) {
            ModConfig.StructureMusicSound musicSound = structure.getValue();
            structures.put(structure.getKey(), registerSound(musicSound.id, musicSound.minDelay, musicSound.maxDelay, musicSound.replaceCurrentMusic));
        }
    }
}
