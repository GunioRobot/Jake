package com.jakeapp.gui.swing;

import com.jakeapp.core.dao.exceptions.NoSuchLogEntryException;
import com.jakeapp.core.domain.*;
import com.jakeapp.core.domain.exceptions.InvalidCredentialsException;
import com.jakeapp.core.domain.exceptions.NotLoggedInException;
import com.jakeapp.core.domain.exceptions.ProjectNotLoadedException;
import com.jakeapp.core.services.IFrontendService;
import com.jakeapp.core.services.MsgService;
import com.jakeapp.core.services.exceptions.ProtocolNotSupportedException;
import com.jakeapp.gui.swing.callbacks.ConnectionStatus;
import com.jakeapp.gui.swing.callbacks.ErrorCallback;
import com.jakeapp.gui.swing.callbacks.ProjectChanged;
import com.jakeapp.gui.swing.callbacks.RegistrationStatus;
import com.jakeapp.gui.swing.exceptions.ProjectFolderMissingException;
import com.jakeapp.gui.swing.exceptions.ProjectNotFoundException;
import com.jakeapp.gui.swing.helpers.FolderObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

public class SpringCoreAccessImpl implements ICoreAccess {

	private static final Logger log = Logger.getLogger(SpringCoreAccessImpl.class);

	private boolean isSignedIn;

	private Map<Project, List<ProjectMember>> peopleProjectMap = new HashMap<Project, List<ProjectMember>>();

	private IFrontendService frontendService;

	private List<Project> projects = new ArrayList<Project>(); // TODO remove
	// after
	// completion of
	// this
	// implementation

	private List<Project> invitedProjects = new ArrayList<Project>(); // TODO
	// remove
	// after
	// completion
	// of
	// this
	// implementation

	/**
	 * SessionId returned by the authentication Method of
	 * FrontendService.authenticate.
	 */
	private String sessionId;

	/**
	 * Core Access Mock initialisation code
	 */
	public SpringCoreAccessImpl() {
		isSignedIn = false;
		connectionStatus = new ArrayList<ConnectionStatus>();
		registrationStatus = new ArrayList<RegistrationStatus>();
		projectChanged = new ArrayList<ProjectChanged>();
		errorCallback = new ArrayList<ErrorCallback>();
	}

	@Override
	public List<Project> getMyProjects() throws NotLoggedInException {
		return frontendService.getProjectsManagingService(sessionId).getProjectList(
			 InvitationState.ACCEPTED);
	}

	@Override
	public List<Project> getInvitedProjects() throws NotLoggedInException {
		return frontendService.getProjectsManagingService(sessionId).getProjectList(
			 InvitationState.INVITED);
	}


	@Override
	public void setFrontendService(IFrontendService frontendService) {
		this.frontendService = frontendService;
	}

	@Override
	public void authenticateOnBackend(Map<String, String> authenticationData)
		 throws InvalidCredentialsException {
		this.sessionId = this.frontendService.authenticate(authenticationData);
	}

	@Override
	public void backendLogOff() {
		try {
			this.frontendService.logout(this.sessionId);
		} catch (NotLoggedInException e) {
			// silently ignore this exception. may be removed from
			// frontendservice in the future

		}
		this.sessionId = "";
	}

	@Override
	public List<MsgService> getMsgServics() throws NotLoggedInException {
		return this.frontendService.getMsgServices(this.sessionId);
	}

	public void addErrorListener(ErrorCallback ec) {
		errorCallback.add(ec);
	}

	public void removeErrorListener(ErrorCallback ec) {
		errorCallback.remove(ec);
	}

	private void fireErrorListener(ErrorCallback.JakeErrorEvent ee) {
		for (ErrorCallback callback : errorCallback) {
			callback.reportError(ee);
		}
	}


	public void signIn(final String user, final String pass) {
		log.info("Signs in: " + user + "pass: " + pass);
		
		ServiceCredentials credentials;
		
		credentials = new ServiceCredentials(user,pass); 
		//TODO
		//This is the only protocoltype used right now. In the future
		//there may be more...
		credentials.setProtocol(ProtocolType.XMPP);
		
		//XXX are credentials prepared enough??
		this.signIn(credentials);
	}
	
	public void signIn(final ServiceCredentials credentials) {
		Runnable runner = new Runnable() {

			public void run() {
				fireConnectionStatus(ConnectionStatus.ConnectionStati.SigningIn, "");
				
				
				try {
					MsgService toLogin = frontendService.addAccount(sessionId, credentials);
					isSignedIn = toLogin.login();
					
					currentUser = credentials.getUserId();

					fireConnectionStatus(ConnectionStatus.ConnectionStati.Online, "");
				} catch (NotLoggedInException e) {
					log.debug("Tried to sign in without a session",e);
				} catch (ProtocolNotSupportedException e) {
					// this should never happen...
					log.error("Login with this protocol is not supported.",e);
				} catch (InvalidCredentialsException e) {
				} catch (Exception e) {
				}
				finally {
					if (!isSignedIn) {
						// XXX NOTIFY GUI ABOUT THIS!!
						fireConnectionStatus(ConnectionStatus.ConnectionStati.Offline, "");
					}
				}
			}
		};

		// start our runner thread, that makes callbacks to connection status
		new Thread(runner).start();
	}

