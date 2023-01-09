package me.samcefalo.mockserverapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author samcefalo
 * Object to store responses
 */
@Getter
@AllArgsConstructor
public class MockHttpResponse implements Serializable {

    private int code;
    private String body;

}
