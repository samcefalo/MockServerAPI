package me.samcefalo.mockserverapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author samcefalo
 * Object to store ExpectationConfig requests/responses
 */
@Getter
@AllArgsConstructor
public class ExpectationConfig implements Serializable {

    private MockHttpRequest mockHttpRequest;

    private MockHttpResponse mockHttpResponse;

}
