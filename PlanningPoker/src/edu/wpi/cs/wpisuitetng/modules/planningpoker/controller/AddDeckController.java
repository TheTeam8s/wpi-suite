/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author mandi1267
 *
 */
public class AddDeckController {
	private static AddDeckController instance;
	private AddDeckRequestObserver observer;
	
	/**
	 * Construct an AddDeckController for the given model, view pair
	
	 */
	private AddDeckController() {
		observer = new AddSessionRequestObserver(this);
	}
	/**
	
	 * @return the instance of the AddSessionController or creates one if it does not
	 * exist. */
	public static AddDeckController getInstance()
	{
		if(instance == null)
		{
			instance = new AddDeckController();
		}
		
		return instance;
	}
	
	/**
	 * This method adds a Deck to the server.
	 * @param newDeck is the Deck to be added to the server.
	 */
	public void addDeck(Deck newDeck) 
	{
		final Request request = Network.getInstance().makeRequest("planningpoker/deck", HttpMethod.PUT); // PUT == create
		request.setBody(newDeck.toJSON()); // put the new Planning Poker Session in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
	
	

}
