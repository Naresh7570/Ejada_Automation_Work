package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageComparisonUtil {

    public static boolean imagesAreDifferent(
            File baseline,
            File actual) throws Exception {

        BufferedImage img1 = ImageIO.read(baseline);
        BufferedImage img2 = ImageIO.read(actual);

        if (img1.getWidth() != img2.getWidth()
                || img1.getHeight() != img2.getHeight()) {
            return true;
        }

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {

                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }
}