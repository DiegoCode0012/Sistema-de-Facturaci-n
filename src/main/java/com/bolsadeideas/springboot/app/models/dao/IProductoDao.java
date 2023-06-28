package com.bolsadeideas.springboot.app.models.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto,Long>{
	//cuando el nombre sea igual a termino(term), el 1 hace referencia al primer parametro
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);
}
