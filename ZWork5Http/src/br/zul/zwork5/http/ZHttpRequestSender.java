package br.zul.zwork5.http;

import br.zul.zwork5.http.data.ZHttpData;
import br.zul.zwork5.http.data.ZHttpDataParams;
import br.zul.zwork5.url.ZUrl;
import br.zul.zwork5.url.ZUrlBuilder;
import br.zul.zwork5.exception.ZHttpRequestException;
import br.zul.zwork5.exception.ZHttpResponseException;
import br.zul.zwork5.exception.ZKeyStoreException;
import br.zul.zwork5.exception.ZUrlIsInvalidException;
import br.zul.zwork5.timestamp.ZDateTime;
import br.zul.zwork5.util.ZList;
import br.zul.zwork5.util.ZStrUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author luizh
 */
class ZHttpRequestSender {

    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private final ZHttpRequest request;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttpRequestSender(ZHttpRequest request) {
        this.request = request;
    }

    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public ZHttpResponse send() throws ZHttpRequestException, ZHttpResponseException, UnsupportedEncodingException, ZUrlIsInvalidException, ZKeyStoreException {
        return sendRequestTo(buildUrl(), null);
    }
    
    //==========================================================================
    //MÉTODOS PRIVADOS
    //==========================================================================
    private ZUrl buildUrl() throws ZHttpRequestException, UnsupportedEncodingException, ZUrlIsInvalidException{
        switch (request.parameterType){
            case URL_PARAMETERS:
                return buildUrlWithParams();
            case DATA_PARAMETERS:
                return request.getUrl();
            default:
                throw new ZHttpRequestException();
        }
    }

    private ZUrl buildUrlWithParams() throws UnsupportedEncodingException, ZUrlIsInvalidException {
        ZUrlBuilder builder = request.getUrl().builder();
        if (request.getData() instanceof ZHttpDataParams){
            ZHttpDataParams params = (ZHttpDataParams) request.getData();
            builder.getQueryMap().putAll(params.getMap());
        }
        return builder.build();
    }

    private ZHttpResponse buildResponse(HttpURLConnection connection, ZUrl url) throws ZHttpResponseException, ZHttpRequestException, ZUrlIsInvalidException, UnsupportedEncodingException, ZKeyStoreException {
        ZHttpResponseBuilder builder = new ZHttpResponseBuilder();
        builder.setConnection(connection);
        builder.setRequest(request);
        builder.setUrl(url);
        return builder.build();
    }
    
    private HttpURLConnection buildConnection(ZUrl url) throws IOException, NoSuchAlgorithmException, KeyManagementException, ZUrlIsInvalidException, ZKeyStoreException {
        HttpURLConnection connection = initConnection(url);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(request.method);
        connection.setUseCaches(false);
        if (request.connectTimeout != null) connection.setConnectTimeout(request.connectTimeout);    
        if (request.readTimeout != null) connection.setReadTimeout(request.readTimeout);
        fillWithCookies(connection);
        fillWithRequestProperties(connection);
        treatHttpsConnection(connection, url);
        sendData(connection);
        return connection;
    }

    private HttpURLConnection initConnection(ZUrl url) throws IOException, MalformedURLException, ZUrlIsInvalidException {
        HttpURLConnection connection;
        if (request.proxy == null) {
            connection = (HttpURLConnection) url.asURL().openConnection();
        } else {
            connection = (HttpURLConnection) url.asURL().openConnection(request.proxy.asProxy());
        }
        return connection;
    }

    private void fillWithCookies(HttpURLConnection connection) {
        if (request.cookieManager.containsCookies()) {
            connection.setRequestProperty("Cookie", request.cookieManager.getCookiesText());
        }
    }

    private void fillWithRequestProperties(HttpURLConnection connection) {
        for (Entry<String, ZList<String>> property : request.requestPropertyMap.entrySet()) {
            boolean first = true;
            for (String value : property.getValue()) {
                if (first) {
                    connection.setRequestProperty(property.getKey(), value);
                    first = false;
                } else {
                    connection.addRequestProperty(property.getKey(), value);
                }
            }
        }
    }

    private void treatHttpsConnection(HttpURLConnection connection, ZUrl url) throws NoSuchAlgorithmException, KeyManagementException, ZUrlIsInvalidException, ZKeyStoreException {
        if ((connection instanceof HttpsURLConnection)) {
            HttpsURLConnection conns = (HttpsURLConnection) connection;
            prepareToIgnoreCertificates(conns);
            if (request.keyStore != null) {
                if (request.autoDownloadCertificates) {
                    prepareAutoDownloadCertificates(conns, url);
                }
                conns.setSSLSocketFactory(request.keyStore.getSocketFactory());
            }
        }
    }

    private void prepareToIgnoreCertificates(HttpsURLConnection conns) throws NoSuchAlgorithmException, KeyManagementException {
        new ZHttpsCertificatesIgnorant(request, conns).apply();
    }
    
    private void prepareAutoDownloadCertificates(HttpsURLConnection connection, ZUrl url) throws ZUrlIsInvalidException, ZKeyStoreException {
        String alias = ZStrUtils.only(url.toString(), ZStrUtils.ONLY_LETTERS_OF_ALPHABET_UPPERCASE_AND_LOWERCASE_AND_NUMBERS);
        if (request.keyStore.containsAlias(alias + "_0")) {
            ZDateTime expDateTime = request.keyStore.getEarliestExpirationDate(alias + "_0");
            if (expDateTime != null && expDateTime.compareTo(new ZDateTime()) > 0) {
                return;
            }
        }
        request.keyStore.downloadCertificates(request.proxy, url.toString(), alias);
    }
    
    private boolean isToFollowRedirect(ZHttpResponse response) {
        return response.getLocation()!=null;
    }
    
    private ZHttpResponse sendRequestTo(ZUrl url, Consumer<HttpURLConnection> connectionConsumer) throws ZHttpRequestException, ZUrlIsInvalidException, ZKeyStoreException, ZHttpResponseException{
        try {
            HttpURLConnection connection = buildConnection(url);
            if (connectionConsumer!=null) {
                connectionConsumer.accept(connection);
            }
            ZHttpResponse response = buildResponse(connection, url);
            if (isToFollowRedirect(response)){
                return followRedirect(url, response);
            }
            return response;
        } catch (NoSuchAlgorithmException|KeyManagementException|IOException ex) {
            throw new ZHttpRequestException(ex);
        }
    }

    private ZHttpResponse followRedirect(ZUrl url, ZHttpResponse response) throws IOException, NoSuchAlgorithmException, KeyManagementException, ZUrlIsInvalidException, ZHttpRequestException, ZKeyStoreException, ZHttpResponseException {
        ZUrl redirectTo = url.getChild(response.getLocation());
        return sendRequestTo(redirectTo, connection->{
            connection.setRequestProperty("referer", url.toString());
        });
    }

    private void sendData(HttpURLConnection connection) throws IOException {
        if (request.getData()!=null){
            ZHttpData data = request.getData();
            String contentType = data.getContentType(connection);
            if (contentType!=null){
                connection.setRequestProperty("Content-Type", contentType);
            }
            Integer contentLength = data.getContentLength(connection);
            if (contentLength!=null){
                connection.setRequestProperty("Content-Length", contentLength.toString());
            }
            data.changeRequest(connection);
            data.sendData(connection, connection.getOutputStream());
        }
    }
    
}
