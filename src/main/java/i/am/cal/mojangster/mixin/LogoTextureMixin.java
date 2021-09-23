package i.am.cal.mojangster.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.config.MojangsterConfig;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Mixin(value = LoadingOverlay.LogoTexture.class, remap = false)
public class LogoTextureMixin extends SimpleTexture {
    public LogoTextureMixin(ResourceLocation p_118133_) {
        super(p_118133_);
    }

    /**
     * @author cal6541
     * @reason tiny potato
     */
    @Overwrite(remap = false)
    public SimpleTexture.TextureImage getTextureImage(ResourceManager resourceManager) {
        try {

            InputStream inputStream;

            if(MojangsterConfig.getInstance().dontAnimate) {
                inputStream = Files.newInputStream(Mojangster.pngPath);
            } else {
                inputStream = Files.newInputStream(Mojangster.animPath);
            }

            SimpleTexture.TextureImage var5;
            try {

                var5 = new SimpleTexture.TextureImage(new TextureMetadataSection(true, true), NativeImage.read(inputStream));
            } catch (Throwable var8) {
                try {
                    inputStream.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }

            inputStream.close();

            return var5;
        } catch (IOException var9) {
            return new SimpleTexture.TextureImage(var9);
        }
    }
}

