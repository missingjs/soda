#ifndef _SODA_UNITTEST_FEATURE_H_
#define _SODA_UNITTEST_FEATURE_H_

#include <functional>

namespace soda::unittest {

template <typename T>
class ObjectFeature {
public:
    virtual size_t hash(const T& t) const = 0;
    virtual bool isEqual(const T& t1, const T& t2) const = 0;
};

} // namespace sode::unittest

#endif
