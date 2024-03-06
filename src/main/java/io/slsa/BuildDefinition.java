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

import io.intoto.attestation.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * The BuildDefinition describes all of the inputs to the build. It SHOULD contain all the information necessary and
 * sufficient to initialize the build and begin execution. The accuracy and completeness are implied by {@code
 * runDetails.builder.id}.
 * <p>
 * See https://slsa.dev/provenance/v1#builddefinition.
 */
public class BuildDefinition {

    public static final String KEY_BUILD_TYPE = "buildType";
    public static final String KEY_EXTERNAL_PARAMETERS = "externalParameters";
    public static final String KEY_INTERNAL_PARAMETERS = "internalParameters";
    public static final String KEY_RESOLVED_DEPENDENCIES = "resolvedDependencies";

    /**
     * Identifies the template for how to perform the build and interpret the parameters and dependencies.
     * <p>
     * The URI SHOULD resolve to a human-readable specification that includes: overall description of the build type; schema for
     * {@code externalParameters} and {@code internalParameters}; unambiguous instructions for how to initiate the build given
     * this {@code BuildDefinition}, and a complete example. Example:
     * https://slsa-framework.github.io/github-actions-buildtypes/workflow/v1.
     */
    private String buildType = null;

    /**
     * The parameters that are under external control, such as those set by a user or tenant of the build platform. They MUST be
     * complete at SLSA Build L3, meaning that that there is no additional mechanism for an external party to influence the build.
     * (At lower SLSA Build levels, the completeness MAY be best effort.)
     * <p>
     * The build platform SHOULD be designed to minimize the size and complexity of {@code externalParameters}, in order to reduce
     * fragility and ease verification. Consumers SHOULD have an expectation of what "good" looks like; the more information that
     * they need to check, the harder that task becomes.
     * <p>
     * Verifiers SHOULD reject unrecognized or unexpected fields within {@code externalParameters}.
     */
    private JsonObject externalParameters = null;

    /**
     * The parameters that are under the control of the entity represented by {@code builder.id}. The primary intention of this
     * field is for debugging, incident response, and vulnerability management. The values here MAY be necessary for reproducing
     * the build. There is no need to verify these parameters because the build platform is already trusted, and in many cases it
     * is not practical to do so.
     */
    private JsonObject internalParameters = null;

    /**
     * Unordered collection of artifacts needed at build time. Completeness is best effort, at least through SLSA Build L3. For
     * example, if the build script fetches and executes "example.com/foo.sh", which in turn fetches "example.com/bar.tar.gz",
     * then both "foo.sh" and "bar.tar.gz" SHOULD be listed here.
     */
    private JsonArray resolvedDependencies = null;

    private BuildDefinition(Builder builder) {
        this.buildType = builder.buildType;
        this.externalParameters = builder.externalParameters;
        this.internalParameters = builder.internalParameters;
        this.resolvedDependencies = builder.resolvedDependencies;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(KEY_BUILD_TYPE, buildType);
        Utils.addIfNonNullAndNotEmpty(externalParameters, KEY_EXTERNAL_PARAMETERS, builder);
        Utils.addIfNonNullAndNotEmpty(internalParameters, KEY_INTERNAL_PARAMETERS, builder);
        Utils.addIfNonNullAndNotEmpty(resolvedDependencies, KEY_RESOLVED_DEPENDENCIES, builder);
        return builder.build();
    }

    public static class Builder {

        private String buildType;
        private JsonObject externalParameters = null;
        private JsonObject internalParameters = null;
        private JsonArray resolvedDependencies = null;

        public Builder(String buildType, JsonObject externalParameters) {
            this.buildType = buildType;
            this.externalParameters = externalParameters;
        }

        public Builder internalParameters(JsonObject internalParameters) {
            this.internalParameters = internalParameters;
            return this;
        }

        public Builder resolvedDependencies(JsonArray resolvedDependencies) {
            this.resolvedDependencies = resolvedDependencies;
            return this;
        }

        public BuildDefinition build() {
            return new BuildDefinition(this);
        }
    }

}
