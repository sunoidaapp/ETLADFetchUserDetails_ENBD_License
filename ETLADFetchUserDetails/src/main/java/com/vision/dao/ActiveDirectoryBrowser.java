package com.vision.dao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

import com.vision.vb.User;

// 
// Decompiled by Procyon v0.5.36
// 

public class ActiveDirectoryBrowser
{
    private String ldapUrl;
    private String principle;
    private String password;
    private String organizationalUnit;
    
    public ActiveDirectoryBrowser(final String ldapUrl, final String principle, final String password, final String organizationalUnit) {
        this.ldapUrl = ldapUrl;
        this.principle = principle;
        this.password = password;
        this.organizationalUnit = organizationalUnit;
    }
    
    public List<User> getUser(final String query) throws Exception {
        final String[] returnedAtts = { "distinguishedName", "sAMAccountName", "userPrincipalName", "displayName", "cn", "sn", "givenName", "mail", "department", "company", "manager", "telephoneNumber", "physicalDeliveryOfficeName", "sex", "division", "memberOf" };
        final SearchControls searchContext = new SearchControls(2, 0L, 0, returnedAtts, false, false);
        final List<User> users = new ArrayList<User>();
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(this.getConnectionSettings(), null);
            final NamingEnumeration<SearchResult> results = ctx.search(this.organizationalUnit, "(&(objectClass=user)" + query + ")", searchContext);
            while (results.hasMoreElements()) {
                final SearchResult item = results.next();
                final Attributes metadata = item.getAttributes();
                final NamingEnumeration<String> attributes = metadata.getIDs();
                final List<String> availableValues = new ArrayList<String>();
                while (attributes.hasMoreElements()) {
                    availableValues.add(attributes.next());
                }
                final User u = new User();
                u.distinguishedName = (availableValues.contains("distinguishedName") ? String.valueOf(metadata.get("distinguishedName").get()) : "");
                u.givenName = (availableValues.contains("givenName") ? String.valueOf(metadata.get("givenName").get()) : "");
                u.initials = (availableValues.contains("initials") ? String.valueOf(metadata.get("initials").get()) : "");
                u.sn = (availableValues.contains("sn") ? String.valueOf(metadata.get("sn").get()) : "");
                u.userPrincipalName = (availableValues.contains("userPrincipalName") ? String.valueOf(metadata.get("userPrincipalName").get()) : "");
                u.sAMAccountName = (availableValues.contains("sAMAccountName") ? String.valueOf(metadata.get("sAMAccountName").get()) : "");
                u.displayName = (availableValues.contains("displayName") ? String.valueOf(metadata.get("displayName").get()) : "");
                u.cn = (availableValues.contains("name") ? String.valueOf(metadata.get("name").get()) : "");
                u.description = (availableValues.contains("description") ? String.valueOf(metadata.get("description").get()) : "");
                u.physicalDeliveryOfficeName = (availableValues.contains("physicalDeliveryOfficeName") ? String.valueOf(metadata.get("physicalDeliveryOfficeName").get()) : "");
                u.telephoneNumber = (availableValues.contains("telephoneNumber") ? String.valueOf(metadata.get("telephoneNumber").get()) : "");
                u.mail = (availableValues.contains("mail") ? String.valueOf(metadata.get("mail").get()) : "");
                u.wWWHomePage = (availableValues.contains("wWWHomePage") ? String.valueOf(metadata.get("wWWHomePage").get()) : "");
                u.password = (availableValues.contains("password") ? String.valueOf(metadata.get("password").get()) : "");
                u.streetAddress = (availableValues.contains("streetAddress") ? String.valueOf(metadata.get("streetAddress").get()) : "");
                u.postOfficeBox = (availableValues.contains("postOfficeBox") ? String.valueOf(metadata.get("postOfficeBox").get()) : "");
                u.l = (availableValues.contains("l") ? String.valueOf(metadata.get("l").get()) : "");
                u.st = (availableValues.contains("st") ? String.valueOf(metadata.get("st").get()) : "");
                u.postalCode = (availableValues.contains("postalCode") ? String.valueOf(metadata.get("postalCode").get()) : "");
                u.co = (availableValues.contains("co") ? String.valueOf(metadata.get("co").get()) : "");
                u.c = (availableValues.contains("c") ? String.valueOf(metadata.get("c").get()) : "");
                u.countryCode = (availableValues.contains("countryCode") ? String.valueOf(metadata.get("countryCode").get()) : "");
                u.memberOf = (availableValues.contains("memberOf") ? String.valueOf(metadata.get("memberOf").get()) : "");
                u.accountExpires = (availableValues.contains("accountExpires") ? String.valueOf(metadata.get("accountExpires").get()) : "");
                u.userAccountControl = (availableValues.contains("userAccountControl") ? String.valueOf(metadata.get("userAccountControl").get()) : "");
                u.profilePath = (availableValues.contains("profilePath") ? String.valueOf(metadata.get("profilePath").get()) : "");
                u.scriptPath = (availableValues.contains("scriptPath") ? String.valueOf(metadata.get("scriptPath").get()) : "");
                u.homeDirectory = (availableValues.contains("homeDirectory") ? String.valueOf(metadata.get("homeDirectory").get()) : "");
                u.homeDrive = (availableValues.contains("homeDrive") ? String.valueOf(metadata.get("homeDrive").get()) : "");
                u.userWorkstations = (availableValues.contains("userWorkstations") ? String.valueOf(metadata.get("userWorkstations").get()) : "");
                u.homePhone = (availableValues.contains("homePhone") ? String.valueOf(metadata.get("homePhone").get()) : "");
                u.pager = (availableValues.contains("pager") ? String.valueOf(metadata.get("pager").get()) : "");
                u.mobile = (availableValues.contains("mobile") ? String.valueOf(metadata.get("mobile").get()) : "");
                u.facsimileTelephoneNumber = (availableValues.contains("facsimileTelephoneNumber") ? String.valueOf(metadata.get("facsimileTelephoneNumber").get()) : "");
                u.ipPhone = (availableValues.contains("ipPhone") ? String.valueOf(metadata.get("ipPhone").get()) : "");
                u.info = (availableValues.contains("info") ? String.valueOf(metadata.get("info").get()) : "");
                u.title = (availableValues.contains("title") ? String.valueOf(metadata.get("title").get()) : "");
                u.department = (availableValues.contains("department") ? String.valueOf(metadata.get("department").get()) : "");
                u.company = (availableValues.contains("company") ? String.valueOf(metadata.get("company").get()) : "");
                u.manager = (availableValues.contains("manager") ? String.valueOf(metadata.get("manager").get()) : "");
                u.mailNickName = (availableValues.contains("mailNickName") ? String.valueOf(metadata.get("mailNickName").get()) : "");
                u.displayNamePrintable = (availableValues.contains("displayNamePrintable") ? String.valueOf(metadata.get("displayNamePrintable").get()) : "");
                u.msExchHideFromAddressLists = (availableValues.contains("msExchHideFromAddressLists") ? String.valueOf(metadata.get("msExchHideFromAddressLists").get()) : "");
                u.submissionContLength = (availableValues.contains("submissionContLength") ? String.valueOf(metadata.get("submissionContLength").get()) : "");
                u.delivContLength = (availableValues.contains("delivContLength") ? String.valueOf(metadata.get("delivContLength").get()) : "");
                u.msExchRequireAuthToSendTo = (availableValues.contains("msExchRequireAuthToSendTo") ? String.valueOf(metadata.get("msExchRequireAuthToSendTo").get()) : "");
                u.unauthOrig = (availableValues.contains("unauthOrig") ? String.valueOf(metadata.get("unauthOrig").get()) : "");
                u.authOrig = (availableValues.contains("authOrig") ? String.valueOf(metadata.get("authOrig").get()) : "");
                u.publicDelegates = (availableValues.contains("publicDelegates") ? String.valueOf(metadata.get("publicDelegates").get()) : "");
                u.altRecipient = (availableValues.contains("altRecipient") ? String.valueOf(metadata.get("altRecipient").get()) : "");
                u.deliverAndRedirect = (availableValues.contains("deliverAndRedirect") ? String.valueOf(metadata.get("deliverAndRedirect").get()) : "");
                u.msExchRecipLimit = (availableValues.contains("msExchRecipLimit") ? String.valueOf(metadata.get("msExchRecipLimit").get()) : "");
                u.mDBuseDefaults = (availableValues.contains("mDBuseDefaults") ? String.valueOf(metadata.get("mDBuseDefaults").get()) : "");
                u.mDBStorageQuota = (availableValues.contains("mDBStorageQuota") ? String.valueOf(metadata.get("mDBStorageQuota").get()) : "");
                u.mDBOverQuotaLimit = (availableValues.contains("mDBOverQuotaLimit") ? String.valueOf(metadata.get("mDBOverQuotaLimit").get()) : "");
                u.mDBOverHardQuotaLimit = (availableValues.contains("mDBOverHardQuotaLimit") ? String.valueOf(metadata.get("mDBOverHardQuotaLimit").get()) : "");
                u.deletedItemFlags = (availableValues.contains("deletedItemFlags") ? String.valueOf(metadata.get("deletedItemFlags").get()) : "");
                u.garbageCollPeriod = (availableValues.contains("garbageCollPeriod") ? String.valueOf(metadata.get("garbageCollPeriod").get()) : "");
                u.msExchOmaAdminWirelessEnable = (availableValues.contains("msExchOmaAdminWirelessEnable") ? String.valueOf(metadata.get("msExchOmaAdminWirelessEnable").get()) : "");
                u.protocolSettings = (availableValues.contains("protocolSettings") ? String.valueOf(metadata.get("protocolSettings").get()) : "");
                u.tsAllowLogon = (availableValues.contains("tsAllowLogon") ? String.valueOf(metadata.get("tsAllowLogon").get()) : "");
                u.tsProfilePath = (availableValues.contains("tsProfilePath") ? String.valueOf(metadata.get("tsProfilePath").get()) : "");
                u.tsHomeDir = (availableValues.contains("tsHomeDir") ? String.valueOf(metadata.get("tsHomeDir").get()) : "");
                u.tsHomeDirDrive = (availableValues.contains("tsHomeDirDrive") ? String.valueOf(metadata.get("tsHomeDirDrive").get()) : "");
                u.tsInheritInitialProgram = (availableValues.contains("tsInheritInitialProgram") ? String.valueOf(metadata.get("tsInheritInitialProgram").get()) : "");
                u.tsIntialProgram = (availableValues.contains("tsIntialProgram") ? String.valueOf(metadata.get("tsIntialProgram").get()) : "");
                u.tsWorkingDir = (availableValues.contains("tsWorkingDir") ? String.valueOf(metadata.get("tsWorkingDir").get()) : "");
                u.tsDeviceClientDrives = (availableValues.contains("tsDeviceClientDrives") ? String.valueOf(metadata.get("tsDeviceClientDrives").get()) : "");
                u.tsDeviceClientPrinters = (availableValues.contains("tsDeviceClientPrinters") ? String.valueOf(metadata.get("tsDeviceClientPrinters").get()) : "");
                u.tsDeviceClientDefaultPrinter = (availableValues.contains("tsDeviceClientDefaultPrinter") ? String.valueOf(metadata.get("tsDeviceClientDefaultPrinter").get()) : "");
                u.tsTimeOutSettingsDisConnections = (availableValues.contains("tsTimeOutSettingsDisConnections") ? String.valueOf(metadata.get("tsTimeOutSettingsDisConnections").get()) : "");
                u.tsTimeOutSettingsConnections = (availableValues.contains("tsTimeOutSettingsConnections") ? String.valueOf(metadata.get("tsTimeOutSettingsConnections").get()) : "");
                u.tsTimeOutSettingsIdle = (availableValues.contains("tsTimeOutSettingsIdle") ? String.valueOf(metadata.get("tsTimeOutSettingsIdle").get()) : "");
                u.tsBrokenTimeOutSettings = (availableValues.contains("tsBrokenTimeOutSettings") ? String.valueOf(metadata.get("tsBrokenTimeOutSettings").get()) : "");
                u.tsReConnectSettings = (availableValues.contains("tsReConnectSettings") ? String.valueOf(metadata.get("tsReConnectSettings").get()) : "");
                u.tsShadowSettings = (availableValues.contains("tsShadowSettings") ? String.valueOf(metadata.get("tsShadowSettings").get()) : "");
                u.preventDeletion = (availableValues.contains("preventDeletion") ? String.valueOf(metadata.get("preventDeletion").get()) : "");
                u.managerCanUpdateMembers = (availableValues.contains("managerCanUpdateMembers") ? String.valueOf(metadata.get("managerCanUpdateMembers").get()) : "");
                u.primaryGroupID = (availableValues.contains("primaryGroupID") ? String.valueOf(metadata.get("primaryGroupID").get()) : "");
                u.msExchAdminGroup = (availableValues.contains("msExchAdminGroup") ? String.valueOf(metadata.get("msExchAdminGroup").get()) : "");
                u.msExchHomeServerName = (availableValues.contains("msExchHomeServerName") ? String.valueOf(metadata.get("msExchHomeServerName").get()) : "");
                u.managedBy = (availableValues.contains("managedBy") ? String.valueOf(metadata.get("managedBy").get()) : "");
                u.targetAddress = (availableValues.contains("targetAddress") ? String.valueOf(metadata.get("targetAddress").get()) : "");
                u.proxyAddresses = (availableValues.contains("proxyAddresses") ? String.valueOf(metadata.get("proxyAddresses").get()) : "");
                u.msExchPoliciesExcluded = (availableValues.contains("msExchPoliciesExcluded") ? String.valueOf(metadata.get("msExchPoliciesExcluded").get()) : "");
                u.GroupMemberObjectId = (availableValues.contains("GroupMemberObjectId") ? String.valueOf(metadata.get("GroupMemberObjectId").get()) : "");
                u.LitigationHoldEnabled = (availableValues.contains("LitigationHoldEnabled") ? String.valueOf(metadata.get("LitigationHoldEnabled").get()) : "");
                u.LitigationHoldDuration = (availableValues.contains("LitigationHoldDuration") ? String.valueOf(metadata.get("LitigationHoldDuration").get()) : "");
                u.InPlaceArchive = (availableValues.contains("InPlaceArchive") ? String.valueOf(metadata.get("InPlaceArchive").get()) : "");
                u.ArchiveName = (availableValues.contains("ArchiveName") ? String.valueOf(metadata.get("ArchiveName").get()) : "");
                u.O365userPrincipalName = (availableValues.contains("O365userPrincipalName") ? String.valueOf(metadata.get("O365userPrincipalName").get()) : "");
                final String[] strings = u.distinguishedName.split(",");
                String[] array;
                for (int length = (array = strings).length, i = 0; i < length; ++i) {
                    final String s = array[i];
                }
                users.add(u);
            }
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
        if (ctx != null) {
            ctx.close();
        }
        return users;
    }
    
