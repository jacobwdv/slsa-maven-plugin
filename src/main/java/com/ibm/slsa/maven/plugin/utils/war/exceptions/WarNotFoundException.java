package com.ibm.slsa.maven.plugin.utils.war.exceptions;

import java.io.File;

public class WarNotFoundException extends WarException {

    private static final String ERROR_MSG = "There were no .war files found in the %s directory.";

    public WarNotFoundException(File buildDirectory) {
        super(String.format(ERROR_MSG, buildDirectory.getAbsolutePath()));
    }

}
