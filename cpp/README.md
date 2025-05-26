# Image Processing Pipeline

## Overview

You are tasked with building an image processing pipeline. The system will read an input image, apply a series of configurable image manipulation operations, and save the resulting image. This exercise focuses on integrating image loading/saving functionalities with custom processing logic.

## Setup

1.  **Clone Repository:**
    ```bash
    git clone <repository_url>
    cd image-processing-pipeline
    ```

3.  **Build the Project:**
    ```bash
    mkdir build && cd build
    cmake ..
    make
    ```
4.  **Run the Application:**
    The `main` application takes an input image path and an output image path.
    ```bash
    ./image_processing_pipeline ../data/input_image.png output_processed.png 
    ```
    (Ensure `data/input_image.png` exists. You can use any sample PNG.)
    The operations to be applied are currently hardcoded in `src/main.cpp`. Optionally, you can extend it to read `data/operations_config.json`.

## Your Task

Your primary task is to implement the image processing logic within the `PipelineExecutor` class in `src/pipeline_executor.cpp`.
You will also need to complete parts of the `Image` struct and `loadImage`/`saveImage` functions in `src/image_utils.cpp` (though `loadImage` and `saveImage` are mostly provided using STB).

1.  **Image Representation (`image_utils.h` & `image_utils.cpp`):**
    *   Understand the `Image` struct.
    *   (Optional, good practice) Consider adding bounds checking to `Image::at()` methods.
    *   Ensure `loadImage` and `saveImage` correctly use `stb_image.h` and `stb_image_write.h`. The STB implementation definitions (`#define STB_IMAGE_IMPLEMENTATION`) are handled in `image_utils.cpp`.

2.  **Pipeline Execution (`pipeline_executor.cpp`):**
    *   The `processImage` method is the main entry point.
    *   It loads an image using `loadImage`.
    *   It iterates through a list of `OperationConfig` structs.
    *   For each operation, you need to dispatch to the correct implementation (e.g., `applyGrayscale`, `applyBrightness`).
    *   Implement the actual pixel manipulation logic for the following operations:
        *   `applyGrayscale(Image& image)`
        *   `applyInvert(Image& image)`
        *   `applyBrightness(Image& image, float factor)` (get `factor` from `OperationConfig::parameters`)
    *   Handle parameters passed via `OperationConfig::parameters`.
    *   Ensure operations modify the `Image` object in place.
    *   It saves the final image using `saveImage`.
    *   Return appropriate `PipelineResult`.

Look for `TODO` comments in `src/pipeline_executor.cpp` and `src/image_utils.cpp`.

## Provided Components

*   **Pipeline Types (`include/pipeline_types.h`):** Defines `OperationConfig`, `PipelineResultStatus`, etc.
*   **Image Utilities (`include/image_utils.h`, `src/image_utils.cpp`):** Defines the `Image` struct and provides (mostly complete) `loadImage` and `saveImage` functions using STB libraries.
*   **Pipeline Executor Skeleton (`src/pipeline_executor.cpp`):** Where you'll implement the core logic.
*   **Main Application (`src/main.cpp`):** Drives the pipeline. It defines a sample sequence of operations and calls your `PipelineExecutor`.
*   **STB Libraries (`lib/external/stb/`):** Single-header libraries for image I/O.
*   **Data (`data/`):**
    *   `input_image.png`: A placeholder for your sample input image.
    *   *(Optional)* `operations_config.json`: If you choose to implement JSON configuration loading.

## Parts of the Problem (Suggested Progression)

1.  **Part 1: Setup and Basic Load/Save**
    *   Ensure you can build and run the project.
    *   Verify that `loadImage` and `saveImage` work by running `main` which (initially) might just load and save the image without operations.
    *   Understand how pixel data is stored in the `Image` struct.

2.  **Part 2: Implement Grayscale Operation**
    *   In `pipeline_executor.cpp`, implement `applyGrayscale`.
    *   Modify `main.cpp` (or the `operations` vector in `PipelineExecutor::processImage`) to include the "grayscale" operation.
    *   Run and verify the output is grayscale.

3.  **Part 3: Implement Brightness and Invert Operations**
    *   Implement `applyBrightness`, ensuring you correctly parse the `factor` parameter from `OperationConfig`.
    *   Implement `applyInvert`.
    *   Test these operations individually and in sequence.

4.  **Part 4: Parameter Handling and Error Checking**
    *   Make sure `OperationConfig::parameters` are safely accessed and type-checked (e.g., using `std::holds_alternative` and `std::get`).
    *   Add error checking for invalid operation parameters (e.g., brightness factor out of reasonable range, though the current struct doesn't enforce this).
    *   Ensure `PipelineResult` is correctly populated for errors (e.g., unknown operation, parameter error).

5.  **Part 5 (Extra Credit / Discussion):**
    *   Implement a more complex operation like a simple Box Blur or Nearest Neighbor Resize. (Note: `stb_image_resize.h` exists for high-quality resizing if allowed).
    *   Discuss how you would abstract individual image operations into separate classes implementing an `IImageOperation` interface (see optional `image_processor.h`).
    *   How would you handle operations that change image dimensions or channel count?
    *   Discuss memory management for very large images.

## Evaluation Criteria
*   **Correctness:** Are image operations implemented correctly? Is the output image as expected?
*   **Code Quality:** Is pixel manipulation code clear and efficient (within reason for an interview)? Are abstractions used well?
*   **Error Handling:** How are file errors, invalid operations, or bad parameters handled?
*   **Problem Solving:** Approach to implementing image algorithms and structuring the pipeline.
*   **Communication:** Explanation of image processing logic and design choices.

Good luck!
