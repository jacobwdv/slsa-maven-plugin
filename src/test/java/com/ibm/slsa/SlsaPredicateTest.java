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
package com.ibm.slsa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.ibm.slsa.test.CommonTestUtils;
import jakarta.json.JsonObject;

public class SlsaPredicateTest {

    private static final String BUILDER_ID = "https://localhost/slsa/v1";
    private static final String BUILD_TYPE = "https://localhost/slsa/v1/buildType";

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    void test_constructor_bothArgsNull() {
        SlsaPredicate predicate = new SlsaPredicate(null, null);
        assertEquals(SlsaPredicate.PREDICATE_TYPE_SLSA_PROVENANCE_V1, predicate.getPredicateType(), "Predicate type did not match expected value.");
        assertEquals(JsonObject.EMPTY_JSON_OBJECT, predicate.getPredicateParameters(), "Predicate parameters should have been empty but weren't.");
    }

    @Test
    void test_getPredicateParameters_buildDefinitionOnly() {
        BuildDefinition.Builder buildDefinitioBuilder = new BuildDefinition.Builder(BUILD_TYPE, null);
        BuildDefinition buildDefinition = buildDefinitioBuilder.build();

        SlsaPredicate predicate = new SlsaPredicate(buildDefinition, null);

        assertEquals(SlsaPredicate.PREDICATE_TYPE_SLSA_PROVENANCE_V1, predicate.getPredicateType(), "Predicate type did not match expected value.");

        JsonObject predicateParameters = predicate.getPredicateParameters();
        testUtils.assertJsonOnlyContainsKeys("Predicate parameters", predicateParameters, SlsaPredicate.KEY_BUILD_DEFINITION);
        JsonObject buildDefinitionJson = predicateParameters.getJsonObject(SlsaPredicate.KEY_BUILD_DEFINITION);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);
    }

    @Test
    void test_getPredicateParameters_runDetailsOnly() {
        RunDetails expectedRunDetails = getRunDetails();

        SlsaPredicate predicate = new SlsaPredicate(null, expectedRunDetails);

        assertEquals(SlsaPredicate.PREDICATE_TYPE_SLSA_PROVENANCE_V1, predicate.getPredicateType(), "Predicate type did not match expected value.");

        JsonObject predicateParameters = predicate.getPredicateParameters();
        testUtils.assertJsonOnlyContainsKeys("Predicate parameters", predicateParameters, SlsaPredicate.KEY_RUN_DETAILS);
        assertRunDetailsMatchExpectedValues(predicateParameters);
    }

    @Test
    void test_getPredicateParameters() {
        BuildDefinition.Builder buildDefinitioBuilder = new BuildDefinition.Builder(BUILD_TYPE, null);
        BuildDefinition buildDefinition = buildDefinitioBuilder.build();
        RunDetails expectedRunDetails = getRunDetails();

        SlsaPredicate predicate = new SlsaPredicate(buildDefinition, expectedRunDetails);

        assertEquals(SlsaPredicate.PREDICATE_TYPE_SLSA_PROVENANCE_V1, predicate.getPredicateType(), "Predicate type did not match expected value.");

        JsonObject predicateParameters = predicate.getPredicateParameters();
        testUtils.assertJsonOnlyContainsKeys("Predicate parameters", predicateParameters, SlsaPredicate.KEY_BUILD_DEFINITION, SlsaPredicate.KEY_RUN_DETAILS);

        JsonObject buildDefinitionJson = predicateParameters.getJsonObject(SlsaPredicate.KEY_BUILD_DEFINITION);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("BuildDefinition", buildDefinitionJson, BuildDefinition.KEY_BUILD_TYPE, BUILD_TYPE);

        assertRunDetailsMatchExpectedValues(predicateParameters);
    }

    private RunDetails getRunDetails() {
        RunDetails.Builder runDetailsBuilder = new RunDetails.Builder(new Builder.BuilderBuilder(BUILDER_ID).build());
        runDetailsBuilder.metadata(getBuildMetadata());
        return runDetailsBuilder.build();
    }

    private BuildMetadata getBuildMetadata() {
        BuildMetadata.Builder buildMetadataBuilder = new BuildMetadata.Builder();
        buildMetadataBuilder.invocationId(String.valueOf(System.currentTimeMillis()));
        buildMetadataBuilder.startedOn(Instant.now().toString());
        return buildMetadataBuilder.build();
    }

    private void assertRunDetailsMatchExpectedValues(JsonObject predicateParameters) {
        JsonObject runDetailsJson = predicateParameters.getJsonObject(SlsaPredicate.KEY_RUN_DETAILS);
        testUtils.assertJsonOnlyContainsKeys("RunDetails", runDetailsJson, RunDetails.KEY_BUILDER, RunDetails.KEY_METADATA);

        JsonObject builderJson = runDetailsJson.getJsonObject(RunDetails.KEY_BUILDER);
        testUtils.assertJsonContainsOnlyExpectedStringEntry("Builder", builderJson, Builder.KEY_ID, BUILDER_ID);

        JsonObject buildMetadataJson = runDetailsJson.getJsonObject(RunDetails.KEY_METADATA);
        testUtils.assertJsonOnlyContainsKeys("BuildMetadata", buildMetadataJson, BuildMetadata.KEY_INVOCATION_ID, BuildMetadata.KEY_STARTED_ON);
        // Don't bother checking exact values for these keys; should be confident the right data is there at this point
    }

}
