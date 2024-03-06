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
package io.slsa.maven.plugin.utils.war;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.slsa.maven.plugin.utils.war.exceptions.MultipleWarsFoundException;
import io.slsa.maven.plugin.utils.war.exceptions.WarException;
import io.slsa.maven.plugin.utils.war.exceptions.WarNotFoundException;
import io.slsa.test.CommonTestUtils;
import io.slsa.test.Constants;

@ExtendWith(MockitoExtension.class)
public class WarUtilsTest {

    @Mock private MavenProject project;
    @Mock private Build projectBuild;
    @Mock private Log log;

    private CommonTestUtils testUtils = new CommonTestUtils();

    @Test
    public void test_noWarFiles() {
        WarUtils utils = new WarUtils(project, log);

        when(project.getBuild()).thenReturn(projectBuild);
        when(projectBuild.getDirectory()).thenReturn(Constants.RESOURCES_DIR + File.separator + "no-wars");

        try {
            File war = utils.getBuiltWar();
            fail("Should have thrown an exception, but got the following WAR file: " + war.getAbsolutePath());
        } catch (WarNotFoundException e) {
            // Expected
        } catch (WarException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_dirDoesNotExist() {
        WarUtils utils = new WarUtils(project, log);

        when(project.getBuild()).thenReturn(projectBuild);
        when(projectBuild.getDirectory()).thenReturn(Constants.RESOURCES_DIR + File.separator + "does-not-exist");

        try {
            File war = utils.getBuiltWar();
            fail("Should have thrown an exception, but got the following WAR file: " + war.getAbsolutePath());
        } catch (WarNotFoundException e) {
            // Expected
        } catch (WarException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_multipleWarFiles() {
        WarUtils utils = new WarUtils(project, log);

        when(project.getBuild()).thenReturn(projectBuild);
        when(projectBuild.getDirectory()).thenReturn(Constants.RESOURCES_DIR + File.separator + "multiple-wars");

        try {
            File war = utils.getBuiltWar();
            fail("Should have thrown an exception, but got the following WAR file: " + war.getAbsolutePath());
        } catch (MultipleWarsFoundException e) {
            // Expected
            testUtils.assertExceptionMatchesPattern(e, "app1.war, app2.war");
        } catch (WarException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

    @Test
    public void test_oneWarFile() {
        WarUtils utils = new WarUtils(project, log);

        when(project.getBuild()).thenReturn(projectBuild);
        when(projectBuild.getDirectory()).thenReturn(Constants.RESOURCES_DIR + File.separator + "one-war");

        try {
            File war = utils.getBuiltWar();
            assertEquals("app.war", war.getName(), "WAR file name did not match expected value.");
        } catch (WarException e) {
            fail("Encountered unexpected exception: " + e);
        }
    }

}
