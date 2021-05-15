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

package org.gecko.http.client.impl.proxy;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;

import org.gecko.http.client.Constants;
import org.gecko.http.client.UriProxyProvider;
import org.gecko.http.client.impl.proxy.DefaultUriProxyProvider.FilterableProxyConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(factory = true, ocd = FilterableProxyConfig.class)
@Component(service = UriProxyProvider.class, configurationPid = Constants.PID_HTTP_CLIENT_PROXY_PROVIDER_DEFAULT)
public class DefaultUriProxyProvider implements UriProxyProvider {

    URI uri;

    private FilterableProxyConfig config;

    private Proxy proxy;

    private PasswordAuthentication pwa = null;

    @ObjectClassDefinition(description = "Configuration of a Proxy")
    @interface FilterableProxyConfig {

        @AttributeDefinition(description = "type of proxy")
        Type proxyType() default Type.HTTP;

        @AttributeDefinition(description = "hostname of the proxy")
        String proxyHostname();

        @AttributeDefinition(description = "proxy port")
        int proxyPort();

        @AttributeDefinition(description = "activates authentication on the proxy. If false, `authBasicUsername` and `.authBasicPassword` will not be used.")
        boolean authBasicEnable() default false;

        @AttributeDefinition(description = "BasicAuth username of the proxy")
        String authBasicUsername();

        @AttributeDefinition(name = ".authBasicPassword", description = "BasicAuth password of the proxy")
        String authBasicPassword() default "";

        @AttributeDefinition(description = "Filter (regex) of the scheme")
        String filterUriScheme() default ".*";

        @AttributeDefinition(description = "Filter (regex) of the host")
        String filterUriHost() default ".*";

        @AttributeDefinition(description = "Filter (regex) of the port")
        String filterUriPort() default ".*";

        int setrvice_ranking();
    }

    @Activate()
    private void activate(FilterableProxyConfig filterableProxyConfig) {

        changeProxy(filterableProxyConfig);
    }

    @Modified()
    private void modified(FilterableProxyConfig filterableProxyConfig) {

        changeProxy(filterableProxyConfig);
    }

    private void changeProxy(FilterableProxyConfig filterableProxyConfig) {

        config = filterableProxyConfig;
        proxy = new Proxy(filterableProxyConfig.proxyType(), new InetSocketAddress(
                filterableProxyConfig.proxyHostname(), filterableProxyConfig.proxyPort()));

        if (config.authBasicEnable()) {
            String pass = config.authBasicPassword() == null ? "" : config.authBasicPassword();
            pwa = new PasswordAuthentication(config.authBasicUsername(), pass.toCharArray());
        } else {
            pwa = null;
        }

    }

    @Override
    public boolean matches(URI uri) {

        if (uri.getHost().matches(config.filterUriHost())) {
            if (Integer.valueOf(uri.getPort()).toString().matches(config.filterUriPort())) {
                if (uri.getScheme().matches(config.filterUriScheme())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Proxy getProxy() {

        return proxy;
    }

    @Override
    public String proxyHostname() {

        return config.proxyHostname();
    }

    @Override
    public int proxyPort() {

        return config.proxyPort();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {

        return pwa;
    }
}
