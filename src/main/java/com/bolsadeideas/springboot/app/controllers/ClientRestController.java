package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.view.xml.ClientList;

@RestController
@RequestMapping("/api/clientes")
public class ClientRestController {
	@Autowired
	private IClienteService clienteService;
	@GetMapping({"/listar"})
	public  ClientList listarRest() {
		return new ClientList(clienteService.findAll());
		
	}
}
