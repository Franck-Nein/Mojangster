package i.am.cal.mojangster;

import i.am.cal.mojangster.audio.AudioManager;
import i.am.cal.mojangster.config.MojangsterConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Environment(EnvType.CLIENT)
public class Prelaunch implements PreLaunchEntrypoint {
    public static final Path gameDir = FabricLoader.getInstance().getGameDir();
    public static final Path mojankDir = Paths.get(gameDir.toString(), "/mojank");
    public static final Path pngPath = Paths.get(mojankDir.toString(), "/static.png");
    public static final Path soundPath = Paths.get(mojankDir.toString(), "/load.wav");
    public static final Path animPath = Paths.get(mojankDir.toString(), "/anim.png");
    public static final Path customs = Paths.get(mojankDir.toString(), "/custom/");

    @Override
    public void onPreLaunch() {
        AutoConfig.register(MojangsterConfig.class, GsonConfigSerializer::new);

        try {
            /* TODO: Make this cleaner and more efficient. */
            //noinspection OptionalGetWithoutIsPresent
            ModMetadata meta = FabricLoader.getInstance().getModContainer("mojangster").get().getMetadata();
            var version = meta.getVersion().getFriendlyString();
            if (!Files.exists(Paths.get(mojankDir.toString(), "/vers.info")) || !Files.readString(Paths.get(mojankDir.toString(), "/vers.info")).equals(version)) {
                Mojangster.logger.info("Old files detected. Purging");
                if (Files.exists(customs)) {
                    FileUtils.copyDirectory(customs.toFile(), Paths.get(gameDir.toString(), ".mojanktmp").toFile());
                }
                FileUtils.deleteDirectory(mojankDir.toFile());
                genFiles(version);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            AudioManager.init();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void genFiles(String vers) throws IOException {
        Path dirs = Files.createDirectories(Paths.get(gameDir.toString(), "mojank"));
        Files.writeString(Paths.get(dirs.toString(), "readme.txt"), "This folder is used by mojangster for the animated loading screens.\n" +
                    "Only remove this folder if you have disabled or uninstalled mojangster.");
        Files.writeString(Paths.get(dirs.toString(), "vers.info"), vers);
        Mojangster.copy(Prelaunch.class.getResourceAsStream("/mojangster/anim.png"), animPath.toString());
        Mojangster.copy(Prelaunch.class.getResourceAsStream("/mojangster/load.wav"), soundPath.toString());
        Mojangster.copy(Prelaunch.class.getResourceAsStream("/mojangster/static.png"), pngPath.toString());
        Files.createDirectory(customs);
        if (Files.exists(Paths.get(gameDir.toString(), ".mojanktmp"))) {
            FileUtils.copyDirectory(Paths.get(gameDir.toString(), ".mojanktmp").toFile(), customs.toFile());
            FileUtils.deleteDirectory(Paths.get(gameDir.toString(), ".mojanktmp").toFile());
        }
        Mojangster.logger.info("Updated files.");
    }
}
