package com.bank.exchange.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtValidationRequest implements Serializable {

	private static final long serialVersionUID = 3074053963691015640L;

	private String token;

	private String applicationName;

	private String operationName;

}