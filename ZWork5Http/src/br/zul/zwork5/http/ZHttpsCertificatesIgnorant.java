package br.zul.zwork5.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author luizh
 */
class ZHttpsCertificatesIgnorant {

    //==========================================================================
    //VARIÁVEIS
    //==========================================================================
    private final ZHttpRequest request;
    private final HttpsURLConnection connection;

    //==========================================================================
    //CONSTRUTORES
    //==========================================================================
    public ZHttpsCertificatesIgnorant(ZHttpRequest request, HttpsURLConnection connection) {
        this.request = request;
        this.connection = connection;
    }

    //==========================================================================
    //METODOS PÚBLICOS
    //==========================================================================
    public void apply() throws NoSuchAlgorithmException, KeyManagementException {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2"); //PROTOCOLO
        
        if (!request.isIgnoreSSLValidators()) return;

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        connection.setSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        connection.setHostnameVerifier(allHostsValid);
    }
    
}
