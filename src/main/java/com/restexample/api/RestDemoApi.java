package com.restexample.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restexample.api.impl.RestDemoApiImpl;

@RestController
@RequestMapping("/demoapi")
public class RestDemoApi {

	@Autowired RestDemoApiImpl restDemoApiImpl;
	
	@RequestMapping(value="/greeting", method=RequestMethod.POST)
	public String getGreeting(@RequestParam(value="name", defaultValue="World") String name) throws Exception
	{
		return restDemoApiImpl.getGreeting(name);
	}
}
