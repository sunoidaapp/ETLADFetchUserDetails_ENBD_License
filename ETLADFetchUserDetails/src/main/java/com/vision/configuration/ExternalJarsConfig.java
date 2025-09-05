package com.vision.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sunoida.LicenseInfoProvider;

//@Configuration
//public class ExternalJarsConfig {
//
//	@Value("${sunoida.license.key}")
//	private String sunoidaLicensePrivateKey;
//	
//	@Value("${sunoida.license.file.hash.key}")
//	private String sunoidaLicenseFileHashKey;
//	
//	@Value("${app.clientName}")
//	private String clientName;
//	
//	@Bean
//	public LicenseInfoProvider LicenseInfoProvider() {
//		LicenseInfoProviderConfig config = new LicenseInfoProviderConfig();
//		config.setPrivateKey(sunoidaLicensePrivateKey);
//		config.setLicenseFileName(clientName+"_"+"");
//		config.setLicenseFileLocation("");
//		config.setSftpFlag("N");
//		config.setSftpHostname("");
//		config.setSftpUsername("");
//		config.setSftpPassword("");
//		config.setSftpPort(22);
//		config.setFileHashingKey(sunoidaLicenseFileHashKey);
//		return new LicenseInfoProvider(config);
//	}
//}

@Configuration
public class ExternalJarsConfig {
	@Bean
	LicenseInfoProvider licenseInfoProvider() {
		return new LicenseInfoProvider();
	}
}
