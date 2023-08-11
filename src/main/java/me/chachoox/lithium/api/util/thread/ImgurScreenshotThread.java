package me.chachoox.lithium.api.util.thread;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class ImgurScreenshotThread extends Thread implements Minecraftable {
    BufferedImage bufferedImage;

    public ImgurScreenshotThread(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public void run() {
        try {
            String string;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(this.bufferedImage, "png", byteArrayOutputStream);
            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("https://api.imgur.com/3/image").openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Authorization", "Client-ID e1d1a0b461d74da");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.connect();
            StringBuilder stringBuilder = new StringBuilder();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()), "UTF-8"));
            outputStreamWriter.flush();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }
            outputStreamWriter.close();
            bufferedReader.close();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(stringBuilder.toString());
            String string2 = jsonElement.getAsJsonObject().getAsJsonObject("data").get("link").getAsString();
            StringSelection stringSelection = new StringSelection(string2);
            mc.addScheduledTask(() -> sendMessage(stringSelection, string2));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static void sendMessage(StringSelection stringSelection, String string) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        Logger.getLogger().log(TextColor.AQUA + "Image uploaded to: " + string);
    }
}
