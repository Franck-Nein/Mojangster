package i.am.cal.mojangster.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ConfigurationScreen extends Screen {
    public ConfigurationScreen(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    public void render(PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
        super.render(p_96562_, p_96563_, p_96564_, p_96565_);
        renderBackground(p_96562_);
        drawString(p_96562_, this.minecraft.font, "Mojangster doesn't support the configuration UI on forge.", 5, this.height / 2, 0xffffff);
    }
}
