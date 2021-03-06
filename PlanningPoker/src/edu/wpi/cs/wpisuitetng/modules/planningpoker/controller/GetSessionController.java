/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller coordinates retrieving all of the requirements
 * from the server.
 *
 * @version $Revision: 1.0 $
 * @author randyacheson kevinbarry
 */
public class GetSessionController implements ActionListener {

	private final GetSessionRequestObserver observer;
	private static final GetSessionController instance = new GetSessionController();

	/**
	 * Constructs the controller given a RequirementModel
	 */
	private GetSessionController() {
		observer = new GetSessionRequestObserver(this);
	}
	
	/**
	 * @return the instance of the GetRequirementController
	 */
	public static GetSessionController getInstance() {
		return instance;
	}

	/**
	 * Sends an HTTP request to store a requirement when the
	 * update button is pressed
	 * @param e ActionEvent
	
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Send a request to the core to save this requirement
		final Request request = Network.getInstance()
				.makeRequest("planningpoker/planningpokersession", HttpMethod.GET); // GET is read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Sends an HTTP request to retrieve all requirements
	 */
	public void retrieveSessions() {
		final Request request = Network.getInstance()
				.makeRequest("planningpoker/planningpokersession", HttpMethod.GET); // GET is read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}

	/**
	 * Add the given sessions to the local model (they were received from the core).
	 * This method is called by the GetSessionRequestObserver
	 * 
	 * @param pokerSessions list of sessions received from the server
	 */
	
	public void receivedSessions(PlanningPokerSession[] pokerSessions) {
		// Empty the local model to eliminate duplications
		PlanningPokerSessionModel.getInstance().emptyModel();
		
		// Make sure the response was not null
		if (pokerSessions != null) {
			
			// add the requirements to the local model
			PlanningPokerSessionModel.getInstance().addPlanningPokerSessions(pokerSessions);
			ViewEventController.getInstance().getOverviewTreePanel().refresh();
		}
	}
	
}
