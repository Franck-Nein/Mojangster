package i.am.cal.mojangster.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public interface F3FunctionCallback {

    Event<F3FunctionCallback> EVENT = EventFactory.createArrayBacked(F3FunctionCallback.class,
            callbacks ->
                    (client, key) -> {
                        for (F3FunctionCallback callback : callbacks) {
                            ActionResult result = callback.onF3(client, key);
                            if (result != ActionResult.PASS) return result;
                        }
                        return ActionResult.PASS;
                    }
    );

    ActionResult onF3(MinecraftClient client, int key);
}
