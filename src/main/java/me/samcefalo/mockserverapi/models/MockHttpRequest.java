package me.samcefalo.mockserverapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author samcefalo
 * Object to store requests
 */
@Getter
@AllArgsConstructor
public class MockHttpRequest implements Serializable {

    private String method;
    private String path;
    private Integer times;

}
