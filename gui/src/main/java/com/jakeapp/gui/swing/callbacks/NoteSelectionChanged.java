package com.jakeapp.gui.swing.callbacks;

import com.jakeapp.core.domain.NoteObject;

import java.util.List;

/**
 * This event is fired when a note is selected/deselected in the NotesPanel.
 * <p/>
 * Consumers include, for example, the inspector.
 */
public interface NoteSelectionChanged {
	/**
	 * Inner class that saves notes and provides convenience methods
	 */
	public class NoteSelectedEvent {
		private List<NoteObject> notes;

		public NoteSelectedEvent(List<NoteObject> notes) {
			this.notes = notes;
		}

		public int size() {
			return notes.size();
		}

		public boolean isSingleNoteSelected() {
			return (notes != null) && notes.size() == 1;
		}

		public boolean isNoNoteSelected() {
			return (notes == null) || notes.size() == 0;
		}

		public boolean isMultipleNotesSelected() {
			return (notes != null) && notes.size() > 1;
		}

		public NoteObject getSingleNote() {
			return (notes != null && notes.size() == 1) ? notes.get(0) : null;
		}

		public List<NoteObject> getNotes() {
			return notes;
		}
	}

	void noteSelectionChanged(NoteSelectedEvent event);
}