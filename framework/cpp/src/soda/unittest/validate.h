#ifndef _SODA_UNITTEST_VALIDATE_H_
#define _SODA_UNITTEST_VALIDATE_H_

#include <functional>
#include <vector>

namespace soda::unittest {

template <typename T, typename E = std::equal_to<T>>
class EqualToIgnoreOrder {
    E is_equal;

public:
    EqualToIgnoreOrder(): is_equal{} {}

    EqualToIgnoreOrder(E is_e): is_equal{is_e} {}

    // FIXME: time complexity O(n^2)
    bool operator()(const std::vector<T>& v1, const std::vector<T>& v2) const {
        if (v1.size() != v2.size()) {
            return false;
        }

        vector<int> used(v1.size());
        for (auto& e1 : v1) {
            bool exist = false;
            for (int i = 0; i < int(v2.size()); ++i) {
                auto& e2 = v2[i];
                if (is_equal(e1, e2) && !used[i]) {
                    exist = true;
                    used[i] = 1;
                    break;
                }
            }
            if (!exist) {
                return false;
            }
        }
        return true;
    }
};

class Validators {
public:
    template <typename T>
    static EqualToIgnoreOrder<T> vector1d() {
        return EqualToIgnoreOrder<T>{};
    }

    template <typename T>
    static EqualToIgnoreOrder<std::vector<T>, EqualToIgnoreOrder<T>> vector2d() {
        return EqualToIgnoreOrder<std::vector<T>, EqualToIgnoreOrder<T>>{EqualToIgnoreOrder<T>{}};
    }
};

} // namespace soda::unittest

#endif
