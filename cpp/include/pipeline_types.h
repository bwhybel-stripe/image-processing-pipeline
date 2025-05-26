#pragma once
#include <string>
#include <vector>
#include <map>
#include <variant>

namespace image_pipeline
{
    struct OperationConfig
    {
        std::string name; // e.g., "grayscale", "resize", "blur", "brightness"
        std::map<std::string, int> parameters;
        // e.g., for resize: {"width": 800, "height": 600}
        // e.g., for brightness: {"factor": 120} (120%)
        // e.g., for resize: {"radius": 50} (50%)
    };

    // Could add more result details if needed
    enum class PipelineResultStatus
    {
        SUCCESS,
        FILE_NOT_FOUND,
        CONFIG_ERROR,
        PROCESSING_ERROR,
        SAVE_ERROR
    };

    struct PipelineResult
    {
        PipelineResultStatus status;
        std::string message;
    };

} // namespace image_pipeline
