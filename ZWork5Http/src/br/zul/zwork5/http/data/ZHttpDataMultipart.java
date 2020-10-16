package br.zul.zwork5.http.data;

import br.zul.zwork5.http.ZHttpFile;
import br.zul.zwork5.random.ZRandom;
import br.zul.zwork5.util.ZList;
import br.zul.zwork5.util.ZUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author luizh
 */
public class ZHttpDataMultipart implements ZHttpData {

    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private final String boundary;
    private final Map<String, String> paramMap;
    private final ZList<ZHttpFile> fileList;
    private String charset = "UTF-8";
    private final String CRLF = "\r\n"; 
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttpDataMultipart() {
        this.boundary = Long.toHexString(new ZRandom().nextLong());
        this.paramMap = new LinkedHashMap<>();
        this.fileList = new ZList<>();
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    @Override
    public String getContentType(HttpURLConnection connection) {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public Integer getContentLength(HttpURLConnection connection) {
        return null;
    }

    @Override
    public void changeRequest(HttpURLConnection connection) {
        
    }

    @Override
    public void sendData(HttpURLConnection connection, OutputStream os) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, charset), true);
        sendParams(writer);
        sendFiles(os, writer);
    }
    
    public void addFile(String name, String filename, InputStream inputStream){
        this.fileList.add(new ZHttpFile(name, filename, inputStream));
    }

    //==========================================================================
    //MÉTODOS PRIVADOS
    //==========================================================================
    private void sendParams(PrintWriter writer) {
        for (Entry<String, String> entry:paramMap.entrySet()){
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(entry.getValue()).append(CRLF).flush();
        }
    }

    private void sendFiles(OutputStream os, PrintWriter writer) throws IOException {
        byte[] buffer = new byte[4096];
        for (ZHttpFile file:fileList){
            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\""+file.getName()+"\"; filename=\"" + file.getFilename() + "\"").append(CRLF);
            writer.append("Content-Type: " + guessContentTypeFromName(file)).append(CRLF);
            writer.append("Content-Transfer-Encoding: ").append(ZUtil.firstNotNull(file.getContentTransferEncoding(),"binary")).append(CRLF);
            writer.append(CRLF).flush();
            
            int length;
            while ((length=file.getInputStream().read(buffer))!=-1){
                os.write(buffer, 0, length);
                os.flush(); // Important before continuing with writer!
            }
            
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        }
    } 

    private String guessContentTypeFromName(ZHttpFile file) {
        if (file.getContentType()!=null){
            return file.getContentType();
        } else {
            return URLConnection.guessContentTypeFromName(file.getFilename());
        }
    }

    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public ZList<ZHttpFile> getFileList() {
        return fileList;
    }

    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
}
