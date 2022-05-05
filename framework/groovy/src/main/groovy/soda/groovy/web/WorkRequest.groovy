package soda.groovy.web

class WorkRequest {

    private Map obj

    WorkRequest(Object obj) {
        this.obj = obj as Map
    }

    String getClasspath() {
        obj.classpath as String
    }

    String getBootClass() {
        obj.bootClass as String
    }

    String getTestCase() {
        obj.testCase as String
    }

}
