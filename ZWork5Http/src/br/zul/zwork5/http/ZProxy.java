package br.zul.zwork5.http;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 *
 * @author Luiz Henrique
 */
public class ZProxy {
    
    //==========================================================================
    //VARIÁVEIS PRIVADAS
    //==========================================================================
    private String host;
    private int port;
    private ZProxyType type;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZProxy(String host,int port,ZProxyType type){
        this.host = host;
        this.port = port;
        this.type = type;
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public Proxy asProxy(){
        Proxy.Type t = Proxy.Type.HTTP;
        switch(type){
            case HTTP:
                t = Proxy.Type.HTTP;
                break;
            case DIRECT:
                t = Proxy.Type.DIRECT;
                break;
            case SOCKS:
                t = Proxy.Type.SOCKS;
                break;
        }
        return new Proxy(t, new InetSocketAddress(host, port));
    }
    
    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public ZProxyType getType() {
        return type;
    }
    public void setType(ZProxyType type) {
        this.type = type;
    }
    
}
