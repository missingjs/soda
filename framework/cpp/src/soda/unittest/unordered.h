#ifndef _SODA_UNITTEST_UNORDERED_H_
#define _SODA_UNITTEST_UNORDERED_H_

#include <memory>
#include <vector>

#include "feature.h"

namespace soda::unittest {

template <typename Key, typename Value>
class XMap {
    struct XEntry {
        const Key key;
        Value     value;
        XEntry(const Key& key, const Value& value): key{key}, value{value} {}
    };

public:
    XMap(std::shared_ptr<ObjectFeature<Key>> feat):
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
            if (feat->isEqual(key, (*i)->key)) {
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
        return feat->hash(key);
    }

    XEntry* _entry(const Key& key) const {
        auto h = _hash(key);
        auto it = dict.find(h);
        if (it == dict.end()) {
            return nullptr;
        }
        for (auto entry : it->second) {
            if (feat->isEqual(key, entry->key)) {
                return entry;
            }
        }
        return nullptr;
    }

private:
    std::shared_ptr<ObjectFeature<Key>> feat;
    std::unordered_map<size_t,std::vector<XEntry*>> dict;
    int size_;
};

template <typename T>
class UnorderedListFeature : public ObjectFeature<std::vector<T>> {
public:
    UnorderedListFeature(std::shared_ptr<ObjectFeature<T>> elemFeat): elemFeat{elemFeat} {}

    size_t hash(const std::vector<T>& t) const override {
        size_t res = 0;
        std::vector<size_t> hashes;
        std::transform(t.begin(), t.end(),
                std::back_inserter(hashes),
                [this](auto& e){ return elemFeat->hash(e); });
        std::sort(hashes.begin(), hashes.end());
        for (auto h : hashes) {
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

        XMap<T, int> xmap{elemFeat};
        for (auto &e : a)
        {
            ++xmap[e];
        }

        for (auto &e : b)
        {
            if (!xmap.contains(e))
            {
                return false;
            }
            if (--xmap[e] == 0)
            {
                xmap.erase(e);
            }
        }
        return true;
    }

private:
    std::shared_ptr<ObjectFeature<T>> elemFeat;
};

} // namespace soda::unittest

#endif