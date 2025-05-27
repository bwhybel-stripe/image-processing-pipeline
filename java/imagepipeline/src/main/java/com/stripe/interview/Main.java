package com.stripe.interview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<PipelineTypes.OperationConfig> loadOperationsFromJson(String configFile) throws IOException {
        // Part IV - Implement this method to read a JSON file and return a list of OperationConfig objects.

        // This method should read the JSON file and parse it into a list of OperationConfig objects.

        return new ArrayList<>(); // Placeholder, replace with actual JSON parsing logic
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Main <input_image_path> <output_image_path> [optional_config_file_path]");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];

        // Validate both files are .png
        if (!inputPath.toLowerCase().endsWith(".png") ||
                !outputPath.toLowerCase().endsWith(".png")) {
            System.err.println("Error: Both input and output files must be PNG files");
            System.exit(1);
        }

        List<PipelineTypes.OperationConfig> operations = new ArrayList<>();
        if (args.length > 2) {
            String configFile = args[2];

             try {
                 System.out.println("Loading operations from: " + configFile);
                 operations = loadOperationsFromJson(configFile);
             } catch (IOException e) {
                 System.err.println("Error loading operations config: " + e.getMessage());
                 return;
             }
        } else {
            // Part II - uncomment the following lines to add operations manually
            // operations.add(new PipelineTypes.OperationConfig("grayscale", new HashMap<>()));

            // Part III - uncomment the following lines to add operations manually
            // Map<String, Integer> brightnessParams = new HashMap<>();
            // brightnessParams.put("factor", 150);
            // operations.add(new PipelineTypes.OperationConfig("brightness", brightnessParams));
            // operations.add(new PipelineTypes.OperationConfig("invert", new HashMap<>()));

            // Part V - uncomment the following lines to add operations manually
            // Map<String, Integer> resizeParams = new HashMap<>();
            // resizeParams.put("width", 150);
            // resizeParams.put("height", 75);
            // operations.add(new PipelineTypes.OperationConfig("resize", resizeParams));
        }

        PipelineExecutor executor = new PipelineExecutor();
        System.out.println("Processing pipeline for image: " + inputPath);
        for (PipelineTypes.OperationConfig op : operations) {
            System.out.println("  - Operation: " + op.getName() +
                    (op.getParameters().isEmpty() ? "" : " with params: " + op.getParameters()));
        }

        PipelineTypes.PipelineResult result = executor.processImage(inputPath, outputPath, operations);

        System.out.println("Pipeline Result: " + result.getMessage());
        if (result.getStatus() == PipelineTypes.PipelineResultStatus.SUCCESS) {
            System.out.println("Output image saved to: " + outputPath);
            System.exit(0);
        } else {
            System.exit(1);
        }
    }
}