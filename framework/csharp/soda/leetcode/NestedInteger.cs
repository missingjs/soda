namespace Soda.Leetcode;

public class NestedInteger
{
    private readonly bool isAtomic;

    private IList<NestedInteger> list = null;

    private int value = 0;

    // Constructor initializes an empty nested list.
    public NestedInteger()
    {
        isAtomic = false;
        list = new List<NestedInteger>();
    }

    // Constructor initializes a single integer.
    public NestedInteger(int value)
    {
        isAtomic = true;
        this.value = value;
    }

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    public bool IsInteger()
    {
        return isAtomic;
    }

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    public int GetInteger()
    {
        return value;
    }

    // Set this NestedInteger to hold a single integer.
    public void SetInteger(int value)
    {
        this.value = value;
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    public void Add(NestedInteger ni)
    {
        list.Add(ni);
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    public IList<NestedInteger> GetList()
    {
        return new List<NestedInteger>(list);
    }
}
