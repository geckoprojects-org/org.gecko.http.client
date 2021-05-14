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

package org.gecko.http.client.cookie;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.gecko.http.client.cookie.PersistentCookieStore.PersistentCookieStoreConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@CookieStoreType("PERSITENT")
@Designate(ocd = PersistentCookieStoreConfig.class, factory = true)
@Component(service = CookieStore.class)
public class PersistentCookieStore extends AbstractCookieStore {

    enum PersistType {
        CACHE, WRITE_ON_ADD, WRITE_ON_DEACTIVATE
    }

    @ObjectClassDefinition
    @interface PersistentCookieStoreConfig {

        @AttributeDefinition(description = "Save all Cookies everytime a Cookie is added.")
        PersistType type() default PersistType.WRITE_ON_ADD;

        @AttributeDefinition(description = "Path where the data should be stored")
        String location() default ".";
    }

    private PersistentCookieStoreConfig config;

    @Activate
    public void activate(PersistentCookieStoreConfig config) {

        this.config = config;

        if (!PersistType.CACHE.equals(config.type())) {
            readFromPerstistance();
        }
    }

    @Deactivate
    public void deactivate() {

        if (PersistType.WRITE_ON_DEACTIVATE.equals(config.type())) {
            persist();
        }

    }

    @Override
    public void add(URI uri, HttpCookie cookie) {

        super.add(uri, cookie);
        if (PersistType.WRITE_ON_ADD.equals(config.type())) {
            persist();
        }
    }

    // not the best way to write. bus uses jdk CookieStore impl
    void persist() {

        cleanPersistence();
        List<URI> uri_list = getURIs();

        for (URI uri : uri_list) {
            List<HttpCookie> list = get(uri);
            for (HttpCookie cookie : list) {
                if (cookie.hasExpired()) {
                    continue;
                }

                URI normalized_uri = toCookieUri(uri, cookie);
                addToPersistence(normalized_uri, cookie);
            }
        }
    }

    private void readFromPerstistance() {

        // TODO Auto-generated method stub
    }

    private void cleanPersistence() {

        // TODO Auto-generated method stub
    }

    private void addToPersistence(URI normalized_uri, HttpCookie cookie) {

        // TODO Auto-generated method stub
    }

    private static URI toCookieUri(URI uri, HttpCookie cookie) {

        URI cookieUri = uri;
        if (cookie.getDomain() != null) {
            String domain = cookie.getDomain();
            if (domain.charAt(0) == '.') {
                domain = domain.substring(1);
            }
            try {
                cookieUri = new URI(Optional.ofNullable(uri.getScheme()).orElse("http"), domain,
                        Optional.ofNullable(cookie.getPath()).orElse("/"), null);
            } catch (URISyntaxException e) {
                System.err.println(e.getMessage());
            }
        }
        return cookieUri;
    }

}