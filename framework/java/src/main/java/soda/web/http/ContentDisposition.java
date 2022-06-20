package soda.web.http;

import soda.web.Utils;

public class ContentDisposition {

    public String name;

    public String filename;

    public boolean isFile() {
        return filename != null;
    }

    public static ContentDisposition fromHeaderValue(String value) {
        String name = Utils.findOne(value, "name=\"(.*?)\"");
        if (name == null) {
            throw new IllegalArgumentException("no name found in Content-Disposition");
        }

        var cd = new ContentDisposition();
        cd.name = name;
        cd.filename = Utils.findOne(value, "filename=\"(.*?)\"");
        return cd;
    }

    public static ContentDisposition fromHeaderLine(String headerLine) {
        // no need to split by ':'
        return fromHeaderValue(headerLine);
    }

}
