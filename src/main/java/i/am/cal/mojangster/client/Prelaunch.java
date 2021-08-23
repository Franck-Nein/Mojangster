package i.am.cal.mojangster.client;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

        System.out.println("Copying ->" + source + "\n\tto ->" + destination);

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
    public static final Path soundPath = Paths.get(mojankDir.toString(), "/load.ogg");
    public static final Path animPath = Paths.get(mojankDir.toString(), "/anim.png");
    @Override
    public void onPreLaunch() {
        AutoConfig.register(MojangsterConfig.class, GsonConfigSerializer::new);
        try {
            ModMetadata meta = FabricLoader.getInstance().getModContainer("mojangster").get().getMetadata();
            String version = meta.getVersion().getFriendlyString();
            if(!Files.exists(Paths.get(mojankDir.toString(), "/vers.info")) || !new String(Files.readAllBytes(Paths.get(mojankDir.toString(), "/vers.info")), Charset.defaultCharset()).equals(version)) {
                try {
                    Files.delete(mojankDir);
                } catch (Exception ignored) {}
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
            Files.write(Paths.get(dirs.toString(), "readme.txt"), ("This folder is used by mojangster for the animated loading screens.\n" +
                    "Only remove this folder if you have disabled or uninstalled mojangster.").getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get(dirs.toString(), "vers.info"), vers.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        copy(Prelaunch.class.getResourceAsStream("/mojangster/anim.png"), animPath.toString());
        copy(Prelaunch.class.getResourceAsStream("/mojangster/load.ogg"), soundPath.toString());
        copy(Prelaunch.class.getResourceAsStream("/mojangster/static.png"), pngPath.toString());
    }
}
