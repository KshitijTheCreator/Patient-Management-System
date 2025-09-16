package org.kshitij.Application.exception;

public class PatientNotExistsException extends RuntimeException {
    public PatientNotExistsException(String message) {
        super(message);
    }
}
