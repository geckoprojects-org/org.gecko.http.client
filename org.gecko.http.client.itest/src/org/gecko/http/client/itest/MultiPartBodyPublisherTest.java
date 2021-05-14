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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublisher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import org.gecko.http.client.publisher.MultiPartBodyPublisher;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.annotation.Testable;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith({ ConfigurationExtension.class, ServiceExtension.class, BundleContextExtension.class })
@Testable
@Nested
public class MultiPartBodyPublisherTest {

    protected String data;

    @Test
    public void test(@InjectService HttpClient.Builder builder, @TempDir Path tempDir)
            throws IOException {

        Path p = tempDir.resolve("test.test");
        InputStream is = new ByteArrayInputStream("InputStreamValue".getBytes());

        Files.write(p, "path".getBytes());
        MultiPartBodyPublisher publisher = new MultiPartBodyPublisher();
        publisher.addPart("pathName", p);
        publisher.addPart("valueName", "value");
        publisher.addPart("valueCTName", "valueCTName", "valueCT");
        publisher.addPart("isName", is, "isFileName", "isCT");
        BodyPublisher bPublisher = publisher.build();

        Subscriber<java.nio.ByteBuffer> s = new Subscriber<>() {

            @Override
            public void onSubscribe(Subscription subscription) {

                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(java.nio.ByteBuffer item) {

                MultiPartBodyPublisherTest.this.data = new String(item.array());

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        bPublisher.subscribe(s);
        assertThat(data).contains(
                "Content-Disposition: form-data; name=pathName; filename=test.test",
                "Content-Type: application/octet-stream", "path",
                "Content-Disposition: form-data; name=valueName",
                "Content-Type: text/plain; charset=UTF-8", "value",
                "Content-Disposition: form-data; name=valueCTName",
                "Content-Type: valueCT; charset=UTF-8", "valueCTName",
                "Content-Disposition: form-data; name=isName; filename=isFileName",
                "Content-Type: isCT", "InputStreamValue");
    }

}
