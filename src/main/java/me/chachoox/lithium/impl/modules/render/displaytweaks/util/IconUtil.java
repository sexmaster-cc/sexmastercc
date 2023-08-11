package me.chachoox.lithium.impl.modules.render.displaytweaks.util;

import me.chachoox.lithium.api.util.logger.Logger;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IconUtil {
    private static final String RESOURCES_ROOT = "/assets/lithium/icons/";

    public static void setWindowIcon(String[] icons) {
        Util.EnumOS osType = Util.getOSType();

        InputStream inputstream = null;
        InputStream inputstream1 = null;

        if (osType != Util.EnumOS.OSX) {
            try {
                inputstream = IconUtil.class.getResourceAsStream(RESOURCES_ROOT + icons[0]);
                inputstream1 = IconUtil.class.getResourceAsStream(RESOURCES_ROOT + icons[1]);

                if (inputstream != null && inputstream1 != null) {
                    Display.setIcon(new ByteBuffer[]{readImageToBuffer(inputstream), readImageToBuffer(inputstream1)});
                    Logger.getLogger().log(Level.INFO, "Icons set successfully");
                }

            } catch (IOException e) {
                Logger.getLogger().log(Level.INFO, "Couldn't set icon: " + e.getMessage());
            } finally {
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(inputstream1);
            }
        }
    }

    private static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(imageStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (int i : aint) {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }
}
