package com.vision.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vision.dao.ActiveDirectoryBrowser;
import com.vision.dao.AdFetchUserDetailsDao;
import com.vision.exception.ExceptionCode;
import com.vision.util.Constants;
import com.vision.vb.BranchAttributesVb;
import com.vision.vb.User;

@Service
public class AdFetchUserDetails {
	
	@Value("${app.enableDebug}")
	protected String enableDebug;
	
	@Autowired
	AdFetchUserDetailsDao AdFetchUserDetailsDao;
	
	public AdFetchUserDetailsDao getAdFetchUserDetailsDao() {
		return AdFetchUserDetailsDao;
	}

	public static boolean isValid(Object pInput) {
		return !(pInput == null);
	}
	
	private boolean isValid(String pInput) {
		return !((pInput == null) || (pInput.trim().length() == 0) || ("".equals(pInput)));
	}
	
	public ExceptionCode fetchAndInsertUsers() {
		ExceptionCode exceptionCode = new ExceptionCode();
		getAdFetchUserDetailsDao().printOutputStatement("Start");
		Map<String, List<BranchAttributesVb>> LdapURLs = getAdFetchUserDetailsDao().getLdapURlBasedOnBranch();
		if (LdapURLs != null && LdapURLs.size() == 0) {
			getAdFetchUserDetailsDao().printOutputStatement(
					"Maintenance not found in the table Macrovar_Tagging[AD_USER_DETAILS_SERVICE] ");
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			return exceptionCode;
		}
		for (Map.Entry<String, List<BranchAttributesVb>> entry : LdapURLs.entrySet()) {
			String country = "";
			String leBook = "";
			String domainName = "";
			String lDapUrl = "";
			String organizationalUnit = "";
			String username = "";
			String password = "";
			try {
				for (BranchAttributesVb branchAttrVb : entry.getValue()) {
					switch (branchAttrVb.getAttribute().toUpperCase()) {
					case "URL":
						lDapUrl = branchAttrVb.getAttributeValue();
						break;
					case "COUNTRY":
						country = branchAttrVb.getAttributeValue();
						break;
					case "LE_BOOK":
						leBook = branchAttrVb.getAttributeValue();
						break;
					case "USERNAME":
						username = branchAttrVb.getAttributeValue();
						break;
					case "PASSWORD":
						password = branchAttrVb.getAttributeValue();
						break;
					case "DOMAIN":
						domainName = branchAttrVb.getAttributeValue();
						break;
					case "ORG_UNIT":
						organizationalUnit = branchAttrVb.getAttributeValue();
						break;
					default:
						break;
					}
				}
				if (!isValid(lDapUrl) || !isValid(country) || !isValid(leBook) || !isValid(username)
						|| !isValid(password) || !isValid(domainName) || !isValid(organizationalUnit)) {
					getAdFetchUserDetailsDao().printOutputStatement(
							"Data not maintained properly in the table Macrovar_Tagging[AD_USER_DETAILS_SERVICE]");
					break;
				}
				getAdFetchUserDetailsDao().printOutputStatement("Proceed to AD User detail Fetch");
				getAdFetchUserDetailsDao().printOutputStatement("Country[" + country + "]LE Book[" + leBook
						+ "]User Name[" + username + "]Password[******]Url[" + lDapUrl + "]Domain[" + domainName
						+ "]Organizational Unit[" + organizationalUnit + "]");
				final ActiveDirectoryBrowser ad = new ActiveDirectoryBrowser(lDapUrl,
						String.valueOf(username) + "@" + domainName, password, organizationalUnit);
				final String filter = "(&(objectclass=user)(description=*))";
				List<User> usersList = ad.getUserNew(filter);
				getAdFetchUserDetailsDao().printOutputStatement("usersList " + usersList.size());
				if (usersList != null && usersList.size() == 0) {
					getAdFetchUserDetailsDao().printOutputStatement("Unable to get User Information from AD");
				} else {
					getAdFetchUserDetailsDao().printOutputStatement(
							"User details fetch completed from AD; Fetch Count[" + usersList.size() + "]");
					getAdFetchUserDetailsDao().addUserList(usersList, country, leBook);
				}
			} catch (Exception e) {
				if ("Y".equalsIgnoreCase(enableDebug)) {
					e.printStackTrace();
				}
			}
		}
		return exceptionCode;
	}
}
