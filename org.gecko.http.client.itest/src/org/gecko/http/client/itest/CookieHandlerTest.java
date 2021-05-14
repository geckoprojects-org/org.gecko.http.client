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

package org.gecko.http.client.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.CookieStore;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith({ ConfigurationExtension.class, ServiceExtension.class, BundleContextExtension.class })
@Testable
@Nested
public class CookieHandlerTest {

    @Test
    @WithFactoryConfiguration(factoryPid = "org.gecko.http.client.cookie.PersistentCookieStore", name = "b", location = "?")
    public void test(
            @InjectService(filter = "(java.net.cookie.store.type=PERSITENT)") CookieStore cookieStore) {

        assertThat(cookieStore).isNotNull();
    }

   

}
