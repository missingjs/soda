cmake_minimum_required(VERSION 3.5 FATAL_ERROR)

project(soda-cpp LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
# set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

file(GLOB_RECURSE SRCS src "*.cpp")

list(REMOVE_ITEM SRCS "${CMAKE_CURRENT_LIST_DIR}/src/soda/unittest/bootstrap.cpp")

set(CMAKE_CXX_FLAGS "$ENV{SODA_INCLUDE} $ENV{SODA_CPP_FLAGS}")

add_library(soda-cpp SHARED ${SRCS})

