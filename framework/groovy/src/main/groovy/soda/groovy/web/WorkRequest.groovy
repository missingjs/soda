package soda.groovy.web

class WorkRequest {

    private Map obj

    WorkRequest(Object obj) {
        this.obj = obj as Map
    }

    String getScriptFile() {
        obj.scriptFile as String
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
