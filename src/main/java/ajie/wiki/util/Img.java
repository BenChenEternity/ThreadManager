package ajie.wiki.util;

import ajie.wiki.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Img {
    public static Image get(String location){
        try {
            Image I;
            URL U = Main.class.getResource(location);
            InputStream ImageInput;
            assert U != null;
            ImageInput = U.openStream();
            I = ImageIO.read(ImageInput);
            return I;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
