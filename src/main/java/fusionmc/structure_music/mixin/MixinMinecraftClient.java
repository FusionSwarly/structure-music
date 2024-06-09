package fusionmc.structure_music.mixin;

import fusionmc.structure_music.client.ClientStructureMusic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.MusicType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "getMusicType", at = @At("RETURN"), cancellable = true)
    private void getMusicType(CallbackInfoReturnable<MusicSound> cir) {
        MusicSound musicType = cir.getReturnValue();
        if (musicType == MusicType.MENU) {
            ClientStructureMusic.setStructureMusic(null);
        }
        if (musicType != MusicType.CREATIVE && musicType != MusicType.CREDITS && musicType != MusicType.DRAGON && musicType != MusicType.END && musicType != MusicType.MENU) {
            @Nullable
            MusicSound structureMusic = ClientStructureMusic.getStructureMusic();
            if (structureMusic != null) {
                cir.setReturnValue(structureMusic);
            }
        }
    }
}
