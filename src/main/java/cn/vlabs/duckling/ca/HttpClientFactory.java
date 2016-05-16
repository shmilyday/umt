/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.duckling.ca;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

public class HttpClientFactory {
    public static HttpClient getHttpClient() {  
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
        registryBuilder.register("http", plainSF);  
//指定信任密钥存储对象和连接套接字工厂  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            //信任任何链接  
            TrustStrategy anyTrustStrategy = new TrustStrategy() {  
                @Override  
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {  
                    return true;  
                }  
            };  
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, anyTrustStrategy).build();  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            		sslContext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            registryBuilder.register("https", sslsf);  
        } catch (KeyStoreException e) {  
            throw new RuntimeException(e);  
        } catch (KeyManagementException e) {  
            throw new RuntimeException(e);  
        } catch (NoSuchAlgorithmException e) {  
            throw new RuntimeException(e);  
        }  
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();  
        //设置连接管理器  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);  
        //构建客户端  
        return HttpClientBuilder.create().setConnectionManager(connManager).build();  
    }  
}
