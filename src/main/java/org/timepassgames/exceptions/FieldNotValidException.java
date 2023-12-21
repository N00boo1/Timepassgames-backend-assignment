package org.timepassgames.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldNotValidException extends RuntimeException {

    String resourceName;
    String fieldName;
    public FieldNotValidException(String fieldName,String resourceName) {
        super(String.format("%s field in %s is invalid", fieldName, resourceName));
        this.fieldName=fieldName;
        this.resourceName=resourceName;
    }
}
