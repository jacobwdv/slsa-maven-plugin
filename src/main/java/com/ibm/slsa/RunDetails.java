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

import com.ibm.intoto.attestation.utils.Utils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Details specific to this particular execution of the build.
 * <p>
 * See https://slsa.dev/provenance/v1#rundetails.
 */
public class RunDetails {

    public static final String KEY_BUILDER = "builder";
    public static final String KEY_METADATA = "metadata";
    public static final String KEY_BYPRODUCTS = "byproducts";

    /**
     * Identifies the build platform that executed the invocation, which is trusted to have correctly performed the operation and
     * populated this provenance.
     */
    private com.ibm.slsa.Builder builder = null;

    /**
     * Metadata about this particular execution of the build.
     */
    private BuildMetadata metadata = null;

    /**
     * Additional artifacts generated during the build that are not considered the "output" of the build but that might be needed
     * during debugging or incident response. For example, this might reference logs generated during the build and/or a digest of
     * the fully evaluated build configuration.
     */
    private JsonArray byproducts = null;

    private RunDetails(Builder runDetailsBuilder) {
        this.builder = runDetailsBuilder.builder;
        this.metadata = runDetailsBuilder.metadata;
        this.byproducts = runDetailsBuilder.byproducts;
    }

    public JsonObject toJson() {
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        Utils.addIfNonNullAndNotEmpty(builder.toJson(), KEY_BUILDER, resultBuilder);
        if (metadata != null) {
            Utils.addIfNonNullAndNotEmpty(metadata.toJson(), KEY_METADATA, resultBuilder);
        }
        Utils.addIfNonNullAndNotEmpty(byproducts, KEY_BYPRODUCTS, resultBuilder);
        return resultBuilder.build();
    }

    public static class Builder {

        private com.ibm.slsa.Builder builder = null;
        private BuildMetadata metadata = null;
        private JsonArray byproducts = null;

        public Builder(com.ibm.slsa.Builder builder) {
            this.builder = builder;
        }

        public Builder metadata(BuildMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder byproducts(JsonArray byproducts) {
            this.byproducts = byproducts;
            return this;
        }

        public RunDetails build() {
            return new RunDetails(this);
        }
    }

}
