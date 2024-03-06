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
package io.slsa.maven.plugin.utils.git;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import io.intoto.attestation.DigestSet;
import io.intoto.attestation.ResourceDescriptor;
import io.intoto.attestation.custom.resource.descriptors.git.GitRepositoryResourceDescriptor;
import io.slsa.maven.plugin.exceptions.GitRepositoryException;
import io.slsa.test.CommonTestUtils;
import jakarta.json.JsonObject;

public class GitUtilsTest {

    private CommonTestUtils testUtils = new CommonTestUtils();

    private GitUtils utils = new GitUtils();

    @Test
    public void test_getGitRepositoryResourceDescriptor() {
        final String expectedRefRegex = "refs/heads/[^/]+";
        final String expectedUriRegex = "git\\+https://github.com/.+@" + expectedRefRegex + "$";
        try {
            GitRepositoryResourceDescriptor descriptor = utils.getGitRepositoryResourceDescriptor();
            testUtils.assertStringMatchesRegex(expectedRefRegex, descriptor.getRef());

            JsonObject descriptorJson = descriptor.toJson();
            testUtils.assertJsonOnlyContainsKeys("GitRepositoryResourceDescriptor", descriptorJson, GitRepositoryResourceDescriptor.KEY_URI, GitRepositoryResourceDescriptor.KEY_DIGEST);
            testUtils.assertStringMatchesRegex(expectedUriRegex, descriptorJson.getString(GitRepositoryResourceDescriptor.KEY_URI));
            testUtils.assertJsonContainsKey("GitRepositoryResourceDescriptor.digest", descriptorJson.getJsonObject(ResourceDescriptor.KEY_DIGEST), DigestSet.GITCOMMIT);
        } catch (GitRepositoryException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

}
