package i.am.cal.mojangster;

import i.am.cal.mojangster.client.F3FunctionCallback;
import i.am.cal.mojangster.editor.EditorLauncher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class Mojangster implements ClientModInitializer {
    public static Logger logger = LogManager.getLogger("Mojangster");

    @Override
    public void onInitializeClient() {
        F3FunctionCallback.EVENT.register((client, key) -> {
            if (key == GLFW.GLFW_KEY_M) {
                EditorLauncher.launch();
                client.skipGameRender = !client.skipGameRender;
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
