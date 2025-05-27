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

        BufferedImage currentImage = image;

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
                        int factorParam = opConfig.getParameters().get("factor");
                        currentImage = applyBrightness(currentImage, factorParam);
                        break;
                    case "resize":
                        int widthParam = opConfig.getParameters().get("factor");
                        int heightParam = opConfig.getParameters().get("factor");
                         currentImage = applyResize(currentImage, widthParam, heightParam);
                        break;
                    default:
                        return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.UNSUPPORTED_OPERATION,
                                "Unknown operation: " + opConfig.getName());
                }
            } catch (Exception e) {
                return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.PROCESSING_ERROR,
                        "Error applying operation '" + opConfig.getName() + "': " + e.getMessage());
            }
        }

        try {
            ImageUtils.saveImage(currentImage, outputImagePath);
        } catch (IOException e) {
            return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.SAVE_ERROR,
                    "Failed to save output image: " + e.getMessage());
        }

        return new PipelineTypes.PipelineResult(PipelineTypes.PipelineResultStatus.SUCCESS,
                "Image processed and saved successfully.");
    }

    // --- TODO: Candidate implements these image operations ---

    private BufferedImage applyGrayscale(BufferedImage original) {
        return new BufferedImage(0, 0, 0); // Placeholder, replace with actual grayscale logic
    }

    private BufferedImage applyInvert(BufferedImage original) {
        return new BufferedImage(0, 0, 0); // Placeholder, replace with actual invert logic
    }

    private BufferedImage applyBrightness(BufferedImage original, int factor) {
        return new BufferedImage(0, 0, 0); // Placeholder, replace with actual brightness logic
    }

    private BufferedImage applyResize(BufferedImage original, int newWidth, int newHeight) {
        return new BufferedImage(0, 0, 0); // Placeholder, replace with actual resize logic
    }
}
