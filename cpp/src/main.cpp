#include "pipeline_types.h" // For OperationConfig, PipelineResult
#include "image_utils.h"    // For Image struct (definition needed)

#include <iostream>
#include <vector>
#include <string>
#include <fstream>

#include "image_utils.cpp"
#include "pipeline_executor.cpp"

using namespace image_pipeline;

#include "nlohmann/json.hpp"
using json = nlohmann::json;
std::vector<OperationConfig> load_operations_from_json(const std::string &config_path)
{
    /* Part IV - to be implemented */
    std::vector<OperationConfig> operations;
    return operations;
}

bool ends_with_png(const std::string &str)
{
    const std::string suffix = ".png";
    return str.size() >= suffix.size() &&
           str.compare(str.size() - suffix.size(), suffix.size(), suffix) == 0;
}

int main(int argc, char *argv[])
{
    if (argc < 3)
    {
        std::cerr << "Usage: " << argv[0] << " <input_image_path> <output_image_path> [optional_config_file]" << std::endl;
        return 1;
    }

    std::string input_path = argv[1];
    std::string output_path = argv[2];

    if (!ends_with_png(input_path) || !ends_with_png(output_path))
    {
        std::cerr << "Error: Both input and output files must be PNG files" << std::endl;
        return 1;
    }

    std::vector<OperationConfig> operations;
    if (argc > 3)
    {
        std::string config_path;
        try
        {
            operations = load_operations_from_json(config_path);
        }
        catch (const std::exception &e)
        {
            std::cerr << "Error loading operations config: " << e.what() << std::endl;
            return 1;
        }
    }
    else
    {
        operations.push_back({});
        /* Part II - uncomment to test */
        // operations.push_back({"grayscale", {}});
        /* Part III - uncomment to test */
        // operations.push_back({"brightness", {{"factor", 1.5f}}});
        // operations.push_back({"invert", {}});
        /* Part V - uncomment to test */
        // operations.push_back({"resize", {{"width", 100}, {"height", 100}}});
    }

    PipelineExecutor executor;
    std::cout << "Processing pipeline for image: " << input_path << std::endl;
    PipelineResult result = executor.processImage(input_path, output_path, operations);

    std::cout << "Pipeline Result: " << result.message << std::endl;
    if (result.status == PipelineResultStatus::SUCCESS)
    {
        std::cout << "Output image saved to: " << output_path << std::endl;
        return 0;
    }
    else
    {
        return 1;
    }
}
