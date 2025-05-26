#include "image_utils.h"    // For Image struct and load/save
#include "pipeline_types.h" // For OperationConfig
#include <iostream>         // For debug output
#include <algorithm>        // For std::clamp (C++17) or manual clamping
#include <cmath>            // For std::round, std::pow, etc.

#ifndef PIPELINE_EXECUTOR_IMPL
#define PIPELINE_EXECUTOR_IMPL

namespace image_pipeline
{

    class PipelineExecutor
    {
    public:
        PipelineExecutor() = default;

        PipelineResult processImage(
            const std::string &inputImagePath,
            const std::string &outputImagePath,
            const std::vector<OperationConfig> &operations)
        {

            Image image;
            if (!loadImage(inputImagePath, image))
            {
                return {PipelineResultStatus::FILE_NOT_FOUND, "Failed to load input image: " + inputImagePath};
            }

            if (!image.isValid())
            {
                return {PipelineResultStatus::PROCESSING_ERROR, "Loaded image is invalid."};
            }

            std::cout << "Loaded image: " << image.width << "x" << image.height << " (" << image.channels << " channels)" << std::endl;

            for (const auto &op_config : operations)
            {
                std::cout << "Applying operation: " << op_config.name << std::endl;
                bool success = false;
                if (op_config.name == "grayscale")
                {
                    success = applyGrayscale(image);
                }
                else if (op_config.name == "invert")
                {
                    success = applyInvert(image);
                }
                else if (op_config.name == "brightness")
                {
                    // TODO: Get brightness factor from op_config.parameters
                    // float factor = 1.0f; // Default
                    // if (op_config.parameters.count("factor")) {
                    //    if (std::holds_alternative<float>(op_config.parameters.at("factor"))) {
                    //        factor = std::get<float>(op_config.parameters.at("factor"));
                    //    } else if (std::holds_alternative<int>(op_config.parameters.at("factor"))) {
                    //        factor = static_cast<float>(std::get<int>(op_config.parameters.at("factor")));
                    //    } else { /* error or log */ }
                    // }
                    // success = applyBrightness(image, factor);
                    float factor = 1.0f;
                    if (op_config.parameters.count("factor"))
                    {
                        const auto &param_val = op_config.parameters.at("factor");
                        if (std::holds_alternative<float>(param_val))
                            factor = std::get<float>(param_val);
                        else if (std::holds_alternative<int>(param_val))
                            factor = static_cast<float>(std::get<int>(param_val));
                        else
                            return {PipelineResultStatus::CONFIG_ERROR, "Invalid type for brightness factor."};
                    }
                    else
                        return {PipelineResultStatus::CONFIG_ERROR, "Missing 'factor' for brightness."};
                    success = applyBrightness(image, factor);
                }
                else if (op_config.name == "resize")
                {
                    // TODO: Get new_width, new_height from op_config.parameters
                    // int new_width = image.width;
                    // int new_height = image.height;
                    // if (op_config.parameters.count("width") && std::holds_alternative<int>(op_config.parameters.at("width"))) {
                    //    new_width = std::get<int>(op_config.parameters.at("width"));
                    // }
                    // if (op_config.parameters.count("height") && std::holds_alternative<int>(op_config.parameters.at("height"))) {
                    //    new_height = std::get<int>(op_config.parameters.at("height"));
                    // }
                    // success = applyResize(image, new_width, new_height); // This is more complex!
                    return {PipelineResultStatus::PROCESSING_ERROR, "Resize operation not yet fully implemented (complex operation)."};
                }
                // TODO: Add more operations like blur, sharpen, contrast, rotate
                else
                {
                    return {PipelineResultStatus::CONFIG_ERROR, "Unknown operation: " + op_config.name};
                }

                if (!success)
                {
                    return {PipelineResultStatus::PROCESSING_ERROR, "Failed to apply operation: " + op_config.name};
                }
            }

            std::string output_format = "png"; // Default, or derive from outputImagePath
            size_t dot_pos = outputImagePath.find_last_of(".");
            if (dot_pos != std::string::npos)
            {
                std::string ext = outputImagePath.substr(dot_pos + 1);
                if (ext == "jpg" || ext == "jpeg" || ext == "bmp" || ext == "tga")
                {
                    output_format = ext;
                }
            }

            if (!saveImage(outputImagePath, image, output_format))
            {
                return {PipelineResultStatus::SAVE_ERROR, "Failed to save output image: " + outputImagePath};
            }

            return {PipelineResultStatus::SUCCESS, "Image processed and saved successfully."};
        }

