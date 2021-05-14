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
import java.io.OutputStream;
import java.net.InetSocketAddress;
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

import org.gecko.http.client.CustomHttpClient;
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
    public void onlyDefaultHttpClientExistsTest(
            @InjectService(cardinality = 1) ServiceAware<HttpClient> sa) {

        assertThat(sa.getServices()).hasSize(1);

        ServiceReference<HttpClient> sr = sa.getServiceReference();
        assertNotNull(sr);
        assertNotNull(sr.getProperties());
        assertThat(Dictionaries.asMap(sa.getServiceReference().getProperties()))
                .containsEntry("osgi.http.client.name", ".default");
    }

    @Test
    @WithFactoryConfiguration(factoryPid = CustomHttpClient.PID, name = "a", location = "?", properties = {
            @Property(key = Constants.SERVICE_RANKING, value = Integer.MAX_VALUE
                    + "", scalar = Scalar.Integer, type = Type.Scalar),
            @Property(key = "osgi.http.client.name", value = "y"),
            @Property(key = "headers", value = { "headerKey1:val1,val2", "headerKey2:", ":" }) })
    public void testWithPropertyName_ShouldNotCreateService(
            @InjectService ServiceAware<HttpClient> sa)
            throws IOException, InterruptedException, URISyntaxException {

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

        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/test", new MyHandler());
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

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}