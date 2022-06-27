package soda.groovy.web.work

class WorkRequest {

    private Map obj

    WorkRequest(Object obj) {
        this.obj = obj as Map
    }

    String getKey() {
        obj.key as String
    }

    String getBootClass() {
        obj.bootClass as String
    }

    String getTestCase() {
        obj.testCase as String
    }

}
