package i.am.cal.mojangster.util.editor;

import i.am.cal.mojangster.util.editor.frames.MainFrame;

import javax.swing.*;
import java.util.Arrays;

public class EditorLauncher {

    private static boolean isDev = false;

    // test
    public static void main(String[] args) {
        if (Arrays.asList(args).contains("dev")) {
            isDev = true;
        }
        launch();
    }

    public static void launch() {
        MainFrame f = new MainFrame();
        f.setSize(500, 400);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setAlwaysOnTop(true);
        f.setVisible(true);//making the frame
        f.setResizable(false);

        if (isDev) {
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
    }
}
