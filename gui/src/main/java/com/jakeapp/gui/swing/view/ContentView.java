package com.jakeapp.gui.swing.view;

import com.jakeapp.gui.swing.model.ContentViewModel;
import com.jakeapp.gui.swing.model.ContentViewModelEnum;
import com.jakeapp.gui.swing.controller.ContentViewController;


import javax.swing.*;
import java.awt.*;
import java.util.Observer;
import java.util.Observable;


public class ContentView extends JPanel implements Observer {

	private final ContentViewModel model;
	private final ContentViewController controller;
//	private final JPanel projectView;


	private ContentSingleView contentSingleView;
	private ContentSplitView splitView;

//	private final JakeSourceList jakeSourceList;



	public ContentView(ContentViewModel model, ContentViewController controller, ContentSingleView contentSingleView, ContentSplitView splitView) {
		super(new BorderLayout());

		this.model = model;
		this.controller = controller;
		this.contentSingleView = contentSingleView;
		this.splitView = splitView;

		model.addObserver(this);

//		this.splitView.setBackground(new Color(0,0,255));


//		this.jakeSourceList = jakeSourceList;
//		this.projectView = projectView;

//		splitView = createSourceListAndMainArea();

		updateMe();

	}



		/**
	 * Creates the SplitPane for SourceList and the Main Content Area.
	 *
	 * @return the JSplitPane
	 */
//	private JSplitPane createSourceListAndMainArea() {


		// creates the special SplitPlane
//		JSplitPane splitPane = MacWidgetFactory.createSplitPaneForSourceList(jakeSourceList.getSourceList(), projectView);

		// TODO: divider location should be a saved property
//		splitPane.setDividerLocation(180);
//		splitPane.getLeftComponent().setMinimumSize(new Dimension(150, 150));
//
//		return splitPane;
//	}



	private void updateMe()
	{
//		this.removeAll();

		switch (model.getViewToShow()) {

			case SINGLE:
				System.out.println("adding single view to center");
				this.add(contentSingleView, BorderLayout.CENTER);
				break;
			case SPLITVIEW:
				System.out.println("adding split view to center");
				this.add(splitView, BorderLayout.CENTER);
				break;
		}

		this.validate();
		this.updateUI();
	}


	@Override
	public void update(Observable o, Object arg) {

		if(o instanceof ContentViewModel && arg instanceof ContentViewModelEnum)
		{
			ContentViewModelEnum changed = (ContentViewModelEnum) arg;

			switch (changed) {
				case currentView:
				    updateMe();
					break;
				case viewToShow:
					break;
			}
		}
//		
//		System.out.println("calling update on ContentView");
//		System.out.println("o = " + o);
//		System.out.println("arg = " + arg);
//
//		updateMe();
	}
}
