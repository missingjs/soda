package soda.unittest.work.parse;

import soda.leetcode.DefaultNestedInteger;
import soda.leetcode.NestedInteger;

import java.util.ArrayList;
import java.util.List;

public class NestedIntegerConverter implements ObjectConverter<NestedInteger, Object> {
    @Override
    public NestedInteger fromJsonSerializable(Object o) {
        return parse(o);
    }

    @Override
    public Object toJsonSerializable(NestedInteger nestedInteger) {
        return serialize(nestedInteger);
    }

    @SuppressWarnings("unchecked")
    public static NestedInteger parse(Object obj) {
        if (obj instanceof Integer) {
            return new DefaultNestedInteger((Integer) obj);
        }
        var ni = new DefaultNestedInteger();
        for (var a : (List<Object>) obj) {
            ni.add(parse(a));
        }
        return ni;
    }

    public static Object serialize(NestedInteger ni) {
        if (ni.isInteger()) {
            return ni.getInteger();
        }
        var list = new ArrayList<Object>();
        for (var e : ni.getList()) {
            list.add(serialize(e));
        }
        return list;
    }
}
