package br.zul.zwork5.http;

/**
 *
 * @author Luiz Henrique
 */
public class ZHttpPost {
/*
    private byte[] content;
    
    @Override
    public ZHttpResponse_1 send() {
        ZLogger logger = new ZLogger(getClass(), "send()");
        try {
            HttpURLConnection connection = prepareConnection(getUrl().asString());
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            if (getRequestProperty("Content-Type")==null){
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            connection.setRequestProperty("Content-Length", Integer.toString(getContentLength()));
            connection.getOutputStream().write(getContent());
            ZHttpResponse_1 response = getResponse(connection);
            if (isInstanceFollowRedirects()&&response.getLocationProperty()!=null){
               ZHttpGet httpGet = new ZHttpGet();
               httpGet.setUrl(response.getLocationProperty());
               prepareRequest(httpGet);
               response = httpGet.send();
            }
            return response;
        } catch (ProtocolException ex) {
            throw logger.error.prepareException(ex);
        } catch (IOException ex) {
            throw logger.error.prepareException(ex);
        }
    }
    
    public ZHttpResponse_1 sendFile(String inputName, String filename, Long size, InputStream is){
        return sendFile(inputName, filename, size, is, "application/octet-stream");
    }
    
    public ZHttpResponse_1 sendFile(String inputName, String filename, Long size, InputStream is, String contentType){
            
        ZLogger logger = new ZLogger(getClass(), "sendFile(String inputName, String filename, Long size, InputStream is)");
        try {
        
            //================================================================================================================================================================================================================
            //CONFIGURA A CONEXÃO//
            //===================/
            final String boundary = "----WebKitFormBoundary4AiZO9A1raexQrAq";
            HttpURLConnection connection = prepareConnection(getUrl().asString());
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",  "multipart/form-data; boundary="+boundary);
            connection.setRequestProperty("Content-Length", Integer.toString(getContentLength()));
    		
            //================================================================================================================================================================================================================
            //PARÂMETROS POST//
            //===============//
            StringBuilder parameterSb = new StringBuilder();
            for (Entry<String,String> parameter:parameterMap().entrySet()){
                    parameterSb.append("--");
                    parameterSb.append(boundary);
                    parameterSb.append("\r\n");
                    parameterSb.append("Content-Disposition: form-data; name=\"");
                    parameterSb.append(parameter.getKey());
                    parameterSb.append("\"\r\n\r\n");
                    parameterSb.append(parameter.getValue());
                    parameterSb.append("\r\n");
            }
    		
            parameterSb.append("--");
            parameterSb.append(boundary);
            parameterSb.append("\r\n");
            parameterSb.append("Content-Disposition: form-data; name=\"");
            parameterSb.append(inputName);
            parameterSb.append("\"; filename=\"");
            parameterSb.append(filename);
            parameterSb.append("\"\r\n");
            parameterSb.append("Content-Type: ").append(contentType);
            parameterSb.append("\r\n\r\n");

            //================================================================================================================================================================================================================
            //ENVIANDO OS PARÂMETROS POST//
            //===========================//
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameterSb.toString());

            //================================================================================================================================================================================================================
            //ENVIANDO O ARQUIVO//
            //==================//
            int bytesRead;
            byte[] buffer = new byte[2048];
            
            //======================================================================================================================================================================================================================================
            //BAIXANDO BYTES//
            //==============//
            while ((bytesRead = is.read(buffer)) != -1) {

                //======================================================================================================================================================================================================================================
                //ENVIANDO OS DADOS DO ARQUIVO//
                //============================//
                wr.write(buffer, 0, bytesRead);
                wr.flush();
           
            }

            //================================================================================================================================================================================================================
            //FINALIZA O ENVIO DAS INFORMAÇÕES//
            //================================//
            wr.writeBytes("\r\n--");
            wr.writeBytes(boundary);
            wr.writeBytes("--\r\n");
            wr.flush();
            wr.close();

            //================================================================================================================================================================================================================
            //OBTENDO A RESPOSTA//
            //==================//
            ZHttpResponse_1 response = getResponse(connection);
            if (isInstanceFollowRedirects()&&response.getLocationProperty()!=null){
               ZHttpGet httpGet = new ZHttpGet();
               httpGet.setUrl(response.getLocationProperty());
               prepareRequest(httpGet);
               response = httpGet.send();
            }
            
            return response;

        } catch (ProtocolException ex) {
            throw logger.error.prepareException(ex);
        } catch (IOException ex) {
            throw logger.error.prepareException(ex);
        }
        
    }
    
    public ZHttpResponse_1 sendFile(String inputName, File file){
        return sendFile(inputName, new ZFile(file));
    }
    
    public ZHttpResponse_1 sendFile(String inputName, ZResource resource){
        return sendFile(inputName, new ZFile(resource));
    }
    
    public ZHttpResponse_1 sendFile(String inputName, ZFile file){
        long size;
        if (file.getFile()!=null){
            size = file.getFile().length();
        } else {
            try {
                size = file.getInputStream().skip(Long.MAX_VALUE);
            } catch (IOException ex) {
                throw new ZLogger(getClass(), "sendFile(ZFile file)").error.prepareException(ex);
            }
        }
        String contentType;
        String filename = file.getFilename().toLowerCase();
        if (filename.endsWith(".png")){
            contentType = "image/jpeg";
        } else if (filename.endsWith(".jpg")||filename.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else {
            contentType = "application/octet-stream";
        }
        ZHttpResponse_1 result = sendFile(inputName, file.getFilename(), size, file.getInputStream(), contentType);
        return result;
    }

    public int getContentLength() {
        return getContent().length;
    }
    
    public ZHttpPost setContent(byte[] content){
        if (content == null){
            this.content = null;
        } else {
            this.content = content;
        }
        return this;
    }
    
    public void setContent(String content){
        if (content==null){
            this.content = null;
        } else {
            this.content = content.getBytes();
        }
    }

    public byte[] getContent() {
        ZLogger logger = new ZLogger(getClass(), "getContent()");
        
        if (content!=null){
            return content;
        }
        
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : parameterMap().entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw logger.error.prepareException(ex, "Não foi possível conveter '%s'!", param.getKey());
            }
            postData.append('=');
            try {
                String str = String.valueOf(param.getValue()).trim();
                if (!ZUtil.isEmptyOrNull(str)/*||!str.startsWith("{")||!str.endsWith("}")*//*){
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                } else {
                    postData.append(str);
                }
            } catch (UnsupportedEncodingException ex) {
                throw logger.error.prepareException(ex, "Não foi possível conveter '%s'!", param.getValue());
            }
        }
        return postData.toString().getBytes();
    }
*/
}
