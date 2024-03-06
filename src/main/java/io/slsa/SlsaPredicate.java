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

import io.intoto.attestation.Predicate;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Predicate type specifically for SLSA provenance.
 * <p>
 * See https://slsa.dev/provenance/v1#schema.
 */
public class SlsaPredicate extends Predicate {

    public static final String PREDICATE_TYPE_SLSA_PROVENANCE_V1 = "https://slsa.dev/provenance/v1";

    public static final String KEY_BUILD_DEFINITION = "buildDefinition";
    public static final String KEY_RUN_DETAILS = "runDetails";

    /**
     * The input to the build. The accuracy and completeness are implied by {@code runDetails.builder.id}.
     */
    private BuildDefinition buildDefinition;

    /**
     * Details specific to this particular execution of the build.
     */
    private RunDetails runDetails;

    public SlsaPredicate(BuildDefinition buildDefinition, RunDetails runDetails) {
        this.buildDefinition = buildDefinition;
        this.runDetails = runDetails;
    }

    @Override
    public String getPredicateType() {
        return PREDICATE_TYPE_SLSA_PROVENANCE_V1;
    }

    @Override
    public JsonObject getPredicateParameters() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (buildDefinition != null) {
            builder.add(KEY_BUILD_DEFINITION, buildDefinition.toJson());
        }
        if (runDetails != null) {
            builder.add(KEY_RUN_DETAILS, runDetails.toJson());
        }
        return builder.build();
    }

}
