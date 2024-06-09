package fusionmc.structure_music.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record StructureChangedPayload(Identifier structureName) implements CustomPayload {
    public static final Id<StructureChangedPayload> ID = CustomPayload.id("structure_changed");
    public static final PacketCodec<PacketByteBuf, StructureChangedPayload> CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, StructureChangedPayload::structureName, StructureChangedPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
