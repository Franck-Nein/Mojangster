package i.am.cal.mojangster.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mojangster")
public class MojangsterConfig implements ConfigData {

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

    public static MojangsterConfig getInstance() {
        return AutoConfig.getConfigHolder(MojangsterConfig.class).getConfig();
    }

}
