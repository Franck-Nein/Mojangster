package i.am.cal.mojangster.mixin;

import i.am.cal.mojangster.client.Prelaunch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Mixin(SplashOverlay.LogoTexture.class)
@Environment(EnvType.CLIENT)
public class LogoTexture extends ResourceTexture {
    public LogoTexture(Identifier location) {
        super(location);
    }

    /**
     * @author cal6541
     * @reason tiny potato
     */
    @Overwrite
    public TextureData loadTextureData(ResourceManager resourceManager) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        DefaultResourcePack defaultResourcePack = minecraftClient.getResourcePackProvider().getPack();

        try {
            InputStream inputStream = Files.newInputStream(Prelaunch.pngPath);

            TextureData var5;
            try {
                var5 = new TextureData(new TextureResourceMetadata(true, true), NativeImage.read(inputStream));
            } catch (Throwable var8) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (inputStream != null) {
                inputStream.close();
            }

            return var5;
        } catch (IOException var9) {
            return new TextureData(var9);
        }
    }
}