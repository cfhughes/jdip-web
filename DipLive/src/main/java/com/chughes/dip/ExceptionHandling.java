package com.chughes.dip;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandling {
	
	@ExceptionHandler
	public ModelAndView handle(Exception e){
		ModelAndView m = new ModelAndView("exception");
		m.addObject("description", e.getLocalizedMessage());
		return m;
	}


}
