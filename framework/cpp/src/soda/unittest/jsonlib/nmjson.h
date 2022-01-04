#ifndef _SODA_UNITTEST_NMJSON_H_
#define _SODA_UNITTEST_NMJSON_H_

#include <memory>
#include <string>

#include "nlohmann/json.hpp"

#include "nmjson_serial.h"

namespace soda::unittest {

namespace nmjson {

class JsonObject {

    using json = nlohmann::json;

    json* const ptr;

    const bool shared;

public:
    JsonObject();

    explicit JsonObject(const std::string& jstr);

    explicit JsonObject(json* j);

    ~JsonObject();

    JsonObject(const JsonObject& jobj);

    JsonObject& operator=(const JsonObject& jobj);

    bool operator==(const JsonObject& j2) const;

    template <typename T>
    T get() const { return ptr->get<T>(); }

    template <typename T>
    void set(const T& t) {
        *ptr = t;
    }

    template <typename T>
    void append(const T& t) {
        ptr->push_back(t);
    }

    std::shared_ptr<JsonObject> operator[](const std::string& key) const;

    std::shared_ptr<JsonObject> operator[](int index) const;

    int size() const;

    static JsonObject array();

    bool isNull() const;

    bool contains(const std::string& key) const;

    std::string dump() const;

    bool isArray() const;

    bool isBoolean() const;

    bool isNumber() const;

    bool isObject() const;

    bool isString() const;

};

} // namespace nmjson

} // namespace soda::unittest

#endif
