package i.am.cal.mojangster.mixin;

import i.am.cal.mojangster.client.F3FunctionCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
@Environment(EnvType.CLIENT)
public class KeyboardMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "processF3",
            at = @At(
                    value = "HEAD",
                    opcode = Opcodes.LOOKUPSWITCH,
                    ordinal = 0
            ),
            cancellable = true
    )
    private void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = F3FunctionCallback.EVENT.invoker().onF3(client, key);
        if (result != ActionResult.PASS)
            cir.setReturnValue(result != ActionResult.FAIL);
    }

}
