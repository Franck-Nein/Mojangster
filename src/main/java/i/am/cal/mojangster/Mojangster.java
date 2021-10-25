package i.am.cal.mojangster;

import i.am.cal.mojangster.audio.AudioManager;
import i.am.cal.mojangster.config.MojangsterConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import i.am.cal.mojangster.config.ConfigurationScreen;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.function.BiFunction;

@Mod("mojangster")
public class Mojangster {
    private static final Logger LOGGER = LogManager.getLogger();
    public static SplashOverlayI OVERLAY_INSTANCE;

    public static final IModInfo container = ModLoadingContext.get().getActiveContainer().getModInfo();

    public static final Path gameDir = FMLPaths.GAMEDIR.get();
    public static final Path mojankDir = Paths.get(gameDir.toString(), "/mojank");
    public static final Path pngPath = Paths.get(mojankDir.toString(), "/static.png");
    public static final Path soundPath = Paths.get(mojankDir.toString(), "/load.wav");
    public static final Path animPath = Paths.get(mojankDir.toString(), "/anim.png");
    public static final Path customs = Paths.get(mojankDir.toString(), "/custom/");

    /**
     * Copy a file from source to destination.
     *
     * @param source      the source
     * @param destination the destination
     */
    public static void copy(InputStream source, String destination) {
        LOGGER.info("Copying ->" + source + "\n\tto ->" + destination);
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Mojangster() {
        prelaunch();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        AutoConfig.register(MojangsterConfig.class, GsonConfigSerializer::new);
        AutoConfig.getConfigHolder(MojangsterConfig.class).registerSaveListener((configHolder, mojangsterConfig) -> {
            Minecraft.getInstance().reloadResourcePacks();
            Mojangster.OVERLAY_INSTANCE.setConfig();
            return InteractionResult.SUCCESS;
        });
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new ConfigurationScreen(new TextComponent("Mojangster"))));
    }

    private void prelaunch() {
        try {
            var meta = Mojangster.container;
            var version = MessageFormat.format("{0}.{1}.{2}", meta.getVersion().getMajorVersion(), meta.getVersion().getMinorVersion(), meta.getVersion().getIncrementalVersion());
            if (!Files.exists(Paths.get(mojankDir.toString(), "/vers.info")) || !Files.exists(Paths.get(mojankDir.toString(), "/anim.png"))|| !Files.readString(Paths.get(mojankDir.toString(), "/vers.info")).equals(version)) {
                LOGGER.info("Old files detected. Purging");
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

    private void setup(final FMLClientSetupEvent event) {}

    public void genFiles(String vers) throws IOException {
        Path dirs = Files.createDirectories(Paths.get(gameDir.toString(), "mojank"));
        Files.writeString(Paths.get(dirs.toString(), "readme.txt"), "This folder is used by mojangster for the animated loading screens.\n" +
                "Only remove this folder if you have disabled or uninstalled mojangster.");
        Files.writeString(Paths.get(dirs.toString(), "vers.info"), vers);
        Mojangster.copy(Mojangster.class.getResourceAsStream("/mojangster/anim.png"), animPath.toString());
        Mojangster.copy(Mojangster.class.getResourceAsStream("/mojangster/load.wav"), soundPath.toString());
        Mojangster.copy(Mojangster.class.getResourceAsStream("/mojangster/static.png"), pngPath.toString());
        Files.createDirectory(customs);
        if (Files.exists(Paths.get(gameDir.toString(), ".mojanktmp"))) {
            FileUtils.copyDirectory(Paths.get(gameDir.toString(), ".mojanktmp").toFile(), customs.toFile());
            FileUtils.deleteDirectory(Paths.get(gameDir.toString(), ".mojanktmp").toFile());
        }
        LOGGER.info("Updated files.");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.warn("MOJANGSTER IS A CLIENTSIDE MOD - IT WONT WORK ON A SERVER!");
    }
}
