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
import java.net.http.HttpClient.Builder;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;

import org.gecko.http.client.CustomHttpClientBuilder.HttpClientConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ServiceDescription(CustomHttpClientBuilder.DESCRIPTION)
@Designate(ocd = HttpClientConfig.class, factory = true)
@Component(service = Builder.class, configurationPid = CustomHttpClientBuilder.PID)
public class CustomHttpClientBuilder extends AbstractHttpClientBuilder {

    public static final String PID = "org.gecko.http.client.CustomHttpClientBuilder";

    protected final static String DESCRIPTION = "CustomHttpClientBuilder is able to handle"
            + " ProxySelector and Authenticators , CookieHandler and ServiceReferences";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private CookieHandler cookieHandler;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private Authenticator authenticator;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private ProxySelector proxySelector;

    @ObjectClassDefinition
    public @interface HttpClientConfig {

        Redirect followRedirects() default Redirect.NEVER;

        long timeoutMs() default -1;

        Version version() default Version.HTTP_2;

        @AttributeDefinition( name= "osgi.http.client.name",required = true)
        String name();
    }

    @Activate
    public void activate(HttpClientConfig cfg) {

        if (authenticator != null) {
            builder.authenticator(authenticator);
            builder.followRedirects(Redirect.NORMAL);
        }

        if (proxySelector != null) {
            builder.proxy(proxySelector);
        }

        if (cfg.timeoutMs() > 0) {
            builder.connectTimeout(Duration.ofMillis(cfg.timeoutMs()));
        }

        if (cfg.followRedirects() != null) {
            builder.followRedirects(cfg.followRedirects());
        }

        if (cfg.followRedirects() != null) {
            builder.version(cfg.version());
        }

        if (cookieHandler != null) {
            builder.cookieHandler(cookieHandler);
        }
    }

}
