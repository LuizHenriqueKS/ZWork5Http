package br.zul.zwork5.http;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.text.AbstractDocument.Content;

/**
 *
 * @author luizh
 */
public class ZHttpContentType {
    
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String APPLICATION_ZIP = "application/zip";
    public static final String APPLICATION_MSWORD = "application/msword";
    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
    public static final String APPLICATION_VND_MS_POWERPOINT = "application/vnd.ms-powerpoint";
    public static final String IMAGE_GIF = "image/gif";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPG = "image/jpg";
    
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
    
    private static Map<String, String> fileExtensionTypeMap;
    
    private ZHttpContentType(){}
    
    public static Map<String, String> getFileExtensionTypeMap(){
        if (fileExtensionTypeMap==null){
            fileExtensionTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            fileExtensionTypeMap.put(".aac", "audio/aac");
            fileExtensionTypeMap.put(".abw", "application/x-abiword");
            fileExtensionTypeMap.put(".arc", "application/octet-stream");
            fileExtensionTypeMap.put(".avi", "video/x-msvideo");
            fileExtensionTypeMap.put(".azw", "application/vnd.amazon.ebook");
            fileExtensionTypeMap.put(".bin", "application/octet-stream");
            fileExtensionTypeMap.put(".bz", "application/x-bzip");
            fileExtensionTypeMap.put(".bz2", "application/x-bzip2");
	    fileExtensionTypeMap.put(".csh", "application/x-csh");
            fileExtensionTypeMap.put(".css", "text/css");
            fileExtensionTypeMap.put(".csv", "text/csv");
            fileExtensionTypeMap.put(".doc", "application/msword");
            fileExtensionTypeMap.put(".eot", "application/vnd.ms-fontobject");
            fileExtensionTypeMap.put(".epub", "application/epub+zip");
            fileExtensionTypeMap.put(".git", "image/gif");
            fileExtensionTypeMap.put(".htm", "text/html");
            fileExtensionTypeMap.put(".html", "text/html");
            fileExtensionTypeMap.put(".ico", "image/x-icon");
            fileExtensionTypeMap.put(".ics", "text/calendar");
            fileExtensionTypeMap.put(".jar", "application/java-archive");
            fileExtensionTypeMap.put(".jpeg", "image/jpeg");
            fileExtensionTypeMap.put(".jpg", "image/jpeg");
            fileExtensionTypeMap.put(".js", "application/javascript");
            fileExtensionTypeMap.put(".json", "application/json");
            fileExtensionTypeMap.put(".mid", "audio/midi");
            fileExtensionTypeMap.put(".midi", "audio/midi");
            fileExtensionTypeMap.put(".mpeg", "video/mpeg");
            fileExtensionTypeMap.put(".mpkg", "application/vnd.apple.installer+xml");
            fileExtensionTypeMap.put(".odp", "application/vnd.oasis.opendocument.presentation");
            fileExtensionTypeMap.put(".ods", "application/vnd.oasis.opendocument.spreadsheet");
            fileExtensionTypeMap.put(".odt", "application/vnd.oasis.opendocument.text");
            fileExtensionTypeMap.put(".oga", "audio/ogg");
            fileExtensionTypeMap.put(".ogv", "video/ogg");
            fileExtensionTypeMap.put(".ogx", "application/ogg");
            fileExtensionTypeMap.put(".otf", "font/otf");
            fileExtensionTypeMap.put(".png", "image/png");
            fileExtensionTypeMap.put(".pdf", "application/pdf");
            fileExtensionTypeMap.put(".ppt", "application/vnd.ms-powerpoint");
            fileExtensionTypeMap.put(".rar", "application/x-rar-compressed");
            fileExtensionTypeMap.put(".rtf", "application/rtf");
            fileExtensionTypeMap.put(".sh", "application/x-sh");
            fileExtensionTypeMap.put(".svg", "image/svg+xml");
            fileExtensionTypeMap.put(".swf", "application/x-shockwave-flash");
            fileExtensionTypeMap.put(".tar", "application/x-tar");
            fileExtensionTypeMap.put(".tif", "image/tiff");
            fileExtensionTypeMap.put(".tiff", "image/tiff");
            fileExtensionTypeMap.put(".ts", "application/typescript");
            fileExtensionTypeMap.put(".ttf", "font/ttf");
            fileExtensionTypeMap.put(".vsd", "application/vnd.visio");
            fileExtensionTypeMap.put(".wav", "audio/x-wav");
            fileExtensionTypeMap.put(".weba", "audio/webm");
            fileExtensionTypeMap.put(".webm", "video/webm");
            fileExtensionTypeMap.put(".webp", "image/webp");
            fileExtensionTypeMap.put(".woff", "font/woff");
            fileExtensionTypeMap.put(".woff2", "font/woff2");
            fileExtensionTypeMap.put(".xhtml", "application/xhtml+xml");
            fileExtensionTypeMap.put(".xls", "application/vnd.ms-excel");
            fileExtensionTypeMap.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            fileExtensionTypeMap.put(".xml", "application/xml");
            fileExtensionTypeMap.put(".xul", "application/vnd.mozilla.xul+xml");
            fileExtensionTypeMap.put(".zip", "application/zip");
            fileExtensionTypeMap.put(".3gp", "video/3gpp");
            fileExtensionTypeMap.put(".3g2", "video/3gpp2");
            fileExtensionTypeMap.put(".7z", "application/x-7z-compressed");
        }
        return fileExtensionTypeMap;
    }
    
}