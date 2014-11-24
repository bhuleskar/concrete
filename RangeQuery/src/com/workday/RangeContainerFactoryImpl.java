package com.workday;

/**
 * Implementation for Range Container Factory.
 * @author Ronald Bhuleskar
 *
 */
public class RangeContainerFactoryImpl implements RangeContainerFactory {

	/**
	 * Creates a RangeContainer from the given data set.
	 */
	@Override
	public RangeContainer createContainer(long[] data) {
		
		RangeContainerImpl rangeContainer = new RangeContainerImpl();
		rangeContainer.add(data);
		
		return rangeContainer;
	}

}
