#pragma once
#include <string>
#include <vector>
#include <map>
#include <variant>

namespace image_pipeline
{

    using OperationParameter = std::variant<int, float, std::string, bool>;

    struct OperationConfig
    {
        std::string name; // e.g., "grayscale", "resize", "blur", "brightness"
        std::map<std::string, OperationParameter> parameters;
        // e.g., for resize: {"width": 800, "height": 600}
        // e.g., for brightness: {"factor": 1.2f}
        // e.g., for blur: {"radius": 5}
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
