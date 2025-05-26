#pragma once
#include <string>
#include <vector>
#include <cstdint> // For uint8_t

namespace image_pipeline
{

    // Simple Image class to hold pixel data
    struct Image
    {
        std::vector<uint8_t> pixels; // Flattened pixel data (R, G, B, A, R, G, B, A, ...)
        int width = 0;
        int height = 0;
        int channels = 0; // Number of color channels (e.g., 3 for RGB, 4 for RGBA)

        Image() = default;
        Image(int w, int h, int c, const uint8_t *data = nullptr);
        Image(const Image &other);
        Image(Image &&other) noexcept;
        Image &operator=(const Image &other);
        Image &operator=(Image &&other) noexcept;

        bool isValid() const { return width > 0 && height > 0 && channels > 0 && !pixels.empty(); }
        size_t pixelDataSize() const { return static_cast<size_t>(width) * height * channels; }
        uint8_t &at(int x, int y, int channel);             // For modification
        const uint8_t &at(int x, int y, int channel) const; // For const access
    };

    bool loadImage(const std::string &filename, Image &image);
    bool saveImage(const std::string &filename, const Image &image, const std::string &format = "png"); // format could be "png", "jpg", "bmp"

} // namespace image_pipeline
