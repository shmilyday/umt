/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.umt.services.user.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.log4j.Logger;
import org.springframework.ldap.InvalidAttributeValueException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.dao.ILdapAccessDAO;
import cn.vlabs.umt.services.user.service.ITransform;

public class LdapAccessDAOImpl implements ILdapAccessDAO {
	private static final Logger LOG = Logger.getLogger(LdapAccessDAOImpl.class);
	private LdapTemplate ldapTemplate;
	private Config config;
	private static final String aciAppAdminStr;
	private static final String aciSuperAdminStr;

	static {
		StringBuffer sbAdmin = new StringBuffer();
		sbAdmin.append("{");
		sbAdmin.append("    identificationTag \"adminRole\",");
		sbAdmin.append("    precedence 0,");
		sbAdmin.append("    authenticationLevel none,");
		sbAdmin.append("    itemOrUserFirst userFirst: ");
		sbAdmin.append("    {");
		sbAdmin.append("        userClasses ");
		sbAdmin.append("        {");
		sbAdmin.append("            userGroup { \"cn=Administrators,ou=groups,ou=system\" } ");
		sbAdmin.append("        }");
		sbAdmin.append("        ,");
		sbAdmin.append("        userPermissions ");
		sbAdmin.append("        {");
		sbAdmin.append("            {");
		sbAdmin.append("                protectedItems ");
		sbAdmin.append("                {");
		sbAdmin.append("                    allUserAttributeTypesAndValues,");
		sbAdmin.append("                    allUserAttributeTypes,");
		sbAdmin.append("                    entry ");
		sbAdmin.append("                }");
		sbAdmin.append("                ,");
		sbAdmin.append("                grantsAndDenials ");
		sbAdmin.append("                {");
		sbAdmin.append("                    grantModify,");
		sbAdmin.append("                    grantRead,");
		sbAdmin.append("                    grantCompare,");
		sbAdmin.append("                    grantAdd,");
		sbAdmin.append("                    grantRemove,");
		sbAdmin.append("                    grantRename,");
		sbAdmin.append("                    grantBrowse,");
		sbAdmin.append("                    grantFilterMatch,");
		sbAdmin.append("                    grantDiscloseOnError,");
		sbAdmin.append("                    grantInvoke,");
		sbAdmin.append("                    grantReturnDN,");
		sbAdmin.append("                    grantExport,");
		sbAdmin.append("                    grantImport ");
		sbAdmin.append("                }");
		sbAdmin.append("            }");
		sbAdmin.append("        }");
		sbAdmin.append("    }");
		sbAdmin.append("}");
		aciSuperAdminStr = sbAdmin.toString();

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("    identificationTag \"enableAllUsersRead\",");
		sb.append("    precedence 0,");
		sb.append("    authenticationLevel simple,");
		sb.append("   itemOrUserFirst userFirst: ");
		sb.append("    {");
		sb.append("        userClasses { parentOfEntry },");
		sb.append("        userPermissions ");
		sb.append("        {");
		sb.append("            {");
		sb.append("                protectedItems { entry, allUserAttributeTypes },");
		sb.append("                grantsAndDenials ");
		sb.append("                {");
		sb.append("                    grantBrowse,");
		sb.append("                    grantFilterMatch,");
		sb.append("                    grantCompare,");
		sb.append("                    grantReturnDN,");
		sb.append("                    grantRead ");
		sb.append("                }");
		sb.append("            }");
		sb.append("        }");
		sb.append("    }");
		sb.append("}");
		aciAppAdminStr = sb.toString();
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void addApp(LdapBean bean) {
		String baseDn = config.getStringProp("ldap.base.dn", "");
		if (CommonUtils.isNull(baseDn)) {
			throw new RuntimeException("why i can't found the base dn for ldap");
		}
		// 创建app
		DirContextAdapter context = new DirContextAdapter(baseDn);
		DistinguishedName name = new DistinguishedName();
		name.add("dc", bean.getRdn());
		context.setDn(name);
		context.setAttributeValues("objectClass", new String[] {
				"organization", "dcObject", "top" });
		context.setAttributeValue("dc", bean.getRdn());
		context.setAttributeValue("o", bean.getRdn());
		// TODO 修改这里，由内部来完成加密方式的选择
		context.setAttributeValue("userPassword",
				"{sha}" + bean.getLdapPassword());
		context.setAttributeValue("administrativeRole",
				"accessControlSpecificArea");
		ldapTemplate.bind(context);
		// 创建app的权限控制
		DirContextAdapter contextPriv = new DirContextAdapter(baseDn);
		name.add("cn", "readonly");
		contextPriv.setDn(name);
		contextPriv.setAttributeValues("objectClass", new String[] {
				"accessControlSubentry", "subentry", "top" });
		contextPriv.setAttributeValue("cn", "readonly");
		contextPriv.setAttributeValue("prescriptiveACI",
				aciAppAdminStr.toString());
		contextPriv.setAttributeValue("subtreeSpecification", "{}");
		ldapTemplate.bind(contextPriv);

		DirContextAdapter contextSu = new DirContextAdapter(baseDn);
		DistinguishedName suDN = new DistinguishedName();
		suDN.add("dc", bean.getRdn());
		suDN.add("cn", "su");
		contextSu.setDn(suDN);
		contextSu.setAttributeValues("objectClass", new String[] {
				"accessControlSubentry", "subentry", "top" });
		contextSu.setAttributeValue("cn", "su");
		contextSu.setAttributeValue("prescriptiveACI",
				aciSuperAdminStr.toString());
		contextSu.setAttributeValue("subtreeSpecification", "{}");
		ldapTemplate.bind(contextSu);

		// bind group -- for linux
		DirContextAdapter contextGroup = new DirContextAdapter(baseDn);
		DistinguishedName groupDN = new DistinguishedName();
		groupDN.add("dc", bean.getRdn());
		groupDN.add("gidNumber", bean.getId() + "");
		contextGroup.setDn(groupDN);
		contextGroup.setAttributeValue("cn", bean.getRdn());
		contextGroup.setAttributeValues("objectClass", new String[] {
				"posixGroup", "top" });
		ldapTemplate.bind(contextGroup);
	}

	@Override
	public void addAccount(LdapBean bean, AppSecret as, String loginName) {
		String baseDn = config.getStringProp("ldap.base.dn", "");
		if (CommonUtils.isNull(baseDn)) {
			throw new RuntimeException("why i can't found the base dn for ldap");
		}
		DirContextAdapter context = new DirContextAdapter(baseDn);
		DistinguishedName name = new DistinguishedName();
		name.add("dc", bean.getRdn());
		name.add("uid", loginName);
		context.setDn(name);
		context.setAttributeValues("objectClass", new String[] {
				"inetOrgPerson", "organizationalPerson", "posixAccount",
				"person", "radiusAccount" });
		context.setAttributeValue("cn", as.getUserCstnetId());
		context.setAttributeValue("sn", as.getUserName());
		context.setAttributeValue("homeDirectory",
				"/home/" + loginName);
		context.setAttributeValue("gidNumber", Integer.toString(bean.getId()));
		context.setAttributeValue("uidNumber", Integer.toString(as.getUid()));
		context.setAttributeValue("loginShell", "/bin/bash");
		context.setAttributeValue("userPassword",
				"{sha}" + as.getHashedSecret(ITransform.TYPE_SHA));
		context.setAttributeValue("ntPassword",as.getHashedSecret(ITransform.TYPE_NT_HASH));
		ldapTemplate.bind(context);
	}

	@Override
	public void removeApp(String rdn) {

		try {
			DistinguishedName readonly = new DistinguishedName();
			readonly.add("dc", rdn);
			readonly.add("cn", "readonly");
			ldapTemplate.unbind(readonly, true);
		} catch (RuntimeException e) {
			LOG.error("app readonly role[" + rdn + "] not found");
		}
		try {
			DistinguishedName su = new DistinguishedName();
			su.add("dc", rdn);
			su.add("cn", "su");
			ldapTemplate.unbind(su, true);
		} catch (RuntimeException e) {
			LOG.error("app su role[" + rdn + "] not found");
		}
		try {
			DistinguishedName name = new DistinguishedName();
			name.add("dc", rdn);
			ldapTemplate.unbind(name, true);
		} catch (RuntimeException e) {
			LOG.error("app [" + rdn + "] not found");
		}
	}

	@Override
	public boolean updateSecret(String rdn, String orginLoginName, AppSecret secret) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("dc", rdn);
		dn.add("uid",secret.getUserLdapName());
		try {
			ModificationItem modifyUserPassword = new ModificationItem(
					DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userPassword", "{sha}"
							+ secret.getHashedSecret(ITransform.TYPE_SHA)));
			ModificationItem modifyNtPassword = new ModificationItem(
					DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("ntPassword", secret.getHashedSecret(ITransform.TYPE_NT_HASH)));
			ModificationItem modifyLdapName= new ModificationItem(
						DirContext.REPLACE_ATTRIBUTE,
						new BasicAttribute("uid", secret.getUserLdapName()));
			Set<String> classSet = searchObjectClass(orginLoginName);
			if (!classSet.contains("radiusAccount")){
				ModificationItem addRadiusAccount = new ModificationItem(
						DirContext.ADD_ATTRIBUTE,
						new BasicAttribute("objectClass", "radiusAccount"));
				ldapTemplate.modifyAttributes(dn,
						new ModificationItem[] {addRadiusAccount,modifyLdapName, modifyUserPassword,modifyNtPassword});
			}else{
				ldapTemplate.modifyAttributes(dn,
						new ModificationItem[] {modifyUserPassword,modifyLdapName, modifyNtPassword});
			}
			return true;
		} catch (InvalidAttributeValueException e) {
			return false;
		}
	}

	@Override
	public void removeSoAccount(String rdn, String loginName) {
		DistinguishedName dn = new DistinguishedName();
		dn.add("dc", rdn);
		dn.add("uid", loginName);

		try {
			ldapTemplate.unbind(dn, true);
		} catch (RuntimeException e) {
			LOG.info("user [" + rdn + "] not found");
		}
	}
	@SuppressWarnings("unchecked")
	private Set<String> searchObjectClass(String ldapUid){
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("objectClass", "inetOrgPerson"));
		af.and(new EqualsFilter("uid", ldapUid));
		List<Object[]> list=ldapTemplate.search("", af.encode(),
				SearchControls.SUBTREE_SCOPE, new ContextMapper() {
					@Override
					public Object mapFromContext(Object arg0) {
						DirContextAdapter adap = (DirContextAdapter) arg0;
						return adap.getObjectAttributes("objectClass");
					}
				});
		HashSet<String> classSet = new HashSet<String>();
		for(Object[] attr:list){
			if (attr!=null){
				for (Object objectClass:attr){
					classSet.add((String)objectClass);
				}
			}
		}
		return classSet;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> searchDn(String ldapUid) {
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("objectClass", "inetOrgPerson"));
		af.and(new EqualsFilter("uid", ldapUid));
		return ldapTemplate.search("", af.encode(),
				SearchControls.SUBTREE_SCOPE, new ContextMapper() {
					@Override
					public Object mapFromContext(Object arg0) {
						DirContextAdapter adap = (DirContextAdapter) arg0;
						return adap.getDn().toString();
					}
				});
	}

	@Override
	public void removeByDn(String dn) {
		ldapTemplate.unbind(dn);
	}
}
