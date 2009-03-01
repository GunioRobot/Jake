package com.jakeapp.core.synchronization;

import java.util.Map;

import com.jakeapp.core.dao.exceptions.NoSuchLogEntryException;
import com.jakeapp.core.dao.exceptions.NoSuchProjectException;
import com.jakeapp.core.domain.ILogable;
import com.jakeapp.core.domain.JakeObject;
import com.jakeapp.core.domain.logentries.LogEntry;
import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.UserId;
import com.jakeapp.core.domain.exceptions.IllegalProtocolException;
import com.jakeapp.jake.ics.exceptions.NotLoggedInException;


public interface IFriendlySyncService extends ISyncService {

	/**
	 * @param project
	 * @throws IllegalProtocolException
	 * @throws NoSuchProjectException 
	 * @see ISyncService#startLogSync(Project, UserId)
	 * @throws IllegalArgumentException
	 * @return
	 */
	public Map<UserId, Iterable<LogEntry<ILogable>>> startLogSync(Project project)
			throws IllegalArgumentException, IllegalProtocolException, NoSuchProjectException;

	/**
	 * @param project
	 * @throws NoSuchProjectException
	 * @see ISyncService#poke(Project, UserId)
	 */
	public void poke(Project project) throws NoSuchProjectException;

	/**
	 * @param objects
	 * @throws NoSuchLogEntryException
	 * @throws IllegalArgumentException 
	 * @throws NotLoggedInException 
	 * @see ISyncService#pullObject(JakeObject)
	 */
	public void pullObjects(Iterable<JakeObject> objects)
			throws NoSuchLogEntryException, NotLoggedInException, IllegalArgumentException;

}