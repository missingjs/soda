package soda.groovy.web.http

import soda.groovy.web.WebUtils

class ContentDisposition {

    String name

    String filename

    boolean isFile() {
        return filename != null
    }

    static ContentDisposition fromHeaderValue(String value) {
        String name = WebUtils.findOne(value, "name=\"(.*?)\"").orElseThrow {
            new IllegalArgumentException("no name found in Content-Disposition")
        }

        var cd = new ContentDisposition()
        cd.name = name
        cd.filename = WebUtils.findOne(value, "filename=\"(.*?)\"").get()
        return cd
    }

    static ContentDisposition fromHeaderLine(String headerLine) {
        // no need to split by ':'
        return fromHeaderValue(headerLine)
    }

}
