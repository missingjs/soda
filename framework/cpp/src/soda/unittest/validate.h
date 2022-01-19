#ifndef _SODA_UNITTEST_VALIDATE_H_
#define _SODA_UNITTEST_VALIDATE_H_

#include <algorithm>
#include <functional>
#include <iterator>
#include <unordered_map>
#include <vector>

namespace soda::unittest {

template <typename T>
class ObjectFeature {
public:
    using ObjectType = T;

    size_t hash(const T& t) const {
        return std::hash<T>()(t);
    }

    bool isEqual(const T& t1, const T& t2) const {
        return std::equal_to<T>()(t1, t2);
    }
};

template <>
class ObjectFeature<double> {
public:
    using ObjectType = double;

    size_t hash(double d) const {
        return std::hash<double>()(d);
    }

    bool isEqual(double a, double b) const {
        return std::abs(a-b) < 1e-6;
    }
};

class FeatureFactory {
public:
    template <typename T>
    static ObjectFeature<T> create() {
        return ObjectFeature<T>{};
    }
};

template <typename Key, typename Value, typename Feature>
class XMap {
    struct XEntry {
        const Key key;
        Value     value;
        XEntry(const Key& key, const Value& value): key{key}, value{value} {}
    };

public:
    XMap(Feature feat):
        feat{feat}, dict{}, size_{0}
    {}

    bool contains(const Key& key) const {
        return _entry(key);
    }

    Value& operator[](const Key& key) {
        auto e = _entry(key);
        if (!e) {
            e = new XEntry(key, Value{});
            auto h = _hash(key);
            dict[h].push_back(e);
            ++size_;
        }
        return e->value;
    }

    Value operator[](const Key& key) const {
        auto e = _entry(key);
        return e ? e->value : Value{};
    }

    void erase(const Key& key) {
        auto h = _hash(key);
        auto it = dict.find(h);
        if (it == dict.end()) {
            return;
        }

        auto i = dict[h].begin();
        for (; i != dict[h].end(); ++i) {
            if (feat.isEqual(key, (*i)->key)) {
                break;
            }
        }

        if (i != dict[h].end()) {
            dict[h].erase(i);
            --size_;
        }
    }

    int size() const {
        return size_;
    }

private:
    size_t _hash(const Key& key) const {
        return feat.hash(key);
    }

    XEntry* _entry(const Key& key) const {
        auto h = _hash(key);
        auto it = dict.find(h);
        if (it == dict.end()) {
            return nullptr;
        }
        for (auto entry : it->second) {
            if (feat.isEqual(key, entry->key)) {
                return entry;
            }
        }
        return nullptr;
    }

private:
    Feature feat;
    std::unordered_map<size_t,std::vector<XEntry*>> dict;
    int size_;
};

class StrategyFactory {
public:
    template <typename F>
    static auto unorderList(F feat)
    {
        using T = typename F::ObjectType;
        return [=](const std::vector<T>& a, const std::vector<T>& b) {
            if (a.size() != b.size()) {
                return false;
            }
    
            XMap<T,int,F> xmap{feat};
            for (auto& e : a) {
                ++xmap[e];
            }
    
            for (auto& e : b) {
                if (!xmap.contains(e)) {
                    return false;
                }
                if (--xmap[e] == 0) {
                    xmap.erase(e);
                }
            }
            return true;
        };
    }

    template <typename F>
    static auto list(F feat) {
        using T = typename F::ObjectType;
        return [=](const std::vector<T>& a, const std::vector<T>& b) {
            if (a.size() != b.size()) {
                return false;
            }
            for (size_t i = 0; i < a.size(); ++i) {
                if (!feat.isEqual(a[i], b[i])) {
                    return false;
                }
            }
            return true;
        };
    }
};

template <typename EF>
class ListFeature {
public:
    using T = typename EF::ObjectType;
    using ObjectType = std::vector<T>;

    ListFeature(EF elemFeat): elemFeat{elemFeat} {}

    size_t hash(const std::vector<T>& t) const {
        size_t res = 0;
        for (auto& e : t) {
            auto h = elemFeat.hash(e);
            res = res * 133 + h;
        }
        return res;
    }

    bool isEqual(const std::vector<T>& a, const std::vector<T>& b) const {
        return StrategyFactory::list(elemFeat)(a, b);
    }

private:
    EF elemFeat;
};

template <typename EF>
class UnorderListFeature {
public:
    using T = typename EF::ObjectType;
    using ObjectType = std::vector<T>;

    UnorderListFeature(EF elemFeat): elemFeat{elemFeat} {}

    size_t hash(const std::vector<T>& t) const {
        size_t res = 0;
        std::vector<size_t> hashes;
        std::transform(t.begin(), t.end(),
                std::back_inserter(hashes),
                [this](auto& e){ return elemFeat.hash(e); });
        std::sort(hashes.begin(), hashes.end());
        for (auto h : hashes) {
            res = res * 133 + h;
        }
        return res;
    }

    bool isEqual(const std::vector<T>& a, const std::vector<T>& b) const {
        return StrategyFactory::unorderList(elemFeat)(a, b);
    }

private:
    EF elemFeat;
};

class Validators {
public:
    template <typename T>
    using Vect = std::vector<T>;

    template <typename T>
    using Vect2d = std::vector<std::vector<T>>;

    template <typename T>
    static std::function<bool(const Vect<T>&,const Vect<T>&)> forVector(bool ordered) {
        auto feat = FeatureFactory::create<T>();
        if (ordered) {
            return [=](const Vect<T>& a, const Vect<T>& b) {
                return StrategyFactory::list(feat)(a, b);
            };
        } else {
            return [=](const Vect<T>& a, const Vect<T>& b) {
                return StrategyFactory::unorderList(feat)(a, b);
            };
        }
    }

    template <typename T>
    static auto forVector2d(bool dim1Ordered, bool dim2Ordered) {
        using func_t = std::function<bool(const Vect2d<T>&,const Vect2d<T>&)>;
        auto elemFeat = FeatureFactory::create<T>();
        auto factory = [=](bool order, auto feat) -> func_t {
            if (order) {
                return [=](const Vect2d<T>& a, const Vect2d<T>& b) {
                    return StrategyFactory::list(feat)(a, b);
                };
            } else {
                return [=](const Vect2d<T>& a, const Vect2d<T>& b) {
                    return StrategyFactory::unorderList(feat)(a, b);
                };
            }
        };
        return dim2Ordered
            ? factory(dim1Ordered, ListFeature{elemFeat})
            : factory(dim1Ordered, UnorderListFeature{elemFeat});
    }
};

class ValidatorFactory {
public:
    template <typename T>
    static std::function<bool(const T&, const T&)> create() {
        return [](const T& a, const T& b) {
            return FeatureFactory::create<T>().isEqual(a, b);
        };
    }
};

template <>
std::function<bool(const std::vector<double>&, const std::vector<double>&)>
ValidatorFactory::create<std::vector<double>>()
{
    return Validators::forVector<double>(true);
}

template <>
std::function<bool(const std::vector<std::vector<double>>&, const std::vector<std::vector<double>>&)>
ValidatorFactory::create<std::vector<std::vector<double>>>()
{
    return Validators::forVector2d<double>(true, true);
}

} // namespace soda::unittest

#endif
