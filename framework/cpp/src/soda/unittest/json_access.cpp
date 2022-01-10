#include <optional>
#include <vector>

#include "jsonlib/nmjson.h"
#include "jsonproxy.h"

namespace soda::unittest {

template <typename T>
T json_proxy_access<T>::get(std::shared_ptr<js_obj_t> v)
{
    return v->get<T>();
}

template <typename T>
void json_proxy_access<T>::set(std::shared_ptr<js_obj_t> v, const T& t)
{
    v->set(t);
}

#define SODA_JSON_ACCESS_TYPE(type) template class json_proxy_access<type>;

using namespace std;

SODA_JSON_ACCESS_TYPE(bool)
SODA_JSON_ACCESS_TYPE(short)
SODA_JSON_ACCESS_TYPE(int)
SODA_JSON_ACCESS_TYPE(long long)
SODA_JSON_ACCESS_TYPE(float)
SODA_JSON_ACCESS_TYPE(double)
SODA_JSON_ACCESS_TYPE(long double)
SODA_JSON_ACCESS_TYPE(string)
SODA_JSON_ACCESS_TYPE(nullptr_t)

SODA_JSON_ACCESS_TYPE(vector<int>)
SODA_JSON_ACCESS_TYPE(vector<vector<int>>)
SODA_JSON_ACCESS_TYPE(vector<optional<int>>)
SODA_JSON_ACCESS_TYPE(vector<string>)
SODA_JSON_ACCESS_TYPE(vector<vector<string>>)
SODA_JSON_ACCESS_TYPE(vector<optional<string>>)
SODA_JSON_ACCESS_TYPE(vector<bool>)

} // namespace soda::unittest

