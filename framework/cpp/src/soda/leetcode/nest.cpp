#include "nest.h"

namespace soda::leetcode {

using namespace std;

NestedInteger::NestedInteger()
    : isAtomic{false}, value{}, nestedList{}
{
}

NestedInteger::NestedInteger(int value)
    : isAtomic{true}, value{value}, nestedList{}
{
}

bool NestedInteger::isInteger() const
{
    return isAtomic;
}

int NestedInteger::getInteger() const
{
    return value;
}

void NestedInteger::add(const NestedInteger& ni)
{
    nestedList.push_back(ni);
}

const vector<NestedInteger>& NestedInteger::getList() const
{
    return nestedList;
}

int NestedInteger::size() const
{
    return isInteger() ? 1 : (int) nestedList.size();
}

bool NestedInteger::operator==(const NestedInteger& other) const
{
    if (isInteger()) {
        return other.isInteger() && getInteger() == other.getInteger();
    }
    if (other.isInteger() || size() != other.size()) {
        return false;
    }
    for (int i = 0; i < size(); ++i) {
        if (getList()[i] != other.getList()[i]) {
            return false;
        }
    }
    return true;
}

bool NestedInteger::operator!=(const NestedInteger& other) const
{
    return !((*this) == other);
}

std::string NestedInteger::toString() const
{
    return toString(*this);
}

namespace {
    void dump(const NestedInteger& ni, std::string& buffer) {
        if (ni.isInteger()) {
            buffer.append(to_string(ni.getInteger()));
        } else {
            buffer.append("[");
            auto& list = ni.getList();
            for (int i = 0; i < int(list.size()) - 1; ++i) {
                dump(list[i], buffer);
                buffer.append(",");
            }
            if (list.size() > 0) {
                dump(list[list.size()-1], buffer);
            }
            buffer.append("]");
        }
    }
}

std::string NestedInteger::toString(const NestedInteger& ni)
{
    std::string buffer;
    dump(ni, buffer);
    return buffer;
}

} // namespace soda::leetcode
