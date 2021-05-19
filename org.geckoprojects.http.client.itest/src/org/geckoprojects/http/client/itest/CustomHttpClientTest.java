/*******************************************************************************
 * Copyright 2021 Data In Motion Consulting GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/

package org.geckoprojects.http.client.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.geckoprojects.http.client.UriProxyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.Property.Type;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@ExtendWith({ ConfigurationExtension.class, ServiceExtension.class, BundleContextExtension.class })
public class CustomHttpClientTest {

    @Test
    public void onlyDefaultHttpClientclientExistsTest(
            @InjectService(cardinality = 1) ServiceAware<HttpClient> sa) {

        assertThat(sa.getServices()).hasSize(1);

        ServiceReference<HttpClient> sr = sa.getServiceReference();
        assertNotNull(sr);
        assertNotNull(sr.getProperties());
        assertThat(Dictionaries.asMap(sa.getServiceReference().getProperties()))
                .containsEntry("osgi.http.client.name", ".default");
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "a", location = "?", properties = {
            @Property(key = Constants.SERVICE_RANKING, value = Integer.MAX_VALUE
                    + "", scalar = Scalar.Integer, type = Type.Scalar),
            @Property(key = "osgi.http.client.name", value = "y") })
    public void testWithPropertyName_ShouldCreateServiceRankingWins(
            @InjectService(timeout = 1000) ServiceAware<HttpClient> sa,
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=y)") HttpClient s)
            throws InterruptedException {

        Thread.sleep(1000);
        assertThat(sa).isNotNull();
        assertThat(sa.getServices()).hasSize(2);
        ServiceReference<HttpClient> sr = sa.getServiceReference();
        assertThat(sr.getProperties()).isNotNull();
        System.out.println(sr.getProperties());
        assertThat(sr.getProperties().get("osgi.http.client.name")).isNotNull().isEqualTo("y");
        HttpClient client = sa.getService(sr);

        assertThat(client.authenticator()).isNotPresent();
        assertThat(client.connectTimeout()).isNotPresent();
        assertThat(client.cookieHandler()).isNotPresent();
        assertThat(client.followRedirects()).isEqualTo(Redirect.NEVER);
        assertThat(client.proxy()).isNotPresent();
        assertThat(client.version()).isEqualTo(Version.HTTP_2);
        assertThat(client.connectTimeout()).isNotPresent();
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "version", value = "HTTP_2") })
    public void testPropertyVersion_HTTP2(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.version()).isEqualTo(Version.HTTP_2);
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "version", value = "HTTP_1_1") })
    public void testPropertyVersion_HTTP1_1(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.version()).isEqualTo(Version.HTTP_1_1);
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "timeoutMs", value = "0") })
    public void testPropertyVersion_timeout_0(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.connectTimeout()).isNotPresent();
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "timeoutMs", value = "1") })
    public void testPropertyVersion_timeout_1(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.connectTimeout()).isPresent().hasValue(Duration.ofMillis(1));
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "followRedirects", value = "NEVER") })
    public void testPropertyVersion_followRedirects_NEVER(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.followRedirects()).isEqualTo(Redirect.NEVER);
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "followRedirects", value = "ALWAYS") })
    public void testPropertyVersion_followRedirects_ALWAYS(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.followRedirects()).isEqualTo(Redirect.ALWAYS);
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "followRedirects", value = "NORMAL") })
    public void testPropertyVersion_followRedirects_NORMAL(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client) {

        assertThat(client.followRedirects()).isEqualTo(Redirect.NORMAL);
    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_AUTH_BASIC, name = "b", location = "?", properties = {
            @Property(key = "serverBasicAuthEnable", value = "true"),
            @Property(key = "serverBasicAuthUsername", value = "user"),
            @Property(key = "serverBasicAuthPassword", value = "pass") })
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x") })
    public void testPropertyVersion_AuthServerOnly(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client,
            @InjectService Authenticator auth) throws InterruptedException {

        Optional<Authenticator> optionalAuthenticator = client.authenticator();
        assertThat(optionalAuthenticator).isPresent();

        if (!optionalAuthenticator.isPresent()) {
            return;
        }
        Authenticator authenticator = optionalAuthenticator.get();
        PasswordAuthentication pwaServer = authenticator.requestPasswordAuthenticationInstance(null,
                null, 0, null, null, null, null, RequestorType.SERVER);
        assertThat(pwaServer).isNotNull();
        assertThat(pwaServer.getUserName()).isNotNull().isEqualTo("user");
        assertThat(pwaServer.getPassword()).isNotNull().isEqualTo("pass".toCharArray());

        PasswordAuthentication pwaProxy = authenticator.requestPasswordAuthenticationInstance(null,
                null, 0, null, null, null, null, RequestorType.PROXY);
        assertThat(pwaProxy).isNull();

    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_AUTH_BASIC, name = "b", location = "?", properties = {
            @Property(key = "serverBasicAuthEnable", value = "false") })
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x") })

    public void testPropertyVersion_AuthServerOnactive(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client,
            @InjectService Authenticator a) {

        Optional<Authenticator> optionalAuthenticator = client.authenticator();
        if (!optionalAuthenticator.isPresent()) {
            return;
        }
        Authenticator authenticator = optionalAuthenticator.get();

        PasswordAuthentication pwaServer = authenticator.requestPasswordAuthenticationInstance(null,
                null, 0, null, null, null, null, RequestorType.SERVER);
        assertThat(pwaServer).isNull();

        PasswordAuthentication pwaProxy = authenticator.requestPasswordAuthenticationInstance(null,
                null, 0, null, null, null, null, RequestorType.PROXY);
        assertThat(pwaProxy).isNull();

    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_AUTH_BASIC, name = "b", location = "?", properties = {
            @Property(key = "proxyBasicAuthEnable", value = "false") })
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x") })

    public void testPropertyVersion_AuthProxyDiabled(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client,
            @InjectService Authenticator a) throws InterruptedException {

        Thread.sleep(2000l);
        Optional<Authenticator> optionalAuthenticator = client.authenticator();
        assertThat(optionalAuthenticator).isPresent();
        if (!optionalAuthenticator.isPresent()) {
            return;
        }
        Authenticator authenticator = optionalAuthenticator.get();

        PasswordAuthentication pwaServer = authenticator.requestPasswordAuthenticationInstance(null,
                null, 0, null, null, null, null, RequestorType.SERVER);
        assertThat(pwaServer).isNull();

        PasswordAuthentication pwaProxy = authenticator.requestPasswordAuthenticationInstance(null,
                null, 0, null, null, null, null, RequestorType.PROXY);
        assertThat(pwaProxy).isNull();

    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_AUTH_BASIC, name = "b", location = "?")
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_PROXY_PROVIDER_DEFAULT, name = "X", location = "?", properties = {
            @Property(key = "authBasicEnable", value = "true"),
            @Property(key = "authBasicUsername", value = "userx"),
            @Property(key = "authBasicPassword", value = "passx"),
            @Property(key = "proxyHostname", value = "hostx"),
            @Property(key = "proxyPort", value = "9999", scalar = Scalar.Integer) })
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_PROXY_PROVIDER_DEFAULT, name = "Z", location = "?", properties = {
            @Property(key = "authBasicEnable", value = "true"),
            @Property(key = "authBasicUsername", value = "userz"),
            @Property(key = "authBasicPassword", value = "passz"),
            @Property(key = "proxyHostname", value = "hostz"),
            @Property(key = "proxyPort", value = "9999", scalar = Scalar.Integer) })
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x") })

    public void testPropertyVersion_AuthProxyWithProxy(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client,
            @InjectService(timeout = 1000, filter = "(proxyHostname=hostx)") UriProxyProvider proxyX,
            @InjectService(timeout = 1000, filter = "(proxyHostname=hostz)") UriProxyProvider proxyZ)
            throws IOException, InterruptedException, URISyntaxException {

        assertThat(proxyX).isNotNull();
        assertThat(proxyZ).isNotNull();
        Optional<Authenticator> optionalAuthenticator = client.authenticator();
        assertThat(optionalAuthenticator).isPresent();
        if (!optionalAuthenticator.isPresent()) {
            return;
        }
        Authenticator authenticator = optionalAuthenticator.get();

        // Works
        PasswordAuthentication pwaServerWorksX = authenticator
                .requestPasswordAuthenticationInstance("hostx", null, 9999, null, null, null, null,
                        RequestorType.PROXY);
        assertThat(pwaServerWorksX).isNotNull();
        assertThat(pwaServerWorksX.getUserName()).isNotNull().isEqualTo("userx");
        assertThat(pwaServerWorksX.getPassword()).isNotNull().isEqualTo("passx".toCharArray());

        // Works
        PasswordAuthentication pwaServerWorksZ = authenticator
                .requestPasswordAuthenticationInstance("hostz", null, 9999, null, null, null, null,
                        RequestorType.PROXY);
        assertThat(pwaServerWorksZ).isNotNull();
        assertThat(pwaServerWorksZ.getUserName()).isNotNull().isEqualTo("userz");
        assertThat(pwaServerWorksZ.getPassword()).isNotNull().isEqualTo("passz".toCharArray());

        // null because wrong port
        PasswordAuthentication pwaServerWrongPort = authenticator
                .requestPasswordAuthenticationInstance("hostx", null, 0, null, null, null, null,
                        RequestorType.PROXY);
        assertThat(pwaServerWrongPort).isNull();

        // null because wrong Host
        PasswordAuthentication pwaServerWrongHost = authenticator
                .requestPasswordAuthenticationInstance("hosty", null, 9999, null, null, null, null,
                        RequestorType.PROXY);
        assertThat(pwaServerWrongHost).isNull();

        // no proxy
        PasswordAuthentication pwaProxy = authenticator.requestPasswordAuthenticationInstance(
                "hostx", null, 9999, null, null, null, null, RequestorType.SERVER);
        assertThat(pwaProxy).isNull();

//                client
//                        .send(HttpRequest.newclient(new URI("http://localhost:9999/test"))
//                                .timeout(Duration.ofMillis(1000))
//                                .GET()
//                                .build(), HttpResponse.BodyHandlers.discarding());

    }

    @Test
    @WithFactoryConfiguration(factoryPid = org.geckoprojects.http.client.Constants.PID_HTTP_CLIENT_CUSTOM, name = "b", location = "?", properties = {
            @Property(key = "osgi.http.client.name", value = "x"),
            @Property(key = "headers", value = { "headerKey1:val1,val2", "headerKey2:", ":" }) })
    public void testPropertyHeadersSet(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=x)") HttpClient client)
            throws IOException, InterruptedException, URISyntaxException {

        assertThat(client).isNotNull();

        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/test", new HttpHandler() {

            @Override
            public void handle(HttpExchange t) throws IOException {

                String response = "This is the response";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        server.setExecutor(null); // creates a default executor
        server.start();
        int port = server.getAddress().getPort();

        HttpResponse<String> bh = client
                .send(HttpRequest.newBuilder(new URI("http://localhost:" + port + "/test"))
                        .timeout(Duration.ofMillis(1000))
                        .GET()
                        .build(), HttpResponse.BodyHandlers.ofString());

        Map<String, List<String>> map = bh.request().headers().map();
        assertThat(map).containsEntry("headerKey1", List.of("val1", "val2"))
                .containsOnlyKeys("headerKey1");

        client.sendAsync(HttpRequest.newBuilder(new URI("http://localhost:" + port + "/test"))
                .timeout(Duration.ofMillis(1000))
                .GET()
                .build(), HttpResponse.BodyHandlers.discarding());
        server.stop(0);

    }

}