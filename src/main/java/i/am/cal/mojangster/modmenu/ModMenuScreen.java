package i.am.cal.mojangster.modmenu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.util.mod.ModIconHandler;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SprucePositioned;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.SpruceTexturedButtonWidget;
import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.config.MojangsterConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Iterator;

public class ModMenuScreen extends SpruceScreen {
    private Screen parent;

    public ModMenuScreen(Screen parent) {
        super(new LiteralText("Mojangster"));
        this.parent = parent;
    }

    protected static final int FULL_ICON_SIZE = 32;
    protected static final int COMPACT_ICON_SIZE = 19;

    @Override
    protected void init() {
        super.init();
        assert this.client != null;
        int startY = this.height / 4 + 48;

        int finalStartY1 = startY;
        this.addDrawable((matrices, mouseX, mouseY, delta) -> {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.f, 1.f, 1.f, 1.F);
            int iconSize = ModMenuConfig.COMPACT_LIST.getValue() ? COMPACT_ICON_SIZE : FULL_ICON_SIZE;
            RenderSystem.setShaderTexture(0, new Identifier(ModMenu.MOD_ID, "mojangster_icon"));
            RenderSystem.enableDepthTest();
            drawTexture(matrices,
                    (this.width / 2) - (iconSize / 2), finalStartY1 - 48,
                    0, 0,
                    iconSize, iconSize,
                    iconSize, iconSize
            );
            RenderSystem.disableBlend();
        });

        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY), 200, 20, new TranslatableText("text.autoconfig.mojangster.configuration"),
                btn -> this.client.setScreen(AutoConfig.getConfigScreen(MojangsterConfig.class, this).get())));
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 100, startY += 25), 200, 20, new TranslatableText("text.autoconfig.mojangster.TestLoadingScreen"),
                btn -> this.client.reloadResources()));
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, SpruceTexts.GUI_DONE,
                btn -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        Iterator var5 = this.drawables.iterator();
        while(var5.hasNext()) {
            Drawable drawable = (Drawable)var5.next();
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }
}