    private Hashtable<String, String> getConnectionSettings() {
        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", this.ldapUrl);
        env.put("java.naming.security.authentication", "simple");
        env.put("java.naming.security.principal", this.principle);
        env.put("java.naming.security.credentials", this.password);
        env.put("java.naming.ldap.attributes.binary", "tokenGroups");
        return env;
    }
    
    private static String binarySidToStringSid(final byte[] SID) {
        String strSID = "";
        strSID = "S";
        final long version = SID[0];
        strSID = String.valueOf(strSID) + "-" + Long.toString(version);
        long authority = SID[4];
        for (int i = 0; i < 4; ++i) {
            authority <<= 8;
            authority += (SID[4 + i] & 0xFF);
        }
        strSID = String.valueOf(strSID) + "-" + Long.toString(authority);
        long count = SID[2];
        count <<= 8;
        count += (SID[1] & 0xFF);
        for (int j = 0; j < count; ++j) {
            long rid = SID[11 + j * 4] & 0xFF;
            for (int k = 1; k < 4; ++k) {
                rid <<= 8;
                rid += (SID[11 - k + j * 4] & 0xFF);
            }
            strSID = String.valueOf(strSID) + "-" + Long.toString(rid);
        }
        return strSID;
    }
    
    public static boolean isValid(final String pInput) {
        return pInput != null && pInput.trim().length() != 0 && !"".equals(pInput);
    }
    
