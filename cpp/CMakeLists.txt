cmake_minimum_required(VERSION 3.10)
project(image_processing_pipeline)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Include directories
include_directories(
    ${CMAKE_CURRENT_SOURCE_DIR}/include
    ${CMAKE_CURRENT_SOURCE_DIR}/lib/external # For stb
    # ${CMAKE_CURRENT_SOURCE_DIR}/lib/external/nlohmann # If using nlohmann/json
)

# Add source files
# For the interview, main.cpp includes other .cpp files for simplicity.
set(SOURCES
    src/main.cpp
    # src/pipeline_executor.cpp # Included by main.cpp
    # src/image_utils.cpp       # Included by main.cpp
)

add_executable(image_processing_pipeline ${SOURCES})

if(MSVC)
    target_compile_options(image_processing_pipeline PRIVATE /W4)
else()
    target_compile_options(image_processing_pipeline PRIVATE -Wall -Wextra -Wno-unused-parameter)
endif()

# Copy data files to build directory
file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/data DESTINATION ${CMAKE_CURRENT_BINARY_DIR})

# Link math library if needed (e.g. for std::round, std::pow with some toolchains/older standards)
# For C++17 and modern compilers, often not explicitly needed for <cmath>
# target_link_libraries(image_processing_pipeline PRIVATE m)
