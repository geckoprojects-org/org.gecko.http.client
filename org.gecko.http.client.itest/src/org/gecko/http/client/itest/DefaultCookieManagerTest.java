package org.gecko.http.client.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.CookieHandler;

import org.gecko.http.client.cookie.DefaultCookieHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.config.WithFactoryConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

@ExtendWith({ ConfigurationExtension.class, ServiceExtension.class, BundleContextExtension.class })
public class DefaultCookieManagerTest {

    @Test
    @WithFactoryConfiguration(factoryPid = DefaultCookieHandler.PID, name = "b", location = "?", properties = {
    })
    @WithFactoryConfiguration(factoryPid = DefaultCookieHandler.PID, name = "c", location = "?", properties = {
    })
    public void testPropertyVersion_HTTP2(
            @InjectService() CookieHandler handler) {

        assertThat(handler).isNotNull();
    }

}
