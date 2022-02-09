using Soda.Unittest.Validate;

namespace Soda.Unittest;

public static class Validators
{
    private static Func<IList<T>, IList<T>, bool> forList<T>(bool ordered, ObjectFeature<T> elemFeat)
    {
        return ordered ? ListFeatureFactory.Ordered(elemFeat).IsEqual : ListFeatureFactory.Unordered(elemFeat).IsEqual;
    }

    public static Func<IList<T>, IList<T>, bool> ForList<T>(bool ordered)
    {
        return forList(ordered, FeatureFactory.create<T>());
    }

    public static Func<IList<IList<T>>, IList<IList<T>>, bool> ForList2d<T>(bool dim1Ordered, bool dim2Ordered)
    {
        var elemFeat = FeatureFactory.create<T>();
        var listFeat = dim2Ordered ? ListFeatureFactory.Ordered(elemFeat) : ListFeatureFactory.Unordered(elemFeat);
        return forList(dim1Ordered, listFeat);
    }

    public static Func<T[], T[], bool> ForArray<T>(bool ordered)
    {
        return ForList<T>(ordered);
    }

    public static Func<T[][], T[][], bool> ForArray2d<T>(bool dim1Ordered, bool dim2Ordered)
    {
        return ForList2d<T>(dim1Ordered, dim2Ordered);
    }
}