#include "workdata.h"

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

