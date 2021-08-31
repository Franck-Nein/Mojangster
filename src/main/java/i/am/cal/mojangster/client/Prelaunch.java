package i.am.cal.mojangster.client;

import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.config.MojangsterConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.util.Identifier;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class Prelaunch implements PreLaunchEntrypoint {
    /**
     * Copy a file from source to destination.
     *
     * @param source
     *        the source
     * @param destination
     *        the destination
     * @return True if succeeded , False if not
     */
    public static boolean copy(InputStream source , String destination) {
        boolean succeess = true;

        Mojangster.logger.info("Copying ->" + source + "\n\tto ->" + destination);

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            succeess = false;
            ex.printStackTrace();
        }

        return succeess;

    }
    public static final Path gameDir = FabricLoader.getInstance().getGameDir();
    public static final Path mojankDir = Paths.get(gameDir.toString(), "/mojank");
    public static final Path pngPath = Paths.get(mojankDir.toString(), "/static.png");
    public static final Path soundPath = Paths.get(mojankDir.toString(), "/load.wav");
    public static final Path animPath = Paths.get(mojankDir.toString(), "/custom/google.png");
    public static final Path animePath = Paths.get(mojankDir.toString(), "/anim-rev.png");
    public static boolean alreadyPlayed = false;
    @Override
    public void onPreLaunch() {
        AutoConfig.register(MojangsterConfig.class, GsonConfigSerializer::new);
        try {
            ModMetadata meta = FabricLoader.getInstance().getModContainer("mojangster").get().getMetadata();
            var version = meta.getVersion().getFriendlyString();
            if(!Files.exists(Paths.get(mojankDir.toString(), "/vers.info")) || !Files.readString(Paths.get(mojankDir.toString(), "/vers.info")).equals(version)) {
                Mojangster.logger.info("Old files detected. Purging");
                try {
                    Files.delete(mojankDir);
                } catch (Exception e) {}
                genFiles(version);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void genFiles(String vers) {
        Path dirs = null;
        try {
            dirs = Files.createDirectories(Paths.get(gameDir.toString(), "mojank"));
            Files.writeString(Paths.get(dirs.toString(), "readme.txt"), "This folder is used by mojangster for the animated loading screens.\n" +
                    "Only remove this folder if you have disabled or uninstalled mojangster.");
            Files.writeString(Paths.get(dirs.toString(), "vers.info"), vers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        copy(Prelaunch.class.getResourceAsStream("/mojangster/anim.png"), animPath.toString());
        copy(Prelaunch.class.getResourceAsStream("/mojangster/anim.png"), animePath.toString());
        copy(Prelaunch.class.getResourceAsStream("/mojangster/load.wav"), soundPath.toString());
        copy(Prelaunch.class.getResourceAsStream("/mojangster/static.png"), pngPath.toString());
        Mojangster.logger.info("Updated files.");
    }
}
