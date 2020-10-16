package br.zul.zwork5.http;

import br.zul.zwork5.http.data.ZHttpData;
import br.zul.zwork5.exception.ZHttpRequestException;
import br.zul.zwork5.exception.ZHttpResponseException;
import br.zul.zwork5.exception.ZKeyStoreException;
import br.zul.zwork5.exception.ZUrlIsInvalidException;
import br.zul.zwork5.util.ZListTreeMap;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

/**
 *
 * @author luizh
 */
public class ZHttpRequest extends ZHttpBase {
    
    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    protected ZHttp http;
    protected String method;
    protected ZHttpParameterType parameterType;
    
    protected ZHttpFile file;
    protected ZHttpData data;
    
    protected boolean ignoreHandshakeException;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    protected ZHttpRequest(ZHttp http) {
        this.http = http;
        this.cookieManager = http.cookieManager;
        this.requestPropertyMap = new ZListTreeMap<>(http.getRequestPropertyMap());
        //this.url;
        this.proxy = http.getProxy();
        this.keyStore = http.getKeyStore();
        this.instanceFollowRedirects = http.isInstanceFollowRedirects();
        this.ignoreSSLValidators = http.isIgnoreSSLValidators();
        this.autoDownloadCertificates = http.isAutoDownloadCertificates();
        this.connectTimeout = http.getConnectTimeout();
        this.readTimeout = http.getReadTimeout();
        this.operationTimeout = http.getOperationTimeout();
        this.ignoreHandshakeException = true;
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public ZHttpResponse send() throws ZHttpRequestException, ZHttpResponseException, UnsupportedEncodingException, ZUrlIsInvalidException, ZKeyStoreException{
        return new ZHttpRequestSender(this).send();
    }
    
    /*
    criar uma classe para isso
    public void sendAsync(){
        sendAsync(null);
    }
    
    public void sendAsync(Consumer<ZHttpResponse> callback){
        new Thread(()->{
            ZHttpResponse response = send();
            if (callback!=null) callback.accept(response);
        }).start();
    }*/
    
    //==========================================================================
    //GETTERS E SETTERS MODIFICADOS
    //==========================================================================
    public ZHttpFile getFile() {
        return file;
    }
    public ZHttpRequest setFile(ZHttpFile file) {
        this.file = file;
        return this;
    }

    public ZHttpData getData() {
        return data;
    }
    public ZHttpRequest setData(ZHttpData data) {
        this.data = data;
        return this;
    }
    
}