	public void addConnectionStatusCallbackListener(ConnectionStatus cb) {
		log.info("Registers connection status callback: " + cb);

		connectionStatus.add(cb);
	}

	public void removeConnectionStatusCallbackListener(ConnectionStatus cb) {
		log.info("Deregisters connection status callback: " + cb);

		connectionStatus.remove(cb);
	}


	private void fireConnectionStatus(ConnectionStatus.ConnectionStati state, String str) {
		log.info("spead callback event...");
		for (ConnectionStatus callback : connectionStatus) {
			callback.setConnectionStatus(state, str);
		}
	}

	public void register(String user, String pass) {
		log.info("Registering user: " + user + " pass: " + pass);

		Runnable runner = new Runnable() {

			public void run() {

				// registering
				fireRegistrationStatus(
					 RegistrationStatus.RegisterStati.RegistrationActive, "");

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				fireRegistrationStatus(RegistrationStatus.RegisterStati.RegisterSuccess,
					 "");

				// logging in after registering
				fireConnectionStatus(ConnectionStatus.ConnectionStati.SigningIn, "");

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				isSignedIn = true;

				fireConnectionStatus(ConnectionStatus.ConnectionStati.Online, "");
			}
		};

		// start our runner thread, that makes callbacks to connection status
		new Thread(runner).start();
	}

	@Override
	public boolean createAccount(ServiceCredentials credentials)
		 throws NotLoggedInException, InvalidCredentialsException,
		 ProtocolNotSupportedException, Exception {
		return this.frontendService.createAccount(this.sessionId, credentials);
	}

	@Override
	public MsgService addAccount(ServiceCredentials credentials)
		 throws NotLoggedInException, InvalidCredentialsException,
		 ProtocolNotSupportedException, Exception {
		return this.frontendService.addAccount(this.sessionId, credentials);
	}

	public void addRegistrationStatusCallbackListener(RegistrationStatus cb) {
		log.info("Registers registration status callback: " + cb);
	}

	public void removeRegistrationStatusCallbackListener(RegistrationStatus cb) {
		log.info("Deregisters registration status callback: " + cb);

	}


	public boolean isSignedIn() {
		return isSignedIn;
	}

	@Override
	public ProjectMember getCurrentProjectMember() {
		// TODO: fix
		return new ProjectMember(new UUID(11, 22), "Nickname", TrustState.AUTO_ADD_REMOVE);
		// return new ProjectMember(new XMPPUserId(new
		// ServiceCredentials("Chuck Norris", "foo"), new UUID(1, 1),
		// "chuck norris", "chuck", "Chuck", "Norris"), TrustState.TRUST);
	}


	public void signOut() {
		isSignedIn = false;

		fireConnectionStatus(ConnectionStatus.ConnectionStati.Offline, "");
	}

	public String[] getLastSignInNames() {
		return new String[]{"pstein", "csutter"};
	}


	public void addProjectChangedCallbackListener(ProjectChanged cb) {
		log.info("Mock: register project changed callback: " + cb);

		projectChanged.add(cb);
	}

	public void removeProjectChangedCallbackListener(ProjectChanged cb) {
		log.info("Mock: deregister project changed callback: " + cb);

		if (projectChanged.contains(cb)) {
			projectChanged.remove(cb);
		}
	}

	private void fireProjectChanged(ProjectChanged.ProjectChangedEvent ev) {
		for (ProjectChanged callback : projectChanged) {
			callback.projectChanged(ev);
		}
	}


	public void stopProject(Project project) {
		log.info("stop project: " + project);

		// if(!project.isStarted())
		// throw new ProjectNotStartedException();

		project.setStarted(false);

		// generate event
		fireProjectChanged(new ProjectChanged.ProjectChangedEvent(project,
			 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.State));
	}

	public void startProject(Project project) {
		project.setStarted(true);

		// generate event
		fireProjectChanged(new ProjectChanged.ProjectChangedEvent(project,
			 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.State));
	}


	public int getProjectFileCount(Project project) {
		return 100;
	}

	public int getProjectSizeTotal(Project project) {
		return 50000;
	}


