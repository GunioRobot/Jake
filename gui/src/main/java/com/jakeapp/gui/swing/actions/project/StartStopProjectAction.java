package com.jakeapp.gui.swing.actions.project;

import com.jakeapp.core.domain.Project;
import com.jakeapp.gui.swing.actions.abstracts.ProjectAction;
import com.jakeapp.gui.swing.helpers.ProjectHelper;
import com.jakeapp.gui.swing.worker.JakeExecutor;
import com.jakeapp.gui.swing.worker.tasks.StartStopProjectTask;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Starts or stops a project
 */
public class StartStopProjectAction extends ProjectAction {
	private static final Logger log = Logger.getLogger(StartStopProjectAction.class);
	private final ProjectHelper projectHelper;


	public StartStopProjectAction(ProjectHelper projectHelper) {
		this.projectHelper = projectHelper;

		putValue(Action.NAME, projectHelper.getProjectStartStopString(getProject()));

		updateAction();
	}

	public void actionPerformed(ActionEvent ignored) {
		log.trace("Start/Stop Project: " + getProject());

		// do nothing if we don't have a project
		if (getProject() == null) {
			log.warn("Attemted to Start/Stop without a project");
			return;
		}
		perform(getProject());
	}

	/**
	 * Actially performs the action
	 * @param p
	 */
	public static void perform(Project p) {
		JakeExecutor.exec(new StartStopProjectTask(p, !p.isStarted()));
	}


	@Override
	public void updateAction() {
		log.trace("update startstopprojectaction with " + getProject());
		String oldName = (String) getValue(Action.NAME);
		String newName = projectHelper.getProjectStartStopString(getProject());
		setEnabled(getProject() != null);

		putValue(Action.NAME, newName);
		firePropertyChange(Action.NAME, oldName, newName);
	}
}