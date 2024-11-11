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
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Metadata about this particular execution of the build.
 * <p>
 * See https://slsa.dev/provenance/v1#buildmetadata.
 */
public class BuildMetadata {

    public static final String KEY_INVOCATION_ID = "invocationId";
    public static final String KEY_STARTED_ON = "startedOn";
    public static final String KEY_FINISHED_ON = "finishedOn";

    /**
     * Identifies this particular build invocation, which can be useful for finding associated logs or other ad-hoc analysis. The
     * exact meaning and format is defined by {@code builder.id}; by default it is treated as opaque and case-sensitive. The value
     * SHOULD be globally unique.
     */
    private String invocationId = null;

    /**
     * The timestamp of when the build started.
     */
    private String startedOn = null;

    /**
     * The timestamp of when the build completed.
     */
    private String finishedOn = null;

    private BuildMetadata(Builder builder) {
        this.invocationId = builder.invocationId;
        this.startedOn = builder.startedOn;
        this.finishedOn = builder.finishedOn;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Utils.addIfNonNullAndNotEmpty(invocationId, KEY_INVOCATION_ID, builder);
        Utils.addIfNonNullAndNotEmpty(startedOn, KEY_STARTED_ON, builder);
        Utils.addIfNonNullAndNotEmpty(finishedOn, KEY_FINISHED_ON, builder);
        return builder.build();
    }

    public static class Builder {

        private String invocationId = null;
        private String startedOn = null;
        private String finishedOn = null;

        public Builder invocationId(String invocationId) {
            this.invocationId = invocationId;
            return this;
        }

        public Builder startedOn(String startedOn) {
            this.startedOn = startedOn;
            return this;
        }

        public Builder finishedOn(String finishedOn) {
            this.finishedOn = finishedOn;
            return this;
        }

        public BuildMetadata build() {
            return new BuildMetadata(this);
        }
    }

}