	public void deleteProject(final Project project) {
		log.info("Mock: delete project: " + project);

		if (project == null) {
			throw new IllegalArgumentException("Cannot delete empty project!");
		}


		Runnable runner = new Runnable() {

			public void run() {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					// search project in list
					boolean ret = projects.remove(project);

					if (!ret) {
						throw new ProjectNotFoundException("Project not found in list!");
					}

					fireProjectChanged(new ProjectChanged.ProjectChangedEvent(
						 project,
						 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.Deleted));

				} catch (RuntimeException run) {
					fireErrorListener(new ErrorCallback.JakeErrorEvent(run));
				}
			}
		};

		// start our runner thread, that makes a callback to project status
		new Thread(runner).start();
	}

	public void joinProject(final String path, final Project project) {
		log.info("Mock: join project: " + project + " path: " + path);

		if (path == null) {
			// throw new
		}

		Runnable runner = new Runnable() {

			public void run() {

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					project.setRootPath(path);
					projects.add(project);
					invitedProjects.remove(project);
					project.setInvitationState(InvitationState.ACCEPTED);


					fireProjectChanged(new ProjectChanged.ProjectChangedEvent(
						 project,
						 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.Joined));

				} catch (RuntimeException run) {
					fireErrorListener(new ErrorCallback.JakeErrorEvent(run));
				}
			}
		};

		// start our runner thread, that makes a callback to project status
		new Thread(runner).start();
	}


	public void rejectProject(final Project project) {
		log.info("Mock: reject project: " + project);


		Runnable runner = new Runnable() {

			public void run() {

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					invitedProjects.remove(project);

					fireProjectChanged(new ProjectChanged.ProjectChangedEvent(
						 project,
						 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.Rejected));

				} catch (RuntimeException run) {
					fireErrorListener(new ErrorCallback.JakeErrorEvent(run));
				}
			}
		};

		// start our runner thread, that makes a callback to project status
		new Thread(runner).start();
	}

	public void setProjectName(Project project, String prName) {
		project.setName(prName);

		fireProjectChanged(new ProjectChanged.ProjectChangedEvent(project,
			 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.Name));
	}

	@Override
	public FolderObject getProjectRootFolder(Project project)
		 throws ProjectFolderMissingException {
		// This is all mocked from the actual file system
		String rootPath = project.getRootPath();
		log.debug("File mocking: Project root path is " + rootPath);

		File rootFolder = new File(rootPath);
		if (!rootFolder.exists()) {
			throw new ProjectFolderMissingException(rootPath);
		}

		FolderObject fo = recursiveFileSystemHelper(project, rootFolder, System
			 .getProperty("file.separator"), "");

		return fo;
	}

	@Override
	public int getFileStatus(Project project, FileObject file) {
		return 0;
	}

	/**
	 * Helper method for this mock: Works recursively through the file system to
	 * build the FolderObject for getProjectRootFolder()
	 *
	 * @param file
	 * @return
	 */
	private FolderObject recursiveFileSystemHelper(Project prj, File file,
	                                               String relPath, String name) {
		FolderObject fo = new FolderObject(relPath, name);
		log.debug("File mocking: Started recursing through folder "
			 + file.getAbsolutePath());

		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				log.debug("File mocking: Recursing into subdirectory " + relPath
					 + f.getName() + System.getProperty("file.separator"));
				FolderObject subfolder = recursiveFileSystemHelper(prj, f, relPath
					 + f.getName() + System.getProperty("file.separator"), f.getName());
				fo.addFolder(subfolder);
			} else {
				log.debug("File mocking: Adding file " + relPath + f.getName());
				fo.addFile(new FileObject(new UUID(5, 3), prj, relPath + f.getName()));
			}
		}

		return fo;
	}

	public List<NoteObject> getNotes(Project project) throws NotLoggedInException,
		 ProjectNotLoadedException {
		return this.frontendService.getProjectsManagingService(this.sessionId).getNotes(
			 project);
	}

	@Override
	public Date getLastEdit(NoteObject note, Project project) {
		return new Date();
	}

	@Override
	public ProjectMember getLastEditor(NoteObject note, Project project) {
		// TODO: fix
		return new ProjectMember(new UUID(11, 22), "Nickname", TrustState.AUTO_ADD_REMOVE);

		// return new ProjectMember(new XMPPUserId(new
		// ServiceCredentials("Chuck Norris", "foo"), new UUID(1, 1),
		// "chuck norris", "chuck", "Chuck", "Norris"), TrustState.TRUST);
	}

	/**
	 * Generates a list so that people are remembered when they change.
	 *
	 * @param project : project that should be evaluated
	 * @return
	 */
	public List<ProjectMember> getPeople(Project project) {
		log.info("Mock: getPeople from project " + project);

		if (project == null) {
			return null;
		}

		if (!peopleProjectMap.containsKey(project)) {
			List<ProjectMember> people = new ArrayList<ProjectMember>();

			// TODO: fix
			people.add(new ProjectMember(new UUID(11, 22), "Nickname",
				 TrustState.AUTO_ADD_REMOVE));


			/*
			 * people.add(new ProjectMember(new XMPPUserId(new
			 * ServiceCredentials("User1", "pass2"), new UUID(22, 33),
			 * "pstein@jabber.fsinf.at", "", "Peter", "Steinberger"),
			 * TrustState.TRUST));
			 * 
			 * people.add(new ProjectMember(new XMPPUserId(new
			 * ServiceCredentials("User2", "pass2"), new UUID(222, 333),
			 * "test@jabber.org", "Pr-" + project.getName(), "ProjectTestUser",
			 * project.getName()), TrustState.AUTO_ADD_REMOVE));
			 * 
			 * 
			 * people.add(new ProjectMember(new XMPPUserId(new
			 * ServiceCredentials("User3", "pass3"), new UUID(22, 33),
			 * "max@jabber.org", "Max", "Max", "Mustermann"),
			 * TrustState.NO_TRUST));
			 */
			peopleProjectMap.put(project, people);
		}

		return peopleProjectMap.get(project);
	}

	@Override
	public boolean setPeopleNickname(Project project, ProjectMember pm, String nick) {
		// TODO: ignore this and create a regex for checking!
		if (nick.indexOf("<") != -1) {
			return false;
		} else {
			pm.setNickname(nick);

			fireProjectChanged(new ProjectChanged.ProjectChangedEvent(project,
				 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.People));

			return true;
		}
	}

	@Override
	public void peopleSetTrustState(Project project, ProjectMember member,
	                                TrustState trust) {
		member.setTrustState(trust);

		fireProjectChanged(new ProjectChanged.ProjectChangedEvent(project,
			 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.People));
	}


	@Override
	public void invitePeople(Project project, String userid) {
		log.info("Mock: invite pm: " + userid + " to project: " + project);

		// TODO: implement the invite

	}

	@Override
	public List<ProjectMember> getSuggestedPeople(Project project) {
		List<ProjectMember> members = new ArrayList<ProjectMember>();
		members.add(getPeople(project).get(0));
		// members.add(getPeople(project).get(1));
		return members;
	}


	@Override
	public List<LogEntry> getLog(Project project, int entries) {
		List<LogEntry> log = new ArrayList<LogEntry>();

		if (project != null) {
			// yeah... what a beautiful interface ;o)
			log.add(new LogEntry(new UUID(1, 2), LogAction.PROJECT_CREATED, new Date(),
				 project, null, getPeople(project).get(0), "comment 1", "checksum???",
				 true));

			log.add(new LogEntry(new UUID(1, 2), LogAction.FILE_ADD, new Date(), project,
				 null, getPeople(project).get(0), "comment 1", "checksum???", true));
		}

		return log;
	}


	public void createProject(final String name, final String path) {
		log.info("Mock: create project: " + name + " path: " + path);
		if (path == null) {
			// throw new
		}

		Runnable runner = new Runnable() {

			public void run() {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					Project pr1 = new Project(name, new UUID(22, 33), null,
						 new File(path));
					pr1.setStarted(true);
					projects.add(pr1);

					fireProjectChanged(new ProjectChanged.ProjectChangedEvent(
						 pr1,
						 ProjectChanged.ProjectChangedEvent.ProjectChangedReason.Created));

				} catch (RuntimeException run) {
					fireErrorListener(new ErrorCallback.JakeErrorEvent(run));
				}
			}
		};

		// start our runner thread, that makes a callback to project status
		new Thread(runner).start();
	}


	private void fireRegistrationStatus(RegistrationStatus.RegisterStati state, String str) {
		for (RegistrationStatus callback : registrationStatus) {
			callback.setRegistrationStatus(state, str);
		}
	}

	@Override
	public Date getFileLastModified(FileObject file) {
		// TODO: Make this useful
		return new Date();
	}


	@Override
	public boolean importExternalFileIntoProject(String absPath, String destFolderRelPath) {
		return false;
	}

	@Override
	public ProjectMember getLastModifier(JakeObject jakeObject) throws NoSuchLogEntryException {
		return null;
	}

	@Override
	public void deleteToTrash(FileObject file) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deleteToTrash(FolderObject folder) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void rename(FileObject file, String newName) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void rename(FolderObject folder, String newName) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public long getFileSize(FileObject file) {
		return 1234;
	}

	@Override
	public long getLocalFileSize(FileObject fo) {
		return 0;
	}

	@Override
	public Date getLocalFileLastModified(FileObject fo) {
		return null;
	}

	private String currentUser = null;

	// event spread
	private List<ConnectionStatus> connectionStatus;

	private List<RegistrationStatus> registrationStatus;

	private List<ProjectChanged> projectChanged;

	private List<ErrorCallback> errorCallback;
}