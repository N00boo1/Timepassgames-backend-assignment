package org.timepassgames.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceAlreadyExistsException extends RuntimeException {

    String resourceName;
    String name;
    String url;
    public ResourceAlreadyExistsException(String resourceName, String name,String url) {
        super(String.format("%s with name %s or url %s already exists in database", resourceName,name,url));
        this.resourceName=resourceName;
        this.name=name;
        this.url=url;
    }
}
