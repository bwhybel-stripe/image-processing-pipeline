package com.stripe.interview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PipelineExecutor {
    public PipelineTypes.PipelineResult processImage(
            String inputImagePath,
            String outputImagePath,
            List<PipelineTypes.OperationConfig> operations) {

        BufferedImage image;
        try {
            image = ImageUtils.loadImage(inputImagePath);
        } catch (IOException e) {
            return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.FILE_NOT_FOUND,
                    "Failed to load input image: " + e.getMessage());
        }

        System.out.println("Loaded image: " + image.getWidth() + "x" + image.getHeight() +
                " Type: " + image.getType());

        BufferedImage currentImage = image; // Work on a copy or the original

        for (PipelineTypes.OperationConfig opConfig : operations) {
            System.out.println("Applying operation: " + opConfig.getName());
            try {
                switch (opConfig.getName().toLowerCase()) {
                    case "grayscale":
                        currentImage = applyGrayscale(currentImage);
                        break;
                    case "invert":
                        currentImage = applyInvert(currentImage);
                        break;
                    case "brightness":
                        // TODO: Extract 'factor' from opConfig.parameters()
                        // Check type (e.g., expect Double or Float)
                        // Double factor = (Double) opConfig.parameters().getOrDefault("factor", 1.0);
                        // currentImage = applyBrightness(currentImage, factor.floatValue());
                        Object factorParam = opConfig.getParameters().get("factor");
                        if (factorParam instanceof Number) {
                            currentImage = applyBrightness(currentImage, ((Number) factorParam).floatValue());
                        } else {
                            System.err.println("Warning: Brightness 'factor' parameter missing or not a number. Using default 1.0.");
                            currentImage = applyBrightness(currentImage, 1.0f);
                            // Or return CONFIG_ERROR
                            // return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.CONFIG_ERROR, "Brightness 'factor' parameter is missing or not a number.");
                        }
                        break;
                    case "resize":
                        // TODO: Extract 'width' and 'height' from opConfig.parameters()
                        // Integer newWidth = (Integer) opConfig.parameters().get("width");
                        // Integer newHeight = (Integer) op_config.parameters().get("height");
                        // if (newWidth == null || newHeight == null) {
                        //    return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.CONFIG_ERROR, "Resize 'width' and 'height' parameters required.");
                        // }
                        // currentImage = applyResize(currentImage, newWidth, newHeight);
                        System.err.println("Warning: Resize operation not fully implemented in skeleton.");
                        // For a simple interview version, skipping robust resize or using a library is fine.
                        // A very basic Graphics2D resize:
                        // int newW = (int)opConfig.parameters().getOrDefault("width", currentImage.getWidth());
                        // int newH = (int)opConfig.parameters().getOrDefault("height", currentImage.getHeight());
                        // currentImage = simpleResize(currentImage, newW, newH);
                        break;
                    // TODO: Add more operations (blur, contrast etc.)
                    default:
                        return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.UNSUPPORTED_OPERATION,
                                "Unknown operation: " + opConfig.getName());
                }
            } catch (Exception e) { // Catching general exception for any processing error
                return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.PROCESSING_ERROR,
                        "Error applying operation '" + opConfig.getName() + "': " + e.getMessage());
            }
        }

        try {
            String outputFormat = "png"; // Default
            int dotIndex = outputImagePath.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < outputImagePath.length() - 1) {
                outputFormat = outputImagePath.substring(dotIndex + 1);
            }
            ImageUtils.saveImage(currentImage, outputImagePath, outputFormat);
        } catch (IOException e) {
            return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.SAVE_ERROR,
                    "Failed to save output image: " + e.getMessage());
        }

        return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.SUCCESS,
                "Image processed and saved successfully.");
    }

    // --- TODO: Candidate implements these image operations ---

    private BufferedImage applyGrayscale(BufferedImage original) {
        // Create a new image of the same size and type (or specific grayscale type)
        BufferedImage grayImage = new BufferedImage(
                original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY // Or original.getType()
        );
        // A common way is to draw the original onto the new image with grayscale conversion
        // Graphics2D g2d = grayImage.createGraphics();
        // g2d.drawImage(original, 0, 0, null);
        // g2d.dispose();
        // return grayImage; // This often works if the type is TYPE_BYTE_GRAY

        // Manual pixel manipulation for learning:
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color color = new Color(original.getRGB(x, y), true); // true for hasAlpha
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                // Luminosity method:
                int grayVal = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                Color grayColor = new Color(grayVal, grayVal, grayVal, color.getAlpha());
                grayImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        return grayImage;
    }

    private BufferedImage applyInvert(BufferedImage original) {
        BufferedImage invertedImage = new BufferedImage(
                original.getWidth(), original.getHeight(), original.getType()
        );
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color color = new Color(original.getRGB(x, y), true);
                int r = 255 - color.getRed();
                int g = 255 - color.getGreen();
                int b = 255 - color.getBlue();
                // Alpha is typically not inverted
                Color invertedColor = new Color(r, g, b, color.getAlpha());
                invertedImage.setRGB(x, y, invertedColor.getRGB());
            }
        }
        return invertedImage;
    }

    private BufferedImage applyBrightness(BufferedImage original, float factor) {
        if (factor < 0) factor = 0; // Prevent negative brightness
        BufferedImage brightenedImage = new BufferedImage(
                original.getWidth(), original.getHeight(), original.getType()
        );
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color color = new Color(original.getRGB(x, y), true);
                int r = Math.min(255, Math.max(0, (int) (color.getRed() * factor)));
                int g = Math.min(255, Math.max(0, (int) (color.getGreen() * factor)));
                int b = Math.min(255, Math.max(0, (int) (color.getBlue() * factor)));
                Color newColor = new Color(r, g, b, color.getAlpha());
                brightenedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return brightenedImage;
    }

    // A very simple resize using Graphics2D (low quality, nearest neighbor like)
    // For better quality, imgscalr or more advanced Graphics2D hints would be needed.
    private BufferedImage simpleResize(BufferedImage original, int newWidth, int newHeight) {
        if (newWidth <=0 || newHeight <=0) {
            System.err.println("Error: Invalid new dimensions for resize.");
            return original; // Or throw exception
        }
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resizedImage.createGraphics();
        // For higher quality, add rendering hints:
        // g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    // TODO: Consider applyBlur, applyContrast.
    // Blur might involve creating a Kernel and using ConvolveOp.
}
