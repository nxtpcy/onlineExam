package com.uestc.www.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uestc.www.shiro.session.OnlineSession;

public class OnlineSessionFactory implements SessionFactory {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Session createSession(SessionContext initData) {
		// TODO Auto-generated method stub
		OnlineSession session = new OnlineSession();
		logger.info("OnlineSessionFactory.createSession={}, status={}", session.getId(),
				new Object[] { session.getStatus() });
		return session;
	}

}
