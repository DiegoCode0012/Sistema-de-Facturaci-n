package com.bolsadeideas.springboot.app.controllers;


import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.view.xml.ClientList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@SessionAttributes("cliente") //los atributos del objeto mapeado al formulario quedan persistentes en cada peticion GET es decir que los atributos estan aunque no los veas(como el ID) en el crear o en el editar
@Controller
public class ClienteController {
	protected final Log logger=LogFactory.getLog(this.getClass());
	@Autowired
	private  IUploadFileService uploadFileService;
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private MessageSource messageSource;
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id,Model model,RedirectAttributes flash) {
		
		Cliente cliente=clienteService.find(id);
		if(cliente==null) {
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo","Detalle del cliente: " + cliente.getNombre());
		return "ver";	
	}
	//responsebody=el listado se guarda en el cuerpo de la respuesta
	@GetMapping({"/listar-rest"})
	public @ResponseBody ClientList listarRest() {
		return new ClientList(clienteService.findAll());
	}
	@GetMapping({"/listar", "/"})
	public String listar(Model model,
			Authentication authentication,
			HttpServletRequest request,
			Locale locale) {
		  if(authentication!=null) {
		    	logger.info("El usuario" +   authentication.getName() + "ha inciado con exito" );
		    }
		  //if(auth!=null) {
		//	  logger.info("utilizando forma estatica SecurityContextHolder.getContext.getAuthenticaction" .concat(authentication.getName().concat("Tienes acceso")));
		 // }
		  if(hasRole("ROLE_ADMIN")) {
			  logger.info("Hola" .concat(authentication.getName().concat("Tienes acceso")));
		  }		 
		  else if(hasRole("ROLE_USER")){
			  logger.info("Hola" .concat(authentication.getName().concat("No Tienes acceso")));
		  }else {
			  logger.info("No se encuentra logueado ");
		  }
		  SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
			
			if(securityContext.isUserInRole("ROLE_ADMIN")) {
				logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(authentication.getName()).concat(" tienes acceso!"));
			} else {
				logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola " .concat(" NO tienes acceso!"));
			}
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo",null,locale));
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
			
	}
	@GetMapping("/form")
	public String crear(Model model) {
	Cliente cliente = new Cliente();	
	model.addAttribute("cliente", cliente);
	model.addAttribute("titulo","Listado de clientes");
	 return "form";
	}
	
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value="id") Long id,Model model,RedirectAttributes flash) {
		Cliente cliente=null;	
		if(id>0) {
			cliente=clienteService.find(id);
			if(cliente==null) {
				flash.addFlashAttribute("error","El cliente no existe en la BBDD");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error","El ID no puede ser igual menor o igual a 0");
		return "redirect:/listar";
		}
		model.addAttribute("cliente",cliente);
		model.addAttribute("titulo","Editar Cliente");
		return "form";
	}
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente,BindingResult result,Model model,@RequestParam("file") MultipartFile foto,SessionStatus status,RedirectAttributes flash) {
		if(result.hasErrors()) {
			model.addAttribute("titulo","Formulario del Cliente");
			return "form";
		}	
		
		if(!foto.isEmpty()) {
			//SI ESTAMOS EDITANDO,VERIFICAR QUE SE ELIMINE LA FOTO ANTERIOR, CON 
			//SESSIONATRIBUTTE SE PUED ECONSEGUIR EL  CLIENTE.GETFOTO , EL DIV FOTO NO ESTA MAPEADO AL OBJETO CLIENTE
			if(cliente.getId() !=null && cliente.getId()>0  && cliente.getFoto()!=null && cliente.getFoto().length()>0) {
			 uploadFileService.delete(cliente.getFoto());
			}
			String uniqueFileName=null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("success","Has Subido correctamente " + uniqueFileName);
			cliente.setFoto(uniqueFileName);
		}
		String msjflash1= (cliente.getId()!=null)? "Cliente editado con exito!": "Cliente creado con exito!";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", msjflash1);
		return "redirect:/listar";	
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,Model model,RedirectAttributes flash) {
		if(id>0) {
			
			Cliente cliente= clienteService.find(id);
			clienteService.delete(id);
			flash.addFlashAttribute("info","Cliente eliminado con exito");
			if(uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("success", "Foto: " + cliente.getFoto() + " eliminada con exitoso");
			}
		}
		return "redirect:/listar";
	}
	public boolean hasRole(String role) {
		
		SecurityContext context=SecurityContextHolder.getContext();
		Authentication auth =context.getAuthentication();
		if(auth==null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities=auth.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		
		
	}
	
	


}
