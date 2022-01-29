#ifndef _SODA_UNITTEST_UTIL_H_
#define _SODA_UNITTEST_UTIL_H_

#include <iostream>
#include <string>
#include <tuple>
#include <type_traits>

#include "convert.h"

namespace soda::unittest {

template <typename Tuple, uint32_t Order>
struct arg_loader {
    static void load(const JsonProxy& data, Tuple& args) {
        if constexpr (Order > 0) {
            constexpr auto tupleIndex = Order - 1;
            using elem_t = typename std::tuple_element<tupleIndex, Tuple>::type;
            auto conv = ConverterFactory::create<elem_t>();
            std::get<tupleIndex>(args) = conv->fromJsonSerializable(data[tupleIndex]);
            arg_loader<Tuple,Order-1>::load(data, args);
        }
    }
};

template <typename... Args>
struct ArgumentLoader {
    using arguments_t = std::tuple<std::remove_const_t<std::remove_reference_t<Args>>...>;
    constexpr static uint32_t num_args = std::tuple_size<arguments_t>::value;
    static arguments_t load(const JsonProxy& params) {
        arguments_t args;
        arg_loader<arguments_t,num_args>::load(params, args);
        return args;
    }
};

class Utils {
public:
    static std::string fromStdin();
};

} // namespace soda::unittest

#endif

