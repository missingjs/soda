package soda.web.http;

import soda.web.WebUtils;

public class ContentDisposition {

    public String name;

    public String filename;

    public boolean isFile() {
        return filename != null;
    }

    public static ContentDisposition fromHeaderValue(String value) {
        String name = WebUtils.findOne(value, "name=\"(.*?)\"").orElseThrow(() ->
            new IllegalArgumentException("no name found in Content-Disposition")
        );

        var cd = new ContentDisposition();
        cd.name = name;
        cd.filename = WebUtils.findOne(value, "filename=\"(.*?)\"").orElse(null);
        return cd;
    }

    public static ContentDisposition fromHeaderLine(String headerLine) {
        // no need to split by ':'
        return fromHeaderValue(headerLine);
    }

}
