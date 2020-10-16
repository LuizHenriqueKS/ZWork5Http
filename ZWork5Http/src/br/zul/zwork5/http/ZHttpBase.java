package br.zul.zwork5.http;

import br.zul.zwork5.url.ZUrl;
import br.zul.zwork5.util.ZListTreeMap;

/**
 *
 * @author luizh
 */
class ZHttpBase {

    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    protected ZCookieManager cookieManager;
    protected ZListTreeMap<String, String> requestPropertyMap;
    
    protected ZUrl url;
    protected ZProxy proxy;
    protected ZKeyStore keyStore;
    protected boolean instanceFollowRedirects = false;
    
    protected boolean ignoreSSLValidators;
    protected boolean autoDownloadCertificates;
    
    protected Integer connectTimeout;
    protected Integer readTimeout;
    protected Integer operationTimeout;

    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    
    
    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public ZCookieManager getCookieManager() {
        return cookieManager;
    }
    public void setCookieManager(ZCookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    public ZListTreeMap<String, String> getRequestPropertyMap() {
        return requestPropertyMap;
    }
    public void setRequestPropertyMap(ZListTreeMap<String, String> requestPropertyMap) {
        this.requestPropertyMap = requestPropertyMap;
    }

    public ZUrl getUrl() {
        return url;
    }
    public void setUrl(ZUrl url) {
        this.url = url;
    }

    public ZProxy getProxy() {
        return proxy;
    }
    public void setProxy(ZProxy proxy) {
        this.proxy = proxy;
    }

    public ZKeyStore getKeyStore() {
        return keyStore;
    }
    public void setKeyStore(ZKeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public boolean isInstanceFollowRedirects() {
        return instanceFollowRedirects;
    }
    public void setInstanceFollowRedirects(boolean instanceFollowRedirects) {
        this.instanceFollowRedirects = instanceFollowRedirects;
    }

    public boolean isIgnoreSSLValidators() {
        return ignoreSSLValidators;
    }
    public void setIgnoreSSLValidators(boolean ignoreSSLValidators) {
        this.ignoreSSLValidators = ignoreSSLValidators;
    }

    public boolean isAutoDownloadCertificates() {
        return autoDownloadCertificates;
    }
    public ZHttpBase setAutoDownloadCertificates(boolean autoDownloadCertificates) {
        this.autoDownloadCertificates = autoDownloadCertificates;
        return this;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }
    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }
    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getOperationTimeout() {
        return operationTimeout;
    }
    public void setOperationTimeout(Integer operationTimeout) {
        this.operationTimeout = operationTimeout;
    }    
    
}
