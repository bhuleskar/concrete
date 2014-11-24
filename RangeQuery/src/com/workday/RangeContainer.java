package com.workday;

/**
 * A specialized container of records optimized for efficient range queries on an attribute of the data.
 * @author Ronald Bhuleskar
 *
 */
public interface RangeContainer {

	/**
	* @return The Id's of all instances found in the container that have data value between fromValue and toValue with optional inclusivity.
	*/
	Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive);
		
}