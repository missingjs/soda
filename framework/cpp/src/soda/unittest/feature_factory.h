#ifndef _SODA_UNITTEST_FEATURE_FACTORY_H_
#define _SODA_UNITTEST_FEATURE_FACTORY_H_

#include <memory>
#include <vector>

#include "feature.h"

namespace soda::unittest {

template <typename T>
class DefaultObjectFeature : public ObjectFeature<T> {
public:
    size_t hash(const T& t) const override {
        return std::hash<T>()(t);
    }
    bool isEqual(const T& a, const T& b) const override {
        return std::equal_to<T>()(a, b);
    }
};

class DoubleFeature : public DefaultObjectFeature<double> {
public:
    bool isEqual(const double& a, const double& b) const override {
        return std::abs(a-b) < 1e-6;
    }
};

template <typename T>
class ListFeature : public ObjectFeature<std::vector<T>> {
public:
    ListFeature(std::shared_ptr<ObjectFeature<T>> elemFeat): elemFeat{elemFeat} {}

    size_t hash(const std::vector<T>& t) const override {
        size_t res = 0;
        for (auto& e : t) {
            auto h = elemFeat->hash(e);
            res = res * 133 + h;
        }
        return res;
    }

    bool isEqual(const std::vector<T> &a, const std::vector<T> &b) const override
    {
        if (a.size() != b.size())
        {
            return false;
        }
        for (size_t i = 0; i < a.size(); ++i)
        {
            if (!elemFeat->isEqual(a[i], b[i]))
            {
                return false;
            }
        }
        return true;
    }

private:
    std::shared_ptr<ObjectFeature<T>> elemFeat;
};

template <typename T>
class _FFactory {
public:
    static std::shared_ptr<ObjectFeature<T>> create() {
        return std::make_shared<DefaultObjectFeature<T>>();
    }
};

class FeatureFactory {
public:
    template <typename T>
    static std::shared_ptr<ObjectFeature<T>> create() {
        // return std::make_shared<DefaultObjectFeature<T>>();
        return _FFactory<T>::create();
    }
};

template <>
class _FFactory<double> {
public:
    static std::shared_ptr<ObjectFeature<double>> create() {
        return std::make_shared<DoubleFeature>();
    }
};

template <typename T>
class _FFactory<std::vector<T>> {
public:
    static std::shared_ptr<ObjectFeature<std::vector<T>>> create() {
        return std::make_shared<ListFeature<T>>(FeatureFactory::create<T>());
    }
};

template <typename T>
class _FFactory<std::vector<std::vector<T>>> {
public:
    static std::shared_ptr<ObjectFeature<std::vector<std::vector<T>>>> create() {
        return std::make_shared<ListFeature<std::vector<T>>>(FeatureFactory::create<std::vector<T>>());
    }
};

} // namespace sode::unittest

#endif
