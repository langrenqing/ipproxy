package com.yuanbaopu.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.Test;

public class HttpConnectionTest {
	@Test
	public void testHttpConnection() throws IOException {
		String url = "https://siemens-home.tmall.com/";
		HttpURLConnection conn = createConnection(new URL(url));
		conn.connect();
		HttpURLConnection.setFollowRedirects(true);
		InputStream is = conn.getInputStream();
		byte[] bytes = new byte[1024];
		while(is.read(bytes) != -1) {
			System.out.println(new String(bytes));
		}
		System.out.println("");
	}
	
	private static HttpURLConnection createConnection(URL url) throws IOException {
    	HttpURLConnection conn = null;
    	conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false); // don't rely on native redirection support
        conn.setConnectTimeout(21000);
        conn.setReadTimeout(5000);

        if (conn instanceof HttpsURLConnection) {
            if (url.getProtocol().equals("https")) {
                initUnSecureTSL();
                ((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
                ((HttpsURLConnection)conn).setHostnameVerifier(getInsecureVerifier());
            }
        }
        return conn;
    }
	
	private static SSLSocketFactory sslSocketFactory;
	private static synchronized void initUnSecureTSL() throws IOException {
        if (sslSocketFactory == null) {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                }

                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("Can't create unsecure trust manager");
            } catch (KeyManagementException e) {
                throw new IOException("Can't create unsecure trust manager");
            }
        }

    }
	
	private static HostnameVerifier getInsecureVerifier() {
        return new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
    }
}
