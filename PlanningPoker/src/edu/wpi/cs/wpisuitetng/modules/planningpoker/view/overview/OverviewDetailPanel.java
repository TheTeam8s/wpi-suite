/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import javax.swing.JSplitPane;

public class OverviewDetailPanel extends JSplitPane {
	PlanningPokerSession currentSession;
	OverviewDetailInfoPanel infoPanel;
	OverviewReqTable reqTable;

	public OverviewDetailPanel () {

		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		String[] columnNames = {"Name", "Estimate"};
		Object[][] data = {};
		
		// Create the info panel and table panel
		infoPanel = new OverviewDetailInfoPanel();
		reqTable = new OverviewReqTable(data, columnNames);
		
		// Put the overview table and sidebar into the tab
		this.setTopComponent(infoPanel);
		this.setBottomComponent(reqTable);
		this.setResizeWeight(0.2);  // set the right screen to not show by default

		ViewEventController.getInstance().setOverviewDetailInfoPanel(infoPanel);
		ViewEventController.getInstance().setOverviewReqTable(reqTable);
				getCurrentSession().setSessionState(SessionState.VOTINGENDED); 
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(getCurrentSession());
				ViewEventController.getInstance().refreshTable();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Have the event controller open a new edit session tab
				ViewEventController.getInstance().editSession(getCurrentSession());
			}
		});

				
	}
	
	public void updatePanel(final PlanningPokerSession session)	{
		
		updateInfoPanel(session);
		updateReqTable(session);
		
		this.currentSession = session;
		String endDate = "No end date";
		Set<Integer> requirements = session.getRequirementIDs();
		
		// Change name
		this.lblSessionName.setText(session.getName());
		
		// Change requirements list
		this.listModel.clear();
		if (session.requirementsGetSize() > 0)
		{
			for (Integer id : requirements) {
				Requirement requirement = RequirementModel.getInstance().getRequirement(id);
				if (requirement != null) {
					this.listModel.addElement(requirement);
				}
			}
		}
		
		requirementsList = new JList<Requirement>(listModel);

		// Change end date
		try {
			endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(session.getEndDate().getTime());
		} catch (NullPointerException ex) {
			endDate = new String("No end date");
		}
		
		this.lblEndDate.setText(endDate);

		// change the visibility of the top buttons
		setButtonVisibility(session);
		
		
		btnVote.setVisible(session.isOpen());
		btnOpen.setVisible(false);
		
		
		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			// TODO bro
			}
		});

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				session.setSessionState(SessionState.OPEN);
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(getCurrentSession());
				ViewEventController.getInstance().refreshTable();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Have the event controller open a new edit session tab
				ViewEventController.getInstance().editSession(getCurrentSession());
			}
		});
		
		// Check if the buttons should appear
/*
		btnEdit.isVisible(false);
		if (session.isEditable()) {
			btnEdit.isVisible(true);
		}
*/

		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
	}	
	
	private void updateReqTable(PlanningPokerSession session) {
		reqTable.refresh(session);
	}

	private void setButtonVisibility(PlanningPokerSession session) {
		// Check if the buttons should appear
		/*
			btnEdit.isVisible(false);
			if (session.isEditable()) {
				btnEdit.isVisible(true);
			}
		*/
	}
	
	/**
	 * 
	 * @return endButton
	 */
	public JButton getEndVoteButton() {
		return this.btnEndVote;
	}
	
	public PlanningPokerSession getCurrentSession() {
		
		return this.currentSession;
	}
}
