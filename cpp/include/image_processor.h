#pragma once
#include "image_utils.h"
#include "pipeline_types.h"

namespace image_pipeline
{

    class IImageOperation
    {
    public:
        virtual ~IImageOperation() = default;
        virtual bool apply(Image &image, const std::map<std::string, OperationParameter> &params) = 0;
        virtual std::string getName() const = 0;
    };

    // Example concrete operations could be:
    // class GrayscaleOperation : public IImageOperation { ... };
    // class ResizeOperation : public IImageOperation { ... };

} // namespace image_pipeline
