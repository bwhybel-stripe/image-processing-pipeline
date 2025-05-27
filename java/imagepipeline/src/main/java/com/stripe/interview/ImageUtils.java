package com.stripe.interview;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    /* Load an image from the specified file path.
     * Throws IOException if the image cannot be read.
     */
    public static BufferedImage loadImage(String filePath) throws IOException {
        // Part I to be implemented
        return new BufferedImage(0,0,0); // Placeholder, replace with actual image loading logic
    }

    public static void saveImage(BufferedImage image, String filePath) throws IOException {
        File outputFile = new File(filePath);
        boolean success = ImageIO.write(image, "png", outputFile);
        if (!success) {
            throw new IOException("Failed to write image");
        }
    }
}
