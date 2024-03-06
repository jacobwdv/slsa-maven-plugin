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

import org.junit.jupiter.api.Test;

import io.slsa.test.CommonTestUtils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class RunDetailsTest {

    private static final String BUILDER_ID = "https://localhost/slsa/v1";

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_constructor() {
        Builder builder = getBuilder();
        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(builder);

        JsonObject runDetailsJson = runDetailsBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("RunDetails", runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER), Builder.KEY_ID, BUILDER_ID);
    }

    @Test
    public void test_metadata_empty() {
        Builder builder = getBuilder();
        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(builder);

        BuildMetadata buildMetadata = new BuildMetadata.Builder().build();
        runDetailsBuilder.metadata(buildMetadata);

        JsonObject runDetailsJson = runDetailsBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("RunDetails", runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER), Builder.KEY_ID, BUILDER_ID);
    }

    @Test
    public void test_metadata() {
        Builder builder = getBuilder();
        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(builder);

        BuildMetadata buildMetadata = getBuildMetadata();
        runDetailsBuilder.metadata(buildMetadata);

        JsonObject runDetailsJson = runDetailsBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER, RunDetails.KEY_METADATA);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("RunDetails", runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER), Builder.KEY_ID, BUILDER_ID);
        testUtils.assertJsonEntryMatches("RunDetails", runDetailsJson, RunDetails.KEY_METADATA, buildMetadata.toJson());
    }

    @Test
    public void test_byproducts_empty() {
        Builder builder = getBuilder();
        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(builder);

        JsonArray byproducts = JsonArray.EMPTY_JSON_ARRAY;
        runDetailsBuilder.byproducts(byproducts);

        JsonObject runDetailsJson = runDetailsBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("RunDetails", runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER), Builder.KEY_ID, BUILDER_ID);
    }

    @Test
    public void test_byproducts() {
        Builder builder = getBuilder();
        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(builder);

        JsonArray byproducts = getByproducts();
        runDetailsBuilder.byproducts(byproducts);

        JsonObject runDetailsJson = runDetailsBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER, RunDetails.KEY_BYPRODUCTS);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("RunDetails", runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER), Builder.KEY_ID, BUILDER_ID);
        testUtils.assertJsonEntryMatches("RunDetails", runDetailsJson, RunDetails.KEY_BYPRODUCTS, byproducts);
    }

    @Test
    public void test_allEntries() {
        Builder builder = getBuilder();

        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(builder);

        BuildMetadata buildMetadata = getBuildMetadata();
        runDetailsBuilder.metadata(buildMetadata);
        JsonArray byproducts = getByproducts();
        runDetailsBuilder.byproducts(byproducts);

        JsonObject runDetailsJson = runDetailsBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER, RunDetails.KEY_METADATA, RunDetails.KEY_BYPRODUCTS);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("RunDetails", runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER), Builder.KEY_ID, BUILDER_ID);
        testUtils.assertJsonEntryMatches("RunDetails", runDetailsJson, RunDetails.KEY_METADATA, buildMetadata.toJson());
        testUtils.assertJsonEntryMatches("RunDetails", runDetailsJson, RunDetails.KEY_BYPRODUCTS, byproducts);
    }

    private Builder getBuilder() {
        return new Builder.BuilderBuilder(BUILDER_ID).build();
    }

    private BuildMetadata getBuildMetadata() {
        return new BuildMetadata.Builder().invocationId(String.valueOf(System.currentTimeMillis())).startedOn("now").build();
    }

    private JsonArray getByproducts() {
        JsonArrayBuilder byproductsBuilder = Json.createArrayBuilder();
        byproductsBuilder.add("string-entry");
        byproductsBuilder.add(Json.createObjectBuilder().add("dep-key", "value").build());
        return byproductsBuilder.build();
    }
}
