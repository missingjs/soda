#include "jsonproxy.h"
#include "jsonlib/nmjson.h"

using namespace std;

namespace soda::unittest {

JsonProxy::JsonProxy(): obj{new js_obj_t} {}

JsonProxy::JsonProxy(const std::string& jstr): obj{new js_obj_t(jstr)} {}

JsonProxy::JsonProxy(std::shared_ptr<js_obj_t> p): obj{p} {}

JsonProxy::JsonProxy(const JsonProxy& jp): obj{jp.obj} {}

JsonProxy& JsonProxy::operator=(const JsonProxy& jp)
{
    if (this != &jp) {
        *obj = *jp.obj;
    }
    return *this;
}

int JsonProxy::size() const
{
    return obj->size();
}

bool JsonProxy::isNull() const
{
    return obj->isNull();
}

bool JsonProxy::isArray() const
{
    return obj->isArray();
}

bool JsonProxy::isBoolean() const
{
    return obj->isBoolean();
}

bool JsonProxy::isNumber() const
{
    return obj->isNumber();
}

bool JsonProxy::isObject() const
{
    return obj->isObject();
}

bool JsonProxy::isString() const
{
    return obj->isString();
}

bool JsonProxy::contains(const std::string& key) const
{
    return obj->contains(key);
}

JsonProxy JsonProxy::operator[](const std::string& key) const
{
    return JsonProxy{obj->operator[](key)};
}

JsonProxy JsonProxy::operator[](int index) const
{
    return JsonProxy{obj->operator[](index)};
}

string JsonProxy::dump() const
{
    return obj->dump();
}

bool JsonProxy::operator==(const JsonProxy& p2) const
{
    return *obj == *p2.obj;
}

JsonProxy JsonProxy::expandTail()
{
    int index = obj->size();
    obj->append(nullptr);
    return this->operator[](index);
}

void JsonProxy::append(const JsonProxy& p)
{
    expandTail() = p;
}

} // namespace soda::unittest
