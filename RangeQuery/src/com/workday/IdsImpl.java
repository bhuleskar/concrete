package com.workday;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A blocking Iterator which iterates on the result set.
 * It can consume the data as soon as it is produced at the producer's end.
 * 
 * 
 * Builds up a container list with n containers each holding the Id's at which the query held true, structured as follows.
 * 
 *   containerList
 * 
 * 		-----         Future<List<Short>> -- holds the result that are generated from each Thread. Hence used with Future as results are loaded later.
 * 		|  	|   ->    List of size <= CONTAINER_SIZE to hold data { id1, id2, id3, ...} where id1, id2, id3 are the result of the query. 
 * 		-----  
 * 		|  	|   ->                ----- do----
 * 		-----  
 * 		|  	|   ->                ----- do----
 * 		-----  
 * 		|  	|   ->                ----- do----
 * 		-----  
 * 		|  	|   ->                ----- do----           
 * 		-----  
 * @author Ronald Bhuleskar
 *
 */
public class IdsImpl implements Ids {

	List<Future<List<Short>>> containerList;
	private int containerIndex = 0;
	private int dataIndex = 0;
 	
	public IdsImpl(List<Future<List<Short>>> containerList) {
		this.containerList = containerList;
	}

	@Override
	public short nextId() {
		try {
			//go to the first available non empty container
			while (containerIndex < containerList.size() &&
					(containerList.get(containerIndex) == null || containerList.get(containerIndex).get().size() == 0)) {
				containerIndex++;
			}

			//validate if pointers are within the boundary
			if (containerIndex>=containerList.size()
					|| containerIndex >= containerList.size()-1 && dataIndex >= containerList.get(containerIndex).get().size()) {
				return END_OF_IDS;
			}
			
			//fetch the List once the thread generates the data. The 1st call may block but not the successive calls within the same container
			Short data = containerList.get(containerIndex).get().get(dataIndex++);
			if (dataIndex == containerList.get(containerIndex).get().size()) { 
				containerIndex++;
				dataIndex = 0;
			}
	 		
			return data;
			
		} catch (InterruptedException e1) {
			System.out.println("Interrupted! Problem iterating.");
			e1.printStackTrace();
		} catch (ExecutionException e2) {
			System.out.println("Execution Error occured while iterating.");
			e2.printStackTrace();		
		}
	
		return END_OF_IDS;
		
	}

}
