package com.uestc.www.shiro.filter;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uestc.www.common.Constants;
import com.uestc.www.shiro.session.OnlineSession;
import com.uestc.www.shiro.session.OnlineSession.OnlineStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControlFilter extends AccessControlFilter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private boolean kickoutAfter = false; // 踢出之前登录的/之后登录的用户, 默认踢出之前登录的用户
	private int maxSession = 1; // 同一个帐号最大会话数 默认1

	private SessionManager sessionManager;
	private Cache<String, Deque<Serializable>> cache;

	@Autowired
	private SessionDAO sessionDAO;

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	// 如果isAccessAllowed返回false,触发onAccessDenied
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		logger.info("-------------------------");
		logger.info("request={}, mappedValue={}", request, mappedValue);
		Subject subject = getSubject(request, response);
		if (subject == null || subject.getSession(false) == null) {
			return true;
		}
		Session session = sessionDAO.readSession(subject.getSession().getId());
		if (session != null && session instanceof OnlineSession) {
			OnlineSession onlineSession = (OnlineSession) session;
			request.setAttribute(Constants.ONLINE_SESSION, onlineSession);
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			// 如果没有登录，直接进行之后的流程
			return true;
		}

		OnlineSession session = (OnlineSession) subject.getSession();
		String username = (String) subject.getPrincipal();
		Serializable sessionId = session.getId();

		synchronized (username) {
			Deque<Serializable> deque = cache.get(username);
			if (deque == null) {
				deque = new LinkedList<Serializable>();
				cache.put(username, deque);
			}

			// 如果队列里没有此sessionId，且用户没有被踢出；放入队列
			if (!deque.contains(sessionId) && session.getStatus() != OnlineStatus.KICKOUT) {
				deque.push(sessionId);
			}

			// 如果队列里的sessionId数超出最大会话数，开始踢人
			while (deque.size() > maxSession) {
				Serializable kickoutSessionId = null;
				if (kickoutAfter) { // 如果踢出后者
					kickoutSessionId = deque.removeFirst();
				} else { // 否则踢出前者
					kickoutSessionId = deque.removeLast();
				}
				try {
					OnlineSession kickoutSession = (OnlineSession) sessionManager
							.getSession(new DefaultSessionKey(kickoutSessionId));
					if (kickoutSession != null) {
						// 设置会话的kickout属性表示踢出了
						kickoutSession.setStatus(OnlineStatus.KICKOUT);
						request.setAttribute(Constants.KICKOUT_SESSION, kickoutSession);
					}
				} catch (Exception e) {// ignore exception
				}
			}
		}

		// 如果被踢出了，直接退出，重定向到踢出后的地址
		if (session.getStatus() == OnlineStatus.KICKOUT) {
			try {
				subject.logout();
			} catch (Exception e) { // ignore
			}
			saveRequest(request);
			return false;
		}
		return true;
	}
}
