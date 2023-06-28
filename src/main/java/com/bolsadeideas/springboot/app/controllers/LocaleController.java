package com.bolsadeideas.springboot.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {
	private final Logger log =LoggerFactory.getLogger(getClass());

@GetMapping("/locale")
public String locale(HttpServletRequest request) {
	String ultimaUrl=request.getHeader("referer"); //redirigirnos a la ultima pagina antes de cambiar  el idioma
	log.info(ultimaUrl);
	return "redirect:".concat(ultimaUrl);
	
}
}
