package com.jakeapp.gui.swing.models;

import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.User;
import com.jakeapp.gui.swing.JakeMainApp;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Special Model to preserve data
 *
 */
// FIXME: could be improved...
public class InvitePeopleComboBoxModel extends DefaultComboBoxModel {
	public InvitePeopleComboBoxModel(Project project) {
		super(convertToProxyMemberProjectList(
						JakeMainApp.getCore().getSuggestedUser(project)).toArray());
	}

	private static List<UserIdProxy> convertToProxyMemberProjectList(
					List<User> members) {
		// proxy
		List<UserIdProxy> list = new ArrayList<UserIdProxy>();

		for (User m : members) {
			list.add(new UserIdProxy(m));
		}
		return list;
	}


	private static class UserIdProxy {
		private User user;

		public UserIdProxy(User user) {
			this.setUser(user);
		}

		@Override
		public String toString() {

			return user.getUserId()/* + " (" + UserHelper
							.getNickOrFullName(getUser(), 30) + ")"*/;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}
}


