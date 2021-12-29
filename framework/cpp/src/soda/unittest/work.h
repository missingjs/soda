#ifndef _SODA_UNITTEST_WORK_H_
#define _SODA_UNITTEST_WORK_H_

#include <array>
#include <chrono>
#include <functional>
#include <iostream>
#include <memory>
#include <tuple>
#include <type_traits>
#include <utility>

#include "loader.h"
#include "parse.h"
#include "special_convert.h"
#include "workdata.h"

namespace soda::unittest {

template <int Index, typename PL, typename Tuple>
void do_load_arg(PL& parserList, WorkInput& wd, Tuple& args) {
    using elem_t = typename std::tuple_element<Index, Tuple>::type;
    using parser_t = TypedDataParser<elem_t>;
    DataParser* ap = parserList[Index];
    if (!ap) {
        ap = new parser_t;
    }
    std::get<Index>(args) = static_cast<parser_t*>(ap)->parse(wd.getArg(Index));
    if (!parserList[Index]) {
        delete ap;
    }
}

template <int Index, typename PL, typename Tuple>
struct arg_loader {
    static void load(PL& parserList, WorkInput& wd, Tuple& args) {
        do_load_arg<Index>(parserList, wd, args);
        arg_loader<Index-1,PL,Tuple>::load(parserList, wd, args);
    }
};

template <typename PL, typename Tuple>
struct arg_loader<0,PL,Tuple> {
    static void load(PL& parserList, WorkInput& wd, Tuple& args) {
        do_load_arg<0>(parserList, wd, args);
    }
};

template <typename T, typename Enable = void>
struct default_validator {
    bool operator() (const T& t1, const T& t2) {
        return t1 == t2;
    }
};

template <typename T>
struct default_validator<T, typename std::enable_if<use_custom_serializer<T>::value>::type> {
    bool operator() (const T& t1, const T& t2) {
        std::cerr << "Warn: you use macro USE_CUSTOM_SERIALIZER, but no custom validator specified\n";
        return false;
    }
};

template <typename Return, typename... Args>
class TestWork {

    using arguments_t = std::tuple<std::remove_reference_t<Args>...>;

    constexpr static int numArgs = std::tuple_size<arguments_t>::value;

    std::function<Return(Args...)> func;

    std::shared_ptr<WorkLoader> loader;

    std::array<DataParser*, numArgs> argParsers;

    TypedDataSerializer<Return>* resultSerializer;

    TypedDataParser<Return>* resultParser;

    std::function<bool(Return&,Return&)> validator;

    bool compareSerial{false};

public:
    TestWork(Return (*pFunc)(Args...)): func(pFunc) {
        initialize();
    }

    template <typename Class>
    TestWork(Class* obj, Return (Class::*memFunc)(Args...)) {
        auto fn = std::mem_fn(memFunc);
        func = [=](Args&&... args) {
            return (obj->*memFunc)(std::forward<Args>(args)...);
        };
        initialize();
    }

    template <typename Func>
    TestWork(Func fn): func(fn) {
        initialize();
    }

    ~TestWork() {
        for (auto it = argParsers.begin(); it != argParsers.end(); ++it) {
            delete *it;
        }
        delete resultSerializer;
        delete resultParser;
    }

    template <int N, typename From, typename Func>
    void setArgParser(Func fn) {
        if (argParsers[N]) {
            delete argParsers[N];
        }
        using to_type = std::tuple_element_t<N, arguments_t>;
        argParsers[N] = new CustomDataParser<From, to_type, Func>(fn);
    }

    template <int N>
    void setArgParser(TypedDataParser<std::tuple_element_t<N, arguments_t>>* parser) {
        if (argParsers[N]) {
            delete argParsers[N];
        }
        argParsers[N] = parser;
    }

    template <typename From, typename Func>
    void setResultParser(Func fn) {
        if (resultParser) {
            delete resultParser;
        }
        resultParser = new CustomDataParser<From, Return, Func>(fn);
    }

    template <typename Func>
    void setResultSerializer(Func fn) {
        if (resultSerializer) {
            delete resultSerializer;
        }
        resultSerializer = new CustomDataSerializer<Return, Func>(fn);
    }

    template <typename Func>
    void setValidator(Func fn) {
        validator = [=](Return& e, Return& r) -> bool { return fn(e, r); };
    }

    void setCompareSerial(bool b) {
        compareSerial = b;
    }

    void run() {
        WorkInput input {loader->load()};
        arguments_t arguments;
        loadArgs(input, arguments);

        auto caller = [&](auto&&... args) {
            return func(std::forward<decltype(args)>(args)...);
        };

        using namespace std::chrono;
        auto startMicro = steady_clock::now();
        auto result = std::apply(caller, arguments);
        auto endMicro = steady_clock::now();
        auto elapseMicro = duration_cast<microseconds>(endMicro - startMicro).count();
        auto elapseMillis = elapseMicro / 1000.0;

        auto json_res = resultSerializer->serialize(result);

        WorkOutput output;
        output.setId(input.getId());
        output.setResult(json_res);
        output.setElapse(elapseMillis);

        bool success = true;
        if (input.hasExpected()) {
            if (!compareSerial) {
                auto expect = resultParser->parse(input.getExpected());
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
        for (auto it = argParsers.begin(); it != argParsers.end(); ++it) {
            *it = nullptr;
        }
        resultSerializer = new TypedDataSerializer<Return>();
        resultParser = new TypedDataParser<Return>();
        // validator = [](Return& e, Return& r) { return e == r; };
        validator = default_validator<Return>();
    }

    void loadArgs(WorkInput& wd, arguments_t& args) {
        arg_loader<numArgs-1,decltype(argParsers),arguments_t>::load(argParsers, wd, args);
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
        return new TestWork<Return,Args...>(obj, memFunc);
    }

    template <typename Return, typename...Args, typename Class>
    static TestWork<Return,Args...>* create(Class& obj, Return (Class::*memFunc)(Args...)) {
        return new TestWork<Return,Args...>(&obj, memFunc);
    }

    template <typename Return, typename...Args, typename Func>
    static TestWork<Return,Args...>* create(Func fn) {
        return new TestWork<Return,Args...>(fn);
    }

    template <typename FClass>
    static decltype(auto) createByFunctor(FClass fn) {
        return create(fn, &FClass::operator());
    }
};

} // namespace soda::unittest

#endif
