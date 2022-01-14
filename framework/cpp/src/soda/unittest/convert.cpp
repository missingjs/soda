#include "convert.h"

namespace soda::unittest {

NestedInteger ObjectConverter<NestedInteger>::parse(const JsonProxy& p)
{
    if (p.isNumber()) {
        return NestedInteger(p.get<int>());
    }
    NestedInteger ni;
    for (int i = 0; i < p.size(); ++i) {
        ni.add(parse(p[i]));
    }
    return ni;
}

JsonProxy ObjectConverter<NestedInteger>::serialize(const NestedInteger& ni)
{
    JsonProxy jp;
    if (ni.isInteger()) {
        jp.set(ni.getInteger());
    } else {
        for (auto& j : ni.getList()) {
            jp.append(serialize(j));
        }
    }
    return jp;
}

} // namespace soda::unittest

