package org.gecko.http.client;

import java.io.IOException;
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
import java.util.function.BiPredicate;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = CustomHttpClient.Config.class, factory = true)
@Component(service = HttpClient.class, scope = ServiceScope.PROTOTYPE)
public class CustomHttpClient extends AbstractHttpClient {

    public static final String PID = "org.gecko.http.client.CustomHttpClient";

    @ObjectClassDefinition(description = "")
    @interface Config {

        String[] headers();
    };

    private Map<String, List<String>> defaultHeaders = null;

    private HttpClient inner = null;

    @Reference()
    public void bindHttpClientBuilder(HttpClient.Builder builder) {

        inner = builder.build();
    }

    public void unbindHttpClientBuilder(HttpClient.Builder builder) {

        inner = null;
    }

    @Activate
    @Modified
    public void activate(Config config) {

        defaultHeaders = calcDefaultHeader(config);

    }

    private Map<String, List<String>> calcDefaultHeader(Config config) {

        Map<String, List<String>> headers = new HashMap<>();

        for (String line : config.headers()) {

            int index = line.indexOf(":");
            if (index <= 0) {
                continue;
            }
            String key = line.substring(0, index);
            String value = line.substring(index+1);
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

        return inner.send(wrap(request), responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
            BodyHandler<T> responseBodyHandler) {

        return inner.sendAsync(wrap(request), responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request,
            BodyHandler<T> responseBodyHandler, PushPromiseHandler<T> pushPromiseHandler) {

        return inner.sendAsync(wrap(request), responseBodyHandler, pushPromiseHandler);
    }

    private CustomHeaderHttpRequest wrap(HttpRequest httpRequest) {

        return new CustomHeaderHttpRequest(httpRequest);

    }

    class CustomHeaderHttpRequest extends HttpRequest {

        private HttpRequest httpRequest;

        public CustomHeaderHttpRequest(HttpRequest httpRequest) {

            this.httpRequest = httpRequest;
        }

        @Override
        public Optional<BodyPublisher> bodyPublisher() {

            return httpRequest.bodyPublisher();
        }

        @Override
        public String method() {

            return httpRequest.method();
        }

        @Override
        public Optional<Duration> timeout() {

            return httpRequest.timeout();
        }

        @Override
        public boolean expectContinue() {

            return httpRequest.expectContinue();
        }

        @Override
        public URI uri() {

            return httpRequest.uri();
        }

        @Override
        public Optional<Version> version() {

            return httpRequest.version();
        }

        @Override
        public HttpHeaders headers() {

            Map<String, List<String>> combindedHeaders = new HashMap<>(defaultHeaders);
            combindedHeaders.putAll(httpRequest.headers().map());

            return HttpHeaders.of(combindedHeaders, ACCEPT_ALL);
        }
    }

    public static final BiPredicate<String, String> ACCEPT_ALL = (x, y) -> true;

    @Override
    protected HttpClient inner() {

        return inner;
    }

}
