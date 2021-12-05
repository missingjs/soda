#ifndef _SODA_UNITTEST_SPETIAL_CONVERT_H_
#define _SODA_UNITTEST_SPETIAL_CONVERT_H_

#include <string>
#include <vector>

#include "soda/leetcode/bitree.h"
#include "soda/leetcode/list.h"

#include "parse.h"

namespace soda::unittest {

using namespace soda::leetcode;

// ---- type: TreeNode* ----
template <>
class TypedDataParser<TreeNode*> : public DataParser {
public:
    virtual TreeNode* parse(const JsonProxy& v) {
        return BiTree::create(v.get<std::vector<std::optional<int>>>());
    }
};
template <>
class TypedDataSerializer<TreeNode*> : public DataSerializer {
public:
    virtual JsonProxy serialize(TreeNode* root) {
        return JsonProxy::fromData(BiTree::inLevelOrder(root));
    }
};

// ---- type: ListNode* ----
template <>
class TypedDataParser<ListNode*> : public DataParser {
public:
    virtual ListNode* parse(const JsonProxy& v) {
        return ListHelper::create(v.get<std::vector<int>>());
    }
};
template <>
class TypedDataSerializer<ListNode*> : public DataSerializer {
public:
    virtual JsonProxy serialize(ListNode* head) {
        return JsonProxy::fromData(ListHelper::dump(head));
    }
};

// ---- type: vector<ListNode*> ----
template <>
class TypedDataParser<std::vector<ListNode*>> : public DataParser {
public:
    virtual std::vector<ListNode*> parse(const JsonProxy& v) {
        std::vector<ListNode*> res;
        for (int i = 0; i < v.size(); ++i) {
            res.push_back(ListHelper::create(v[i].get<std::vector<int>>()));
        }
        return res;
    }
};
template <>
class TypedDataSerializer<std::vector<ListNode*>> : public DataSerializer {
public:
    virtual JsonProxy serialize(std::vector<ListNode*>& lists) {
        // return JsonProxy::fromData(ListHelper::dump(head));
        JsonProxy arr;
        for (auto L : lists) {
            arr.append(ListHelper::dump(L));
        }
        return arr;
    }
};

// ---- type: char ----
template <>
class TypedDataParser<char> : public DataParser {
public:
    virtual char parse(const JsonProxy& v) {
        return v.get<std::string>()[0];
    }
};
template <>
class TypedDataSerializer<char> : public DataSerializer {
public:
    virtual JsonProxy serialize(char ch) {
        return JsonProxy::fromData(std::string(1, ch));
    }
};

// ---- type: vector<char> ----
template <>
class TypedDataParser<std::vector<char>> : public DataParser {
public:
    virtual std::vector<char> parse(const JsonProxy& v) {
        auto strs = v.get<std::vector<std::string>>();
        std::vector<char> res(strs.size());
        for (int i = 0; i < strs.size(); ++i) {
            res[i] = strs[i][0];
        }
        return res;
    }
};
template <>
class TypedDataSerializer<std::vector<char>> : public DataSerializer {
public:
    virtual JsonProxy serialize(const std::vector<char>& chars) {
        std::vector<std::string> strs(chars.size());
        for (int i = 0; i < chars.size(); ++i) {
            strs[i].push_back(chars[i]);
        }
        return JsonProxy::fromData(strs);
    }
};

// ---- type: std::vector<JsonProxy> ----
template <>
class TypedDataParser<std::vector<JsonProxy>> : public DataParser {
public:
    virtual std::vector<JsonProxy> parse(const JsonProxy& v) {
        std::vector<JsonProxy> arr;
        for (int i = 0; i < v.size(); ++i) {
            arr.push_back(v[i]);
        }
        return arr;
    }
};
template <>
class TypedDataSerializer<std::vector<JsonProxy>> : public DataSerializer {
public:
    virtual JsonProxy serialize(const std::vector<JsonProxy>& arr) {
        JsonProxy res;
        for (auto& p : arr) {
            res.append(p);
        }
        return res;
    }
};

} // soda::unittest

#endif
