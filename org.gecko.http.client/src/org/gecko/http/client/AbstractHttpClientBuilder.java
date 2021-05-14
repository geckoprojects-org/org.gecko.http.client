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

package org.gecko.http.client;

import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

public class AbstractHttpClientBuilder implements Builder {

    protected final Builder builder = HttpClient.newBuilder();

    public AbstractHttpClientBuilder() {

        super();
    }

    @Override
    public Builder authenticator(Authenticator authenticator) {

        return builder.authenticator(authenticator);
    }

    @Override
    public HttpClient build() {

        return builder.build();
    }

    @Override
    public Builder connectTimeout(Duration duration) {

        return builder.connectTimeout(duration);
    }

    @Override
    public Builder cookieHandler(CookieHandler cookieHandler) {

        return builder.cookieHandler(cookieHandler);
    }

    @Override
    public Builder executor(Executor executor) {

        return builder.executor(executor);
    }

    @Override
    public Builder followRedirects(Redirect policy) {

        return builder.followRedirects(policy);
    }

    @Override
    public Builder priority(int priority) {

        return builder.priority(priority);
    }

    @Override
    public Builder proxy(ProxySelector proxySelector) {

        return builder.proxy(proxySelector);
    }

    @Override
    public Builder sslContext(SSLContext sslContext) {

        return builder.sslContext(sslContext);
    }

    @Override
    public Builder sslParameters(SSLParameters sslParameters) {

        return builder.sslParameters(sslParameters);
    }

    @Override
    public Builder version(Version version) {

        return builder.version(version);
    }

}