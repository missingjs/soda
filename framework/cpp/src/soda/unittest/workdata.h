#ifndef _SODA_UNITTEST_JSONPARSE_H_
#define _SODA_UNITTEST_JSONPARSE_H_

#include <memory>
#include <string>
#include <type_traits>
#include <utility>

#include "jsonproxy.h"

namespace soda::unittest {

class WorkInput {

    JsonProxy proxy;

public:
    WorkInput(const std::string& jstr);

    int getId() const;

    bool hasExpected() const;

    JsonProxy getExpected() const;

    JsonProxy getArg(int index) const;

    JsonProxy getArguments() const;

};

class WorkOutput {

    JsonProxy proxy;

public:
    WorkOutput();
    
    void setResult(const JsonProxy& res);

    void setId(int id);

    void setSuccess(bool s);

    void setElapse(double e);

    std::string toJSONString() const;

};

} // namespace soda::unittest

#endif
