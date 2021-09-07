package i.am.cal.mojangster.audio;

import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.Prelaunch;
import i.am.cal.mojangster.config.MojangsterConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.LiteralText;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class AudioManager {
    private static final HashMap<String, Clip> CLIPS = new HashMap<>();

    public static void init() throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        Clip defaultClip = AudioSystem.getClip();
        AudioInputStream defaultStream = AudioSystem.getAudioInputStream(Prelaunch.soundPath.toFile());
        defaultClip.open(defaultStream);
        defaultClip.addLineListener((LineEvent event) -> {
            if (event.getType() == LineEvent.Type.STOP) {
                defaultClip.setMicrosecondPosition(0);
            }
        });

        CLIPS.put("default.wav", defaultClip);

        for (final File fileEntry : Objects.requireNonNull(Prelaunch.customs.toFile().listFiles())) {
            if(fileEntry.getName().endsWith(".wav")) {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(fileEntry);
                clip.open(inputStream);
                clip.addLineListener((LineEvent event) -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.setMicrosecondPosition(0);
                    }
                });
                CLIPS.put(fileEntry.getName(), clip);
            }
        }
    }

    public static void play(String id) {
        Clip clip = CLIPS.get(id);

        if(!MojangsterConfig.getInstance().playSound)
            return;

        if(Objects.isNull(clip)) {
            Mojangster.OVERLAY_INSTANCE.addEndListener(() -> {
                MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.PACK_LOAD_FAILURE, new LiteralText("Sound not found."), new LiteralText("Couldn't find " + id + " in the /mojank/custom folder.")));
            });
            return;
        }
        clip.start();
    }
}
