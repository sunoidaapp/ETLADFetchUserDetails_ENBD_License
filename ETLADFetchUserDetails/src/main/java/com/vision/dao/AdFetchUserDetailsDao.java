package com.vision.dao;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.vb.BranchAttributesVb;
import com.vision.vb.LicenseVb;
import com.vision.vb.StartupVb;
import com.vision.vb.User;

@Component
public class AdFetchUserDetailsDao extends CommonDao {
	
	@Value("${app.enableDebug}")
	protected String enableDebug;
	
	@Value("${app.clientName}")
	private String clientName;

	int ERROR_OPERATION = 1;
	int SUCCESS_OPERATION = 0;
	int successCount = 0;
	int failedCount = 0;

	private static String uniqueDepartmentCombo;

	private static String databaseType;

	@Value("${app.databaseType}")
	public void setDatabaseType(String privateName) {
		AdFetchUserDetailsDao.databaseType = privateName;
	}

	public Map<String, List<BranchAttributesVb>> getLdapURlBasedOnBranch() throws DataAccessException {
		String sql = "select MACROVAR_TYPE,TAG_NAME, DISPLAY_NAME, MACROVAR_DESC from MACROVAR_TAGGING where MACROVAR_NAME = 'AD_USER_DETAILS_SERVICE' order by MACROVAR_TYPE, TAG_NO";
		printOutputStatement(sql);
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, List<BranchAttributesVb>>>() {
			@Override
			public Map<String, List<BranchAttributesVb>> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<String, List<BranchAttributesVb>> returnMap = new HashMap<String, List<BranchAttributesVb>>();
				while (rs.next()) {
					String branch = rs.getString("MACROVAR_TYPE");
					String branchAttributes = rs.getString("TAG_NAME");
					String attributeValues = rs.getString("DISPLAY_NAME");

					if (returnMap.get(branch) == null) {
						returnMap.put(branch, new ArrayList<BranchAttributesVb>(
								Arrays.asList(new BranchAttributesVb(branchAttributes, attributeValues))));
					} else {
						List<BranchAttributesVb> branchAttrVb = returnMap.get(branch);
						branchAttrVb.add(new BranchAttributesVb(branchAttributes, attributeValues));
						returnMap.put(branch, branchAttrVb);
					}
				}
				return returnMap;
			}
		});
	}

	public void truncateTable(String tableName) {
		try {
			String query = "TRUNCATE TABLE " + tableName + "";
			printOutputStatement(query);
			getJdbcTemplate().execute(query);
		} catch (Exception e) {
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			printOutputStatement("Failed to truncate table[" + tableName + "]");
			throw e;
		}
	}

	/*public void insertUserDataBatch(List<User> usersList, String country, String lebook) throws SQLException {
		int batchCount = 1000;
		String compiledQuery = "Insert Into ETL_ALERT_USER_UPL (USER_NAME, COUNTRY, LE_BOOK,"
				+ " USER_LOGIN_ID, USER_EMAIL_ID, USER_PHONE_NO,AD_ACCOUNT_STATUS_AT,"
				+ " AD_ACCOUNT_STATUS, USER_STATUS_NT, USER_STATUS,"
				+ " RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, V_DEPARTMENT_ID)"
				+ " Values (?, ?, ?, ?, ?, ?, 4506, ?, 1, 0, 7, 0, 9999, 9999, 0, "
				+ getDbFunction(Constants.SYSDATE, null) + ", " + getDbFunction(Constants.SYSDATE, null) + ", ?)";
		printOutputStatement(compiledQuery);
		try {
			final long start = System.currentTimeMillis();
			getJdbcTemplate().batchUpdate(compiledQuery, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					User user = usersList.get(i);
					ps.setString(1, user.displayName);
					ps.setString(2, country);
					ps.setString(3, lebook);
					ps.setString(4, user.sAMAccountName);
					ps.setString(5, user.mail);
					ps.setString(6, user.mobile);
					ps.setString(7, user.userAccountControl);
					
					// Form value for Department Unique Combo - Start
					StringBuffer uniqueValueBuf = new StringBuffer();
					try {
						// System.out.println("uniqueDepartmentCombo "+uniqueDepartmentCombo);
						String comboArr[] = uniqueDepartmentCombo.split(",");
						Class cls = user.getClass();
						for (String strStr : comboArr) {
							Field f = cls.getDeclaredField(strStr);
							uniqueValueBuf.append(f.get(user));
						}
					} catch (Exception e) {
						throw new SQLException("Problem with forming value for V_DEPARTMENT_ID formation - Cause ["
								+ e.getMessage() + "]");
					}
					// Form value for Department Unique Combo - End
					ps.setString(8, String.valueOf(uniqueValueBuf));
				}

				@Override
				public int getBatchSize() {
					return batchCount;
				}
			});
			final long end = System.currentTimeMillis();
			long durationinMinutes = TimeUnit.MILLISECONDS.toMinutes(end - start);
			printOutputStatement("Total time taken to insert the batch - " + durationinMinutes + " minutes");
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}
	}*/
	
	public void insertUserDataBatch(List<User> usersList, String country, String lebook) throws SQLException {
		String compiledQuery = "Insert Into ETL_ALERT_USER_UPL (USER_NAME, COUNTRY, LE_BOOK,"
				+ " USER_LOGIN_ID, USER_EMAIL_ID, USER_PHONE_NO,AD_ACCOUNT_STATUS_AT,"
				+ " AD_ACCOUNT_STATUS, USER_STATUS_NT, USER_STATUS,"
				+ " RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, V_DEPARTMENT_ID)"
				+ " Values (?, ?, ?, ?, ?, ?, 4506, ?, 1, 0, 7, 0, 9999, 9999, 0, "
				+ getDbFunction(Constants.SYSDATE, null) + ", " + getDbFunction(Constants.SYSDATE, null) + ", ?)";
		printOutputStatement(compiledQuery);
		printOutputStatement("usersList "+usersList.size());
		try {
			final long start = System.currentTimeMillis();
			getJdbcTemplate().batchUpdate(compiledQuery,usersList, 1000, (PreparedStatement ps, User user) -> {
				ps.setString(1, user.displayName);
				ps.setString(2, country);
				ps.setString(3, lebook);
				ps.setString(4, user.sAMAccountName);
				ps.setString(5, user.mail);
				ps.setString(6, user.mobile);
				ps.setString(7, user.userAccountControl);
				
				StringBuffer uniqueValueBuf = new StringBuffer();
				try {
					// System.out.println("uniqueDepartmentCombo "+uniqueDepartmentCombo);
					String comboArr[] = uniqueDepartmentCombo.split(",");
					Class cls = user.getClass();
					for (String strStr : comboArr) {
						Field f = cls.getDeclaredField(strStr);
						uniqueValueBuf.append(f.get(user));
					}
				} catch (Exception e) {
					throw new SQLException("Problem with forming value for V_DEPARTMENT_ID formation - Cause ["
							+ e.getMessage() + "]");
				}
				// Form value for Department Unique Combo - End
				ps.setString(8, String.valueOf(uniqueValueBuf));
			});
			final long end = System.currentTimeMillis();
			long durationinMinutes = TimeUnit.MILLISECONDS.toMinutes(end - start);
			printOutputStatement("Total time taken to insert the batch - " + durationinMinutes + " minutes");
		} catch (Exception e) {
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			throw e;
		}
	}

	public void insertDataToTable(String type) {
		try {
			String query = "";
			if ("PEND".equalsIgnoreCase(type)) {
				query = " insert into ETL_ALERT_USER_PEND select * from ETL_ALERT_USER";
			} else {
				query = " insert into ETL_ALERT_USER select * from ETL_ALERT_USER_UPL";
			}
			printOutputStatement(query);
			getJdbcTemplate().execute(query);
		} catch (Exception e) {
			printOutputStatement("Failed to insert data into ["
					+ (("PEND".equalsIgnoreCase(type)) ? "ETL_ALERT_USER_PEND from ETL_ALERT_USER"
							: "ETL_ALERT_USER from ETL_ALERT_USER_UPL]"));
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			throw e;
		}
	}

	public int insertPendToMainTable() {
		try {
			String query = " insert into ETL_ALERT_USER select * from ETL_ALERT_USER_PEND";
			printOutputStatement(query);
			getJdbcTemplate().execute(query);
		} catch (Exception e) {
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			return ERROR_OPERATION;
		}
		return SUCCESS_OPERATION;
	}

	public int updateVisionVariables(String updateStatus) {
		try {
			String query = " Update Vision_variables set value='" + updateStatus
					+ "' WHERE VARIABLE='AD_USER_FETCH_PROCESS_STATUS'";
			printOutputStatement(query);
			getJdbcTemplate().execute(query);
		} catch (Exception e) {
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			return ERROR_OPERATION;
		}
		return SUCCESS_OPERATION;
	}

	@Transactional(rollbackForClassName = "com.vision.exception.RuntimeCustomException")
	public int addUserList(List<User> usersList, String country, String lebook) {
		try {
			printOutputStatement("add user List ");
			printOutputStatement("usersList "+usersList.size());
			insertDataToTable("PEND");
			truncateTable("ETL_ALERT_USER_UPL");
			printOutputStatement("Insert Process Start");
			insertUserDataBatch(usersList, country, lebook);
			printOutputStatement("Insert Process End");
			printOutputStatement("Insert data in Main Table Start");
			truncateTable("ETL_ALERT_USER");
			insertDataToTable("MAIN");
			truncateTable("ETL_ALERT_USER_PEND");
			printOutputStatement("Insert data in Main Table End");
			return SUCCESS_OPERATION;
		} catch (Exception e) {
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			throw new RuntimeCustomException(e);
		}
	}

	public void printOutputStatement(String errorMsg) {
		if ("Y".equalsIgnoreCase(enableDebug)) {
			System.out.println(new Date() + " : " + errorMsg);
		}
	}

	@Value("${department.unique.combo}")
	public void setUniqueDepartmentCombo(String uniqueDepartmentCombo) {
		AdFetchUserDetailsDao.uniqueDepartmentCombo = uniqueDepartmentCombo;
	}
	
	public static String getDbFunction(String reqFunction, String val) {
		String functionName = "";
		try {
			if ("MSSQL".equalsIgnoreCase(databaseType)) {
				switch (reqFunction) {
				case "DATEFUNC":
					functionName = "FORMAT";
					break;
				case "SYSDATE":
					functionName = "GetDate()";
					break;
				case "NVL":
					functionName = "ISNULL";
					break;
				case "TIME":
					functionName = "HH:mm:ss";
					break;
				case "DD_Mon_RRRR":
					functionName = "dd-MMM-yyyy";
					break;
				case "DD_MM_YYYY":
					functionName = "dd-MM-yyyy";
					break;
				case "CONVERT":
					functionName = "CONVERT";
					break;
				case "TYPE":
					functionName = "varchar,";
					break;
				case "TIMEFORMAT":
					functionName = "108";
					break;
				case "PIPELINE":
					functionName = "+";
					break;
				case "TO_DATE":
					functionName = "CONVERT (datetime,'" + val + "', 103) ";
					break;
				case "LENGTH":
					functionName = "len";
					break;
				case "SUBSTR":
					functionName = "SUBSTRING";
					break;
				case "TO_NUMBER":
					functionName = "cast(" + val + " AS integer(38))";
					break;
				case "TO_CHAR":
					functionName = "cast(" + val + " AS varchar(4000))";
					break;
				case "DUAL":
					functionName = null;
					break;
				case "SYSTIMESTAMP":
					functionName = "SYSDATETIME()";
					break;
				case "SYSTIMESTAMP_FORMAT":
					functionName = "yyyyMMddHHmmssffffff";
					break;
				case "TO_DATE_NO_TIMESTAMP":
					functionName = "CONVERT(VARCHAR, " + val + ", 105)";
					break;
				case "TO_DATE_NO_TIMESTAMP_VAL":
					functionName = "CONVERT(VARCHAR, '" + val + "', 105) ";
					break;
				case "FN_UNIX_TIME_TO_DATE":
					functionName = "[dbo].[FN_UNIX_TIME_TO_DATE]";
					break;
				}
			} else if ("ORACLE".equalsIgnoreCase(databaseType)) {
				switch (reqFunction) {
				case "DATEFUNC":
					functionName = "TO_CHAR";
					break;
				case "SYSDATE":
					functionName = "SYSDATE";
					break;
				case "NVL":
					functionName = "NVL";
					break;
				case "TIME":
					functionName = "HH24:MI:SS";
					break;
				case "DD_Mon_RRRR":
					functionName = "DD-Mon-RRRR";
					break;
				case "DD_MM_YYYY":
					functionName = "DD-MM-YYYY";
					break;
				case "CONVERT":
					functionName = "TO_CHAR";
					break;
				case "TYPE":
					functionName = "";
					break;
				case "TIMEFORMAT":
					functionName = "'HH:MM:SS'";
					break;
				case "PIPELINE":
					functionName = "||";
				case "TO_DATE":
					functionName = "TO_DATE ('" + val + "', 'DD-Mon-YYYY HH24:MI:SS') ";
					break;
				case "LENGTH":
					functionName = "LENGTH";
					break;
				case "SUBSTR":
					functionName = "SUBSTR";
					break;
				case "TO_NUMBER":
					functionName = "TO_NUMBER(" + val + ")";
					break;
				case "TO_CHAR":
					functionName = "to_char (" + val + ")";
					break;
				case "DUAL":
					functionName = "FROM DUAL";
					break;
				case "SYSTIMESTAMP":
					functionName = "SYSTIMESTAMP";
					break;
				case "SYSTIMESTAMP_FORMAT":
					functionName = "yyyymmddhh24missff";
					break;
				case "TO_DATE_NO_TIMESTAMP":
					functionName = "TO_DATE(" + val + ", 'DD-MON-RRRR')";
					break;
				case "TO_DATE_NO_TIMESTAMP_VAL":
					functionName = "TO_DATE('" + val + "', 'DD-MM-RRRR') ";
					break;
				case "FN_UNIX_TIME_TO_DATE":
					functionName = "FN_UNIX_TIME_TO_DATE";
					break;
				}
			}
		} catch (Exception e) {
		}
		return functionName;
	}

	public StartupVb getSchedulerStatus() {
		StartupVb startupVb = new StartupVb();
		try {
			String sql = "select CRON_RUN_STATUS from PRD_CRON_CONTROL where CRON_TYPE = 'ETL_FETCH_USER_SCHEDULER' ";
			startupVb.setStatus(getJdbcTemplate().queryForObject(sql, String.class));
			sql = "select VALUE from VISION_VARIABLES where VARIABLE = 'ETL_FETCH_USER_DEBUG_FLAG' ";
			startupVb.setDebugFlag(getJdbcTemplate().queryForObject(sql, String.class));
		} catch (Exception e) {
		}
		return startupVb;
	}
	public LicenseVb getLicenseDetails(LicenseVb lUser) {
		String sql = "SELECT LICENSE_STR, LICENSE_KEY, LICENSE_HASH_VALUE FROM PRD_LICENSE_DETAILS WHERE CLIENT_ID = ? AND STATUS = 0";
		Object[] params = { clientName };
		try {
			return getJdbcTemplate().query(sql, new ResultSetExtractor<LicenseVb>() {
				@Override
				public LicenseVb extractData(ResultSet rs) throws SQLException, DataAccessException {
					while (rs.next()) {
						lUser.setLicenseStr(rs.getString("LICENSE_STR"));
						lUser.setLicenseKey(rs.getString("LICENSE_KEY"));
						lUser.setLicenseHashValue(rs.getString("LICENSE_HASH_VALUE"));
					}
					return lUser;
				}
			}, params);
		} catch (Exception e) {
			if ("Y".equalsIgnoreCase(enableDebug)) {
				e.printStackTrace();
			}
			return lUser;
		}
	}
}
