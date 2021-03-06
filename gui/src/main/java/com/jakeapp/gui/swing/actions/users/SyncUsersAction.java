package com.jakeapp.gui.swing.actions.users;

import com.jakeapp.core.synchronization.UserInfo;
import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.JakeMainView;
import com.jakeapp.gui.swing.actions.abstracts.UserAction;
import com.jakeapp.gui.swing.globals.JakeContext;
import com.jakeapp.gui.swing.helpers.UserHelper;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Syncronizes with Users of your Project.
 */
public class SyncUsersAction extends UserAction {
	private static final Logger log = Logger.getLogger(SyncUsersAction.class);

	public SyncUsersAction(JList list) {
		super(list);

		String actionStr = JakeMainView.getMainView().getResourceMap().
						getString("syncPeopleMenuItem.text");

		putValue(Action.NAME, actionStr);
	}


	public void actionPerformed(ActionEvent actionEvent) {
		for (UserInfo userInfo : getSelectedUsers()) {
			JakeMainApp.getCore()
							.syncProject(JakeContext.getProject(), userInfo.getUser());
		}
	}

	@Override
	public void updateAction() {
		super.updateAction();
		setEnabled(this.isEnabled() && hasSelectedUser() && !UserHelper
						.isCurrentProjectMember(getSelectedUser().getUser()));
	}
}