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

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.gecko.http.client.UriProxyProvider;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.propertytypes.ServiceDescription;

@ServiceDescription("The proxy selector that selects the proxy server to use, filtered by the matches on an given URL.")

@Component(service = ProxySelector.class, immediate = true, configurationPid = org.gecko.http.client.Constants.PID_HTTP_CLIENT_PROXY_SELECTOR_DEFAULT)
public class DefaultProxySelector extends ProxySelector {

    private final List<ComponentServiceObjects<UriProxyProvider>> filterableProxies = new ArrayList<>();

    @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE)
    void bindFilterableProxy(final ComponentServiceObjects<UriProxyProvider> filterableProxy) {

        filterableProxies.add(filterableProxy);
        sortByServiceRanking();
    }

    void unbindFilterableProxy(final ComponentServiceObjects<UriProxyProvider> filterableProxy) {

        filterableProxies.remove(filterableProxy);
        sortByServiceRanking();
    }

    void unpdatedFilterableProxy(final ComponentServiceObjects<UriProxyProvider> filterableProxy) {

        sortByServiceRanking();
    }

    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException e) {

    }

    @Override
    public synchronized List<Proxy> select(final URI uri) {

        final Proxy proxy = filterableProxies.stream()//
                .map(so -> so.getService())//
                .filter(Objects::nonNull)//
                .filter(pp -> pp.matches(uri))//
                .findFirst()
                .map(UriProxyProvider::getProxy)//
                .orElse(Proxy.NO_PROXY);

        return List.of(proxy);
    }

    private void sortByServiceRanking() {

        filterableProxies.sort((so1, so2) -> {
            final Integer sr1 = (Integer) so1.getServiceReference()
                    .getProperty(Constants.SERVICE_RANKING);
            final Integer sr2 = (Integer) so2.getServiceReference()
                    .getProperty(Constants.SERVICE_RANKING);

            return sr1.compareTo(sr2);
        });
    }

}