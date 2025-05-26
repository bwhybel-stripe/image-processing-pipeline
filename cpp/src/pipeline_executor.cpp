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
                    int factor = 100;
                    if (op_config.parameters.count("factor"))
                    {
                        factor = op_config.parameters.at("factor");
                    }
                    else
                    {
                        return {PipelineResultStatus::CONFIG_ERROR, "Missing 'factor' for brightness."};
                    }
                    success = applyBrightness(image, factor);
                }
                else if (op_config.name == "resize")
                {
                    int new_width = image.width;
                    int new_height = image.height;
                    if (op_config.parameters.count("width") && op_config.parameters.count("height"))
                    {
                        new_width = op_config.parameters.at("width");
                        new_height = op_config.parameters.at("height");
                    }
                    else
                    {
                        return {PipelineResultStatus::CONFIG_ERROR, "Missing 'height' and 'weight' for resize."};
                    }
                    success = applyResize(image, new_width, new_height);
                }
                else
                {
                    return {PipelineResultStatus::CONFIG_ERROR, "Unknown operation: " + op_config.name};
                }

                if (!success)
                {
                    return {PipelineResultStatus::PROCESSING_ERROR, "Failed to apply operation: " + op_config.name};
                }
            }

            std::string output_format = "png";
            size_t dot_pos = outputImagePath.find_last_of(".");

            if (dot_pos == std::string::npos ||
                outputImagePath.substr(dot_pos + 1) != output_format)
            {
                return {PipelineResultStatus::CONFIG_ERROR, "Output image filepath must end with ." + output_format + ": " + outputImagePath};
            }

            if (!saveImage(outputImagePath, image))
            {
                return {PipelineResultStatus::SAVE_ERROR, "Failed to save output image: " + outputImagePath};
            }

            return {PipelineResultStatus::SUCCESS, "Image processed and saved successfully."};
        }

    private:
        bool applyGrayscale(Image &image)
        {
            /* Part II - to be implemented */
            return true;
        }

        bool applyInvert(Image &image)
        {
            /* Part III - to be implemented */
            return true;
        }

        bool applyBrightness(Image &image, int factor)
        {
            /* Part III - to be implemented */
            return true;
        }

        bool applyResize(Image &image, int new_width, int new_height)
        {
            /* Part V - to be implemented */
            return true;
        }
    };

} // namespace image_pipeline
#endif // PIPELINE_EXECUTOR_IMPL
