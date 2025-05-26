package com.stripe.interview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java com.example.imagepipeline.Main <input_image_path> <output_image_path> [optional_config_file_path]");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];

        List<PipelineTypes.OperationConfig> operations = new ArrayList<>();
        if (args.length > 2) {
            String configFile = args[2];

            // try {
            //     System.out.println("Loading operations from: " + configFile);
            //     operations = loadOperationsFromJson(configFile);
            // } catch (IOException e) {
            //     System.err.println("Error loading operations config: " + e.getMessage());
            //     System.err.println("Falling back to default hardcoded operations.");
            //     // operations.clear(); // If you want to ensure it's empty before adding defaults
            //     // Add default operations here again if needed
            //     if (operations.isEmpty()) { // Only add defaults if loading failed AND operations is empty
            //        operations.add(new PipelineTypes.OperationConfig("grayscale", new HashMap<>()));
            //        Map<String, Object> defaultBrightnessParams = new HashMap<>();
            //        defaultBrightnessParams.put("factor", 1.2f);
            //        operations.add(new PipelineTypes.OperationConfig("brightness", defaultBrightnessParams));
            //     }
            // }



        } else {
            operations.add(new PipelineTypes.OperationConfig("grayscale", new HashMap<>()));

            Map<String, Object> brightnessParams = new HashMap<>();
            brightnessParams.put("factor", 1.5f);
            operations.add(new PipelineTypes.OperationConfig("brightness", brightnessParams));
        }

        // --- Define Operations Configuration (Hardcoded for simplicity) ---


        // operations.add(new PipelineTypes.OperationConfig("invert", new HashMap<>()));

        // Map<String, Object> resizeParams = new HashMap<>();
        // resizeParams.put("width", 150);
        // resizeParams.put("height", 100);
        // operations.add(new PipelineTypes.OperationConfig("resize", resizeParams));

        // operations.add(new PipelineTypes.OperationConfig("non_existent_op", new HashMap<>()));


        // --- If loading from JSON file: ---

        if (operations.isEmpty()) {
            System.err.println("No operations configured to perform.");
            System.exit(1);
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