    private User toUser(final Attributes attributes) throws NamingException {
        if (attributes != null) {
            final String distinguishedName = (attributes.get("distinguishedName") != null) ? attributes.get("distinguishedName").get().toString() : "";
            final String userPrincipalName = (attributes.get("userPrincipalName") != null) ? attributes.get("userPrincipalName").get().toString() : "";
            final String mail = (attributes.get("mail") != null) ? attributes.get("mail").get().toString() : "";
            final String staffId = (attributes.get("description") != null) ? attributes.get("description").get().toString() : "";
            final String sAMAccountName = (attributes.get("sAMAccountName") != null) ? attributes.get("sAMAccountName").get().toString() : "";
            String userName = (attributes.get("cn") != null) ? attributes.get("cn").get().toString() : "";
            final String country = (attributes.get("c") != null) ? attributes.get("c").get().toString() : "";
            String accountExpires = (attributes.get("accountExpires") != null) ? attributes.get("accountExpires").get().toString() : "";
            final String useraccountcontrol = (attributes.get("useraccountcontrol") != null) ? attributes.get("useraccountcontrol").get().toString() : "";
            final String displayName = (attributes.get("name") != null) ? attributes.get("name").get().toString() : null;
            final String memberOf = (attributes.get("memberOf") != null) ? attributes.get("memberOf").get().toString() : null;
            final String department = (attributes.get("department") != null) ? attributes.get("department").get().toString() : null;
            final String mobile = (attributes.get("mobile") != null) ? attributes.get("mobile").get().toString() : null;
            final String telephoneNumber = (attributes.get("telephoneNumber") != null) ? attributes.get("telephoneNumber").get().toString() : null;
            final String physicalDeliveryOfficeName = (attributes.get("physicalDeliveryOfficeName") != null) ? attributes.get("physicalDeliveryOfficeName").get().toString() : null;
            final String streetAddress = (attributes.get("streetAddress") != null) ? attributes.get("streetAddress").get().toString() : null;
            final String ipPhone = (attributes.get("ipPhone") != null) ? attributes.get("ipPhone").get().toString() : null;
            final String title = (attributes.get("title") != null) ? attributes.get("title").get().toString() : null;
            final String mailNickName = (attributes.get("mailNickName") != null) ? attributes.get("mailNickName").get().toString() : null;
            if (isValid(accountExpires)) {
                final String pattern = "dd-MMM-yyyy HH:mm:ss";
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                final long fileTime = Long.parseLong(accountExpires) / 10000L - 11644473600000L;
                final Date inputDate = new Date(fileTime);
                accountExpires = simpleDateFormat.format(inputDate);
            }
            if (userPrincipalName != null) {
                final String[] user = userPrincipalName.split("@");
                if (user != null && user.length > 0) {
                    userName = user[0];
                }
            }
            final User user2 = new User();
            user2.mail = mail;
            user2.cn = userName;
            user2.description = staffId;
            user2.sAMAccountName = sAMAccountName;
            user2.co = country;
            user2.accountExpires = accountExpires.replaceAll("30828", "9999");
            user2.userAccountControl = useraccountcontrol;
            user2.displayName = displayName;
            user2.distinguishedName = distinguishedName;
            user2.memberOf = memberOf;
            user2.department = department;
            user2.mobile = mobile;
            user2.telephoneNumber = telephoneNumber;
            user2.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
            user2.streetAddress = streetAddress;
            user2.ipPhone = ipPhone;
            user2.title = title;
            user2.mailNickName = mailNickName;
            return user2;
        }
        return null;
    }
    
