package com.vision;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.vision.services.AdFetchUserDetails;

/*@SpringBootApplication
public class EtladFetchUserDetailsApplication implements CommandLineRunner {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AdFetchUserDetails adFetchUserDetails;

	public static void main(String[] args) {
		SpringApplication.run(EtladFetchUserDetailsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
	    	adFetchUserDetails.getAdFetchUserDetailsDao().updateVisionVariables("Y");
	    	adFetchUserDetails.fetchAndInsertUsers();
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			adFetchUserDetails.getAdFetchUserDetailsDao().updateVisionVariables("N");
		}
	}

}*/

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class EtladFetchUserDetailsApplication {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	AdFetchUserDetails adFetchUserDetails;

	public static void main(String[] args) {
		SpringApplication.run(EtladFetchUserDetailsApplication.class, args);
	}

}
