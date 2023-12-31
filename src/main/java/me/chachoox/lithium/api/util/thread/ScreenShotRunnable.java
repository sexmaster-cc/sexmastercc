package me.chachoox.lithium.api.util.thread;

import net.minecraft.client.renderer.texture.TextureUtil;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ScreenShotRunnable extends SafeFinishable {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    private final AtomicReference<String> finishedString;
    private final AtomicReference<File> fileReference;
    private final String screenShotName;
    private final File dir;
    private final int width;
    private final int height;
    private final int[] pixels;

    public ScreenShotRunnable(AtomicReference<String> finishedString, AtomicReference<File> fileReference, AtomicBoolean finished, int width, int height, int[] pixels, File dir, @Nullable String screenshotName) {
        super(finished);
        this.fileReference = fileReference;
        this.finishedString = finishedString;
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.dir = dir;
        this.screenShotName = screenshotName;
    }

    @Override
    public void runSafely() throws IOException {
        TextureUtil.processPixelValues(pixels, width, height);
        BufferedImage bufferedimage = new BufferedImage(width, height, 1);
        bufferedimage.setRGB(0, 0, width, height, pixels, 0, width);
        File file = makeScreenshotFile(dir, screenShotName);
        ImageIO.write(bufferedimage, "png", file);
        finishedString.set("Screenshot: " + file.getName());
        fileReference.set(file);
    }

    @Override
    public void handle(Throwable t) {
        finishedString.set("Screenshot Error: " + t.getMessage());
        super.handle(t);
    }

    private static File makeScreenshotFile(File gameDirectory, @Nullable String screenshotName) throws IOException {
        File directory = new File(gameDirectory, "screenshots");
        //noinspection ResultOfMethodCallIgnored
        directory.mkdir();
        File file;

        if (screenshotName == null) {
            file = getTimestampedPNGFileForDirectory(directory);
        } else {
            file = new File(directory, screenshotName);
        }

        file = file.getCanonicalFile();
        return file;
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String s = DATE_FORMAT.format(new Date());
        int i = 1;

        while (true) {
            File file = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file.exists()) {
                return file;
            }
            ++i;
        }
    }

}

