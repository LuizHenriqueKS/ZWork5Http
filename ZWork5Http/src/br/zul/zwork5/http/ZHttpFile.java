package br.zul.zwork5.http;

import java.io.InputStream;

/**
 *
 * @author luizh
 */
public class ZHttpFile {
    
    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private String contetDisposition;
    private String contentTransferEncoding;
    private String contentType;
    private final String name;
    private final String filename;
    private final InputStream inputStream;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttpFile(String name, String filename, InputStream inputStream) {
        this.name = name;
        this.filename = filename;
        this.inputStream = inputStream;
    }
    
    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public String getContetDisposition() {
        return contetDisposition;
    }
    public void setContetDisposition(String contetDisposition) {
        this.contetDisposition = contetDisposition;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }
    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
    
}
