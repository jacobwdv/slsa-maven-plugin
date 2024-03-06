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

import io.intoto.attestation.custom.resource.descriptors.maven.MavenArtifactResourceDescriptor;
import io.slsa.test.CommonTestUtils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class BuildDefinitionTest {

    private static final String BUILD_TYPE = "https://localhost/slsa/v1/buildType";

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_constructor_noExternalParameters() {
        final JsonObject externalParameters = JsonObject.EMPTY_JSON_OBJECT;
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
    }

    @Test
    public void test_constructor_withExternalParameters() {
        JsonObject externalParameters = getExternalParameters();
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BuildDefinition.KEY_EXTERNAL_PARAMETERS);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_EXTERNAL_PARAMETERS, externalParameters);
    }

    @Test
    public void test_internalParameters_empty() {
        JsonObject externalParameters = getExternalParameters();
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonObject internalParameters = JsonObject.EMPTY_JSON_OBJECT;
        buildDefinitionBuilder.internalParameters(internalParameters);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BuildDefinition.KEY_EXTERNAL_PARAMETERS);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_EXTERNAL_PARAMETERS, externalParameters);
    }

    @Test
    public void test_internalParameters_nonEmpty() {
        JsonObject externalParameters = getExternalParameters();
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonObject internalParameters = getInternalParameters();
        buildDefinitionBuilder.internalParameters(internalParameters);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BuildDefinition.KEY_EXTERNAL_PARAMETERS, BuildDefinition.KEY_INTERNAL_PARAMETERS);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_EXTERNAL_PARAMETERS, externalParameters);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_INTERNAL_PARAMETERS, internalParameters);
    }

    @Test
    public void test_resolvedDependencies_empty() {
        JsonObject externalParameters = getExternalParameters();
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonArray resolvedDependencies = JsonArray.EMPTY_JSON_ARRAY;
        buildDefinitionBuilder.resolvedDependencies(resolvedDependencies);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BuildDefinition.KEY_EXTERNAL_PARAMETERS);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_EXTERNAL_PARAMETERS, externalParameters);
    }

    @Test
    public void test_resolvedDependencies_nonEmpty() {
        JsonObject externalParameters = getExternalParameters();
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonArray resolvedDependencies = getResolvedDependencies();
        buildDefinitionBuilder.resolvedDependencies(resolvedDependencies);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BuildDefinition.KEY_EXTERNAL_PARAMETERS, BuildDefinition.KEY_RESOLVED_DEPENDENCIES);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_EXTERNAL_PARAMETERS, externalParameters);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_RESOLVED_DEPENDENCIES, resolvedDependencies);
    }

    @Test
    public void test_allEntries() {
        JsonObject externalParameters = getExternalParameters();
        BuildDefinition.Builder buildDefinitionBuilder = new BuildDefinition.Builder(BUILD_TYPE, externalParameters);

        JsonObject internalParameters = getInternalParameters();
        buildDefinitionBuilder.internalParameters(internalParameters);
        JsonArray resolvedDependencies = getResolvedDependencies();
        buildDefinitionBuilder.resolvedDependencies(resolvedDependencies);

        JsonObject buildDefinitionJson = buildDefinitionBuilder.build().toJson();
        testUtils.assertJsonOnlyContainsKeys("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BuildDefinition.KEY_EXTERNAL_PARAMETERS, BuildDefinition.KEY_INTERNAL_PARAMETERS, BuildDefinition.KEY_RESOLVED_DEPENDENCIES);
        testUtils.assertJsonStringEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_EXTERNAL_PARAMETERS, externalParameters);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_INTERNAL_PARAMETERS, internalParameters);
        testUtils.assertJsonEntryMatches("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_RESOLVED_DEPENDENCIES, resolvedDependencies);
    }

    private JsonObject getExternalParameters() {
        JsonObjectBuilder externalParametersBuilder = Json.createObjectBuilder();
        externalParametersBuilder.add("repository", "https://github.com/octocat/hello-world.git");
        externalParametersBuilder.add("ref", "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d");
        externalParametersBuilder.add("userProperties", Json.createObjectBuilder().add("string", "value").build());
        return externalParametersBuilder.build();
    }

    private JsonObject getInternalParameters() {
        JsonObjectBuilder internalParametersBuilder = Json.createObjectBuilder();
        internalParametersBuilder.add("key", "value");
        internalParametersBuilder.add("sub-array", Json.createArrayBuilder().add("value1").add("value2").build());
        return internalParametersBuilder.build();
    }

    private JsonArray getResolvedDependencies() {
        MavenArtifactResourceDescriptor mavenArtifact1 = new MavenArtifactResourceDescriptor(testUtils.createDependency("com.empire", "stardust", "0.9.9", "provided", "pom"));
        MavenArtifactResourceDescriptor mavenArtifact2 = new MavenArtifactResourceDescriptor(testUtils.createDependency("org.rebels", "fulcrum", "1.0.0", "provided", "jar"));

        JsonArrayBuilder resolvedDependenciesBuilder = Json.createArrayBuilder();
        resolvedDependenciesBuilder.add(mavenArtifact1.toJson());
        resolvedDependenciesBuilder.add(mavenArtifact2.toJson());
        return resolvedDependenciesBuilder.build();
    }

}
