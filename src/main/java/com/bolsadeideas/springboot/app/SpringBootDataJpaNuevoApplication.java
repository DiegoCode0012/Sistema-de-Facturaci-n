package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaNuevoApplication implements CommandLineRunner{

	@Autowired
	private IUploadFileService uploadFileService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaNuevoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
		String password="12345";
		for(int i=0;i<2;i++) {
			String bcryptPassword= passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
		
	}

}
