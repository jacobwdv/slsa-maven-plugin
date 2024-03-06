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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.slsa.test.CommonTestUtils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class BuilderTest {

    private static final String BUILDER_ID = "https://localhost/slsa/v1";

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_constructor() {
        Builder.BuilderBuilder builderBuilder = new Builder.BuilderBuilder(BUILDER_ID);

        JsonObject builderJson = builderBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("Builder", builderJson, Builder.KEY_ID);
        testUtils.assertJsonStringEntryMatches("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);
    }

    @Test
    public void test_builderDependencies_empty() {
        Builder.BuilderBuilder builderBuilder = new Builder.BuilderBuilder(BUILDER_ID);
        builderBuilder.builderDependencies(JsonArray.EMPTY_JSON_ARRAY);

        JsonObject builderJson = builderBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("Builder", builderJson, Builder.KEY_ID);
        testUtils.assertJsonStringEntryMatches("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);
    }

    @Test
    public void test_builderDependencies_nonEmpty() {
        Builder.BuilderBuilder builderBuilder = new Builder.BuilderBuilder(BUILDER_ID);

        JsonArray dependencies = getDependencies();
        builderBuilder.builderDependencies(dependencies);

        JsonObject builderJson = builderBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("Builder", builderJson, Builder.KEY_ID, Builder.KEY_BUILDER_DEPENDENCIES);
        testUtils.assertJsonStringEntryMatches("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);
        testUtils.assertJsonEntryMatches("Builder", builderJson, Builder.KEY_BUILDER_DEPENDENCIES, dependencies);
    }

    @Test
    public void test_version_empty() {
        Builder.BuilderBuilder builderBuilder = new Builder.BuilderBuilder(BUILDER_ID);
        builderBuilder.version(new HashMap<>());

        JsonObject builderJson = builderBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("Builder", builderJson, Builder.KEY_ID);
        testUtils.assertJsonStringEntryMatches("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);
    }

    @Test
    public void test_version_nonEmpty() {
        Builder.BuilderBuilder builderBuilder = new Builder.BuilderBuilder(BUILDER_ID);
        Map<String, String> versions = getVersions();
        builderBuilder.version(versions);

        JsonObject builderJson = builderBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("Builder", builderJson, Builder.KEY_ID, Builder.KEY_VERSION);
        testUtils.assertJsonStringEntryMatches("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);
        JsonObject builderVersionJson = builderJson.getJsonObject(Builder.KEY_VERSION);
        assertEquals(versions.keySet(), builderVersionJson.keySet(), "\"" + Builder.KEY_VERSION + "\" entry did not have the expected set of keys. Value was: " + builderVersionJson);
        for (String key : versions.keySet()) {
            assertEquals(versions.get(key), builderVersionJson.getString(key), "Version entry for \"" + key + "\" did not match the expected value.");
        }
    }

    @Test
    public void test_allEntries() {
        Builder.BuilderBuilder builderBuilder = new Builder.BuilderBuilder(BUILDER_ID);

        JsonArray dependencies = getDependencies();
        builderBuilder.builderDependencies(dependencies);
        Map<String, String> versions = getVersions();
        builderBuilder.version(versions);

        JsonObject builderJson = builderBuilder.build().toJson();

        testUtils.assertJsonOnlyContainsKeys("Builder", builderJson, Builder.KEY_ID,  Builder.KEY_BUILDER_DEPENDENCIES, Builder.KEY_VERSION);
        testUtils.assertJsonStringEntryMatches("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);
        testUtils.assertJsonEntryMatches("Builder", builderJson, Builder.KEY_BUILDER_DEPENDENCIES, dependencies);
        JsonObject builderVersionJson = builderJson.getJsonObject(Builder.KEY_VERSION);
        assertEquals(versions.keySet(), builderVersionJson.keySet(), "\"" + Builder.KEY_VERSION + "\" entry did not have the expected set of keys. Value was: " + builderVersionJson);
        for (String key : versions.keySet()) {
            assertEquals(versions.get(key), builderVersionJson.getString(key), "Version entry for \"" + key + "\" did not match the expected value.");
        }
    }

    private JsonArray getDependencies() {
        JsonArrayBuilder dependenciesBuilder = Json.createArrayBuilder();
        dependenciesBuilder.add("string-dependency");
        dependenciesBuilder.add(Json.createObjectBuilder().add("dep-key", "value").build());
        return dependenciesBuilder.build();
    }

    private Map<String, String> getVersions() {
        Map<String, String> versions = new HashMap<>();
        versions.put("build.platform.component.1", "0.1");
        versions.put("build.platform.component.2", "1.2");
        versions.put("build.platform.component.3", "1.1.2");
        return versions;
    }

}
