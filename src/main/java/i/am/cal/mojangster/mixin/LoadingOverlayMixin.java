package i.am.cal.mojangster.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import i.am.cal.mojangster.Color;
import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.SplashOverlayI;
import i.am.cal.mojangster.audio.AudioManager;
import i.am.cal.mojangster.config.MojangsterConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

@Mixin(value = LoadingOverlay.class, remap = false)
public abstract class LoadingOverlayMixin extends Overlay implements SplashOverlayI {
    private boolean isConfig;

    public void setConfig() {
        this.isConfig = true;
    }

    @Shadow(remap = false) private float currentProgress;

    @Shadow(remap = false) @Final private Minecraft minecraft;

    @Shadow(remap = false) @Final private Consumer<Optional<Throwable>> onFinish;

    @Shadow(remap = false) @Final private ReloadInstance reload;

    @Shadow(remap = false) private long fadeOutStart;

    @Shadow(remap = false) @Final private boolean fadeIn;

    @Shadow(remap = false) private long fadeInStart;

    @Shadow(remap = false) @Final private static IntSupplier BRAND_BACKGROUND;
    private long animationStart;
    private boolean dontAnimate;
    private boolean playedSound;
    private int animationSpeed;

    @Shadow(remap = false) @Final private static ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION;
    private boolean canPlaySound;
    private int barColor;
    private int logoColor;
    private boolean customColor;
    private boolean disableColorTint;
    private int barOutlineColor;
    private int barBackgroundColor;
    private boolean enableBarBackground;
    private int soundFrame;
    private String soundName;
    private boolean animationStarted;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Minecraft client, ReloadInstance monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        Mojangster.OVERLAY_INSTANCE = (SplashOverlayI) this;
        dontAnimate = MojangsterConfig.getInstance().dontAnimate;
        animationSpeed = MojangsterConfig.getInstance().animationSpeed;
        canPlaySound = MojangsterConfig.getInstance().playSound;
        barColor = MojangsterConfig.getInstance().barColor;
        logoColor = MojangsterConfig.getInstance().logoColor;
        customColor = MojangsterConfig.getInstance().useCustomColor;
        disableColorTint = MojangsterConfig.getInstance().disableLogoColorTint;
        barOutlineColor = MojangsterConfig.getInstance().barOutlineColor;
        barBackgroundColor = MojangsterConfig.getInstance().barBackgroundColor;
        enableBarBackground = MojangsterConfig.getInstance().enableBarBackground;
        soundFrame = MojangsterConfig.getInstance().soundFrame;
        soundName = MojangsterConfig.getInstance().soundName;
    }

    /**
     * @author cal6541
     * @reason tiny potato
     */
    @Overwrite(remap = false)
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        
        if(!animationStarted) {
            animationStarted = true;
            animationStart = Util.getMillis();
        }
        
        int scaledWidth = this.minecraft.getWindow().getGuiScaledWidth();
        int scaledHeight = this.minecraft.getWindow().getGuiScaledHeight();
        long currentTime = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = currentTime;
        }

        float fade = this.fadeOutStart > -1L ? (float)(currentTime - this.fadeOutStart) / 1000.0F : -1.0F;
        float fade1 = this.fadeInStart > -1L ? (float)(currentTime - this.fadeInStart) / 500.0F : -1.0F;
        float fade2;
        if (fade >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(stack, 0, 0, delta);
            }

            int l = Mth.ceil((1.0F - Mth.clamp(fade - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(stack, 0, 0, scaledWidth, scaledHeight, replaceAlpha(BRAND_BACKGROUND.getAsInt(), l));
            fade2 = 1.0F - Mth.clamp(fade - 1.0F, 0.0F, 1.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && fade1 < 1.0F) {
                this.minecraft.screen.render(stack, mouseX, mouseY, delta);
            }

            int l1 = Mth.ceil(Mth.clamp((double)fade1, 0.15D, 1.0D) * 255.0D);
            fill(stack, 0, 0, scaledWidth, scaledHeight, replaceAlpha(BRAND_BACKGROUND.getAsInt(), l1));
            fade2 = Mth.clamp(fade1, 0.0F, 1.0F);
        } else {
            int i2 = BRAND_BACKGROUND.getAsInt();
            float f3 = (float)(i2 >> 16 & 255) / 255.0F;
            float f4 = (float)(i2 >> 8 & 255) / 255.0F;
            float f5 = (float)(i2 & 255) / 255.0F;
            GlStateManager._clearColor(f3, f4, f5, 1.0F);
            GlStateManager._clear(16384, Minecraft.ON_OSX);
            fade2 = 1.0F;
        }

        int j2 = (int)((double)this.minecraft.getWindow().getGuiScaledWidth() * 0.5D);
        int k2 = (int)((double)this.minecraft.getWindow().getGuiScaledHeight() * 0.5D);
        double d1 = Math.min((double)this.minecraft.getWindow().getGuiScaledWidth() * 0.75D, (double)this.minecraft.getWindow().getGuiScaledHeight()) * 0.25D;
        int i1 = (int)(d1 * 0.5D);
        double d0 = d1 * 4.0D;
        int j1 = (int)(d0 * 0.5D);
        long currentFrame = Math.min(63, (currentTime - animationStart) / animationSpeed);
        if (currentFrame == soundFrame && canPlaySound) {
            AudioManager.play(soundName);
        }
        Color z = Color.ofTransparent(logoColor);

        RenderSystem.setShaderTexture(0, MOJANG_STUDIOS_LOGO_LOCATION);
        RenderSystem.enableBlend();

        if(isConfig) {
            dontAnimate = true;
            RenderSystem.setShaderTexture(0, new ResourceLocation("mojangster", "reload.png"));
        }

        if (customColor) {
            RenderSystem.setShaderColor(z.getRed() * 0.00392156862F, z.getGreen() * 0.00392156862F, z.getBlue() * 0.00392156862F, fade2);
        } else if (!disableColorTint) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, fade2);
        }

        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        if(!dontAnimate) {
            blit(stack, j2 - j1, k2 - i1, j1 * 2, (int) d1, (currentFrame / 16) * 1024.0F, (currentFrame % 16) * 256.0F, 1024, 256, 4096, 4096);
        } else {
            blit(stack, j2 - j1, k2 - i1, j1 * 2, (int) d1, 0, 0, 1024, 256, 1024, 256);
        }
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        int k1 = (int)((double)this.minecraft.getWindow().getGuiScaledHeight() * 0.8325D);
        float f6 = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + f6 * 0.050000012F, 0.0F, 1.0F);
        net.minecraftforge.fmlclient.ClientModLoader.renderProgressText();
        if (fade < 1.0F) {
            if (!MojangsterConfig.getInstance().hideLoadingBar) {
                this.drawProgressBar(stack, scaledWidth / 2 - j1, k1 - 5, scaledWidth / 2 + j1, k1 + 5, 1.0F - Mth.clamp(fade, 0.0F, 1.0F));
            }
        }

        if (fade >= 2.0F) {
            this.minecraft.setOverlay(null);
        }

        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || fade1 >= 2.0F)) {
            this.fadeOutStart = Util.getMillis(); // Moved up to guard against inf loops caused by callback
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.onFinish.accept(Optional.of(throwable));
            }

            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
            }
        }
    }

    public int replaceAlpha(int p_169325_, int p_169326_) {
        return p_169325_ & 16777215 | p_169326_ << 24;
    }

    private void drawProgressBar(PoseStack matrices, int minX, int minY, int maxX, int maxY, float opacity) {
        int i = Mth.ceil((float) (maxX - minX - 2) * this.currentProgress);
        if (customColor) {
            Color c = Color.ofTransparent(barColor);
            Color e = Color.ofTransparent(barOutlineColor);
            int k = FastColor.ARGB32.color(Math.round(opacity * 255.0F), c.getRed(), c.getGreen(), c.getBlue());
            int t = FastColor.ARGB32.color(Math.round(opacity * 255.0F), e.getRed(), e.getGreen(), e.getBlue());

            if (enableBarBackground) {
                Color r = Color.ofTransparent(barBackgroundColor);
                int j = FastColor.ARGB32.color(Math.round(opacity * 255.0F), r.getRed(), r.getGreen(), r.getBlue());
                fill(matrices, minX + 1, minY + 1, maxX - 1, maxY - 1, j);
            }

            fill(matrices, minX + 2, minY + 2, minX + i, maxY - 2, k);
            fill(matrices, minX + 1, minY, maxX - 1, minY + 1, t);
            fill(matrices, minX + 1, maxY, maxX - 1, maxY - 1, t);
            fill(matrices, minX, minY, minX + 1, maxY, t);
            fill(matrices, maxX, minY, maxX - 1, maxY, t);

        } else {
            int k = FastColor.ARGB32.color(Math.round(opacity * 255.0F), 255, 255, 255);
            fill(matrices, minX + 2, minY + 2, minX + i, maxY - 2, k);
            fill(matrices, minX + 1, minY, maxX - 1, minY + 1, k);
            fill(matrices, minX + 1, maxY, maxX - 1, maxY - 1, k);
            fill(matrices, minX, minY, minX + 1, maxY, k);
            fill(matrices, maxX, minY, maxX - 1, maxY, k);
        }

    }
}

