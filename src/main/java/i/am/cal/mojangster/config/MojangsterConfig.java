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

    @ConfigEntry.Category("ccolor")
    public boolean useCustomColor = false;
    @ConfigEntry.Category("ccolor")
    @ConfigEntry.ColorPicker()
    @ConfigEntry.Gui.PrefixText()
    public int bgColor = 15675965;
    
    @ConfigEntry.Category("ccolor")
    @ConfigEntry.ColorPicker()
    public int barColor = 16777215;

    @ConfigEntry.Category("ccolor")
    @ConfigEntry.ColorPicker()
    public int logoColor = 16777215;

    @ConfigEntry.Category("ccolor")
    public boolean disableLogoColorTint = false;

    @ConfigEntry.Category("sound")
    public boolean playSound = true;
    @ConfigEntry.Category("sound")
    public String soundName = "default.wav";

    @ConfigEntry.Category("anim")
    @ConfigEntry.Gui.PrefixText()
    public boolean dontAnimate = false;
    @ConfigEntry.Category("anim")
    public int animationSpeed = 33;
    @ConfigEntry.Category("anim")
    public boolean hideLoadingBar = false;
    @ConfigEntry.Category("anim")
    public String animName = "default.png";
    @ConfigEntry.Category("anim")
    public String staticName = "default-static.png";

}
