#include <cstddef>
#include <optional>
#include <sstream>

#include "workdata.h"
#include "json_access.h"  // must included after workdata.h

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

namespace soda::unittest {

WorkInput::WorkInput(const std::string& jstr): proxy{jstr} {}

int WorkInput::getId() const
{
    return proxy["id"].get<int>();
}

bool WorkInput::hasExpected() const
{
    return proxy.contains("expected") && !proxy["expected"].isNull();
}

JsonProxy WorkInput::getExpected() const 
{
    return proxy["expected"];
}

JsonProxy WorkInput::getArg(int index) const
{
    return getArguments()[index];
}

JsonProxy WorkInput::getArguments() const
{
    return proxy["args"];
}

WorkOutput::WorkOutput(): proxy{} {}

void WorkOutput::setResult(const JsonProxy& res)
{
    proxy["result"] = res;
}

void WorkOutput::setId(int id)
{
    proxy["id"] = id;
}

void WorkOutput::setSuccess(bool s)
{
    proxy["success"] = s;
}

void WorkOutput::setElapse(double e)
{
    proxy["elapse"] = e;
}

std::string WorkOutput::toJSONString() const
{
    return proxy.dump();
}

} // namespace soda::unittest
