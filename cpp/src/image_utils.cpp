#include "image_utils.h"
#include <iostream>
#include <algorithm> // For std::min, std::max

// Define STB_IMAGE_IMPLEMENTATION and STB_IMAGE_WRITE_IMPLEMENTATION
// in *one* C/C++ file before #including stb_image.h and stb_image_write.h
#define STB_IMAGE_IMPLEMENTATION
#include "stb/stb_image.h"
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb/stb_image_write.h"

#ifndef IMAGE_UTILS_IMPL
#define IMAGE_UTILS_IMPL

namespace image_pipeline
{

    Image::Image(int w, int h, int c, const uint8_t *data) : width(w), height(h), channels(c)
    {
        if (w > 0 && h > 0 && c > 0)
        {
            size_t size = static_cast<size_t>(w) * h * c;
            pixels.resize(size);
            if (data)
            {
                std::copy(data, data + size, pixels.begin());
            }
        }
    }

    Image::Image(const Image &other) : pixels(other.pixels), width(other.width), height(other.height), channels(other.channels) {}

    Image::Image(Image &&other) noexcept : pixels(std::move(other.pixels)), width(other.width), height(other.height), channels(other.channels)
    {
        other.width = 0;
        other.height = 0;
        other.channels = 0;
    }

    Image &Image::operator=(const Image &other)
    {
        if (this != &other)
        {
            pixels = other.pixels;
            width = other.width;
            height = other.height;
            channels = other.channels;
        }
        return *this;
    }

    Image &Image::operator=(Image &&other) noexcept
    {
        if (this != &other)
        {
            pixels = std::move(other.pixels);
            width = other.width;
            height = other.height;
            channels = other.channels;
            other.width = 0;
            other.height = 0;
            other.channels = 0;
        }
        return *this;
    }

    uint8_t &Image::at(int x, int y, int channel)
    {
        if (x < 0 || x >= width || y < 0 || y >= height || channel < 0 || channel >= channels) {
            throw std::out_of_range("Image::at access out of bounds");
        }
        return pixels[(static_cast<size_t>(y) * width + x) * channels + channel];
    }

    const uint8_t &Image::at(int x, int y, int channel) const
    {
        if (x < 0 || x >= width || y < 0 || y >= height || channel < 0 || channel >= channels) {
            throw std::out_of_range("Image::at const access out of bounds");
        }
        return pixels[(static_cast<size_t>(y) * width + x) * channels + channel];
    }

    // --- Load/Save Functions ---
    bool loadImage(const std::string &filename, Image &image)
    {
        int w, h, c;
        unsigned char *data = stbi_load(filename.c_str(), &w, &h, &c, 0);
        if (!data)
        {
            std::cerr << "STB Load Error: " << stbi_failure_reason() << " for " << filename << std::endl;
            image.width = 0;
            image.height = 0;
            image.channels = 0;
            image.pixels.clear();
            return false;
        }

        /* Part I - to be completed */
        
        stbi_image_free(data);
        return true;
    }

    bool saveImage(const std::string &filename, const Image &image)
    {
        if (!image.isValid())
        {
            std::cerr << "Save Error: Image data is invalid." << std::endl;
            return false;
        }

        int success = 0;
        success = stbi_write_png(filename.c_str(), image.width, image.height, image.channels, image.pixels.data(), image.width * image.channels);

        if (!success)
        {
            std::cerr << "Save Error: Failed to write image to " << filename << std::endl;
            return false;
        }
        return true;
    }

} // namespace image_pipeline
#endif // IMAGE_UTILS_IMPL
