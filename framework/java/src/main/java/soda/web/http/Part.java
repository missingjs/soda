package soda.web.http;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class Part {

//    public static class Disposition {
//        public String name;
//        public String fileName;
//
//        private static Pattern namePattern = Pattern.compile("name=\"(.*?)\"");
//        private static Pattern filenamePattern = Pattern.compile("filename=\"(.*?)\"");
//
//        public static Disposition parse(String content) {
//            var dis = new Disposition();
//            var m1 = namePattern.matcher(content);
//            if (m1.find()) {
//                dis.name = m1.group(1);
//            }
//
//            var m2 = filenamePattern.matcher(content);
//            if (m2.find()) {
//                dis.fileName = m2.group(1);
//            }
//            return dis;
//        }
//    }

    public ContentDisposition contentDisposition;

    public String contentType;

    public byte[] payload;

    public String getName() {
        return contentDisposition.name;
    }

    public String bodyString() {
        return new String(payload, StandardCharsets.UTF_8);
    }

}
