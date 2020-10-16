package br.zul.zwork5.http.data;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 *
 * @author luizh
 */
public interface ZHttpData {
    
    public String getContentType(HttpURLConnection connection);
    public Integer getContentLength(HttpURLConnection connection);
    public void changeRequest(HttpURLConnection connection);
    
    public void sendData(HttpURLConnection connection, OutputStream os) throws IOException;
    
}