    private:
        // --- TODO: Candidate implements these image operations ---

        bool applyGrayscale(Image &image)
        {
            if (!image.isValid() || image.channels < 3)
                return false; // Need at least RGB

            for (int y = 0; y < image.height; ++y)
            {
                for (int x = 0; x < image.width; ++x)
                {
                    // Simple average grayscale: (R + G + B) / 3
                    // uint8_t r = image.at(x, y, 0);
                    // uint8_t g = image.at(x, y, 1);
                    // uint8_t b = image.at(x, y, 2);
                    // uint8_t gray = static_cast<uint8_t>((r + g + b) / 3);

                    // Luminosity method (often better): 0.299R + 0.587G + 0.114B
                    float r_f = image.at(x, y, 0) / 255.0f;
                    float g_f = image.at(x, y, 1) / 255.0f;
                    float b_f = image.at(x, y, 2) / 255.0f;
                    float gray_f = 0.299f * r_f + 0.587f * g_f + 0.114f * b_f;
                    uint8_t gray = static_cast<uint8_t>(std::min(std::max(gray_f * 255.0f, 0.0f), 255.0f));

                    image.at(x, y, 0) = gray;
                    image.at(x, y, 1) = gray;
                    image.at(x, y, 2) = gray;
                    // Alpha channel (if present) is usually kept as is for grayscale
                }
            }
            return true;
        }

        bool applyInvert(Image &image)
        {
            if (!image.isValid())
                return false;
            for (int y = 0; y < image.height; ++y)
            {
                for (int x = 0; x < image.width; ++x)
                {
                    for (int c = 0; c < std::min(3, image.channels); ++c)
                    { // Invert RGB, leave Alpha
                        image.at(x, y, c) = 255 - image.at(x, y, c);
                    }
                }
            }
            return true;
        }

        bool applyBrightness(Image &image, float factor)
        {
            if (!image.isValid() || factor < 0)
                return false;
            for (int y = 0; y < image.height; ++y)
            {
                for (int x = 0; x < image.width; ++x)
                {
                    for (int c = 0; c < std::min(3, image.channels); ++c)
                    { // Adjust RGB, leave Alpha
                        float val = static_cast<float>(image.at(x, y, c));
                        val *= factor;
                        image.at(x, y, c) = static_cast<uint8_t>(std::min(std::max(val, 0.0f), 255.0f));
                    }
                }
            }
            return true;
        }

        bool applyResize(Image& image, int new_width, int new_height) {
            // This is a significantly more complex operation involving resampling.
            // For an interview, it might be out of scope unless a library like stb_image_resize is allowed.
            // Or, a very simple nearest-neighbor could be implemented.
            // STB does have stb_image_resize.h for this.
            // If not using stb_image_resize, this is a big task.
            std::cout << "Warning: Resize called but not fully implemented without a resizing library or complex algorithm." << std::endl;
            if (new_width <=0 || new_height <=0 || !image.isValid()) return false;
        
            std::vector<uint8_t> new_pixels(static_cast<size_t>(new_width) * new_height * image.channels);
            // TODO: Implement resampling (e.g., nearest neighbor, bilinear)
            // Nearest neighbor example:
            // float x_ratio = static_cast<float>(image.width) / new_width;
            // float y_ratio = static_cast<float>(image.height) / new_height;
            // for (int y_new = 0; y_new < new_height; ++y_new) {
            //     for (int x_new = 0; x_new < new_width; ++x_new) {
            //         int x_old = static_cast<int>(std::round(x_new * x_ratio));
            //         int y_old = static_cast<int>(std::round(y_new * y_ratio));
            //         x_old = std::clamp(x_old, 0, image.width - 1);
            //         y_old = std::clamp(y_old, 0, image.height - 1);
            //         for (int c = 0; c < image.channels; ++c) {
            //            new_pixels[(static_cast<size_t>(y_new) * new_width + x_new) * image.channels + c] = image.at(x_old, y_old, c);
            //         }
            //     }
            // }
            // image.pixels = std::move(new_pixels);
            // image.width = new_width;
            // image.height = new_height;
            return false; // Mark as not fully done for now
        }

        // TODO: Add applyBlur, applySharpen, applyContrast, etc.
        // Blur often involves convolution with a kernel (e.g., Gaussian blur).
        // This could be a good "extra credit" or discussion point.
    };

} // namespace image_pipeline
#endif // PIPELINE_EXECUTOR_IMPL
