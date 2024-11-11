package com.ibm.slsa.maven.plugin.utils.war;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.ibm.slsa.maven.plugin.utils.war.exceptions.MultipleWarsFoundException;
import com.ibm.slsa.maven.plugin.utils.war.exceptions.WarException;
import com.ibm.slsa.maven.plugin.utils.war.exceptions.WarNotFoundException;

public class WarUtils {

    private MavenProject project;
    private Log log;

    public WarUtils(MavenProject project, Log log) {
        this.project = project;
        this.log = log;
    }

    public File getBuiltWar() throws WarException {
        File buildDirectory = new File(project.getBuild().getDirectory());
        File[] wars = buildDirectory.listFiles(new WarFilenameFilter());
        if (wars == null || wars.length == 0) {
            throw new WarNotFoundException(buildDirectory);
        }
        if (wars.length > 1) {
            throw new MultipleWarsFoundException(buildDirectory, wars);
        }
        File war = wars[0];
        log.info("Found war: " + war.getAbsolutePath());
        return war;
    }

    private final class WarFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".war");
        }
    }

}
