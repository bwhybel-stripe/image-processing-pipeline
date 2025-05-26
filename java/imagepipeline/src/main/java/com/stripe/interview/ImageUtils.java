package com.stripe.interview;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static BufferedImage loadImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            throw new IOException("Image file not found: " + filePath);
        }
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Could not read image (unsupported format or corrupt): " + filePath);
        }
        return image;
    }

    public static void saveImage(BufferedImage image, String filePath, String formatName) throws IOException {
        // formatName examples: "png", "jpg", "bmp"
        // Ensure the directory exists or ImageIO.write might fail silently or throw error depending on OS/permissions
        File outputFile = new File(filePath);
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Could not create parent directories for: " + filePath);
            }
        }

        boolean success = ImageIO.write(image, formatName, outputFile);
        if (!success) {
            throw new IOException("Failed to write image. No appropriate writer found for format: " + formatName);
        }
    }

}
