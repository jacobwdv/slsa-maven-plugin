/*
 * Copyright 2023 International Business Machines Corp..
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.slsa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.slsa.test.CommonTestUtils;
import jakarta.json.JsonObject;

public class BuildMetadataTest {

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_constructor() {
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        assertEquals(JsonObject.EMPTY_JSON_OBJECT, buildMetadataJson, "BuildMetadata was not expected to have any entries, but it did.");
    }

    @Test
    public void test_invocationId_empty() {
        final String invocationId = "";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.invocationId(invocationId);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        assertEquals(JsonObject.EMPTY_JSON_OBJECT, buildMetadataJson, "BuildMetadata was not expected to have any entries, but it did.");
    }

    @Test
    public void test_invocationId() {
        final String invocationId = String.valueOf(System.currentTimeMillis());
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.invocationId(invocationId);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_INVOCATION_ID, invocationId);
    }

    @Test
    public void test_startedOn_empty() {
        final String startedOn = "";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.startedOn(startedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        assertEquals(JsonObject.EMPTY_JSON_OBJECT, buildMetadataJson, "BuildMetadata was not expected to have any entries, but it did.");
    }

    @Test
    public void test_startedOn_notADate() {
        final String startedOn = "now";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.startedOn(startedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_STARTED_ON, startedOn);
    }

    @Test
    public void test_startedOn_isoADate() {
        final String startedOn = "2023-01-01T00:00:00.000Z";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.startedOn(startedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_STARTED_ON, startedOn);
    }

    @Test
    public void test_finishedOn_empty() {
        final String finishedOn = "";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.finishedOn(finishedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        assertEquals(JsonObject.EMPTY_JSON_OBJECT, buildMetadataJson, "BuildMetadata was not expected to have any entries, but it did.");
    }

    @Test
    public void test_finishedOn_notADate() {
        final String finishedOn = "now";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.finishedOn(finishedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_FINISHED_ON, finishedOn);
    }

    @Test
    public void test_finishedOn_isoADate() {
        final String finishedOn = "2023-01-01T12:23:34.456Z";
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.finishedOn(finishedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_FINISHED_ON, finishedOn);
    }

    @Test
    public void test_allEntries() {
        final String invocationId = String.valueOf(System.currentTimeMillis());
        final String startedOn = "2023-01-01T00:00:00.000Z";
        final String finishedOn = "2023-01-01T12:23:34.456Z";

        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.invocationId(invocationId);
        buildMetadataBuilder.startedOn(startedOn);
        buildMetadataBuilder.finishedOn(finishedOn);

        JsonObject buildMetadataJson = buildMetadataBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_INVOCATION_ID, BuildMetadata.KEY_STARTED_ON, BuildMetadata.KEY_FINISHED_ON);
        testUtils.assertJsonStringEntryMatches("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_INVOCATION_ID, invocationId);
        testUtils.assertJsonStringEntryMatches("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_STARTED_ON, startedOn);
        testUtils.assertJsonStringEntryMatches("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_FINISHED_ON, finishedOn);
    }

}
