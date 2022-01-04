#ifndef _SODA_UNITTEST_STRUCT_H_
#define _SODA_UNITTEST_STRUCT_H_

#include <functional>
#include <map>
#include <memory>
#include <string>
#include <tuple>
#include <type_traits>
#include <utility>
#include <vector>

#include "jsonproxy.h"
#include "util.h"

namespace soda::unittest {

template <typename... Args>
struct ArgTypes {};

template <typename T>
class StructTester {

    std::function<T*(const JsonProxy&)> factory;

    std::map<std::string, std::function<JsonProxy(T*, const JsonProxy&)>> funcMap;

public:

    using struct_t = T;

    JsonProxy test(const std::vector<std::string>& operations, const JsonProxy& parameters) {
        JsonProxy res;
        T* obj = factory(parameters[0]);
        res.append(JsonProxy{});

        for (int i = 1; i < parameters.size(); ++i) {
            auto name = operations[i];
            auto func = funcMap[name];
            res.append(func(obj, parameters[i]));
        }
        return res;
    }

    template <typename... Args>
    void withConstructorArgs(ArgTypes<Args...>) {
        factory = [=](const JsonProxy& params) {
            auto caller = [](auto&&... args) {
                return new T(std::forward<decltype(args)>(args)...);
            };
            auto args = ArgumentLoader<Args...>::load(params);
            return std::apply(caller, args);
        };
    }

    template <typename Ret, typename... Args>
    void withFunction(const std::string& name, Ret (T::*memFunc)(Args...)) {
        funcMap[name] = [=](T* obj, const JsonProxy& params) {
            auto caller = [=](auto&&... args) {
                return (obj->*memFunc)(std::forward<decltype(args)>(args)...);
            };
            auto args = ArgumentLoader<Args...>::load(params);
            auto r = std::apply(caller, args);
            return JsonProxy::fromData(r);
        };
    }

    template <typename... Args>
    void withFunction(const std::string& name, void (T::*memFunc)(Args...)) {
        funcMap[name] = [=](T* obj, const JsonProxy& params) {
            auto caller = [=](auto&&... args) {
                (obj->*memFunc)(std::forward<decltype(args)>(args)...);
            };
            auto args = ArgumentLoader<Args...>::load(params);
            std::apply(caller, args);
            return JsonProxy{};
        };
    }
};

template <typename Ts>
struct StructType {
};

template <typename Ts>
struct StructType<Ts*> {
    using type = typename Ts::struct_t;
};

template <typename Ts>
using StructTypeT = typename StructType<Ts>::type;

} // namespace soda::unittest

#endif