package i.am.cal.mojangster.editor.frames;

import i.am.cal.mojangster.Mojangster;
import i.am.cal.mojangster.editor.GifDecoder;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private JLabel openedGif;
    private BufferedImage[] loadedGifFrames;

    public static Color MOJANG_RED() {
        float[] e = Color.RGBtoHSB(239, 50, 61, null);
        return Color.getHSBColor(e[0], e[1], e[2]);
    }

    public void loadGif(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            GifDecoder d = new GifDecoder();
            d.read(selectedFile.getPath());
            int n = d.getFrameCount();
            ArrayList<BufferedImage> e = new ArrayList<BufferedImage>();
            for (int i = 0; i < n; i++) {
                BufferedImage frame = d.getFrame(i);  // frame i
                System.out.println(i);
                e.add(frame);

            }
            BufferedImage[] frames = e.toArray(new BufferedImage[0]);
            System.out.println(frames.length);
            if (frames[1].getHeight() != 256 || frames[1].getWidth() != 1024) {
                JOptionPane.showMessageDialog(this, "Gif isn't 1024x256");
                return;
            }
            if (frames.length != 64) {
                JOptionPane.showMessageDialog(this, "Gif doesn't have exactly 64 frames.");
                return;
            }
            BufferedImage tmp = frames[63];
            tmp = Scalr.resize(tmp, Scalr.Method.QUALITY, 256, 64);
            loadedGifFrames = frames;
            openedGif.setIcon(new ImageIcon(tmp));
        }
    }

    public MainFrame() {
        JButton openGifButton = new JButton("Open GIF");
        openGifButton.setBounds(5, 5, 128, 40);
        openGifButton.addActionListener(this::loadGif);

        JLabel openedGifPath = new JLabel("No GIF opened.");
        openedGifPath.setBounds(5 + 128 + 5, 7, 128, 30);

        InputStream inputStream = Mojangster.class.getResourceAsStream("/mojangster/static.png");
        BufferedImage img = null;
        try {
            assert inputStream != null;
            img = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert img != null;
        img = Scalr.resize(img, Scalr.Method.QUALITY, 256, 64);
        openedGif = new JLabel(new ImageIcon(img));
        openedGif.setBounds(5, 10 + 40, img.getWidth(rootPane), img.getHeight(rootPane));
        openedGif.setOpaque(true);
        openedGif.setBackground(MOJANG_RED());

        JButton gen = new JButton("Generate");
        gen.addActionListener(this::generate);
        gen.setBounds(500 - 5 - 128, 400 - 5 - 40, 128, 40);

        this.add(openedGif);
        this.add(gen);
        this.add(openGifButton);
        this.add(openedGifPath);
    }

    private void generate(ActionEvent actionEvent) {
        var e = new BufferedImage(4096, 4096, BufferedImage.TYPE_INT_ARGB);
        Graphics2D c = e.createGraphics();
        c.setColor(new Color(1f, 0f, 0f, 0f));
        var i = 0;
        var hi = 0;
        for (BufferedImage frame : loadedGifFrames) {
            if(i == 16) {
                hi += 1024;
                i = 0;
            }
            c.drawImage(frame, hi, 256 * i, 1024, 256, new Color(1f, 0f, 0f, 0f), null);
            i++;
        }
        try {
            ImageIO.write(e, "PNG", new File("C:\\Users\\rb01px\\IdeaProjects\\Mojangster\\run\\mojank\\custom\\google.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
