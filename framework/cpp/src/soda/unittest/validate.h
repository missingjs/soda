#ifndef _SODA_UNITTEST_VALIDATE_H_
#define _SODA_UNITTEST_VALIDATE_H_

#include <algorithm>
#include <functional>
#include <iterator>
#include <unordered_map>
#include <vector>

#include "feature.h"
#include "feature_factory.h"
#include "unordered.h"

namespace soda::unittest {

class Validators {
public:
    template <typename T>
    using Vect = std::vector<T>;

    template <typename T>
    using Vect2d = std::vector<std::vector<T>>;

    template <typename T>
    static std::function<bool(const Vect<T>&, const Vect<T>&)> forVector(bool ordered) {
        return forVector(ordered, FeatureFactory::create<T>());
    }

    template <typename T>
    static auto forVector2d(bool dim1Ordered, bool dim2Ordered) {
        // using func_t = std::function<bool(const Vect2d<T>&,const Vect2d<T>&)>;
        auto elemFeat = FeatureFactory::create<T>();
        return dim2Ordered
            ? forVector<std::vector<T>>(dim1Ordered, std::make_shared<ListFeature<T>>(elemFeat))
            : forVector<std::vector<T>>(dim1Ordered, std::make_shared<UnorderedListFeature<T>>(elemFeat));
    }

private:
    template <typename T>
    static std::function<bool(const std::vector<T>&, const std::vector<T>&)>
    forVector(bool ordered, std::shared_ptr<ObjectFeature<T>> elemFeat) {
        if (ordered) {
            return [=](const std::vector<T>& a, const std::vector<T>& b) {
                return ListFeature<T>{elemFeat}.isEqual(a, b);
            };
        } else {
            return [=](const std::vector<T>& a, const std::vector<T>& b) {
                return UnorderedListFeature<T>{elemFeat}.isEqual(a, b);
            };
        }
    }

};

} // namespace soda::unittest

#endif
