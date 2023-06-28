package com.bolsadeideas.springboot.app.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

//ESTO YA ES UN COMPONENTE
public interface IclienteDao extends CrudRepository<Cliente,Long> {
}
