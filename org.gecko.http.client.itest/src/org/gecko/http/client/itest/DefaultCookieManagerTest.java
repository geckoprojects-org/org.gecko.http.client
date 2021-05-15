package org.gecko.http.client.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.CookieHandler;

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
    @WithFactoryConfiguration(factoryPid = org.gecko.http.client.Constants.PID_HTTP_CLIENT_COOKIE_HANDLER_DEFAULT, name = "b", location = "?", properties = {})
    public void testCookiehandler_exists(@InjectService(timeout = 1000) CookieHandler handler) {

        assertThat(handler).isNotNull();
    }

}
