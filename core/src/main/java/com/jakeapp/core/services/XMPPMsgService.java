package com.jakeapp.core.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jakeapp.core.domain.JakeMessage;
import com.jakeapp.core.domain.ProtocolType;
import com.jakeapp.core.domain.ServiceCredentials;
import com.jakeapp.core.domain.UserId;
import com.jakeapp.core.domain.exceptions.UserIdFormatException;
import com.jakeapp.jake.ics.ICService;
import com.jakeapp.jake.ics.exceptions.NetworkException;
import com.jakeapp.jake.ics.exceptions.TimeoutException;
import com.jakeapp.jake.ics.impl.xmpp.XmppICService;
import com.jakeapp.jake.ics.impl.xmpp.XmppUserId;

/**
 * Implementation of the MessageService for the XMPP Messaging Protocol
 */
public class XMPPMsgService extends MsgService<com.jakeapp.core.domain.UserId> {

	private static Logger log = Logger.getLogger(XMPPMsgService.class);

	public static final String namespace = "http://jakeapp.com/protocols/xmpp/versions/1";

	private XmppICService mainIcs = new XmppICService(namespace, "Jake");

	public XMPPMsgService() {

	}

	@Override
	protected boolean doCredentialsCheck() {
		ServiceCredentials cred = this.getServiceCredentials();
		log.debug("got credentials: " + cred.getUserId() + " pwl: "
				+ cred.getPlainTextPassword().length());
		if (!this.getMainUserId().isOfCorrectUseridFormat()) {
			return false;
		}
		if (cred.getPlainTextPassword().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean doLogin() throws TimeoutException, NetworkException {
		log
				.debug("got credentials: " + this.getServiceCredentials().getUserId()
						+ " pwl: "
						+ this.getServiceCredentials().getPlainTextPassword().length());
		boolean success = this.mainIcs.getStatusService().login(this.getMainUserId(),
				this.getServiceCredentials().getPlainTextPassword());


		if (success) {
			log.debug("login success");
		}

		return success;
	}

	@Override
	protected void doLogout() throws TimeoutException, NetworkException {
		log.debug("XMPPMsgService -> logout");

		this.mainIcs.getStatusService().logout();
	}

	@Override
	public UserId getUserId(String userId) throws UserIdFormatException {
		return new UserId(ProtocolType.XMPP, userId);
	}

	@Override
	public UserId getUserId() {
		return this.userId;
	}

	@Override
	public void createAccount() throws NetworkException {
		this.mainIcs.getStatusService().createAccount(getMainUserId(),
				this.getServiceCredentials().getPlainTextPassword());
	}

	@Override
	protected ICService getMainIcs() {
		return this.mainIcs;
	}

	@Override
	protected XmppUserId getMainUserId() {
		return new XmppUserId(new XmppUserId(this.getServiceCredentials().getUserId())
				.getUserIdWithOutResource()
				+ "/Jake");
	}

	@Override
	protected com.jakeapp.jake.ics.UserId getIcsUser(ICService ics,
			com.jakeapp.core.services.MsgService.ICData listeners) {
		return new XmppUserId(this.getMainUserId().getUserIdWithOutResource() + "/"
				+ listeners.name);
	}
}
