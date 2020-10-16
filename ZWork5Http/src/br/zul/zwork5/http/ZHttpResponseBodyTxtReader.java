package br.zul.zwork5.http;

import br.zul.zwork5.exception.ZConversionErrorException;
import br.zul.zwork5.exception.ZHttpResponseBodyException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author luizh
 */
class ZHttpResponseBodyTxtReader {

    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private final ZHttpResponse response;
    private final InputStream inputStream;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttpResponseBodyTxtReader(ZHttpResponse response, InputStream inputStream) {
        this.response = response;
        this.inputStream = inputStream;
    }

    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public String read() throws ZHttpResponseBodyException {
        String charsetName = response.getChartsetName();
        return read(charsetName);
    }
    
    //==========================================================================
    //MÉTODOS PRIVADOS
    //==========================================================================
    private String read(String charsetName) throws ZHttpResponseBodyException {
        try {
            if (isResponseGzip()) {
                return readGzipResponse(charsetName);
            } else if (isResponseBr()) {
                return readBrResponse(charsetName);
            } else {
                return readRawResponse(charsetName);
            }
        } catch (Exception ex) {
            throw new ZHttpResponseBodyException(ex);
        }
    }

    private boolean isResponseGzip() {
        return response.getResponsePropertyMap().containsKey("Content-Encoding") && response.getResponsePropertyMap().get("Content-Encoding").get(0).equalsIgnoreCase("gzip");
    }

    private boolean isResponseBr() {
        return response.getResponsePropertyMap().containsKey("Content-Encoding") && response.getResponsePropertyMap().get("Content-Encoding").get(0).equalsIgnoreCase("br");
    }

    private String readGzipResponse(String charsetName) throws IOException {
        InputStream bodyStream = new GZIPInputStream(inputStream);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = bodyStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, length);
        }
        return new String(outStream.toByteArray(), charsetName);
    }

    private String readBrResponse(String charsetName) throws IOException {
        try {
            Class<? extends InputStream> brotliClass = (Class<? extends InputStream>)Class.forName("org.brotli.dec.BrotliInputStream");
            InputStream bodyStream = brotliClass.getConstructor(InputStream.class).newInstance(inputStream);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = bodyStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            return new String(outStream.toByteArray(), charsetName);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException  ex) {
            throw new IOException(ex);
        }
    }

    private String readRawResponse(String charsetName) throws UnsupportedEncodingException, IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charsetName));
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
            response.append("\r\n");
        }
        return response.toString();
    }
    
}
