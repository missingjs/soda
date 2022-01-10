#ifndef _SODA_UNITTEST_JSONPROXY_H_
#define _SODA_UNITTEST_JSONPROXY_H_

#include <memory>
#include <string>
#include <type_traits>

namespace soda::unittest {

namespace nmjson {
    class JsonObject;
}

using js_obj_t = nmjson::JsonObject;

template <typename T>
struct json_proxy_access {
    static T get(std::shared_ptr<js_obj_t> v);
    static void set(std::shared_ptr<js_obj_t> v, const T& t);
};

class JsonProxy {

    std::shared_ptr<js_obj_t> obj;

public:
    JsonProxy();

    explicit JsonProxy(const std::string& jstr);

    JsonProxy(std::shared_ptr<js_obj_t> p);

    JsonProxy(const JsonProxy& jp);

    // !! conflict with JsonProxy(const std::string&);
    // template <typename T>
    // explicit JsonProxy(const T& t): JsonProxy() {
    //     set(t);
    // }

    template <typename T>
    static JsonProxy fromData(const T& t) {
        JsonProxy jp;
        jp.set(t);
        return jp;
    }

    JsonProxy& operator=(const JsonProxy& jp);

    bool operator==(const JsonProxy& jp) const;

    template <typename T>
    T get() const {
        return json_proxy_access<std::remove_reference_t<T>>::get(obj);
    }

    template <typename T>
    void set(const T& t) {
        json_proxy_access<std::remove_reference_t<T>>::set(obj, t);
    }

    template <typename T>
    JsonProxy& operator=(const T& t) {
        set(t);
        return *this;
    }

    template <typename T>
    void append(const T& t) {
        auto tail = expandTail();
        tail = t;
    }

    void append(const JsonProxy& p);

    int size() const;

    bool isNull() const;

    bool contains(const std::string& key) const;

    JsonProxy operator[](const std::string& key) const;

    JsonProxy operator[](int index) const;

    std::string dump() const;

    bool isArray() const;

    bool isBoolean() const;

    bool isNumber() const;

    bool isObject() const;

    bool isString() const;

private:
    JsonProxy expandTail();

};

} // namespace soda::unittest

#endif
