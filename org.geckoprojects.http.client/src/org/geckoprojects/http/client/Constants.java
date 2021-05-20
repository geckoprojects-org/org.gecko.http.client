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
package org.geckoprojects.http.client;

public interface Constants {

    public final static String PID_HTTP_CLIENT_CUSTOM = "org.geckoprojects.http.client.custom";

    public final static String PID_HTTP_CLIENT_DEFAULT = "org.geckoprojects.http.client.default";

    public static final String PID_HTTP_CLIENT_AUTH_BASIC = "org.geckoprojects.http.client.auth.basic";

    public static final String PID_HTTP_CLIENT_COOKIE_POLICY_ALL = "org.geckoprojects.http.client.cookie.policy.all";

    public static final String PID_HTTP_CLIENT_COOKIE_POLICY_NONE = "org.geckoprojects.http.client.cookie.policy.none";

    public static final String PID_HTTP_CLIENT_COOKIE_POLICY_ORIGIN = "org.geckoprojects.http.client.cookie.policy.origin";

    public static final String PID_HTTP_CLIENT_COOKIE_STORE_BUNDLE = "org.geckoprojects.http.client.cookie.store.bundle";

    public static final String PID_HTTP_CLIENT_COOKIE_STORE_EMPTY = "org.geckoprojects.http.client.cookie.store.empty";

    public static final String PID_HTTP_CLIENT_COOKIE_STORE_SINGLETON = "org.geckoprojects.http.client.cookie.store.singleton";

    public static final String PID_HTTP_CLIENT_COOKIE_STORE_PROTOTYPE = "org.geckoprojects.http.client.cookie.store.prototype";

    public static final String PID_HTTP_CLIENT_PROXY_SELECTOR_DEFAULT = "org.geckoprojects.http.client.proxy.selector.default";

    public static final String PID_HTTP_CLIENT_PROXY_PROVIDER_DEFAULT = "org.geckoprojects.http.client.proxy.provider.default";

    public static final String PID_HTTP_CLIENT_COOKIE_HANDLER_DEFAULT = "org.geckoprojects.http.client.cookie.handler.default";

    public static final String PID_HTTP_CLIENT_COOKIE_STORE_PERSISTENT = "org.geckoprojects.http.client.cookie.store.persistent";

}
