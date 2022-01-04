#ifndef _SODA_UNITTEST_WORK_H_
#define _SODA_UNITTEST_WORK_H_

#include <chrono>
#include <functional>
#include <memory>
#include <tuple>
#include <type_traits>
#include <utility>

#include "convert.h"
#include "loader.h"
#include "struct.h"
#include "util.h"
#include "workdata.h"

namespace soda::unittest {

template <typename Return, typename... Args>
class TestWork {

    std::function<Return(Args...)> func;

    std::shared_ptr<WorkLoader> loader;

    std::function<bool(const Return&, const Return&)> validator;

    bool compareSerial{false};

public:
    template <typename Func>
    TestWork(Func fn): func(fn) {
        initialize();
    }

    template <typename Func>
    void setValidator(Func fn) {
        validator = [=](const Return& e, const Return& r) -> bool { return fn(e, r); };
    }

    void setCompareSerial(bool b) {
        compareSerial = b;
    }

    void run() {
        WorkInput input {loader->load()};
        auto arguments = ArgumentLoader<Args...>::load(input.getArguments());

        auto caller = [&](auto&&... args) {
            return func(std::forward<decltype(args)>(args)...);
        };

        using namespace std::chrono;
        auto startMicro = steady_clock::now();
        auto result = std::apply(caller, arguments);
        auto endMicro = steady_clock::now();
        auto elapseMicro = duration_cast<microseconds>(endMicro - startMicro).count();
        auto elapseMillis = elapseMicro / 1000.0;

        auto resConv = ConverterFactory::create<Return>();
        auto json_res = resConv->toJsonSerializable(result);

        WorkOutput output;
        output.setId(input.getId());
        output.setResult(json_res);
        output.setElapse(elapseMillis);

        bool success = true;
        if (input.hasExpected()) {
            if (!compareSerial) {
                auto expect = resConv->fromJsonSerializable(input.getExpected());
                success = validator(expect, result);
            } else {
                success = (input.getExpected() == json_res);
            }
        }
        output.setSuccess(success);

        loader->store(output.toJSONString());
    }

private:
    void initialize() {
        loader = LoaderFactory::byStdin();
        setValidator([](const Return& e, const Return& r) { return e == r; });
    }

};

class WorkFactory {
public:
    template <typename Return, typename... Args>
    static TestWork<Return,Args...>* create(Return (*pFunc)(Args...)) {
        return new TestWork<Return,Args...>(pFunc);
    }

    template <typename Return, typename...Args, typename Class>
    static TestWork<Return,Args...>* create(Class* obj, Return (Class::*memFunc)(Args...)) {
        auto fn = [=](Args&&... args) {
            return (obj->*memFunc)(std::forward<Args>(args)...);
        };
        return new TestWork<Return,Args...>(fn);
    }

    template <typename Return, typename...Args, typename Class>
    static TestWork<Return,Args...>* create(Class& obj, Return (Class::*memFunc)(Args...)) {
        return create(&obj, memFunc);
    }

    template <typename Return, typename...Args, typename Func>
    static TestWork<Return,Args...>* create(Func fn) {
        return new TestWork<Return,Args...>(fn);
    }

    template <typename FClass>
    static decltype(auto) createByFunctor(FClass fn) {
        return create(fn, &FClass::operator());
    }

    template <typename Arg, typename Class>
    static decltype(auto) create(Class* obj, void(Class::*memFunc)(Arg&)) {
        auto fn = [=](Arg& arg) -> Arg {
            (obj->*memFunc)(arg);
            return arg;
        };
        return new TestWork<Arg,Arg&>(fn);
    }

    template <typename Arg, typename Class>
    static decltype(auto) create(Class& obj, void(Class::*memFunc)(Arg&)) {
        return create(&obj, memFunc);
    }

    template <typename T, typename... Args>
    static StructTester<T>* createStructTester() {
        auto st = new StructTester<T>();
        st->withConstructorArgs(ArgTypes<Args...>{});
        return st;
    }

    template <typename T>
    static decltype(auto) forStruct(StructTester<T>* st) {
        return create(st, &StructTester<T>::test);
    }
};

} // namespace soda::unittest

#define NEW_STRUCT_TESTER(T, ...) WorkFactory::createStructTester<T,__VA_ARGS__>();
#define ADD_FUNCTION(ts, name) ts->withFunction(#name, &StructTypeT<decltype(ts)>::name);

#endif
