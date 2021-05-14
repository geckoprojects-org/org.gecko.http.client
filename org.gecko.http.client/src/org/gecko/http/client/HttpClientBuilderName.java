package org.gecko.http.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.osgi.service.component.annotations.ComponentPropertyType;

/**
 * Component Property Type for the http.client.builder service property.
 * <p>
 * This annotation can be used on a HttpClientBuilder service
 * 
 * @see "Component Property Types"
 * @author $Id$
 */
@ComponentPropertyType
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface HttpClientBuilderName {

    /**
     * Prefix for the property name. This value is prepended to each property name.
     */
    String PREFIX_ = "osgi.";

    String value() ;
}
