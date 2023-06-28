package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;
import java.util.Map;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@SuppressWarnings("unchecked")

@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {
	

public ClienteListXmlView(Jaxb2Marshaller marshaller) {
	super(marshaller);
}
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.remove("titulo");
		List<Cliente> clientes=(List<Cliente>) model.get("clientes");
		model.remove("clientes");
		model.put("clientList", new ClientList(clientes));
		super.renderMergedOutputModel(model, request, response);
	}






}
