package com.string.frequencyManager.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Kamna
 * Response.java
 */

@Setter
@ToString
@NoArgsConstructor
public class Response {

        @JsonProperty("response")
	private String response;
}