    public List<User> getUserNew(final String query) throws Exception {
        final String[] returnedAtts = { "distinguishedName", "sAMAccountName", "userPrincipalName", "displayName", "cn", "sn", "givenName", "mail", "department", "company", "manager", "telephoneNumber", "physicalDeliveryOfficeName", "sex", "division", "memberOf", "mobile", "streetAddress", "ipPhone", "title", "mailNickName" };
        final SearchControls searchContext = new SearchControls(2, 0L, 0, returnedAtts, false, false);
        final List<User> users = new ArrayList<User>();
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(this.getConnectionSettings(), null);
            final int pageSize = 1000;
            byte[] cookie = null;
            ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, false) });
            do {
                final SearchControls sc = new SearchControls();
                sc.setSearchScope(2);
                final String filtro = "(&(sAMAccountName=*)&(objectClass=user))";
                final NamingEnumeration results1 = ctx.search(this.organizationalUnit, "(&(objectClass=user)" + query + ")", sc);
                while (results1.hasMoreElements()) {
                    final SearchResult result = (SearchResult)results1.nextElement();
                    final Attributes attributes = result.getAttributes();
                    final User user = this.toUser(attributes);
                    final StringBuffer memberofGroups = new StringBuffer();
                    int count = 0;
                    final List<String> groups = this.getUserGroups(user);
                    for (final String userGroup : groups) {
                        memberofGroups.append(userGroup);
                        if (count + 1 < groups.size()) {
                            memberofGroups.append(", ");
                        }
                        ++count;
                    }
                    user.memberOf = memberofGroups.toString();
                    users.add(user);
                }
                final Control[] controls = ctx.getResponseControls();
                if (controls != null) {
                    for (int i = 0; i < controls.length; ++i) {
                        if (controls[i] instanceof PagedResultsResponseControl) {
                            final PagedResultsResponseControl prrc = (PagedResultsResponseControl)controls[i];
                            final int total = prrc.getResultSize();
                            if (total != 0) {
                                // System.out.println("***************** END-OF-PAGE (total : " + total + ") *****************\n");
                            }
                            cookie = prrc.getCookie();
                        }
                    }
                }
                else {
                    // System.out.println("No controls were sent from the server");
                }
                ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, true) });
            } while (cookie != null);
            ctx.close();
        }
        catch (NamingException e) {
            System.err.println("PagedSearch failed.");
            // e.printStackTrace();
        }
        catch (IOException ie) {
            System.err.println("PagedSearch failed.");
            ie.printStackTrace();
        }
        catch (Exception ie2) {
            System.err.println("PagedSearch failed.");
            ie2.printStackTrace();
        }
       //  System.out.println("listUsers() size = " + users.size());
        return users;
    }
    
    public List<String> getUserGroups(final User user) throws Exception {
        final List<String> groups = new ArrayList<String>();
        final String[] returnedAtts = { "tokenGroups" };
        final SearchControls searchContext = new SearchControls(0, 0L, 0, returnedAtts, false, false);
        final StringBuffer groupsSearchFilter = new StringBuffer();
        groupsSearchFilter.append("(|");
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(this.getConnectionSettings(), null);
            final NamingEnumeration<SearchResult> results = ctx.search(user.distinguishedName.replaceAll("/", " "), "(&(objectClass=user))", searchContext);
            while (results.hasMoreElements()) {
                final SearchResult item = results.next();
                final Attributes metadata = item.getAttributes();
                final Attribute attribute = metadata.get("tokenGroups");
                final NamingEnumeration<?> tokens = attribute.getAll();
                while (tokens.hasMore()) {
                    final byte[] sid = (byte[])tokens.next();
                    groupsSearchFilter.append("(objectSid=" + binarySidToStringSid(sid) + ")");
                }
            }
            groupsSearchFilter.append(")");
            final SearchControls groupsSearchCtls = new SearchControls();
            groupsSearchCtls.setSearchScope(2);
            final String[] groupsReturnedAtts = { "sAMAccountName" };
            groupsSearchCtls.setReturningAttributes(groupsReturnedAtts);
            final NamingEnumeration<?> groupsAnswer = ctx.search(this.organizationalUnit, groupsSearchFilter.toString(), groupsSearchCtls);
            while (groupsAnswer.hasMoreElements()) {
                final SearchResult sr = (SearchResult)groupsAnswer.next();
                final Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    groups.add(String.valueOf(attrs.get("sAMAccountName").get()));
                }
            }
        }
        catch (Exception e) {
            // System.out.println(String.valueOf(user.description) + " Error Message [" + e.getMessage() + "]");
            return groups;
        }
        finally {
            if (ctx != null) {
                ctx.close();
            }
        }
        if (ctx != null) {
            ctx.close();
        }
        return groups;
    }
}
