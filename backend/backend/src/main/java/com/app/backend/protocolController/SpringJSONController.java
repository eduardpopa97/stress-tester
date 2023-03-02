package com.app.backend.protocolController;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringJSONController {

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/spring")
	public String springRequest()
	{
		System.out.println(HTTPController.springServerResponse);
		return HTTPController.springServerResponse;
	}
	
}
