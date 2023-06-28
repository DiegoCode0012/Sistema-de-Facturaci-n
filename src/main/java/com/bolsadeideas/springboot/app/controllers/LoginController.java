package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class LoginController {
	@GetMapping("/login")
    public String login(@RequestParam(value="error",required=false)String error,
    		@RequestParam(value="logout",required=false)String logout,
    		Model model, Principal principal, RedirectAttributes flash) {
	//Comprobar si ya se ha iniciado session anteriormente, si se quiere colar por la url
	if(principal != null) {
		flash.addFlashAttribute("info","Ya ha iniciado sesión anteriormente");
		return "redirect:/";
	}
	//esto es cuando se envia el form,el metodo post se hace por de atras y nos manda al GetMapping otra vez pero con nuevos
	//parametros como error y el logout
	if(error!=null) {
		model.addAttribute("error","Error en el login:Nombre de usuario o contraseña incorrectos");
	}
    
	if(logout!=null) {
		model.addAttribute("success","Ha cerrado la sesion con exito");
	}
	return "login";
}
}
