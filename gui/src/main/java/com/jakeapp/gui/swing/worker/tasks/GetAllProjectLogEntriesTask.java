package com.jakeapp.gui.swing.worker.tasks;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.jakeapp.availablelater.AvailableLaterObject;
import com.jakeapp.core.domain.FileObject;
import com.jakeapp.core.domain.Project;
import com.jakeapp.gui.swing.xcore.ObjectCache;

/**
 * @author studpete
 */
// TODO??
public class GetAllProjectLogEntriesTask extends AbstractTask<List<FileObject>> {
	private static final Logger log =
					Logger.getLogger(GetAllProjectLogEntriesTask.class);
	private Project project;

	public GetAllProjectLogEntriesTask(Project project) {
		this.project = project;
	}

	@Override
	protected AvailableLaterObject<List<FileObject>> calculateFunction() {
		log.debug("calling GetAllProjectFilesTask:calculateFunction");
		//return JakeMainApp.getCore().getL
		return null;
	}

	@Override
	protected void onDone() {
		log.info("Done GetAllProjectFilesTask");

		// done! save into object cache
		try {
			ObjectCache.get().setFiles(project, get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}