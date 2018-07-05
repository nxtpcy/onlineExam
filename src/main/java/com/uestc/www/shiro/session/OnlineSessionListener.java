package com.uestc.www.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uestc.www.common.Constants;


public class OnlineSessionListener implements SessionListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onStart(Session session) {
		// TODO Auto-generated method stub
		logger.info("onStart session created:id= {}, begin at {}", session.getId(), session.getStartTimestamp());
	}

	@Override
	public void onStop(Session session) {
		// TODO Auto-generated method stub
		logger.info("onStop session stop:id= {}", session.getId());
	}

	@Override
	public void onExpiration(Session session) {
		// TODO Auto-generated method stub
		logger.info("onExpiration session created:id= {}, begin at {}", session.getId(), session.getStartTimestamp());
		logger.info("currentUser={}",session.getAttribute(Constants.CURRENT_USER));
	}

}
