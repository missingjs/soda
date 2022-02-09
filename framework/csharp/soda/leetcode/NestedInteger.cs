namespace Soda.Leetcode;

public interface NestedInteger {

    // Constructor initializes an empty nested list.
    // public NestedInteger();

    // Constructor initializes a single integer.
    // public NestedInteger(int value);

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    bool IsInteger();

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    int GetInteger();

    // Set this NestedInteger to hold a single integer.
    public void SetInteger(int value);

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    public void Add(NestedInteger ni);

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    IList<NestedInteger> GetList();
}

public class NestedIntegerImpl : NestedInteger
{
    private readonly bool isAtomic;

    private IList<NestedInteger> list = null;

    private int value = 0;

    public NestedIntegerImpl()
    {
        isAtomic = false;
        list = new List<NestedInteger>();
    }

    public NestedIntegerImpl(int value)
    {
        isAtomic = true;
        this.value = value;
    }

    public bool IsInteger()
    {
        return isAtomic;
    }

    public int GetInteger()
    {
        return value;
    }

    public void SetInteger(int value)
    {
        this.value = value;
    }

    public void Add(NestedInteger ni)
    {
        list.Add(ni);
    }

    public IList<NestedInteger> GetList()
    {
        return new List<NestedInteger>(list);
    }
}
