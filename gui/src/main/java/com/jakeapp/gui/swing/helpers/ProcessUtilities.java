package com.jakeapp.gui.swing.helpers;

import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProcessUtilities {
	private static final Logger log = Logger.getLogger(ProcessUtilities.class);

	public interface LineListener {
		public void processLine(String line);
	}

	/**
	 * Collects lines into an ArrayList.
	 */
	public static class ArrayListLineListener implements LineListener {
		private ArrayList<String> arrayList;

		public ArrayListLineListener(final ArrayList<String> arrayList) {
			this.arrayList = arrayList;
			if (arrayList == null) {
				throw new IllegalArgumentException("ArrayList 'arrayList' may not be null");
			}
		}

		public void processLine(String line) {
			arrayList.add(line);
		}
	}

	public static void spawn(final File directory, final String[] command) {
		spawn(directory, command, null);
	}

	/**
	 * Runs a command and ignores the output. The child is waited for on a
	 * separate thread, so there's no indication of whether the spawning was
	 * successful or not. A better design might be to exec in the current
	 * thread, and hand the Process over to another Thread; you'd still not
	 * get the return code (but losing that is part of the deal), but you
	 * would at least know that Java had no trouble in exec. Is that worth
	 * anything?
	 * <p/>
	 * listener may be null.
	 */
	public static void spawn(final File directory, final String[] command, final ProcessListener listener) {
		final String quotedCommand = shellQuotedFormOf(Arrays.asList(command));
		try {
			final Process p = Runtime.getRuntime().exec(command, null, directory);
			new Thread("Process Spawn: " + quotedCommand) {
				@Override
				public void run() {
					try {
						p.getOutputStream().close();
						p.getInputStream().close();
						p.getErrorStream().close();
						int status = p.waitFor();
						if (listener != null) {
							listener.processExited(status);
						}
					} catch (Exception ex) {
						log.warn("Problem waiting for command to finish: " + quotedCommand, ex);
					}
				}
			}.start();
		} catch (Exception ex) {
			log.warn("Failed to spawn command: " + quotedCommand, ex);
		}
	}

	public interface ProcessListener {
		public void processExited(int status);
	}


	/**
	 * Returns the process id of the given process, or -1 if we couldn't work it out.
	 */
	public static int getProcessId(Process process) {
		try {
			Field pidField = process.getClass().getDeclaredField("pid");
			pidField.setAccessible(true);
			return pidField.getInt(process);
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * Returns the process id of the current JVM, or -1 if we couldn't work it out.
	 */
	public static int getVmProcessId() {
		try {
			if (Platform.isMac()) {
				// This only works if we were started with -Xdock, but that's true for our applications, and I don't have a better solution.
				Map<String, String> env = System.getenv();
				for (String key : env.keySet()) {
					if (key.startsWith("APP_NAME_")) {
						log.warn(key);
						log.warn(key.substring(9));
						return Integer.parseInt(key.substring(9));
					}
				}
				return -1;
			} else {
				// We can't use StringUtilities.readFile because most files in /proc (including this one) report their length as 0.
				BufferedReader in = new BufferedReader(new FileReader("/proc/self/stat"));
				String content = in.readLine();
				in.close();
				return Integer.parseInt(content.substring(0, content.indexOf(' ')));
			}
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * Returns a String[] suitable as argument to Runtime.exec or
	 * ProcessBuilder's constructor. The arguments ask the system's default
	 * command interpreter to interpret and run the given command.
	 * <p/>
	 * The details of all this are obviously OS-specific, but it should be
	 * suitable for executing user input in a manner that's unsurprising to
	 * the user.
	 * <p/>
	 * On Win32, cmd.exe is used as a command interpreter.
	 * <p/>
	 * On other operating systems (assumed to be Unixes), the SHELL environment
	 * variable is queried. If this isn't set, a default of /bin/sh is used,
	 * though it probably won't work unless that happens to be bash(1).
	 */
	public static String[] makeShellCommandArray(String command) {
		String shell = System.getenv("SHELL");
		if (shell == null) {
			shell = "bash";
		}
		ArrayList<String> result = new ArrayList<String>();
		// Try to put the command in its own process group, so it's easier to kill it and its children.
		File setsidBinary = FileUtilities.findOnPath("setsid");
		if (setsidBinary != null) {
			result.add(setsidBinary.toString());
		} else if (Platform.isWin() && FileUtilities.findOnPath(shell) == null) {
			// If we found setsid, then setsid will be able to find /bin/bash.
			// The JVM won't, as it's not a Cygwin program and so is not aware of Cygwin's /bin mount.
			// /bin/bash is what SHELL will be if Evergreen's started from Terminator.
			return new String[]{"cmd", "/c", command};
		}
		result.add(shell);
		result.add("--login");
		result.add("-c");
		result.add(command);
		return result.toArray(new String[result.size()]);
	}

	/**
	 * The HUP (hang up) signal.
	 */
	public static final int SIGHUP = 1;

	/**
	 * The INT (interrupt) signal.
	 */
	public static final int SIGINT = 2;

	/**
	 * The QUIT (quit) signal.
	 */
	public static final int SIGQUIT = 3;

	/**
	 * The KILL (non-catchable, non-ignorable kill) signal.
	 */
	public static final int SIGKILL = 9;

	/**
	 * The TERM (soft termination) signal.
	 */
	public static final int SIGTERM = 15;

	/**
	 * Sends the given signal to the given process. Returns false if
	 * the signal could not be sent.
	 */
	public static boolean terminateProcess(Process process) {
		int pid = getProcessId(process);
		if (pid == -1) {
			return false;
		}
		try {
			System.out.println("killing " + pid);
			Runtime.getRuntime().exec("kill -" + SIGTERM + " -" + pid);
			Runtime.getRuntime().exec("kill -" + SIGTERM + " " + pid);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	/**
	 * Returns the integer file descriptor corresponding to one of
	 * FileDescriptor.in, FileDescriptor.out or FileDescriptor.err
	 * for the given process. Returns -1 on error.
	 */
	public static int getFd(Process process, FileDescriptor which) {
		if (which == FileDescriptor.in) {
			return getFd(process, "stdin_fd");
		} else if (which == FileDescriptor.out) {
			return getFd(process, "stdout_fd");
		} else if (which == FileDescriptor.err) {
			return getFd(process, "stderr_fd");
		} else {
			return -1;
		}
	}

	private static int getFd(Process process, String which) {
		try {
			Field fileDescriptorField = process.getClass().getDeclaredField(which);
			fileDescriptorField.setAccessible(true);
			FileDescriptor fileDescriptor = (FileDescriptor) fileDescriptorField.get(process);
			Field fdField = FileDescriptor.class.getDeclaredField("fd");
			fdField.setAccessible(true);
			return fdField.getInt(fileDescriptor);
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * Returns a single string representing the given command, quoted for
	 * passing to a shell or, more usually, for unambiguous human-readable
	 * output.
	 */
	public static String shellQuotedFormOf(List<String> command) {
		// FIXME: we only cope with spaces in commands, not other shell meta-characters.
		StringBuilder result = new StringBuilder();
		for (String word : command) {
			if (result.length() > 0) {
				result.append(' ');
			}
			if (word.contains(" ")) {
				result.append('"');
				result.append(word);
				result.append('"');
			} else {
				result.append(word);
			}
		}
		return result.toString();
	}

	/**
	 * Prevents instantiation.
	 */
	private ProcessUtilities() {
	}
}
