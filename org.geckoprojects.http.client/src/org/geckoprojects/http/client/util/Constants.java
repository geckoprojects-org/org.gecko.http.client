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

package org.geckoprojects.http.client.util;

/**
 * The Interface Constants.
 */
public interface Constants {

    /**
     * The Interface Header.
     */
    public interface Header {

        /** The accept. */
        public static String ACCEPT = "Accept";

        /** The content disposition. */
        public static String CONTENT_DISPOSITION = "Content-Disposition";

        /** The content type. */
        public static String CONTENT_TYPE = "Content-Type";
    }

    /**
     * The Interface MediaType.
     */
    public interface MediaType {

        /** The any. */
        public static String ANY = "*/*";

        /** The docx. */
        public static String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

        /** The form url encoded. */
        public static String FORM_URL_ENCODED = "application/x-www-form-urlencoded";

        /** The json. */
        public static String JSON = "application/json";

        /** The multipart form data. */
        public static String MULTIPART_FORM_DATA = "multipart/form-data";

        /** The octet stream. */
        public static String OCTET_STREAM = "application/octet-stream";

        /** The text plain. */
        public static String TEXT_PLAIN = "text/plain";
    }

    /**
     * The Interface MultiPart.
     */
    public interface MultiPart {

        /** The boundary format. */
        public static String BOUNDARY_FORMAT = "; boundary=%s";

        /** The data part. */
        public static String DATA_PART = "data";

        /** The file part. */
        public static String FILE_PART = "file";
    }
}