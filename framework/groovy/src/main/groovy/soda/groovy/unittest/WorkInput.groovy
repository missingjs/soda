package soda.groovy.unittest

class WorkInput {

    private Map obj

    WorkInput(Object obj) {
        this.obj = obj as Map
    }

    int getId() {
        obj.id as int
    }

    Object getExpected() {
        obj.expected
    }

    List getArgs() {
        obj.args as List
    }

}
