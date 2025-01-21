package cat.institutmarianao.sailing.security;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
@PropertySource("classpath:application.properties")
public class CustomHttpClientFactory implements FactoryBean<HttpClient> {

	@Value("${server.ssl.key-store}")
	private String serverSslKeystore;

	@Value("${server.ssl.key-store-password}")
	private String serverSslKeystorePassword;

	@Override
	public HttpClient getObject() throws Exception {
		// https://www.baeldung.com/spring-resttemplate-secure-https-service
		KeyStore keyStore = KeyStore.getInstance(ResourceUtils.getFile(serverSslKeystore),
				serverSslKeystorePassword.toCharArray());

		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(keyStore, serverSslKeystorePassword.toCharArray())
				.loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

		HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(csf).build();

		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

	@Override
	public Class<HttpClient> getObjectType() {
		return HttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}