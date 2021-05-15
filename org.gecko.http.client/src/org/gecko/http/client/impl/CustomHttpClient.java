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
package org.gecko.http.client.impl;

import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.PushPromiseHandler;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.gecko.http.client.Constants;
import org.gecko.http.client.impl.CustomHttpClient.HttpClientConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ServiceDescription(CustomHttpClient.DESCRIPTION)
@Designate(ocd = HttpClientConfig.class, factory = true)
@Component(service = HttpClient.class, configurationPid = Constants.PID_HTTP_CLIENT_CUSTOM)
public class CustomHttpClient extends AbstractHttpClient {

    private class CustomHeaderHttpRequest extends HttpRequest {

        private HttpRequest innerHttpRequest;

        public CustomHeaderHttpRequest(HttpRequest httpRequest) {

            this.innerHttpRequest = httpRequest;

        }

        @Override
        public Optional<BodyPublisher> bodyPublisher() {

            return innerHttpRequest.bodyPublisher();
        }

        @Override
        public boolean expectContinue() {

            return innerHttpRequest.expectContinue();
        }

        @Override
        public HttpHeaders headers() {

            return modifyHeader(innerHttpRequest.headers());

        }

        @Override
        public String method() {

            return innerHttpRequest.method();
        }

        @Override
        public Optional<Duration> timeout() {

            return innerHttpRequest.timeout();
        }

        @Override
        public URI uri() {

            return innerHttpRequest.uri();
        }

        @Override
        public Optional<Version> version() {

            return innerHttpRequest.version();
        }
    }

    @ObjectClassDefinition
    public @interface HttpClientConfig {

        Redirect followRedirects() default Redirect.NEVER;

        String[] headers();

        @AttributeDefinition(name = "osgi.http.client.name", required = true)
        String name();

        long timeoutMs() default -1;

        Version version() default Version.HTTP_2;

    }

    protected final static String DESCRIPTION = "The Custom-Configurable java.net.http.HttpClient.";

    private Optional<Duration> connectTimeout;

    private Map<String, List<String>> defaultHeaders = null;

    private Redirect followRedirects;

    private HttpClient inner = HttpClient.newHttpClient();

    private Optional<Authenticator> oAuthenticator = Optional.empty();

    private Optional<CookieHandler> oCookieHandler = Optional.empty();

    private Optional<ProxySelector> oProxySelector = Optional.empty();

    private Version version;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    public void bindAuthenticator(Authenticator authenticator) {

        this.oAuthenticator = Optional.of(authenticator);
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    public void bindCookieHandler(CookieHandler cookieHandler) {

        this.oCookieHandler = Optional.of(cookieHandler);
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    public void bindProxySelector(ProxySelector proxySelector) {

        this.oProxySelector = Optional.of(proxySelector);
    }

    public void unbindACookieHandler(CookieHandler cookieHandler) {

        this.oCookieHandler = Optional.empty();
    }

    public void unbindAuthenticator(Authenticator authenticator) {

        this.oAuthenticator = Optional.empty();
    }

    public void unbindProxySelector(ProxySelector proxySelector) {

        this.oProxySelector = Optional.empty();
    }

    @Override
    public Optional<Authenticator> authenticator() {

        return oAuthenticator;
    }

    @Override
    public Optional<Duration> connectTimeout() {

        return connectTimeout;
    }

    @Override
    public Optional<CookieHandler> cookieHandler() {

        return oCookieHandler;
    }

    @Activate
    @Modified
    public void activate(HttpClientConfig cfg) {

        this.followRedirects = cfg.followRedirects();
        this.version = cfg.version();

        if (cfg.timeoutMs() > 0) {
            this.connectTimeout = Optional.of(Duration.ofMillis(cfg.timeoutMs()));
        } else {
            this.connectTimeout = Optional.empty();
        }
        defaultHeaders = calcDefaultHeader(cfg);
    }

    @Deactivate
    public void deActivate() {

        defaultHeaders = Map.of();
        this.connectTimeout = Optional.empty();

    }

    @Override
    public Redirect followRedirects() {

        return followRedirects;
    }

    @Override
    public Version version() {

        return version;
    }

    @Override
    protected HttpClient inner() {

        return inner;
    }

    private HttpHeaders modifyHeader(HttpHeaders headers) {

        Map<String, List<String>> combindedHeaders = new HashMap<>(defaultHeaders);
        combindedHeaders.putAll(headers.map());

        return HttpHeaders.of(combindedHeaders, (x, y) -> true);

    }

    @Override
    public Optional<ProxySelector> proxy() {

        return oProxySelector;
    }

    private Map<String, List<String>> calcDefaultHeader(HttpClientConfig config) {

        Map<String, List<String>> headers = new HashMap<>();

        for (String line : config.headers()) {

            int index = line.indexOf(":");
            if (index <= 0) {
                continue;
            }
            String key = line.substring(0, index);
            String value = line.substring(index + 1);
            List<String> headerValue = null;
            if (value == null) {
                headerValue = List.of("");
            } else {
                headerValue = Arrays.asList(value.split(","));
            }
            headers.put(key, headerValue);
        }
        return headers;
    }

    @Override
    public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler)
            throws IOException, InterruptedException {

        return inner().send(wrap(request), responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
            BodyHandler<T> responseBodyHandler) {

        return inner().sendAsync(wrap(request), responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
            BodyHandler<T> responseBodyHandler, PushPromiseHandler<T> pushPromiseHandler) {

        return inner().sendAsync(wrap(request), responseBodyHandler, pushPromiseHandler);
    }

    private CustomHeaderHttpRequest wrap(HttpRequest httpRequest) {

        return new CustomHeaderHttpRequest(httpRequest);
    }

}
