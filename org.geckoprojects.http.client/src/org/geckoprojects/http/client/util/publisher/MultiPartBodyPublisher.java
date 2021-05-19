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

package org.geckoprojects.http.client.util.publisher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.geckoprojects.http.client.util.Constants;

/**
 * The Class MultiPartBodyPublisher.
 */
public class MultiPartBodyPublisher {

    /**
     * The Class PartsSpecification.
     */
    static class PartsSpecification {

        /**
         * The Enum TYPE.
         */
        public enum TYPE {

            /** The string. */
            STRING,
            /** The file. */
            FILE,
            /** The stream. */
            STREAM,
            /** The final boundary. */
            FINAL_BOUNDARY
        }

        /** The type. */
        PartsSpecification.TYPE type;

        /** The name. */
        String name;

        /** The value. */
        String value;

        /** The path. */
        Path path;

        /** The stream. */
        InputStream stream;

        /** The filename. */
        String filename;

        /** The content type. */
        String contentType;

    }

    /** The Constant DD. */
    private static final String DD = "--";

    /** The Constant RN. */
    private static final String RN = "\r\n";

    /** The parts specification list. */
    private final List<PartsSpecification> partsSpecificationList = new ArrayList<>();

    /** The boundary. */
    private final String boundary = UUID.randomUUID().toString();

    /**
     * Adds the final boundary part.
     */
    private void addFinalBoundaryPart() {

        final PartsSpecification partsSpecification = new PartsSpecification();

        partsSpecification.type = PartsSpecification.TYPE.FINAL_BOUNDARY;
        partsSpecification.value = DD + boundary + DD;

        partsSpecificationList.add(partsSpecification);
    }

    /**
     * Adds the part.
     *
     * @param name        the name
     * @param value       the value
     * @param filename    the filename
     * @param contentType the content type
     * @return the multi part body publisher
     */
    public MultiPartBodyPublisher addPart(String name, InputStream value, String filename,
            String contentType) {

        final PartsSpecification partsSpecification = new PartsSpecification();

        partsSpecification.type = PartsSpecification.TYPE.STREAM;
        partsSpecification.name = name;
        partsSpecification.stream = value;
        partsSpecification.filename = filename;
        partsSpecification.contentType = contentType;

        partsSpecificationList.add(partsSpecification);

        return this;
    }

    /**
     * Adds the part.
     *
     * @param name  the name
     * @param value the value
     * @return the multi part body publisher
     */
    public MultiPartBodyPublisher addPart(String name, Path value) {

        final PartsSpecification partsSpecification = new PartsSpecification();

        partsSpecification.type = PartsSpecification.TYPE.FILE;
        partsSpecification.name = name;
        partsSpecification.path = value;

        partsSpecificationList.add(partsSpecification);

        return this;
    }

    /**
     * Adds the part.
     *
     * @param name  the name
     * @param value the value
     * @return the multi part body publisher
     */
    public MultiPartBodyPublisher addPart(String name, String value) {

        return addPart(name, value, null);
    }

    /**
     * Adds the part.
     *
     * @param name        the name
     * @param value       the value
     * @param contentType the content type
     * @return the multi part body publisher
     */
    public MultiPartBodyPublisher addPart(String name, String value, String contentType) {

        final PartsSpecification partsSpecification = new PartsSpecification();

        partsSpecification.type = PartsSpecification.TYPE.STRING;
        partsSpecification.name = name;
        partsSpecification.value = value;
        partsSpecification.contentType = contentType;

        partsSpecificationList.add(partsSpecification);

        return this;
    }

    /**
     * Builds the.
     *
     * @return the http request. body publisher
     */
    public HttpRequest.BodyPublisher build() {

        if (partsSpecificationList.size() == 0) {
            throw new IllegalStateException(
                    "Must have at least one part to build multipart message.");
        }

        addFinalBoundaryPart();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (final PartsSpecification partsSpecification : partsSpecificationList) {
            byte[] data;

            try {
                data = computeNext(partsSpecification);
                baos.write(data);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        final byte[] data = baos.toByteArray();

        return HttpRequest.BodyPublishers.ofByteArray(data);
    }

    /**
     * Compute next.
     *
     * @param partsSpecification the parts specification
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private byte[] computeNext(PartsSpecification partsSpecification) throws IOException {

        if (PartsSpecification.TYPE.STRING.equals(partsSpecification.type)) {
            final String contentType = partsSpecification.contentType == null
                    ? Constants.MediaType.TEXT_PLAIN
                    : partsSpecification.contentType;
            final String part = DD + boundary + RN + Constants.Header.CONTENT_DISPOSITION
                    + ": form-data; name=" + partsSpecification.name + RN
                    + Constants.Header.CONTENT_TYPE + ": " + contentType + "; charset=UTF-8" + RN
                    + RN + partsSpecification.value + RN;

            return part.getBytes(StandardCharsets.UTF_8);
        }

        if (PartsSpecification.TYPE.FINAL_BOUNDARY.equals(partsSpecification.type)) {
            return partsSpecification.value.getBytes(StandardCharsets.UTF_8);
        }

        String filename;
        String contentType;

        byte[] dataPart;

        if (PartsSpecification.TYPE.FILE.equals(partsSpecification.type)) {
            final Path path = partsSpecification.path;

            filename = path.getFileName().toString();
            contentType = Files.probeContentType(path);

            if (contentType == null) {
                contentType = Constants.MediaType.OCTET_STREAM;
            }

            dataPart = dataPart(Files.newInputStream(path));
        } else {
            filename = partsSpecification.filename;
            contentType = partsSpecification.contentType;

            if (contentType == null) {
                contentType = Constants.MediaType.OCTET_STREAM;
            }

            dataPart = dataPart(partsSpecification.stream);
        }

        final String partHeader = DD + boundary + RN + Constants.Header.CONTENT_DISPOSITION
                + ": form-data; name=" + partsSpecification.name + "; filename=" + filename + RN
                + Constants.Header.CONTENT_TYPE + ": " + contentType + RN + RN;

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(partHeader.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(dataPart);
        byteArrayOutputStream.write(RN.getBytes(StandardCharsets.UTF_8));

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Data part.
     *
     * @param inputStream the input stream
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private byte[] dataPart(InputStream inputStream) throws IOException {

        final byte[] buf = inputStream.readAllBytes();

        inputStream.close();

        if (buf.length > 0) {
            return buf;
        } else {
            return RN.getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * Gets the boundary.
     *
     * @return the boundary
     */
    public String getBoundary() {

        return boundary;
    }
}
