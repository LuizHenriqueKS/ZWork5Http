package br.zul.zwork5.http;

import br.zul.zwork5.conversion.ZConversionOut;
import br.zul.zwork5.exception.ZConversionErrorException;
import br.zul.zwork5.exception.ZHttpResponseBodyException;
import br.zul.zwork5.exception.ZUnexpectedException;
import br.zul.zwork5.html.ZHtml;
import br.zul.zwork5.html.exception.ZHtmlParseException;
import br.zul.zwork5.value.ZValue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author luiz.silva
 */
public class ZHttpResponseBody implements ZValue {
    
    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private final ZHttpResponse response;
    private final InputStream inputStream;
    
    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    ZHttpResponseBody(ZHttpResponse response, InputStream inputStream) {
        this.response = response;
        this.inputStream = inputStream;
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public void save(File file) throws IOException{
        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS SOBRESCRITOS
    //==========================================================================
    @Override
    public ZConversionOut<String> asString() {
        return ()->{
            try {
                return readText();
            } catch (ZHttpResponseBodyException ex) {
                throw new ZConversionErrorException(ex, null);
            }
        };
    }

    @Override
    public Object asObject() {
        try {
            return asString().get().trim();
        } catch (ZConversionErrorException ex) {
            throw new ZUnexpectedException(ex);
        }
    }
    
    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public ZHtml asHtml() throws ZHtmlParseException, ZHttpResponseBodyException {
        return new ZHtml(readText());
    }
    
    //==========================================================================
    //MÉTODOS PRIVADOS
    //==========================================================================
    private String readText() throws ZHttpResponseBodyException {
        return new ZHttpResponseBodyTxtReader(response, inputStream).read();
    }
    
    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public InputStream getInputStream() {
        return inputStream;
    }
    
}
