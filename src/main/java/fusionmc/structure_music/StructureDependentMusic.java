package fusionmc.structure_music;

import fusionmc.structure_music.client.ClientStructureMusic;
import fusionmc.structure_music.network.StructureChangedPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StructureDependentMusic implements ModInitializer {
    public static Map<PlayerEntity, Identifier> playerStructures = new HashMap<>();
    private static int ticks = 0;
    public void setCurrentStructure(PlayerEntity player, World world, String structure) {
        @Nullable
        Identifier oldStructure = playerStructures.get(player);
        Identifier newStructure = Identifier.of("none");
        if (!Objects.equals(structure, null)) {
            newStructure = Identifier.of(structure);
        }
        if (Objects.equals(oldStructure, newStructure)) {
            return;
        }
        //System.out.println("(Player " + player.getNameForScoreboard() + ") Setting new structure to: " + newStructure + ", old: " + oldStructure);
        playerStructures.put(player, newStructure);
        if (world.isClient()) {
            ClientStructureMusic.setStructureMusic(structure);
            return;
        }
        ServerPlayNetworking.send((ServerPlayerEntity) player, new StructureChangedPayload(newStructure));
    }
    public void checkAllStructures(ServerWorld world, PlayerEntity player) {
        Registry<Structure> registry = world.getStructureAccessor().getRegistryManager().get(RegistryKeys.STRUCTURE);
        IndexedIterable<RegistryEntry<Structure>> structures = registry.getIndexedEntries();
        boolean structureFound = false;
        for (RegistryEntry<Structure> structure: structures) {
            StructureStart hasStructure = world.getStructureAccessor().getStructureContaining(BlockPos.ofFloored(player.getPos()), structure.value());
            if (hasStructure.hasChildren()) {
                setCurrentStructure(player, world, structure.getIdAsString());
                structureFound = true;
                break;
            }
        }
        if (!structureFound) {
            setCurrentStructure(player, world, null);
        }
    }
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(StructureChangedPayload.ID, StructureChangedPayload.CODEC);
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            if (world.isClient() && !playerStructures.isEmpty()) {
                playerStructures.clear();
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            playerStructures.remove(handler.getPlayer());
        });
        ServerTickEvents.START_WORLD_TICK.register((world) -> {
            if (ticks > 0) {
                ticks -= 1;
                return;
            }
            List<ServerPlayerEntity> players = world.getPlayers();
            for (ServerPlayerEntity player : players) {
                checkAllStructures(world, player);
            }
            ticks = 250;
        });
    }
}
