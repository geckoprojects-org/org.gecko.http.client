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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.PushPromiseHandler;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

public abstract class AbstractHttpClient extends HttpClient {

    protected abstract HttpClient inner();

    @Override
    public Optional<CookieHandler> cookieHandler() {

        return inner().cookieHandler();
    }

    @Override
    public Optional<Duration> connectTimeout() {

        return inner().connectTimeout();

    }

    @Override
    public Redirect followRedirects() {

        return inner().followRedirects();
    }

    @Override
    public Optional<ProxySelector> proxy() {

        return inner().proxy();
    }

    @Override
    public SSLContext sslContext() {

        return inner().sslContext();
    }

    @Override
    public SSLParameters sslParameters() {

        return inner().sslParameters();
    }

    @Override
    public Optional<Authenticator> authenticator() {

        return inner().authenticator();
    }

    @Override
    public Version version() {

        return inner().version();
    }

    @Override
    public Optional<Executor> executor() {

        return inner().executor();
    }

    @Override
    public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler)
            throws IOException, InterruptedException {

        return inner().send(request, responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
            BodyHandler<T> responseBodyHandler) {

        return inner().sendAsync(request, responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
            BodyHandler<T> responseBodyHandler, PushPromiseHandler<T> pushPromiseHandler) {

        return inner().sendAsync(request, responseBodyHandler, pushPromiseHandler);
    }

}
