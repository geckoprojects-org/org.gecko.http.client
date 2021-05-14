package org.gecko.http.client;

import java.net.http.HttpClient;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

@HttpClientName(value = ".default")
@Component(service = HttpClient.class, scope = ServiceScope.PROTOTYPE)
public class DefaultHttpClient extends AbstractHttpClient {

    private HttpClient inner = null;

    @Reference()
    public void bindHttpClientBuilder(HttpClient.Builder builder) {

        inner = builder.build();
    }

    public void unbindHttpClientBuilder(HttpClient.Builder builder) {

        inner = null;
    }

    @Override
    protected HttpClient inner() {

        return inner;
    }

}
