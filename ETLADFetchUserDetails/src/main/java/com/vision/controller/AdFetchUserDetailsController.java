package com.vision.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.sunoida.LicenseInfoProvider;

import com.vision.services.AdFetchUserDetails;
import com.vision.util.CommonUtils;
import com.vision.util.ValidationUtil;
import com.vision.vb.LicenseVb;
import com.vision.vb.StartupVb;

@Component
public class AdFetchUserDetailsController {

	@Autowired
	AdFetchUserDetails adFetchUserDetails;
	
	@Value("${ad.pickup.time.interval}")
	String fixedRateInterval;
	
	@Value("${sunoida.license.file.hash.key}")
	private String sunoidaLicenseFileHashKey;
	
	public static Logger logger = LoggerFactory.getLogger(AdFetchUserDetailsController.class);
	
	@Scheduled(cron = "${ad.pickup.time.interval}")
	public void fetchMetrics() {
	    try {
	    	StartupVb startupVb =  adFetchUserDetails.getAdFetchUserDetailsDao().getSchedulerStatus();
	    	if ("Y".equalsIgnoreCase(startupVb.getStatus()) && licenseStatus) {
	    		Date date = new Date();
	    		if ("Y".equalsIgnoreCase(startupVb.getDebugFlag())) {
		    		// System.out.println(date+" - AdFetchUserDetailsController Started:"+fixedRateInterval);
	    		}
		    //	adFetchUserDetails.getAdFetchUserDetailsDao().updateVisionVariables("Y");
		    	adFetchUserDetails.fetchAndInsertUsers();
		    	if ("Y".equalsIgnoreCase(startupVb.getDebugFlag())) {
			    	// System.out.println(date+" - AdFetchUserDetailsController End");
		    	}
	    	}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
		//	adFetchUserDetails.getAdFetchUserDetailsDao().updateVisionVariables("N");
		}
	    
	}
	boolean licenseStatus = true;

	@Scheduled(fixedRate = 900000, initialDelay = 15000)
	public void licenseValidation() {
		LicenseVb licenseVb = new LicenseVb();
		try {
			adFetchUserDetails.getAdFetchUserDetailsDao().getLicenseDetails(licenseVb);
			if (!ValidationUtil.isValid(licenseVb.getLicenseHashValue())
					|| !ValidationUtil.isValid(licenseVb.getLicenseKey())
					|| !ValidationUtil.isValid(licenseVb.getLicenseStr())) {
				licenseStatus = false;
				logger.info("Client Id not Maintained. - Contact System Admin");
			}
			if (!ValidationUtil.isValid(licenseVb.getLicenseHashValue())) {
				licenseStatus = false;
				logger.info("License Validation Failed. - Contact System Admin");
			}
			String decryptlicenseInfo = LicenseInfoProvider.getInfo("all", licenseVb.getLicenseHashValue(), licenseVb.getLicenseStr(), licenseVb.getLicenseKey(), sunoidaLicenseFileHashKey);
			// o=A0001||bc=A0001||d=3||cu=50||nuc=50||ad=24-May-2024||ed=23-Nov-2029||g=10||
			if (!ValidationUtil.isValid(decryptlicenseInfo)) {
				licenseStatus = false;
				logger.info("License Validation Failed. - Contact System Admin");
			} else {
				String activationDateStr = CommonUtils.getattributeValues(decryptlicenseInfo, "ad");
				String expiryDateStr = CommonUtils.getattributeValues(decryptlicenseInfo, "ed");
				int graceDays = Integer.parseInt(CommonUtils.getattributeValues(decryptlicenseInfo, "g"));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
				LocalDate activationDate = LocalDate.parse(activationDateStr, formatter);
				LocalDate expiryDate = LocalDate.parse(expiryDateStr, formatter);
				LocalDate currentDate = LocalDate.now();
				boolean isCurrentDateInRange = !currentDate.isBefore(activationDate)
						&& !currentDate.isAfter(expiryDate.plusDays(graceDays));
				if (isCurrentDateInRange) {
					licenseStatus = true;
				} else {
					licenseStatus = false;
					logger.info("License Expired. - Contact System Admin");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			licenseStatus = false;
			logger.info("License Validation Failed. - Contact System Admin");
		}
	}
}