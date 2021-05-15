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

package org.gecko.http.client.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.osgi.framework.ServiceReference;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.dictionary.Dictionaries;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith({ ConfigurationExtension.class, ServiceExtension.class, BundleContextExtension.class })
@Testable
@Nested
public class DefaultHttpClientTest {

    @Test
    public void test(@InjectService(timeout = 1000) HttpClient client) {

        assertThat(client).isNotNull();

        assertThat(client.authenticator()).isNotPresent();
        assertThat(client.connectTimeout()).isNotPresent();
        assertThat(client.cookieHandler()).isNotPresent();
        assertThat(client.followRedirects()).isEqualTo(Redirect.NEVER);
        assertThat(client.proxy()).isNotPresent();
    }

    @Test
    public void defaultHttpClientBuilderPropsTest(
            @InjectService(timeout = 1000, cardinality = 1) ServiceAware<HttpClient> sa) {

        assertThat(sa.getServices()).hasSize(1);

        ServiceReference<HttpClient> sr = sa.getServiceReference();
        assertNotNull(sr);
        assertNotNull(sr.getProperties());
        assertThat(Dictionaries.asMap(sa.getServiceReference().getProperties()))
                .containsEntry("osgi.http.client.name", ".default");
    }

    @Test
    public void defaultHttpClientBuilderOverProps(
            @InjectService(timeout = 1000, filter = "(osgi.http.client.name=.default)", cardinality = 1) HttpClient httpClient) {

        assertNotNull(httpClient);
    }

}
