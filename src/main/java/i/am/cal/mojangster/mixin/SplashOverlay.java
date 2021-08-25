//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package i.am.cal.mojangster.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

import i.am.cal.mojangster.client.Prelaunch;
import i.am.cal.mojangster.config.MojangsterConfig;
import me.shedaniel.math.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper.ColorMixer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.sound.sampled.*;

import static i.am.cal.mojangster.client.Prelaunch.alreadyPlayed;
import static i.am.cal.mojangster.client.Prelaunch.soundPath;

@Mixin(value = net.minecraft.client.gui.screen.SplashOverlay.class, priority = 150)
@Environment(EnvType.CLIENT)
public abstract class SplashOverlay extends Overlay {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private boolean reloading;

    @Shadow
    private long reloadStartTime;

    @Shadow
    private long reloadCompleteTime;

    private long animationStart;
    private boolean dontAnimate;
    private int animationSpeed;
    private boolean playedSound = false;
    private boolean onlyChimeOnce;

    @Shadow
    private static int withAlpha(int color, int alpha) {
        return color & 16777215 | alpha << 24;
    }

    @Shadow @Final private static int MONOCHROME_BLACK;
    @Shadow @Final private static int MOJANG_RED;

    @Shadow
    @Final
    private static IntSupplier BRAND_ARGB = () -> {
        if(!MojangsterConfig.getInstance().useCustomColor) {
            return MojangsterConfig.getInstance().useDarkBG ? MONOCHROME_BLACK : MOJANG_RED;
        } else {
            return MojangsterConfig.getInstance().bgColor;
        }

    };

    @Shadow
    @Final
    static Identifier LOGO;

    @Shadow
    @Final
    private ResourceReload reload;

    @Shadow
    private float progress;

    @Shadow
    @Final
    private Consumer<Optional<Throwable>> exceptionHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        animationStart = Util.getMeasuringTimeMs();
        dontAnimate = MojangsterConfig.getInstance().dontAnimate;
        animationSpeed = MojangsterConfig.getInstance().animationSpeed;
        playedSound = MojangsterConfig.getInstance().onlyChimeOnce && alreadyPlayed;
    }

    private static void playSound()
    {
        try {
            var aux = AudioSystem.getAudioInputStream(soundPath.toFile());
            AudioFormat audioFormat = aux.getFormat();
            SourceDataLine line = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            int nBytesRead = 0;
            byte[] abData = new byte[128000];
            while (nBytesRead != -1)
            {
                nBytesRead = aux.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                {
                    line.write(abData, 0, nBytesRead);
                }
            }
            line.drain();
            line.close();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }
    /**
     * @author cal6541
     * @reason tiny potato
     */
    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int scaledWidth = this.client.getWindow().getScaledWidth();
        int scaledHeight = this.client.getWindow().getScaledHeight();
        long currentTime = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = currentTime;
        }

        float f = this.reloadCompleteTime > -1L ? (float) (currentTime - this.reloadCompleteTime) / 1000.0F : -1.0F;
        float g = this.reloadStartTime > -1L ? (float) (currentTime - this.reloadStartTime) / 500.0F : -1.0F;
        float s;
        int m;
        if (f >= 1.0F) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(matrices, 0, 0, delta);
            }

            m = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(matrices, 0, 0, scaledWidth, scaledHeight, withAlpha(BRAND_ARGB.getAsInt(), m));
            s = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.reloading) {
            if (this.client.currentScreen != null && g < 1.0F) {
                this.client.currentScreen.render(matrices, mouseX, mouseY, delta);
            }

            m = MathHelper.ceil(MathHelper.clamp(g, 0.15D, 1.0D) * 255.0D);
            fill(matrices, 0, 0, scaledWidth, scaledHeight, withAlpha(BRAND_ARGB.getAsInt(), m));
            s = MathHelper.clamp(g, 0.0F, 1.0F);
        } else {
            m = BRAND_ARGB.getAsInt();
            float p = (float) (m >> 16 & 255) / 255.0F;
            float q = (float) (m >> 8 & 255) / 255.0F;
            float r = (float) (m & 255) / 255.0F;
            GlStateManager._clearColor(p, q, r, 1.0F);
            GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
            s = 1.0F;
        }

        m = (int) ((double) this.client.getWindow().getScaledWidth() * 0.5D);
        int u = (int) ((double) this.client.getWindow().getScaledHeight() * 0.5D);
        double d = Math.min((double) this.client.getWindow().getScaledWidth() * 0.75D, (double) this.client.getWindow().getScaledHeight()) * 0.25D;
        int v = (int) (d * 0.5D);
        double e = d * 4.0D;
        int w = (int) (e * 0.5D);

        long currentFrame = Math.min(63, (currentTime - animationStart) / animationSpeed);
        if(currentFrame == 35) {
            if(!playedSound) {
                Prelaunch.alreadyPlayed = true;
                playedSound = true;
                Thread t = new Thread(SplashOverlay::playSound);
                t.start();
            }
        }
        if(!dontAnimate) {
            RenderSystem.setShaderTexture(0, LOGO);
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
            RenderSystem.blendEquation(32774);
            RenderSystem.blendFunc(770, 1);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            drawTexture(matrices, m - w, u - v, w * 2, (int) d, (currentFrame / 16) * 1024.0F, (currentFrame % 16) * 256.0F, 1024, 256, 4096, 4096);
        } else {
            RenderSystem.setShaderTexture(0, LOGO);
            RenderSystem.enableBlend();
            RenderSystem.blendEquation(32774);
            RenderSystem.blendFunc(770, 1);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
            drawTexture(matrices, m - w, u - v, w * 2, (int) d, 0, 0, 1024, 256, 1024, 256);
        }
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();

        int x = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8325D);
        float y = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95F + y * 0.050000012F, 0.0F, 1.0F);
        if (f < 1.0F) {
            if (!MojangsterConfig.getInstance().hideLoadingBar) {
                this.renderProgressBar(matrices, scaledWidth / 2 - w, x - 5, scaledWidth / 2 + w, x + 5, 1.0F - MathHelper.clamp(f, 0.0F, 1.0F));
            }
        }

        if (f >= 2.0F) {
            this.client.setOverlay(null);
        }

        if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || g >= 2.0F)) {
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable var23) {
                this.exceptionHandler.accept(Optional.of(var23));
            }

            this.reloadCompleteTime = Util.getMeasuringTimeMs();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());
            }
        }


    }

    private void renderProgressBar(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity) {
        int i = MathHelper.ceil((float) (maxX - minX - 2) * this.progress);
        int j = Math.round(opacity * 255.0F);
        int k = ColorMixer.getArgb(j, 255, 255, 255);
        fill(matrices, minX + 2, minY + 2, minX + i, maxY - 2, k);
        fill(matrices, minX + 1, minY, maxX - 1, minY + 1, k);
        fill(matrices, minX + 1, maxY, maxX - 1, maxY - 1, k);
        fill(matrices, minX, minY, minX + 1, maxY, k);
        fill(matrices, maxX, minY, maxX - 1, maxY, k);
    }
}
