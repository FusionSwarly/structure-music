package fusionmc.structure_music.client;

import fusionmc.structure_music.client.config.ModConfig;
import fusionmc.structure_music.client.registry.StructureMusicSounds;
import fusionmc.structure_music.network.StructureChangedPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ClientStructureMusic implements ClientModInitializer {
    public void onInitializeClient() {
        ModConfig.init();
        StructureMusicSounds.registerMusic();
        ClientPlayNetworking.registerGlobalReceiver(StructureChangedPayload.ID, ((payload, context) -> {
            context.client().execute(() -> {
                ClientStructureMusic.setStructureMusic(payload.structureName().toString());
            });
        }));

    }
    @Nullable
    public static MusicSound structureMusic = null;
    @Nullable
    public static MusicSound getStructureMusic() {
        return structureMusic;
    };

    public static void setStructureMusic(String structure) {
        //System.out.println("Setting current structure to " + structure);
        if (structure == null || Objects.equals(structure, "minecraft:none")) {
            structureMusic = null;
            return;
        }
        structureMusic = StructureMusicSounds.structures.get(structure);
    }
}
