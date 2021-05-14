/*******************************************************************************
 * Copyright (c) 2021 Data In Motion Consulting GmbH
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

package org.gecko.http.client.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.PasswordAuthentication;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.Optional;

import org.gecko.http.client.CustomHttpClientBuilder;
import org.gecko.http.client.auth.BasicAuthenticator;
import org.gecko.http.client.proxy.UriProxyProvider;
import org.gecko.http.client.proxy.UriProxyProviderImpl;
import org.junit.jupiter.api.Nested;
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

@ExtendWith({ ConfigurationExtension.class, ServiceExtension.class, BundleContextExtension.class })
public class CustomHttpClientBuilderTest {

    @Nested
    class WithOutConfiguration {

        @Test
        public void onlyDefaultHttpClientBuilderExistsTest(
                @InjectService(cardinality = 1) ServiceAware<HttpClient.Builder> sa) {

            assertThat(sa.getServices()).hasSize(1);

            ServiceReference<HttpClient.Builder> sr = sa.getServiceReference();
            assertNotNull(sr);
            assertNotNull(sr.getProperties());
            assertThat(Dictionaries.asMap(sa.getServiceReference().getProperties()))
                    .containsEntry("osgi.http.client.builder.name", ".default");
        }
    }

    @Nested
    class WithConfiguration {

        @Nested
        class BuilderConfiguration {

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "a", location = "?", properties = {
                    @Property(key = Constants.SERVICE_RANKING, value = Integer.MAX_VALUE
                            + "", scalar = Scalar.Integer, type = Type.Scalar),
                    @Property(key = "osgi.http.client.builder.name", value = "y") })
            public void testWithPropertyName_ShouldNotCreateService(
                    @InjectService ServiceAware<HttpClient.Builder> sa) {

                assertThat(sa).isNotNull();
                assertThat(sa.getServices()).hasSize(2);
                ServiceReference<HttpClient.Builder> sr = sa.getServiceReference();
                assertThat(sr.getProperties()).isNotNull();
                System.out.println(sr.getProperties());
                assertThat(sr.getProperties().get("osgi.http.client.builder.name")).isNotNull()
                        .isEqualTo("y");
                HttpClient.Builder builder = sa.getService(sr);
                assertThat(builder).isNotNull();
                HttpClient client = builder.build();
                assertThat(client.authenticator()).isNotPresent();
                assertThat(client.connectTimeout()).isNotPresent();
                assertThat(client.cookieHandler()).isNotPresent();
                assertThat(client.followRedirects()).isEqualTo(Redirect.NEVER);
                assertThat(client.proxy()).isNotPresent();
                assertThat(client.version()).isEqualTo(Version.HTTP_2);
                assertThat(builder.build().connectTimeout()).isNotPresent();
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "version", value = "HTTP_2") })
            public void testPropertyVersion_HTTP2(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().version()).isEqualTo(Version.HTTP_2);
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "version", value = "HTTP_1_1") })
            public void testPropertyVersion_HTTP1_1(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().version()).isEqualTo(Version.HTTP_1_1);
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "timeoutMs", value = "0") })
            public void testPropertyVersion_timeout_0(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().connectTimeout()).isNotPresent();
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "timeoutMs", value = "1") })
            public void testPropertyVersion_timeout_1(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().connectTimeout()).isPresent()
                        .hasValue(Duration.ofMillis(1));
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "followRedirects", value = "NEVER") })
            public void testPropertyVersion_followRedirects_NEVER(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().followRedirects()).isEqualTo(Redirect.NEVER);
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "followRedirects", value = "ALWAYS") })
            public void testPropertyVersion_followRedirects_ALWAYS(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().followRedirects()).isEqualTo(Redirect.ALWAYS);
            }

            @Test
            @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                    @Property(key = "osgi.http.client.builder.name", value = "x"),
                    @Property(key = "followRedirects", value = "NORMAL") })
            public void testPropertyVersion_followRedirects_NORMAL(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                assertThat(builder.build().followRedirects()).isEqualTo(Redirect.NORMAL);
            }
        }

        @Nested
        @WithFactoryConfiguration(factoryPid = CustomHttpClientBuilder.PID, name = "b", location = "?", properties = {
                @Property(key = "osgi.http.client.builder.name", value = "x") })
        class AuthenticatorConfiguration {

            @Test
            @WithFactoryConfiguration(factoryPid = BasicAuthenticator.PID, name = "b", location = "?", properties = {
                    @Property(key = "serverBasicAuthEnable", value = "true"),
                    @Property(key = "serverBasicAuthUsername", value = "user"),
                    @Property(key = "serverBasicAuthPassword", value = "pass") })
            public void testPropertyVersion_AuthServerOnly(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder) {

                Optional<Authenticator> optionalAuthenticator = builder.build().authenticator();
                assertThat(optionalAuthenticator).isPresent();

                if (!optionalAuthenticator.isPresent()) {
                    return;
                }
                Authenticator authenticator = optionalAuthenticator.get();
                PasswordAuthentication pwaServer = authenticator
                        .requestPasswordAuthenticationInstance(null, null, 0, null, null, null,
                                null, RequestorType.SERVER);
                assertThat(pwaServer).isNotNull();
                assertThat(pwaServer.getUserName()).isNotNull().isEqualTo("user");
                assertThat(pwaServer.getPassword()).isNotNull().isEqualTo("pass".toCharArray());

                PasswordAuthentication pwaProxy = authenticator
                        .requestPasswordAuthenticationInstance(null, null, 0, null, null, null,
                                null, RequestorType.PROXY);
                assertThat(pwaProxy).isNull();

            }

            @Test
            @WithFactoryConfiguration(factoryPid = BasicAuthenticator.PID, name = "b", location = "?", properties = {
                    @Property(key = "serverBasicAuthEnable", value = "false") })
            public void testPropertyVersion_AuthServerOnactive(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder,
                    @InjectService Authenticator a) {

                Optional<Authenticator> optionalAuthenticator = builder.build().authenticator();
                if (!optionalAuthenticator.isPresent()) {
                    return;
                }
                Authenticator authenticator = optionalAuthenticator.get();

                PasswordAuthentication pwaServer = authenticator
                        .requestPasswordAuthenticationInstance(null, null, 0, null, null, null,
                                null, RequestorType.SERVER);
                assertThat(pwaServer).isNull();

                PasswordAuthentication pwaProxy = authenticator
                        .requestPasswordAuthenticationInstance(null, null, 0, null, null, null,
                                null, RequestorType.PROXY);
                assertThat(pwaProxy).isNull();

            }

            @Test
            @WithFactoryConfiguration(factoryPid = BasicAuthenticator.PID, name = "b", location = "?", properties = {
                    @Property(key = "proxyBasicAuthEnable", value = "false") })
            public void testPropertyVersion_AuthProxyDiabled(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder,
                    @InjectService Authenticator a) throws InterruptedException {

                Thread.sleep(2000l);
                Optional<Authenticator> optionalAuthenticator = builder.build().authenticator();
                assertThat(optionalAuthenticator).isPresent();
                if (!optionalAuthenticator.isPresent()) {
                    return;
                }
                Authenticator authenticator = optionalAuthenticator.get();

                PasswordAuthentication pwaServer = authenticator
                        .requestPasswordAuthenticationInstance(null, null, 0, null, null, null,
                                null, RequestorType.SERVER);
                assertThat(pwaServer).isNull();

                PasswordAuthentication pwaProxy = authenticator
                        .requestPasswordAuthenticationInstance(null, null, 0, null, null, null,
                                null, RequestorType.PROXY);
                assertThat(pwaProxy).isNull();

            }

            @Test
            @WithFactoryConfiguration(factoryPid = BasicAuthenticator.PID, name = "b", location = "?")
            @WithFactoryConfiguration(factoryPid = UriProxyProviderImpl.PID, name = "X", location = "?", properties = {
                    @Property(key = "authBasicEnable", value = "true"),
                    @Property(key = "authBasicUsername", value = "userx"),
                    @Property(key = "authBasicPassword", value = "passx"),
                    @Property(key = "proxyHostname", value = "hostx"),
                    @Property(key = "proxyPort", value = "9999", scalar = Scalar.Integer) })
            @WithFactoryConfiguration(factoryPid = UriProxyProviderImpl.PID, name = "Z", location = "?", properties = {
                    @Property(key = "authBasicEnable", value = "true"),
                    @Property(key = "authBasicUsername", value = "userz"),
                    @Property(key = "authBasicPassword", value = "passz"),
                    @Property(key = "proxyHostname", value = "hostz"),
                    @Property(key = "proxyPort", value = "9999", scalar = Scalar.Integer) })
            public void testPropertyVersion_AuthProxyWithProxy(
                    @InjectService(filter = "(osgi.http.client.builder.name=x)") HttpClient.Builder builder,
                    @InjectService(filter = "(proxyHostname=hostx)") UriProxyProvider proxyX,
                    @InjectService(filter = "(proxyHostname=hostz)") UriProxyProvider proxyZ)
                    throws IOException, InterruptedException, URISyntaxException {

                assertThat(proxyX).isNotNull();
                assertThat(proxyZ).isNotNull();
                Optional<Authenticator> optionalAuthenticator = builder.build().authenticator();
                assertThat(optionalAuthenticator).isPresent();
                if (!optionalAuthenticator.isPresent()) {
                    return;
                }
                Authenticator authenticator = optionalAuthenticator.get();

                // Works
                PasswordAuthentication pwaServerWorksX = authenticator
                        .requestPasswordAuthenticationInstance("hostx", null, 9999, null, null,
                                null, null, RequestorType.PROXY);
                assertThat(pwaServerWorksX).isNotNull();
                assertThat(pwaServerWorksX.getUserName()).isNotNull().isEqualTo("userx");
                assertThat(pwaServerWorksX.getPassword()).isNotNull()
                        .isEqualTo("passx".toCharArray());

                // Works
                PasswordAuthentication pwaServerWorksZ = authenticator
                        .requestPasswordAuthenticationInstance("hostz", null, 9999, null, null,
                                null, null, RequestorType.PROXY);
                assertThat(pwaServerWorksZ).isNotNull();
                assertThat(pwaServerWorksZ.getUserName()).isNotNull().isEqualTo("userz");
                assertThat(pwaServerWorksZ.getPassword()).isNotNull()
                        .isEqualTo("passz".toCharArray());

                // null because wrong port
                PasswordAuthentication pwaServerWrongPort = authenticator
                        .requestPasswordAuthenticationInstance("hostx", null, 0, null, null, null,
                                null, RequestorType.PROXY);
                assertThat(pwaServerWrongPort).isNull();

                // null because wrong Host
                PasswordAuthentication pwaServerWrongHost = authenticator
                        .requestPasswordAuthenticationInstance("hosty", null, 9999, null, null,
                                null, null, RequestorType.PROXY);
                assertThat(pwaServerWrongHost).isNull();

                // no proxy
                PasswordAuthentication pwaProxy = authenticator
                        .requestPasswordAuthenticationInstance("hostx", null, 9999, null, null,
                                null, null, RequestorType.SERVER);
                assertThat(pwaProxy).isNull();

//                builder.build()
//                        .send(HttpRequest.newBuilder(new URI("http://localhost:9999/test"))
//                                .timeout(Duration.ofMillis(1000))
//                                .GET()
//                                .build(), HttpResponse.BodyHandlers.discarding());

            }

        }

    }
}