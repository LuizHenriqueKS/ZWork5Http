package br.zul.zwork5.http;

import br.zul.zwork5.exception.ZUrlIsInvalidException;
import br.zul.zwork5.url.ZUrl;
import br.zul.zwork5.util.ZListTreeMap;

/**
 *
 * @author luizh
 */
public class ZHttp extends ZHttpBase {

    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttp() {
        this.keyStore = new ZKeyStore();
        this.cookieManager = new ZCookieManager();
        this.requestPropertyMap = new ZListTreeMap<>(String.CASE_INSENSITIVE_ORDER);
        fillWithGoogleRequestProperties();
    }
    
    public ZHttp(ZUrl url){
        this();
        this.url = url;
    }
    
    public ZHttp(String url) throws ZUrlIsInvalidException{
        this();
        this.url = new ZUrl(url);
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public ZHttpRequest get() throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("GET");
        requestBuilder.setParameterType(ZHttpParameterType.URL_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest get(ZUrl url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("GET");
        requestBuilder.setUrl(url);
        requestBuilder.setParameterType(ZHttpParameterType.URL_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest get(String url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("GET");
        requestBuilder.setUrlStr(url);
        requestBuilder.setParameterType(ZHttpParameterType.URL_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest post() throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("POST");
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest post(ZUrl url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("POST");
        requestBuilder.setUrl(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest post(String url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("POST");
        requestBuilder.setUrlStr(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest put() throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("PUT");
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest put(ZUrl url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("PUT");
        requestBuilder.setUrl(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest put(String url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("PUT");
        requestBuilder.setUrlStr(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest delete() throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("DELETE");
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest delete(ZUrl url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("DELETE");
        requestBuilder.setUrl(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest delete(String url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("DELETE");
        requestBuilder.setUrlStr(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest head() throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("HEAD");
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest head(ZUrl url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("HEAD");
        requestBuilder.setUrl(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    public ZHttpRequest head(String url) throws ZUrlIsInvalidException {
        ZHttpRequestBuilder requestBuilder = new ZHttpRequestBuilder();
        requestBuilder.setHttp(this);
        requestBuilder.setMethod("HEAD");
        requestBuilder.setUrlStr(url);
        requestBuilder.setParameterType(ZHttpParameterType.DATA_PARAMETERS);
        return requestBuilder.build();
    }

    //==========================================================================
    //MÉTODOS PRIVADOS
    //==========================================================================
    private void fillWithGoogleRequestProperties() {
        getRequestPropertyMap().addValue("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        getRequestPropertyMap().addValue("Accept-Encoding", "gzip");
        getRequestPropertyMap().addValue("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7");
        getRequestPropertyMap().addValue("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
    }

}
