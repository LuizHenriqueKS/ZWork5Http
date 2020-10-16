package br.zul.zwork5.http.data;

import br.zul.zwork5.entity.ZEntity;
import br.zul.zwork5.exception.ZJsonException;
import br.zul.zwork5.json.ZJson;
import br.zul.zwork5.json.ZJsonArray;
import br.zul.zwork5.json.ZJsonObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 *
 * @author luizh
 */
public class ZHttpDataJson implements ZHttpData {

    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private ZJson json;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttpDataJson(){
        this.json = new ZJson();
    }
    
    public ZHttpDataJson(String json) throws ZJsonException{
        this.json = new ZJson(json);
    }
    
    public ZHttpDataJson(ZJson json) throws ZJsonException{
        this.json = new ZJson(json.toString());
    }
    
    public ZHttpDataJson(ZJsonObject jsonObject) throws ZJsonException{
        this.json = new ZJson(jsonObject);
    }
    
    public ZHttpDataJson(ZJsonArray jsonArray) throws ZJsonException{
        this.json = new ZJson(jsonArray);
    }
    
    public static ZHttpDataJson fromEntity(ZEntity entity) throws ZJsonException{
        return new ZHttpDataJson(ZJson.fromEntity(entity));
    }
    
    public static ZHttpDataJson fromObj(Object obj) throws ZJsonException{
        return new ZHttpDataJson(ZJson.fromObj(obj));
    }

    //==========================================================================
    //MÉTODOS PÚBLICOS SOBRESCRITOS
    //==========================================================================
    @Override
    public String getContentType(HttpURLConnection connection) {
        return connection.getRequestProperty("Content-Length")==null?"application/json":null;
    }

    @Override
    public Integer getContentLength(HttpURLConnection connection) {
        return json.toString().getBytes().length;
    }

    @Override
    public void changeRequest(HttpURLConnection connection) {}

    @Override
    public void sendData(HttpURLConnection connection, OutputStream os) throws IOException {
        os.write(json.toString().getBytes());
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================

    
    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public ZJson getJson() {
        return json;
    }
    public void setJson(ZJson json) {
        this.json = json;
    }
    
}
