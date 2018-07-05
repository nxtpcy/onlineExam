package com.uestc.www.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uestc.www.common.AccessType;
import com.uestc.www.common.Constants;
import com.uestc.www.pojo.access.Access;
import com.uestc.www.service.access.AccessService;

public class UserRealm extends AuthorizingRealm {

	private final Logger logger = LoggerFactory.getLogger(UserRealm.class);

	@Autowired
	private AccessService service;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		String userId = (String) SecurityUtils.getSubject().getPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		try {
			Set<String> roles = new HashSet<String>();
			Access access = service.query(userId);
			if(access.getStatus() == AccessType.FORBID.getVal()) {
				logger.error("userId={}, status is forbid", userId);
				throw new UnauthorizedException();
			}
			String userRoles = access.getUserRole();
			if (!StringUtils.isEmpty(userRoles))
				roles.add(userRoles);
			logger.debug("role={}", roles);
			if(roles != null && roles.size() > 0)
				authorizationInfo.setRoles(roles);
		} catch (UnauthorizedException e) {
			logger.error("UnauthorizedException error", e);
		} catch (AuthorizationException e) {
			logger.error("AuthorizationException error", e);
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		String userId = (String) token.getPrincipal();
		Access access = service.query(userId);

		if (access != null && access.getStatus() == AccessType.NORMAL.getVal()) {
			SimpleAuthenticationInfo authcInfo = new SimpleAuthenticationInfo(access, access.getPassword(),
					"shiro:" + access.getUserId());
			this.setSession(Constants.CURRENT_USER, access);
			return authcInfo;
		}
		return null;
	}

	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}
