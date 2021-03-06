/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

public class RequirementEstimateStatsTest {
	ArrayList<Estimate> testEstimates = new ArrayList<Estimate>();
	
	RequirementEstimateStats testStats = new RequirementEstimateStats(0, testEstimates);
	
	@Test
	public void testCalculateMean() {
		assertEquals(0, testStats.getMean(), 0.1);
		testStats.add(new Estimate(1, 1, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(1, testStats.getMean(), 0.1);
		testStats.add(new Estimate(2, 2, UUID.randomUUID()));
		testStats.add(new Estimate(3, 3, UUID.randomUUID()));
		testStats.add(new Estimate(7, 7, UUID.randomUUID()));
		testStats.add(new Estimate(8, 8, UUID.randomUUID()));
		assertEquals(4.2, testStats.getMean(), 0.01);
		testStats.add(new Estimate(9, 13, UUID.randomUUID()));
		assertEquals(5.66, testStats.getMean(), 0.01);
		testStats.add(new Estimate(10, 0, UUID.randomUUID()));
		assertEquals(5.66, testStats.getMean(), 0.01);
	}
	
	@Test
	public void testCalculateMedian() {
		assertEquals(0, testStats.getMedian(), 0.1);
		testStats.add(new Estimate(1, 1, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(1, testStats.getMedian(), 0.1);
		testStats.add(new Estimate(2, 2, UUID.randomUUID()));
		testStats.add(new Estimate(3, 3, UUID.randomUUID()));
		testStats.add(new Estimate(7, 7, UUID.randomUUID()));
		testStats.add(new Estimate(8, 8, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(3, testStats.getMedian(), 0.1);
		testStats.add(new Estimate(9, 9, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(5, testStats.getMedian(), 0.1);
		testStats.add(new Estimate(10, 0, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(5, testStats.getMedian(), 0.1);
	}
	
	@Test
	public void testCalculateStdDev() {
		assertEquals(0, testStats.getStdDev(), 0.1);
		testStats.add(new Estimate(1, 1, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(0, testStats.getStdDev(), 0.1); 
		testStats.add(new Estimate(2, 2, UUID.randomUUID()));
		testStats.add(new Estimate(3, 3, UUID.randomUUID()));
		testStats.add(new Estimate(7, 7, UUID.randomUUID()));
		testStats.add(new Estimate(8, 8, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(2.8, testStats.getStdDev(), 0.1);
		testStats.add(new Estimate(9, 9, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(3.1, testStats.getStdDev(), 0.1);
		testStats.add(new Estimate(10, 0, UUID.randomUUID()));
		testStats.refreshAll();
		assertEquals(3.1, testStats.getStdDev(), 0.1);
	}
	
	@Test
	public void testGetters() {
		assertEquals(0, testStats.getID());
		ArrayList<Estimate> aList = new ArrayList<Estimate>();
		assertEquals(aList, testStats.getEstimates());
		UUID firstID = UUID.randomUUID();
		testStats.add(new Estimate(1, 1, firstID));
		aList.add(new Estimate(1, 1, firstID));
		assertEquals(aList.size(), testStats.getEstimates().size());
		assertEquals(aList.get(0).getRequirementID(), 
				testStats.getEstimates().get(0).getRequirementID());
		assertEquals(aList.get(0).getVote(), testStats.getEstimates().get(0).getVote());
		assertEquals(aList.get(0).getSessionID(), 
				testStats.getEstimates().get(0).getSessionID());
	}
}
