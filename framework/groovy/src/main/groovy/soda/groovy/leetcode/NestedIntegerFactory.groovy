package soda.groovy.leetcode

class NestedIntegerFactory {

    static NestedInteger parse(Object obj) {
        if (obj instanceof Integer) {
            return new DefaultNestedInteger(obj as int)
        }
        def ni = new DefaultNestedInteger()
        for (a in obj as List) {
            ni.add(parse(a))
        }
        ni
    }

    static Object serialize(NestedInteger ni) {
        if (ni.isInteger()) {
            return ni.getInteger()
        }
        def list = []
        for (e in ni.getList()) {
            list << serialize(e)
        }
        list
    }

    static List<NestedInteger> parseList(List<Object> objects) {
        objects.collect { parse(it) }
    }

    static List<Object> serializeList(List<NestedInteger> nestedIntegers) {
        nestedIntegers.collect { serialize(it) }
    }

}
