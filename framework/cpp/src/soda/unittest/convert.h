#ifndef _SODA_UNITTEST_CONVERT_H_
#define _SODA_UNITTEST_CONVERT_H_

#include <memory>

#include "soda/leetcode/bitree.h"
#include "soda/leetcode/list.h"
#include "soda/leetcode/nest.h"

#include "jsonproxy.h"

namespace soda::unittest {

using namespace soda::leetcode;

template <typename T>
class ObjectConverter {
public:
    T fromJsonSerializable(const JsonProxy& v) {
        return v.get<T>();
    }

    JsonProxy toJsonSerializable(const T& data) {
        return JsonProxy::fromData(data);
    }
};

class ConverterFactory {
public:
    template <typename T>
    static std::shared_ptr<ObjectConverter<T>> create() {
        return std::make_shared<ObjectConverter<T>>();
    }
};

template <>
class ObjectConverter<NestedInteger> {
public:
    NestedInteger fromJsonSerializable(const JsonProxy& v) {
        return parse(v);
    }

    JsonProxy toJsonSerializable(const NestedInteger& data) {
        return serialize(data);
    }

    static NestedInteger parse(const JsonProxy& p) {
        if (p.isNumber()) {
            return NestedInteger(p.get<int>());
        }
        NestedInteger ni;
        for (int i = 0; i < p.size(); ++i) {
            ni.add(parse(p[i]));
        }
        return ni;
    }
    static JsonProxy serialize(const NestedInteger& ni) {
        JsonProxy jp;
        if (ni.isInteger()) {
            jp.set(ni.getInteger());
        } else {
            if (ni.getList().size() > 0) {
                for (auto& j : ni.getList()) {
                    jp.append(serialize(j));
                }
            } else {
                jp = JsonProxy::array();
            }
        }
        return jp;
    }
};

template <>
class ObjectConverter<std::vector<NestedInteger>> {
public:
    std::vector<NestedInteger> fromJsonSerializable(const JsonProxy& v) {
        std::vector<NestedInteger> res;
        for (int i = 0; i < v.size(); ++i) {
            res.push_back(ObjectConverter<NestedInteger>::parse(v[i]));
        }
        return res;
    }

    JsonProxy toJsonSerializable(const std::vector<NestedInteger>& nestedList) {
        JsonProxy jp;
        for (auto& ni : nestedList) {
            jp.append(ObjectConverter<NestedInteger>::serialize(ni));
        }
        return jp;
    }
};

template <>
class ObjectConverter<ListNode*> {
public:
    ListNode* fromJsonSerializable(const JsonProxy& v) {
        return ListHelper::create(v.get<std::vector<int>>());
    }

    JsonProxy toJsonSerializable(ListNode* head) {
        return JsonProxy::fromData(ListHelper::dump(head));
    }
};

template <>
class ObjectConverter<TreeNode*> {
public:
    TreeNode* fromJsonSerializable(const JsonProxy& v) {
        return BiTree::create(v.get<std::vector<std::optional<int>>>());
    }

    JsonProxy toJsonSerializable(TreeNode* root) {
        return JsonProxy::fromData(BiTree::inLevelOrder(root));
    }
};

template <>
class ObjectConverter<std::vector<ListNode*>> {
public:
    std::vector<ListNode*> fromJsonSerializable(const JsonProxy& v) {
        std::vector<ListNode*> res;
        for (int i = 0; i < v.size(); ++i) {
            res.push_back(ListHelper::create(v[i].get<std::vector<int>>()));
        }
        return res;
    }

    JsonProxy toJsonSerializable(const std::vector<ListNode*>& lists) {
        JsonProxy arr;
        for (auto L : lists) {
            arr.append(ListHelper::dump(L));
        }
        return arr;
    }
};

template <>
class ObjectConverter<char> {
public:
    char fromJsonSerializable(const JsonProxy& v) {
        return v.get<std::string>()[0];
    }

    JsonProxy toJsonSerializable(char ch) {
        return JsonProxy::fromData(std::string(1, ch));
    }
};

template <>
class ObjectConverter<std::vector<char>> {
public:
    std::vector<char> fromJsonSerializable(const JsonProxy& v) {
        auto strs = v.get<std::vector<std::string>>();
        return s2c(strs);
    }

    static std::vector<char> s2c(const std::vector<std::string>& s) {
        std::vector<char> res(s.size());
        for (int i = 0; i < s.size(); ++i) {
            res[i] = s[i][0];
        }
        return res;
    }

    JsonProxy toJsonSerializable(const std::vector<char>& chars) {
        return JsonProxy::fromData(c2s(chars));
    }

    static std::vector<std::string> c2s(const std::vector<char>& chars) {
        std::vector<std::string> strs(chars.size());
        for (int i = 0; i < chars.size(); ++i) {
            strs[i].push_back(chars[i]);
        }
        return strs;
    }
};

template <>
class ObjectConverter<std::vector<std::vector<char>>> {
public:
    std::vector<std::vector<char>> fromJsonSerializable(const JsonProxy& v) {
        auto str2d = v.get<std::vector<std::vector<std::string>>>();
        std::vector<std::vector<char>> res(str2d.size());
        for (int i = 0; i < str2d.size(); ++i) {
            res[i] = ObjectConverter<std::vector<char>>::s2c(str2d[i]);
        }
        return res;
    }

    JsonProxy toJsonSerializable(const std::vector<std::vector<char>>& chars2d) {
        std::vector<std::vector<std::string>> strs2d(chars2d.size());
        for (int i = 0; i < chars2d.size(); ++i) {
            strs2d[i] = ObjectConverter<std::vector<char>>::c2s(chars2d[i]);
        }
        return JsonProxy::fromData(strs2d);
    }
};

template <>
class ObjectConverter<JsonProxy> {
public:
    JsonProxy fromJsonSerializable(const JsonProxy& v) {
        return v;
    }

    JsonProxy toJsonSerializable(const JsonProxy& data) {
        return data;
    }
};

template <>
class ObjectConverter<std::vector<JsonProxy>> {
public:
    std::vector<JsonProxy> fromJsonSerializable(const JsonProxy& v) {
        std::vector<JsonProxy> arr;
        for (int i = 0; i < v.size(); ++i) {
            arr.push_back(v[i]);
        }
        return arr;
    }

    JsonProxy toJsonSerializable(const std::vector<JsonProxy>& arr) {
        JsonProxy res;
        for (auto& p : arr) {
            res.append(p);
        }
        return res;
    }
};

} // namespace soda::unittest

#endif
