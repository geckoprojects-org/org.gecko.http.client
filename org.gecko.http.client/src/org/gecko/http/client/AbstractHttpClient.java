package org.gecko.http.client;

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
