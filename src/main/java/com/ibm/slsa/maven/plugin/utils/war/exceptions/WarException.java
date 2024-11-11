package com.ibm.slsa.maven.plugin.utils.war.exceptions;

public class WarException extends Exception {

    private static final String ERROR_MSG = "An error occurred while reading or processing the .war file: %s";

    private final String errorMsg;

    public WarException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_MSG, errorMsg);
    }

}
