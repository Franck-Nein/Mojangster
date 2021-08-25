package i.am.cal.mojangster.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.math.Color;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Config(name = "mojangster")
public class MojangsterConfig implements ConfigData {

    public static MojangsterConfig getInstance() {
        return AutoConfig.getConfigHolder(MojangsterConfig.class).getConfig();
    }

    public boolean hideLoadingBar = false;
    public boolean useDarkBG = false;
    @ConfigEntry.Gui.PrefixText()
    public boolean dontAnimate = false;
    public int animationSpeed = 33;

    @ConfigEntry.Category("ccolor")
    public boolean useCustomColor = false;
    @ConfigEntry.Category("ccolor")
    @ConfigEntry.ColorPicker(allowAlpha = true)
    @ConfigEntry.Gui.PrefixText()
    public int bgColor = 28835;

    @ConfigEntry.Category("sound")
    public boolean playSound = true;
    @ConfigEntry.Category("sound")
    @ConfigEntry.Gui.Tooltip()
    public boolean onlyChimeOnce = false;

}
