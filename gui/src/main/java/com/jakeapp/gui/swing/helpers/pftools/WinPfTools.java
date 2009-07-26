package com.jakeapp.gui.swing.helpers.pftools;

import com.jakeapp.gui.swing.helpers.FileIconLabelHelper;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;

/**
 * Mac implementation if PfTools
 *
 * @author: studpete
 */
public class WinPfTools extends AbstractPfTools {
	private static final Logger log = Logger.getLogger(WinPfTools.class);

	// TODO: implement custom code for 16x16 pxl getter
	// TODO: use this for FileTreeTable
	// TODO: find methode to getInstance larger icon without scaling.
	/**
	 * Windows impl. of get file icon. Native up to 32x32.
	 *
	 * @param file
	 * @param size
	 * @return ImageIcon of file.
	 */
	@Override
	public Icon getFileIcon(File file, int size) {

		return FileIconLabelHelper.getIcon(file);

		// TODO: fix this, call using reflections!
		/*
		try {
			sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(file);

			// Get large icon
			ImageIcon ico = new ImageIcon(sf.getIcon(true), sf.getFolderType());

			// scale icon if different size is needed.
			if (ico.getIconWidth() != size) {
				Image img = ico.getImage();
				img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				return new ImageIcon(img);
			} else {
				return ico;
			}

		} catch (Exception e) {
			log.warn(e);
			return null;
		}*/
	}
}
