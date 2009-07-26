package com.jakeapp.gui.swing.actions.project;

import com.jakeapp.gui.swing.JakeMainApp;
import com.jakeapp.gui.swing.MainWindow;
import com.jakeapp.gui.swing.globals.JakeContext;
import com.jakeapp.gui.swing.actions.abstracts.ProjectAction;
import com.jakeapp.gui.swing.helpers.FileUtilities;
import com.jakeapp.gui.swing.helpers.ImageLoader;
import com.jakeapp.gui.swing.helpers.ProjectHelper;
import org.apache.log4j.Logger;
import org.jdesktop.application.ResourceMap;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Project Action for creating a new project.
 */
public class CreateProjectAction extends ProjectAction {
	private static final Logger log = Logger.getLogger(CreateProjectAction.class);

	/**
	 * Create new <code>CreateProjectAction</code>.
	 *
	 * @param ellipsis if <code>true</code> the <code>Action.NAME</code> ends with an ellipsis (...), if
	 *                 <code>false</code> the dots are omitted.
	 */
	public CreateProjectAction(boolean ellipsis, ResourceMap resourceMap) {
		super();

		putValue(Action.NAME, resourceMap.getString("createProjectMenuItem.text") + (ellipsis ? "..." : ""));

		Icon createProjectIcon = ImageLoader.getScaled(getClass(),
				"/icons/createproject.png", 32);
		putValue(Action.LARGE_ICON_KEY, createProjectIcon);
	}


	public void actionPerformed(ActionEvent actionEvent) {
		log.info("Create Project action invoked");

		String path = FileUtilities.openDirectoryChooser(null,
						MainWindow.getMainView().getResourceMap().getString(
										"createProjectDialogTitle"));
		log.info("Directory was: " + path);

		// create the directory if path was not null
		if (path != null) {
			JakeMainApp.getCore().createProject(ProjectHelper.createDefaultPath(path),
							path,
							JakeContext.getMsgService(), false);
		}
	}


	@Override
	public void updateAction() {
		setEnabled(JakeContext.getMsgService() != null);
	}
}