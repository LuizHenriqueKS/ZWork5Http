package br.zul.zwork5.http;

import br.zul.zwork5.exception.ZCookieException;
import br.zul.zwork5.exception.ZException;
import br.zul.zwork5.log.ZLogger;
import br.zul.zwork5.str.ZStr;
import br.zul.zwork5.str.ZStrList;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Luiz Henrique
 */
public class ZCookie {

    //==========================================================================
    //VARIÁVEIS PRIVADAS
    //==========================================================================
    private String name;
    private String value;
    private Date expires;
    private String path;
    private String domain;
    boolean secure;

    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZCookie() {
    }

    public ZCookie(String code) throws ZCookieException {
        init(code);
    }

    //==========================================================================
    //MÉTODOS CONSTRUTORES
    //==========================================================================
    private void init(String code) throws ZCookieException {
        ZLogger logger = new ZLogger(getClass(), "init(String code)");
        ZStr zs = new ZStr(code, false);
        if (zs.startsWith("Set-Cookie:")) {
            zs = zs.from("Set-Cookie:").trim();
        }
        ZStrList part = zs.split(";");
        this.name = part.get(0).till("=").toString();
        this.value = part.get(0).from("=").toString();
        this.secure = false;
        for (int i = 1; i < part.size(); i++) {
            ZStr p = part.get(i);
            String _name = p.till("=").toString().toLowerCase().trim();
            String _value = p.from("=").toString();
            switch (_name) {
                case "expires":
                    try {
                        //expires = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z").parse();
                        ZStr x = new ZStr(_value, true);
                        String dateStr = x.from(" ").tillLast(" ").toString().toLowerCase();
                        try {
                            expires = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss").parse(dateStr);
                        } catch (Exception e) {
                            try {
                                dateStr = dateStr.replace("feb", "fev");
                                dateStr = dateStr.replace("apr", "abr");
                                dateStr = dateStr.replace("may", "mai");
                                dateStr = dateStr.replace("aug", "ago");
                                dateStr = dateStr.replace("dec", "dez");
                                expires = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss").parse(dateStr);
                            } catch (Exception ex) {
                                expires = null;
                            }
                        }
                    } catch (Exception ex) {
                        throw new ZCookieException(ex);
                    }   
                    break;
                case "path":
                    path = _value;
                    break;
                case "domain":
                    domain = _value;
                    break;
                case "secure":
                    secure = true;
                    break;
                default:
                    break;
            }
        }
    }

    //==========================================================================
    //GETTERS AND SETTERS
    //==========================================================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

}
