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

} // namespace soda::leetcode
