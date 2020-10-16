package br.zul.zwork5.http;

import br.zul.zwork5.exception.ZException;
import br.zul.zwork5.exception.ZKeyStoreException;
import br.zul.zwork5.exception.ZUrlIsInvalidException;
import br.zul.zwork5.log.ZLog;
import br.zul.zwork5.log.ZLogger;
import br.zul.zwork5.str.ZStr;
import br.zul.zwork5.timestamp.ZDateTime;
import br.zul.zwork5.url.ZUrl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author luiz.silva
 */
public class ZKeyStore {

    //==========================================================================
    //VARIÁVEIS PRIVADAS
    //==========================================================================
    private KeyStore keyStore;
    private String password;
    private File file;

    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZKeyStore() {
        this(false, null);
    }

    public ZKeyStore(String password) {
        this(false, password);
    }

    public ZKeyStore(Boolean loadDefault, String password) {
        try {

            if (password == null) {
                password = "changeit";
            }

            if (loadDefault) {

                char SEP = File.separatorChar;
                File dir = new File(System.getProperty("java.home") + SEP
                        + "lib" + SEP + "security");
                file = new File(dir, "jssecacerts");
                if (!file.isFile() || !file.exists()) {
                    file = new File(dir, "cacerts");
                }
                try (InputStream in = new FileInputStream(file)) {
                    this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    this.keyStore.load(in, password.toCharArray());
                }

            } else {
                this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                this.keyStore.load(null, password.toCharArray());
            }

            this.password = password;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            ZLog.e(getClass()).println(ex);
        }
    }

    public ZKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    //==========================================================================
    //MÉTODOS PÚBLICOS PARA MANIPULAR ARQUIVOS
    //==========================================================================
    public void save() {
        if (file == null) {
            return;
        }
        saveAs(file);
    }

    public void saveAs(File file) {
        try (OutputStream out = new FileOutputStream(file)) {
            keyStore.store(out, password.toCharArray());
            this.file = file;
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException ex) {
            ZLog.e(getClass()).println(ex);
        }
    }

    public void saveCopy(File file) {
        File aux = this.file;
        saveAs(file);
        this.file = aux;
    }

    public void load(File file) {
        load(file, password);
    }

    public void load(File file, String password) {
        try (InputStream in = new FileInputStream(file)) {
            this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            this.keyStore.load(in, password.toCharArray());
            this.password = password;
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException ex) {
            ZLog.e(getClass()).println(ex);
        }
    }

    //==========================================================================
    //MÉTODOS PÚBLICOS
    //==========================================================================
    public List<Certificate> downloadCertificates(ZProxy proxy, String address, String alias) throws ZUrlIsInvalidException, ZKeyStoreException {
        List<Certificate> result = downloadCertificates(proxy, address);
        for (int i = 0; i < result.size(); i++) {
            setCertificate(String.format("%s_%d", alias, i), result.get(i));
        }
        return result;
    }

