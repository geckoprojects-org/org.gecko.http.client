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

package org.gecko.http.client.impl.cookie;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.gecko.http.client.Constants;
import org.gecko.http.client.impl.cookie.DefaultCookieHandler.CookieHandlerConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Designate(ocd = CookieHandlerConfig.class, factory = true)
@Component(service = CookieHandler.class, configurationPid = Constants.PID_HTTP_CLIENT_COOKIE_HANDLER_DEFAULT)
public class DefaultCookieHandler extends CookieHandler {

    @ObjectClassDefinition
    public static @interface CookieHandlerConfig {

        @AttributeDefinition()
        String cookiePolicy_target() default "(org.gecko.http.client.cookie.policy.type=ALL)";

        @AttributeDefinition()
        String cookieStore_target() default "(org.gecko.http.client.cookie.store.type=PROTOTYPE)";
    }

    private CookieManager cookieManagerDelegate;

    private CookiePolicy cookiePolicy;

    private CookieStore cookieStore;

    @Activate
    public void activate() {

        cookieManagerDelegate = new CookieManager(cookieStore, cookiePolicy);
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    public void bindCookieStore(CookieStore cookieStore) {

        this.cookieStore = cookieStore;
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    public void bindCookiePolicy(CookiePolicy cookiePolicy) {

        this.cookiePolicy = cookiePolicy;
        setCookiePolicy();
    }

    public void unbindCookiePolicy(CookiePolicy cookiePolicy) {

        this.cookiePolicy = null;

        setCookiePolicy();
    }

    public void updateCookiePolicy(CookiePolicy cookiePolicy) {

        this.cookiePolicy = cookiePolicy;
        setCookiePolicy();
    }

    private void setCookiePolicy() {

        if (cookieManagerDelegate == null) {
            return;
        }
        if (cookiePolicy == null) {
            // TODO: null does nothing inside setCookiePolicy. Back to Default(origin) oder
            // more secure NONE?
            cookieManagerDelegate.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
        } else {
            cookieManagerDelegate.setCookiePolicy(cookiePolicy);
        }
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders)
            throws IOException {

        return cookieManagerDelegate.get(uri, requestHeaders);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

        cookieManagerDelegate.put(uri, responseHeaders);
    }

}
