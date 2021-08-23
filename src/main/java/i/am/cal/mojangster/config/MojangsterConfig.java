package i.am.cal.mojangster.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mojangster")
public class MojangsterConfig implements ConfigData {

    public static MojangsterConfig getInstance() {
        return AutoConfig.getConfigHolder(MojangsterConfig.class).getConfig();
    }

    public boolean hideLoadingBar = false;
    public boolean useDarkBG = false;
    @ConfigEntry.Gui.PrefixText()
    public boolean dontAnimate = false;

    @ConfigEntry.Category("ccolor")
    public boolean useCustomColor = false;
    @ConfigEntry.Category("ccolor")
    @ConfigEntry.ColorPicker(allowAlpha = true)
    @ConfigEntry.Gui.PrefixText()
    public int bgColor = 28835;
}