    public List<Certificate> downloadCertificates(ZProxy proxy, String address) throws ZUrlIsInvalidException, ZKeyStoreException {
        ZLogger logger = new ZLogger(getClass(), "downloadCertificates(String address)");
        List<Certificate> result = new ArrayList<>();
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
            context.init(null, new TrustManager[]{tm}, null);

            try {
                URL obj = new ZUrl(address).getHostPortUrl().asURL();
                HttpsURLConnection con;

                /*if (proxy != null) {
                con = (HttpsURLConnection) obj.openConnection(proxy);
                } else {*/
                if (proxy == null) {
                    con = (HttpsURLConnection) obj.openConnection();
                } else {
                    con = (HttpsURLConnection) obj.openConnection(proxy.asProxy());
                }
                //}

                con.setSSLSocketFactory(context.getSocketFactory());
                //add request header
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
                con.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
                con.setRequestProperty("Accept-Charset", "UTF-8");

                con.setConnectTimeout(5000); //set timeout to 5 seconds
                con.setReadTimeout(5000);
                con.getResponseMessage();
                con.getResponseCode();
            } catch (SSLException e) {
            } catch (IOException ex) {
            }

            X509Certificate[] chain = tm.chain;
            if (chain == null) {
                return result;
            }

            result.addAll(Arrays.asList(chain));

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
            throw new ZKeyStoreException(ex);
        }
        return result;
    }

    public SSLSocketFactory getSocketFactory() throws ZKeyStoreException {

        try {

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tmf.getTrustManagers(), null);

            return ctx.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
            throw new ZKeyStoreException(ex);
        }

    }

    //==========================================================================
    //MÉTODOS PÚBLICOS PARA MANIPULAR CERTIFICADOS
    //==========================================================================
    public boolean containsAlias(String alias) throws ZKeyStoreException {
        try {
            return keyStore.containsAlias(alias);
        } catch (KeyStoreException ex) {
            throw new ZKeyStoreException(ex);
        }
    }

    public void putCertificate(String alias, Certificate certificate) throws ZKeyStoreException {
        List<Certificate> certList = new ArrayList<>(listCertificates(alias));
        certList.add(certificate);
        setCertificates(alias, certList);
    }

    public void setCertificates(String alias, List<Certificate> certList) throws ZKeyStoreException {
        setCertificates(alias, certList, getKey(alias));
    }

    public void setCertificates(String alias, List<Certificate> certList, Key key) throws ZKeyStoreException {
        try {
            keyStore.setKeyEntry(alias, key, password.toCharArray(), certList.toArray(new Certificate[certList.size()]));
        } catch (KeyStoreException ex) {
            throw new ZKeyStoreException(ex);
        }
    }

    public void setCertificate(String alias, Certificate certificate) throws ZKeyStoreException {
        try {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException ex) {
            throw new ZKeyStoreException(ex);
        }
    }

    public void setCertificateWithKey(String alias, Certificate certificate) throws ZKeyStoreException {
        setCertificates(alias, Arrays.asList(certificate));
    }

    public void setCertificateWithKey(String alias, Certificate certificate, Key key) throws ZKeyStoreException {
        setCertificates(alias, Arrays.asList(certificate), key);
    }

    public Certificate getCertificate(String alias) throws ZKeyStoreException {
        try {
            return keyStore.getCertificate(alias);
        } catch (KeyStoreException ex) {
            throw new ZKeyStoreException(ex);
        }
    }

    public List<String> listAlias() throws ZKeyStoreException {
        try {
            List<String> result = new ArrayList<>();
            Enumeration<String> e = keyStore.aliases();
            while (e.hasMoreElements()) {
                String alias = e.nextElement();
                result.add(alias);
            }
            return Collections.unmodifiableList(result);
        } catch (KeyStoreException ex) {
            throw new ZKeyStoreException(ex);
        }
    }

    public List<Certificate> listCertificates() throws ZKeyStoreException {
        List<Certificate> result = new ArrayList<>();
        for (String alias : listAlias()) {
            result.addAll(listCertificates(alias));
        }
        return result;
    }

    public List<Certificate> listCertificates(String alias) throws ZKeyStoreException {
        try {
            Certificate[] certs = keyStore.getCertificateChain(alias);
            Certificate cert = keyStore.getCertificate(alias);
            HashSet<Certificate> result = new HashSet<>();
            if (certs != null) {
                result.addAll(Arrays.asList(certs));
            }
            if (cert != null) {
                result.add(cert);
            }
            return Collections.unmodifiableList(new ArrayList<>(result));
        } catch (KeyStoreException ex) {
            throw new ZKeyStoreException(ex);
        }
    }

    public ZDateTime getEarliestExpirationDate(String alias) throws ZKeyStoreException {
        ZDateTime result = null;
        List<Certificate> certificateList = listCertificates(alias);
        for (Certificate cert : certificateList) {
            ZDateTime d = getExpirationDate(cert);
            if (result == null || result.compareTo(d) > 0) {
                result = d;
            }
        }
        return result;
    }

    public ZDateTime getEarliestExpirationDate() throws ZKeyStoreException {
        ZDateTime result = null;
        List<Certificate> certificateList = listCertificates();
        for (Certificate cert : certificateList) {
            ZDateTime d = getExpirationDate(cert);
            if (result == null || result.compareTo(d) > 0) {
                result = d;
            }
        }
        return result;
    }

    //==========================================================================
    //MÉTODOS PÚBLICOS ESTATICOS
    //==========================================================================
    public static ZDateTime getExpirationDate(Certificate cert) throws ZKeyStoreException {
        ZStr z = new ZStr(cert.toString(), true).from("Validity:").till("]").from("To: ");
        z = z.till(",");
        try {
            Date date;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
                date = sdf.parse(z.toString());
            } catch (ParseException e) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
                date = sdf.parse(z.toString());
            }
            return new ZDateTime(date);
        } catch (ParseException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Erro na conversão de string para data. Isso é uma data válida '");
            sb.append(z);
            sb.append("'?");
            throw new ZKeyStoreException(sb.toString(), e);
        }
    }

    //==========================================================================
    //GETTERS E SETTERS MODIFICADOS
    //==========================================================================
    public Key getKey(String alias) {
        try {
            return keyStore.getKey(alias, password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            ZLog.e(getClass()).println(ex);
            return null;
        }
    }

    //==========================================================================
    //GETTERS E SETTERS
    //==========================================================================
    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //==========================================================================
    //PRIVATE CLASSES
    //==========================================================================
    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {

            /**
             * This change has been done due to the following resolution advised
             * for Java 1.7+
             * http://infposs.blogspot.kr/2013/06/installcert-and-java-7.html
             *
             */
            return new X509Certificate[0];
            //throw new UnsupportedOperationException();
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

}
