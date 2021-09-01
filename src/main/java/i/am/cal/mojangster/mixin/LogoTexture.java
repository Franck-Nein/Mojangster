package i.am.cal.mojangster.mixin;

import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.client.Prelaunch;
import i.am.cal.mojangster.config.MojangsterConfig;
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
import java.nio.file.Path;
import java.nio.file.Paths;

@Mixin(value = SplashOverlay.LogoTexture.class, priority = 150)
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
        try {

            InputStream inputStream;

            if(MojangsterConfig.getInstance().dontAnimate) {
                if(MojangsterConfig.getInstance().staticName.equals("default-static.png")) {
                    inputStream = Files.newInputStream(Prelaunch.pngPath);
                } else {
                    Path p = Paths.get(String.valueOf(Prelaunch.customs), MojangsterConfig.getInstance().staticName);
                    inputStream = Files.newInputStream(p);
                }

            } else {
               if(MojangsterConfig.getInstance().animName.equals("default.png")) {
                   inputStream = Files.newInputStream(Prelaunch.animPath);
               } else {
                   Path p = Paths.get(String.valueOf(Prelaunch.customs), MojangsterConfig.getInstance().animName);
                   inputStream = Files.newInputStream(p);
               }
            }

            TextureData var5;
            try {

                var5 = new TextureData(new TextureResourceMetadata(true, true), NativeImage.read(inputStream));
                Mojangster.logger.info("Injected animation texture.");
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
            return new TextureData(var9);
        }
    }
}