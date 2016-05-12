package com.restexample.api.impl;

import org.springframework.stereotype.Component;

@Component
public class RestDemoApiImpl {

	public String getGreeting(String name)
	{
		return new StringBuilder("Hello ").append(name).append("!!!").toString();
	}
}
