package i.am.cal.mojangster.client;

import net.minecraft.client.util.math.MatrixStack;

import java.util.TimerTask;

public class CustomTimerTask extends TimerTask {
    private final func funce;

    public CustomTimerTask(func funce) {
        this.funce = funce;
    }
    @Override
    public void run() {
        funce.run(this);
    }

    @FunctionalInterface
    public interface func {
        public void run(CustomTimerTask self);
    }
}
