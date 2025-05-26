#pragma once
#include <string>
#include <vector>
#include <cstdint> // For uint8_t

namespace image_pipeline
{
    struct Image
    {
        std::vector<uint8_t> pixels;
        int width = 0;
        int height = 0;
        int channels = 0;

        Image() = default;
        Image(int w, int h, int c, const uint8_t *data = nullptr);
        Image(const Image &other);
        Image(Image &&other) noexcept;
        Image &operator=(const Image &other);
        Image &operator=(Image &&other) noexcept;

        bool isValid() const {
            /* Part I - to be completed */
            return false;
        }
        size_t pixelDataSize() const { return static_cast<size_t>(width) * height * channels; }
        uint8_t &at(int x, int y, int channel);
        const uint8_t &at(int x, int y, int channel) const;
    };

    bool loadImage(const std::string &filename, Image &image);
    bool saveImage(const std::string &filename, const Image &image);

} // namespace image_pipeline
