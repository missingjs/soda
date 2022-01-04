#include "nmjson.h"

using namespace std;

namespace soda::unittest {

namespace nmjson {

JsonObject::JsonObject(): ptr{new json}, shared{false} {}

JsonObject::JsonObject(const std::string& jstr):
    ptr{new json(json::parse(jstr))}, shared{false} {}

JsonObject::JsonObject(json *j): ptr{j}, shared{true} {}

JsonObject::~JsonObject()
{
    if (!shared) {
        delete ptr;
    }
}

JsonObject::JsonObject(const JsonObject& jobj): ptr{new json(*jobj.ptr)}, shared{false} {}

JsonObject& JsonObject::operator=(const JsonObject& jobj)
{
    if (ptr != jobj.ptr) {
        *ptr = *jobj.ptr;
    }
    return *this;
}

bool JsonObject::operator==(const JsonObject& j2) const
{
    return *ptr == *j2.ptr;
}

std::shared_ptr<JsonObject> JsonObject::operator[](const std::string& key) const
{
    return make_shared<JsonObject>(&(*ptr)[key]);
}

std::shared_ptr<JsonObject> JsonObject::operator[](int index) const
{
    return make_shared<JsonObject>(&(*ptr)[index]);
}

int JsonObject::size() const
{
    return ptr->size();
}

bool JsonObject::isNull() const
{
    return !ptr || ptr->is_null();
}

bool JsonObject::contains(const std::string& key) const
{
    return ptr->contains(key);
}

string JsonObject::dump() const
{
    return ptr->dump();
}

JsonObject JsonObject::array()
{
    JsonObject obj;
    *obj.ptr = json::array();
    return obj;
}

bool JsonObject::isArray() const
{
    return ptr->is_array();
}

bool JsonObject::isBoolean() const
{
    return ptr->is_boolean();
}

bool JsonObject::isNumber() const
{
    return ptr->is_number();
}

bool JsonObject::isObject() const
{
    return ptr->is_object();
}

bool JsonObject::isString() const
{
    return ptr->is_string();
}

} // namespace nmjson

} // namespace soda::unittest
