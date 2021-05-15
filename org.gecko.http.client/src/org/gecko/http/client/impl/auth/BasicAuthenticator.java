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

package org.gecko.http.client.impl.auth;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.gecko.http.client.Constants;
import org.gecko.http.client.UriProxyProvider;
import org.gecko.http.client.impl.auth.BasicAuthenticator.BasicAuthenticatorConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = BasicAuthenticatorConfig.class, factory = true)
@ServiceDescription(BasicAuthenticator.DESCRIPTION)
@Component(service = Authenticator.class, configurationPid = Constants.PID_HTTP_CLIENT_AUTH_BASIC)
public class BasicAuthenticator extends Authenticator {

    public static final String DESCRIPTION = "Handles ServerAuthentication using the configured credentials, if the property `serverBasicAuthEnable`is true. Also does ProxyAuthentication by Delegating to the UriProxyProvider";

    private List<UriProxyProvider> proxys = new ArrayList<>();

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addUriProxyProvider(UriProxyProvider uriProxyProvider) {

        proxys.add(uriProxyProvider);
    }

    public void removeUriProxyProvider(UriProxyProvider uriProxyProvider) {

        proxys.remove(uriProxyProvider);
    }

    @ObjectClassDefinition(description = DESCRIPTION)
    static @interface BasicAuthenticatorConfig {

        @AttributeDefinition(description = "If true, the Proxy authentication is delegated to UriProxyProviders PasswordAuthentication.")
        boolean proxyBasicAuthEnable() default true;

        @AttributeDefinition(description = "activates authentication on the server. If false, `serverBasicAuthUsername` and `.serverBasicAuthPassword` will not be used.")
        boolean serverBasicAuthEnable() default false;

        @AttributeDefinition(description = "the Username to authenticate with on the server. `serverBasicAuthEnable` must be true.")
        String serverBasicAuthUsername();

        @AttributeDefinition(name = ".serverBasicAuthPassword", description = "the password to authenticate with on the server. `serverBasicAuthEnable` must be true.")
        String serverBasicAuthPassword();
    }

    private BasicAuthenticatorConfig config;

    private PasswordAuthentication serverPassAuthentication;

    @Activate
    @Modified
    public void setup(BasicAuthenticatorConfig config) {

        this.config = config;
        serverPassAuthentication = config
                .serverBasicAuthEnable()
                        ? new PasswordAuthentication(config.serverBasicAuthUsername(),
                                config.serverBasicAuthPassword() == null ? null
                                        : config.serverBasicAuthPassword().toCharArray())
                        : null;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {

        System.out.println("scheme: " + getRequestingScheme());
        switch (getRequestorType()) {
            case PROXY:
                if (config.proxyBasicAuthEnable()) {
                    for (UriProxyProvider proxy : proxys) {
                        if (Objects.equals(getRequestingHost(), proxy.proxyHostname())
                                && Objects.equals(getRequestingPort(), proxy.proxyPort())) {
                            return proxy.getPasswordAuthentication();
                        }
                    }
                }
                break;
            case SERVER:
                if (config.serverBasicAuthEnable()) {
                    return serverPassAuthentication;
                }
                break;
            default:
                break;
        }
        return null;
    }
}