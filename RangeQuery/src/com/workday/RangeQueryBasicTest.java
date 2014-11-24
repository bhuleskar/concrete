package com.workday;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing Range Query.
 * @author Ronald Bhuleskar
 *
 */
public class RangeQueryBasicTest {

	private RangeContainer rangeContainer;
	
	@Before
	public void setUp() {
		long data[] = {10,12,17,21,2,15,16};
		RangeContainerFactory factory = new RangeContainerFactoryImpl();
		rangeContainer = factory.createContainer(data);
		System.out.println(rangeContainer);	
	}
	
	@Test
	public void runARangeQuery() {
		
		Ids ids;
		
		ids = rangeContainer.findIdsInRange(14, 17, true, true);
		assertEquals(2, ids.nextId());
		assertEquals(5, ids.nextId());
		assertEquals(6, ids.nextId());
		assertEquals(Ids.END_OF_IDS, ids.nextId());
		
		
		ids = rangeContainer.findIdsInRange(14, 17, true, false);
		assertEquals(5, ids.nextId());
		assertEquals(6, ids.nextId());
		assertEquals(Ids.END_OF_IDS, ids.nextId());

		
		ids = rangeContainer.findIdsInRange(20, Long.MAX_VALUE, false, true);
		assertEquals(3, ids.nextId());
		assertEquals(Ids.END_OF_IDS, ids.nextId());

	}
	
}
