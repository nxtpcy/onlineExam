package com.uestc.www.shiro.session;

import org.apache.shiro.session.mgt.SimpleSession;

public class OnlineSession extends SimpleSession {

	private static final long serialVersionUID = 1L;

	public static enum OnlineStatus {
		ONLINE("在线"), KICKOUT("强制退出");
		private final String info;

		private OnlineStatus(String info) {
			this.info = info;
		}
		public String getInfo() {
			return info;
		}
	}

	private OnlineStatus status = OnlineStatus.ONLINE;

	public OnlineStatus getStatus() {
		return status;
	}

	public void setStatus(OnlineStatus status) {
		this.status = status;
	}

}
