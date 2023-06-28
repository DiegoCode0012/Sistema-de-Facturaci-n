package com.bolsadeideas.springboot.app.view.json;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.view.xml.ClientList;
@Component("listar.json")
public class ClientListJsonView extends MappingJackson2JsonView{

	@SuppressWarnings("unchecked")
	@Override
	protected Object filterModel(Map<String, Object> model) {
		model.remove("titulo"); // lo borramos porque no quiero convertir esto a json 
		List<Cliente> clientes=(List<Cliente>) model.get("clientes");
		model.remove("clientes");
		model.put("clientList", new ClientList(clientes));
		return super.filterModel(model);
	}

}
