# org.geckoprojects.http.client

## Links

* [Documentation](https://github.com/geckoprojects-org/org.geckoprojects.http.client)
* [Source Code](https://github.com/geckoprojects-org/org.geckoprojects.http.client) (clone with `scm:git:git@github.com:geckoprojects-org/org.geckoprojects.http.client.git`)

## Coordinates

### Maven

```xml
<dependency>
    <groupId>org.geckoprojects</groupId>
    <artifactId>org.geckoprojects.http.client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### OSGi

```
Bundle Symbolic Name: org.geckoprojects.http.client
Version             : 0.0.1.SNAPSHOT
```

### Feature-Coordinate

```
"bundles": [
   {
    "id": "org.geckoprojects:org.geckoprojects.http.client:0.0.1-SNAPSHOT"
   }
]
```

## Components

### org.geckoprojects.http.client.impl.DefaultHttpClient - *state = enabled, activation = delayed*

#### Description

The default java.net.http.HttpClient.

#### Services - *scope = prototype*

|Interface name |
|--- |
|java.net.http.HttpClient |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|osgi.http.client.name |String |".default" |
|service.description |String |"The default java.net.http.HttpClient." |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.DefaultHttpClient`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.DefaultHttpClient
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.DefaultHttpClient":{
        //# Component properties
        /*
         * Type = String
         * Default = ".default"
         */
         // "osgi.http.client.name": null,

        /*
         * Type = String
         * Default = "The default java.net.http.HttpClient."
         */
         // "service.description": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.auth.BasicAuthenticator - *state = enabled, activation = delayed*

#### Description

Handles ServerAuthentication using the configured credentials, if the property `serverBasicAuthEnable`is true. Also does ProxyAuthentication by Delegating to the UriProxyProvider

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.Authenticator |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|proxyBasicAuthEnable |Boolean |true |
|serverBasicAuthEnable |Boolean |false |
|service.description |String |"Handles ServerAuthentication using the configured credentials, if the property `serverBasicAuthEnable`is true. Also does ProxyAuthentication by Delegating to the UriProxyProvider" |

#### Configuration - *policy = require*

##### Factory Pid: `org.gecko.http.client.auth.basic`

|Attribute |Value |
|--- |--- |
|Id |`proxyBasicAuthEnable` |
|Required |**true** |
|Type |**Boolean** |
|Description |If true, the Proxy authentication is delegated to UriProxyProviders PasswordAuthentication. |
|Default |true |

|Attribute |Value |
|--- |--- |
|Id |`serverBasicAuthEnable` |
|Required |**true** |
|Type |**Boolean** |
|Description |activates authentication on the server. If false, `serverBasicAuthUsername` and `.serverBasicAuthPassword` will not be used. |
|Default |false |

|Attribute |Value |
|--- |--- |
|Id |`serverBasicAuthUsername` |
|Required |**true** |
|Type |**String** |
|Description |the Username to authenticate with on the server. `serverBasicAuthEnable` must be true. |

|Attribute |Value |
|--- |--- |
|Id |`serverBasicAuthPassword` |
|Required |**true** |
|Type |**String** |
|Description |the password to authenticate with on the server. `serverBasicAuthEnable` must be true. |

#### Reference bindings

|Attribute |Value |
|--- |--- |
|name |UriProxyProvider |
|interfaceName |org.geckoprojects.http.client.UriProxyProvider |
|target | |
|cardinality |0..n |
|policy |dynamic |
|policyOption |reluctant |
|scope |bundle |

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.auth.BasicAuthenticator
 * policy:    require
 */
"org.gecko.http.client.auth.basic~FactoryNameChangeIt":{
        //# Component properties
        /*
         * Type = Boolean
         * Default = true
         */
         // "proxyBasicAuthEnable": null,

        /*
         * Type = Boolean
         * Default = false
         */
         // "serverBasicAuthEnable": null,

        /*
         * Type = String
         * Default = "Handles ServerAuthentication using the configured credentials, if the property `serverBasicAuthEnable`is true. Also does ProxyAuthentication by Delegating to the UriProxyProvider"
         */
         // "service.description": null,


        //# Reference bindings
        // "UriProxyProvider.target": "(component.pid=*)",


        //# ObjectClassDefinition - Attributes
        /*
         * Required = true
         * Type = Boolean
         * Description = If true, the Proxy authentication is delegated to UriProxyProviders PasswordAuthentication.
         * Default = true
         */
         "proxyBasicAuthEnable": null,

        /*
         * Required = true
         * Type = Boolean
         * Description = activates authentication on the server. If false, `serverBasicAuthUsername` and `.serverBasicAuthPassword` will not be used.
         * Default = false
         */
         "serverBasicAuthEnable": null,

        /*
         * Required = true
         * Type = String
         * Description = the Username to authenticate with on the server. `serverBasicAuthEnable` must be true.
         */
         "serverBasicAuthUsername": null,

        /*
         * Required = true
         * Type = String
         * Description = the password to authenticate with on the server. `serverBasicAuthEnable` must be true.
         */
         "serverBasicAuthPassword": null
}
```

---

### org.geckoprojects.http.client.impl.cookie.store.PrototypeCookieStore - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = prototype*

|Interface name |
|--- |
|java.net.CookieStore |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.store.type |String |"PROTOTYPE" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.store.PrototypeCookieStore`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.store.PrototypeCookieStore
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.store.PrototypeCookieStore":{
        //# Component properties
        /*
         * Type = String
         * Default = "PROTOTYPE"
         */
         // "org.gecko.http.client.cookie.store.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.proxy.DefaultProxySelector - *state = enabled, activation = immediate*

#### Description

The proxy selector that selects the proxy server to use, filtered by the matches on an given URL.

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.ProxySelector |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|service.description |String |"The proxy selector that selects the proxy server to use, filtered by the matches on an given URL." |

#### Configuration - *policy = optional*

##### Pid: `org.gecko.http.client.proxy.selector.default`

No information available.

#### Reference bindings

|Attribute |Value |
|--- |--- |
|name |FilterableProxy |
|interfaceName |org.geckoprojects.http.client.UriProxyProvider |
|target | |
|cardinality |1..n |
|policy |static |
|policyOption |reluctant |
|scope |bundle |

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.proxy.DefaultProxySelector
 * policy:    optional
 */
"org.gecko.http.client.proxy.selector.default":{
        //# Component properties
        /*
         * Type = String
         * Default = "The proxy selector that selects the proxy server to use, filtered by the matches on an given URL."
         */
         // "service.description": null,


        //# Reference bindings
        // "FilterableProxy.target": "(component.pid=*)"


        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.proxy.DefaultUriProxyProvider - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|org.geckoprojects.http.client.UriProxyProvider |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|proxyType |String |"HTTP" |
|authBasicEnable |Boolean |false |
|authBasicPassword |String |"" |
|filterUriScheme |String |".*" |
|filterUriHost |String |".*" |
|filterUriPort |String |".*" |

#### Configuration - *policy = require*

##### Factory Pid: `org.gecko.http.client.proxy.provider.default`

|Attribute |Value |
|--- |--- |
|Id |`proxyType` |
|Required |**true** |
|Type |**String** |
|Description |type of proxy |
|Default |"HTTP" |
|Value range |"DIRECT", "HTTP", "SOCKS" |

|Attribute |Value |
|--- |--- |
|Id |`proxyHostname` |
|Required |**true** |
|Type |**String** |
|Description |hostname of the proxy |

|Attribute |Value |
|--- |--- |
|Id |`proxyPort` |
|Required |**true** |
|Type |**Integer** |
|Description |proxy port |

|Attribute |Value |
|--- |--- |
|Id |`authBasicEnable` |
|Required |**true** |
|Type |**Boolean** |
|Description |activates authentication on the proxy. If false, `authBasicUsername` and `.authBasicPassword` will not be used. |
|Default |false |

|Attribute |Value |
|--- |--- |
|Id |`authBasicUsername` |
|Required |**true** |
|Type |**String** |
|Description |BasicAuth username of the proxy |

|Attribute |Value |
|--- |--- |
|Id |`authBasicPassword` |
|Required |**true** |
|Type |**String** |
|Description |BasicAuth password of the proxy |
|Default |"" |

|Attribute |Value |
|--- |--- |
|Id |`filterUriScheme` |
|Required |**true** |
|Type |**String** |
|Description |Filter (regex) of the scheme |
|Default |".*" |

|Attribute |Value |
|--- |--- |
|Id |`filterUriHost` |
|Required |**true** |
|Type |**String** |
|Description |Filter (regex) of the host |
|Default |".*" |

|Attribute |Value |
|--- |--- |
|Id |`filterUriPort` |
|Required |**true** |
|Type |**String** |
|Description |Filter (regex) of the port |
|Default |".*" |

|Attribute |Value |
|--- |--- |
|Id |`setrvice.ranking` |
|Required |**true** |
|Type |**Integer** |

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.proxy.DefaultUriProxyProvider
 * policy:    require
 */
"org.gecko.http.client.proxy.provider.default~FactoryNameChangeIt":{
        //# Component properties
        /*
         * Type = String
         * Default = "HTTP"
         */
         // "proxyType": null,

        /*
         * Type = Boolean
         * Default = false
         */
         // "authBasicEnable": null,

        /*
         * Type = String
         * Default = ""
         */
         // "authBasicPassword": null,

        /*
         * Type = String
         * Default = ".*"
         */
         // "filterUriScheme": null,

        /*
         * Type = String
         * Default = ".*"
         */
         // "filterUriHost": null,

        /*
         * Type = String
         * Default = ".*"
         */
         // "filterUriPort": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        /*
         * Required = true
         * Type = String
         * Description = type of proxy
         * Default = "HTTP"
         * Value restriction = "DIRECT", "HTTP", "SOCKS"
         */
         "proxyType": null,

        /*
         * Required = true
         * Type = String
         * Description = hostname of the proxy
         */
         "proxyHostname": null,

        /*
         * Required = true
         * Type = Integer
         * Description = proxy port
         */
         "proxyPort": null,

        /*
         * Required = true
         * Type = Boolean
         * Description = activates authentication on the proxy. If false, `authBasicUsername` and `.authBasicPassword` will not be used.
         * Default = false
         */
         "authBasicEnable": null,

        /*
         * Required = true
         * Type = String
         * Description = BasicAuth username of the proxy
         */
         "authBasicUsername": null,

        /*
         * Required = true
         * Type = String
         * Description = BasicAuth password of the proxy
         * Default = ""
         */
         "authBasicPassword": null,

        /*
         * Required = true
         * Type = String
         * Description = Filter (regex) of the scheme
         * Default = ".*"
         */
         "filterUriScheme": null,

        /*
         * Required = true
         * Type = String
         * Description = Filter (regex) of the host
         * Default = ".*"
         */
         "filterUriHost": null,

        /*
         * Required = true
         * Type = String
         * Description = Filter (regex) of the port
         * Default = ".*"
         */
         "filterUriPort": null,

        /*
         * Required = true
         * Type = Integer
         */
         "setrvice.ranking": null
}
```

---

### org.geckoprojects.http.client.impl.CustomHttpClient - *state = enabled, activation = delayed*

#### Description

The Custom-Configurable java.net.http.HttpClient.

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.http.HttpClient |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|followRedirects |String |"NEVER" |
|timeoutMs |Long |-1 |
|version |String |"HTTP_2" |
|service.description |String |"The Custom-Configurable java.net.http.HttpClient." |

#### Configuration - *policy = require*

##### Factory Pid: `org.gecko.http.client.custom`

|Attribute |Value |
|--- |--- |
|Id |`followRedirects` |
|Required |**true** |
|Type |**String** |
|Default |"NEVER" |
|Value range |"NEVER", "ALWAYS", "NORMAL" |

|Attribute |Value |
|--- |--- |
|Id |`headers` |
|Required |**true** |
|Type |**String[]** |

|Attribute |Value |
|--- |--- |
|Id |`name` |
|Required |**true** |
|Type |**String** |

|Attribute |Value |
|--- |--- |
|Id |`timeoutMs` |
|Required |**true** |
|Type |**Long** |
|Default |-1 |

|Attribute |Value |
|--- |--- |
|Id |`version` |
|Required |**true** |
|Type |**String** |
|Default |"HTTP_2" |
|Value range |"HTTP_1_1", "HTTP_2" |

#### Reference bindings

|Attribute |Value |
|--- |--- |
|name |Authenticator |
|interfaceName |java.net.Authenticator |
|target | |
|cardinality |0..1 |
|policy |static |
|policyOption |reluctant |
|scope |bundle ||Attribute |Value |
|--- |--- |
|name |CookieHandler |
|interfaceName |java.net.CookieHandler |
|target | |
|cardinality |0..1 |
|policy |static |
|policyOption |reluctant |
|scope |bundle ||Attribute |Value |
|--- |--- |
|name |ProxySelector |
|interfaceName |java.net.ProxySelector |
|target | |
|cardinality |0..1 |
|policy |static |
|policyOption |reluctant |
|scope |bundle |

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.CustomHttpClient
 * policy:    require
 */
"org.gecko.http.client.custom~FactoryNameChangeIt":{
        //# Component properties
        /*
         * Type = String
         * Default = "NEVER"
         */
         // "followRedirects": null,

        /*
         * Type = Long
         * Default = -1
         */
         // "timeoutMs": null,

        /*
         * Type = String
         * Default = "HTTP_2"
         */
         // "version": null,

        /*
         * Type = String
         * Default = "The Custom-Configurable java.net.http.HttpClient."
         */
         // "service.description": null,


        //# Reference bindings
        // "Authenticator.target": "(component.pid=*)",
        // "CookieHandler.target": "(component.pid=*)",
        // "ProxySelector.target": "(component.pid=*)",


        //# ObjectClassDefinition - Attributes
        /*
         * Required = true
         * Type = String
         * Default = "NEVER"
         * Value restriction = "NEVER", "ALWAYS", "NORMAL"
         */
         "followRedirects": null,

        /*
         * Required = true
         * Type = String[]
         */
         "headers": null,

        /*
         * Required = true
         * Type = String
         */
         "name": null,

        /*
         * Required = true
         * Type = Long
         * Default = -1
         */
         "timeoutMs": null,

        /*
         * Required = true
         * Type = String
         * Default = "HTTP_2"
         * Value restriction = "HTTP_1_1", "HTTP_2"
         */
         "version": null
}
```

---

### org.geckoprojects.http.client.impl.cookie.policy.AcceptOriginServerCookiePolicy - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.CookiePolicy |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.policy.type |String |"ORIGINAL_SERVER" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.policy.AcceptOriginServerCookiePolicy`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.policy.AcceptOriginServerCookiePolicy
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.policy.AcceptOriginServerCookiePolicy":{
        //# Component properties
        /*
         * Type = String
         * Default = "ORIGINAL_SERVER"
         */
         // "org.gecko.http.client.cookie.policy.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.cookie.store.BundleCookieStore - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = bundle*

|Interface name |
|--- |
|java.net.CookieStore |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.store.type |String |"BUNDLE" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.store.BundleCookieStore`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.store.BundleCookieStore
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.store.BundleCookieStore":{
        //# Component properties
        /*
         * Type = String
         * Default = "BUNDLE"
         */
         // "org.gecko.http.client.cookie.store.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.cookie.store.EmptyCookieStore - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.CookieStore |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.store.type |String |"EMPTY" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.store.EmptyCookieStore`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.store.EmptyCookieStore
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.store.EmptyCookieStore":{
        //# Component properties
        /*
         * Type = String
         * Default = "EMPTY"
         */
         // "org.gecko.http.client.cookie.store.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.cookie.DefaultCookieHandler - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.CookieHandler |

#### Properties

No properties.

#### Configuration - *policy = require*

##### Factory Pid: `org.gecko.http.client.cookie.handler.default`

|Attribute |Value |
|--- |--- |
|Id |`cookiePolicy.target` |
|Required |**true** |
|Type |**String** |
|Default |"(org.gecko.http.client.cookie.policy.type=ALL)" |

|Attribute |Value |
|--- |--- |
|Id |`cookieStore.target` |
|Required |**true** |
|Type |**String** |
|Default |"(org.gecko.http.client.cookie.store.type=PROTOTYPE)" |

#### Reference bindings

|Attribute |Value |
|--- |--- |
|name |CookiePolicy |
|interfaceName |java.net.CookiePolicy |
|target | |
|cardinality |0..1 |
|policy |static |
|policyOption |reluctant |
|scope |bundle ||Attribute |Value |
|--- |--- |
|name |CookieStore |
|interfaceName |java.net.CookieStore |
|target | |
|cardinality |0..1 |
|policy |static |
|policyOption |reluctant |
|scope |bundle |

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.DefaultCookieHandler
 * policy:    require
 */
"org.gecko.http.client.cookie.handler.default~FactoryNameChangeIt":{
        //# Component properties
        // none

        //# Reference bindings
        // "CookiePolicy.target": "(component.pid=*)",
        // "CookieStore.target": "(component.pid=*)",


        //# ObjectClassDefinition - Attributes
        /*
         * Required = true
         * Type = String
         * Default = "(org.gecko.http.client.cookie.policy.type=ALL)"
         */
         "cookiePolicy.target": null,

        /*
         * Required = true
         * Type = String
         * Default = "(org.gecko.http.client.cookie.store.type=PROTOTYPE)"
         */
         "cookieStore.target": null
}
```

---

### org.geckoprojects.http.client.impl.cookie.policy.AcceptAllCookiePolicy - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.CookiePolicy |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.policy.type |String |"ALL" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.policy.AcceptAllCookiePolicy`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.policy.AcceptAllCookiePolicy
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.policy.AcceptAllCookiePolicy":{
        //# Component properties
        /*
         * Type = String
         * Default = "ALL"
         */
         // "org.gecko.http.client.cookie.policy.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.cookie.policy.AcceptNoneCookiePolicy - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.CookiePolicy |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.policy.type |String |"NONE" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.policy.AcceptNoneCookiePolicy`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.policy.AcceptNoneCookiePolicy
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.policy.AcceptNoneCookiePolicy":{
        //# Component properties
        /*
         * Type = String
         * Default = "NONE"
         */
         // "org.gecko.http.client.cookie.policy.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

---

### org.geckoprojects.http.client.impl.cookie.store.SingletonCookieStore - *state = enabled, activation = delayed*

#### Description

#### Services - *scope = singleton*

|Interface name |
|--- |
|java.net.CookieStore |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|org.gecko.http.client.cookie.store.type |String |"SINGLETON" |

#### Configuration - *policy = optional*

##### Pid: `org.geckoprojects.http.client.impl.cookie.store.SingletonCookieStore`

No information available.

#### Reference bindings

No bindings.

#### OSGi-Configurator


```
/*
 * Component: org.geckoprojects.http.client.impl.cookie.store.SingletonCookieStore
 * policy:    optional
 */
"org.geckoprojects.http.client.impl.cookie.store.SingletonCookieStore":{
        //# Component properties
        /*
         * Type = String
         * Default = "SINGLETON"
         */
         // "org.gecko.http.client.cookie.store.type": null,


        //# Reference bindings
        // none

        //# ObjectClassDefinition - Attributes
        // (No PidOcd available.)
}
```

## Developers

* **Juergen Albert** (jalbert) / [j.albert@data-in-motion.biz](mailto:j.albert@data-in-motion.biz) @ [Data In Motion](https://www.datainmotion.de) - *architect*, *developer*
* **Mark Hoffmann** (mhoffmann) / [m.hoffmann@data-in-motion.biz](mailto:m.hoffmann@data-in-motion.biz) @ [Data In Motion](https://www.datainmotion.de) - *developer*, *architect*

## Licenses

**Apache License 2.0**

## Copyright

Data In Motion Consuling GmbH - All rights reserved

---
Data In Motion Consuling GmbH - [info@data-in-motion.biz](mailto:info@data-in-motion.biz)