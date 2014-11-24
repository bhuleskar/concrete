package com.workday;
/**
 * An iterator of Id's
 * @author Ronald Bhuleskar
 */
public interface Ids {
	
	/**
	 * Marker to denote the end of the IDs
	 */
	static final short END_OF_IDS = -1;

	/**
	 * Return the next id in sequence, -1 if at end of data.
	 * The id's should be in sorted order (from lower to higher) to facilitate the query distribution into multiple containers.
	 * @return id at which the query held true.
	 */
	short nextId(); 
}