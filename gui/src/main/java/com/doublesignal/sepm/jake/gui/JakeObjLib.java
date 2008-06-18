package com.doublesignal.sepm.jake.gui;

import com.doublesignal.sepm.jake.core.domain.JakeObject;
import com.doublesignal.sepm.jake.core.domain.Tag;
import com.doublesignal.sepm.jake.core.domain.exceptions.InvalidTagNameException;
import com.doublesignal.sepm.jake.core.services.IJakeGuiAccess;
import com.doublesignal.sepm.jake.gui.i18n.ITranslationProvider;
import com.doublesignal.sepm.jake.gui.i18n.TranslatorFactory;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Library for various JakeObject-related gui functions.
 * 
 * @author peter
 * 
 */
public final class JakeObjLib {
	private static final Logger log = Logger.getLogger(JakeObjLib.class);
	
	private static final ITranslationProvider translator = TranslatorFactory.getTranslator();
	
	/**
	 * Get String of Tags
	 * 
	 * @param tags A set of Tags
	 * @return the concatenated Tag string
	 */
	public static String getTagString(Set<Tag> tags) {
		String sTags = "";


		for (Tag tag : tags) {
			sTags = tag.toString() + ((!sTags.isEmpty()) ? ", " + sTags : "");
		}
		return sTags;
	}

    /**
     * Updates the tag list of the jakeObject in question
     * @param jakeGuiAccess
     * @param jakeObject
     * @param tagsNew
     * @return the concatenated Tag string as produced by getTagString() 
     */
	public static String generateNewTagString(IJakeGuiAccess jakeGuiAccess, JakeObject jakeObject, String tagsNew)
	{
		log.debug("adding tags to jakeObject");

		String[] tagsArray = tagsNew.split("[,\\s]");
		for (String sTag : tagsArray)
		{
			if (sTag.equals(",") || sTag.equals(" "))
			{
				continue;
			}

			Tag tTag = null;
			try
			{
				tTag = new Tag(sTag);
				if (!jakeObject.getTags().contains(tTag))
				{
					jakeGuiAccess.addTag(jakeObject, tTag);
				}
			}
			catch (InvalidTagNameException e)
			{
				log.debug("cought an InvalidTagNameException but ignoring "
						+ "it, because it will simply not show up in the gui" +
						" tagname is: " + sTag);
			}
		}

		// remove the non existent tagsNew from the jakeObject
		List<String> tagsFromArray = Arrays.asList(tagsArray);
		log.debug("removing tags from jakeObject");
		Tag[] foundTags = jakeObject.getTags().toArray(new Tag[jakeObject.getTags().size()]);
		for (Tag tag : foundTags)
		{
			if (!tagsFromArray.contains(tag.toString()))
			{
				jakeGuiAccess.removeTag(jakeObject, tag);
			}
		}

		log.debug("creating new tag string");
		String sTags = "";

		Set<Tag> objTags = jakeObject.getTags();
		sTags = JakeObjLib.getTagString(objTags);

		return sTags;

	}


}
