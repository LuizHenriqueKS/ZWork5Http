package br.zul.zwork5.http;

/**
 *
 * @author Luiz Henrique
 */
public class ZHttpGet {
/*
    @Override
    public ZHttpResponse_1 send() {
       ZLogger logger = new ZLogger(getClass(), "send()");
        try {
            String _url = getUrl().asString();
            if (_url==null){
                throw logger.info.prepareException("A url não é válida (%s)!", _url);
            }
            if (getContentLength()>0){
                if (!_url.contains("?")){
                    _url+="?";
                }
                _url+=getContent();
            }
            HttpURLConnection connection = prepareConnection(_url);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            ZHttpResponse_1 response = getResponse(connection);
            if (isInstanceFollowRedirects()&&response.getLocationProperty()!=null){
               ZHttpGet httpGet = new ZHttpGet();
               ZString z = new ZString(response.getLocationProperty(), true);
               if (z.startsWith(".")){
                   z = z.fromLeft(".");
                   ZUrl url = new ZUrl(_url);
                   z = z.appendLeft(url.getPart(0));
               }
               if (z.startsWith("/")){
                   z = new ZUrl(_url).removeLastPart().removeParameters().addPart(z.asString()).asString(true);
               }
               httpGet.setRequestProperty("referer", _url);
               httpGet.setUrl(z.asString());
               prepareRequest(httpGet);
               response = httpGet.send();
            }
            return response;
        } catch (ProtocolException ex) {
            throw logger.error.prepareException(ex);
        }
    }

    public int getContentLength() {
        return getContent().length();
    }
    
    public String getContent() {
        ZLogger logger = new ZLogger(getClass(), "getContent()");
        StringBuilder getData = new StringBuilder();
        for (Map.Entry<String, String> param : parameterMap().entrySet()) {
            if (getData.length() != 0) {
                getData.append('&');
            }
            try {
                getData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw logger.error.prepareException(ex, "Não foi possível conveter '%s'!", param.getKey());
            }
            getData.append('=');
            try {
                String str = String.valueOf(param.getValue()).trim();
                if (ZUtil.isEmptyOrNull(str)||!str.startsWith("{")||!str.endsWith("}")){
                    getData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                } else {
                    getData.append(str);
                }
            } catch (UnsupportedEncodingException ex) {
                throw logger.error.prepareException(ex, "Não foi possível conveter '%s'!", param.getValue());
            }
        }
        return getData.toString();
    }*/

}
