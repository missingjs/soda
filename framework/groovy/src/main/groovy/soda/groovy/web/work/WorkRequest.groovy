package soda.groovy.web.work

class WorkRequest {

    private Map obj

    WorkRequest(Object obj) {
        this.obj = obj as Map
    }

    String getKey() {
        obj.key as String
    }

    void setKey(String key) {
        obj.key = key
    }

    String getEntryClass() {
        obj.entryClass as String
    }

    void setEntryClass(String entryClass) {
        obj.entryClass = entryClass
    }

    String getTestCase() {
        obj.testCase as String
    }

    void setTestCase(String testCase) {
        obj.testCase = testCase
    }

}
