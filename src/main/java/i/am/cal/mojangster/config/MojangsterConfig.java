package i.am.cal.mojangster.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.TranslatableText;

@Config(name = "mojangster")
public class MojangsterConfig implements ConfigData {

    public static MojangsterConfig getInstance() {
        return AutoConfig.getConfigHolder(MojangsterConfig.class).getConfig();
    }

    public boolean hideLoadingBar = false;
    public boolean useDarkBG = false;

}
