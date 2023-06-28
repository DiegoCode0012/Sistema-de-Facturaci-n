package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="clientes")
public class ClientList {
@XmlElement(name="cliente")
public List<Cliente> clientes;

public ClientList(List<Cliente> clientes) {
	super();
	this.clientes = clientes;
}

public ClientList() {
	super();
}

}
