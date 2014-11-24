package com.workday;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Range Container Implmentation. 
 * Builds up a container list with n containers each holding some data, structured as follows.
 * 
 *   containerList
 * 
 * 		-----  		  List<Long> -- holds the data
 * 		|  	|   ->    List of size CONTAINER_SIZE to hold data { D1, D2, D3, ...} 
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
public class RangeContainerImpl implements RangeContainer {

	/**
	 * Defines the maximum elements each container can hold.
	 */
	private static final short CONTAINER_SIZE = 3;
	/**
	 * Defines the maximum number of threads to be created in the thread pool.
	 */
	private static short MAX_THREADS_IN_POOL = 2;

	private List<List<Long>> containerList = null;
	
	public RangeContainerImpl() {
		containerList = new ArrayList<List<Long>>();
	}
	
	public void add(long[] data) {
		
		//allocate the containers
		double numberOfRequiredContainers = Math.ceil((double)data.length/(double)CONTAINER_SIZE);
		for (int i= 0; i<numberOfRequiredContainers; i++) {
			containerList.add(i, new ArrayList<Long>(CONTAINER_SIZE));
		}
		
		//load data into container
		int containerIndex = 0;
		for (int dataIndex = 0; dataIndex<data.length; dataIndex++) {
			containerList.get(containerIndex).add(data[dataIndex]);
			if ((dataIndex+1) % CONTAINER_SIZE == 0) containerIndex++;
		}
	}
	
	/**
	 * Entry point for Query Execution. 
	 * This creates a worker thread and returns immediately.
	 * @return Iterator of type {@link Ids}.
	 */
	@Override
	public Ids findIdsInRange(long fromValue, 
			long toValue,
			boolean fromInclusive, 
			boolean toInclusive) {
		
		List<Future<List<Short>>> futureResults = new ArrayList<Future<List<Short>>>();
		//create thread pool
		ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS_IN_POOL);
		
		for (int i = 0; i<containerList.size(); i++) {
			//create worker threads
			ContainerSearchThread thread = new ContainerSearchThread(containerList.get(i), i, fromValue, toValue, fromInclusive, toInclusive);
			Future<List<Short>> future = threadPool.submit(thread);
			futureResults.add(i, future);
			
		}
		Ids iterator = new IdsImpl(futureResults);
		return iterator;	
	}
	
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for (List<Long> l1 : containerList) {
			sb.append('{');
			for (Long l2 : l1) {
				sb.append(l2.longValue()).append(',');
			}
			sb.append("},");
		}
		
		return sb.toString();
	}
	
	/**
	 * Thread to query each container.
	 * @author Ronald Bhuleskar
	 *
	 */
	private static class ContainerSearchThread implements Callable<List<Short>> {

		List<Long> container;
		int containerIndex;
		long fromValue;
		long toValue;
		boolean fromInclusive;
		boolean toInclusive;
		
		public ContainerSearchThread(List<Long> container,
				int containerIndex,
				long fromValue, 
				long toValue,
				boolean fromInclusive, 
				boolean toInclusive) {
			this.container = container;
			this.containerIndex = containerIndex;
			this.fromValue = fromValue;
			this.toValue = toValue;
			this.fromInclusive = fromInclusive;
			this.toInclusive = toInclusive;
		}
		
		/**
		 * Entry point of a thread in execution.
		 * @return List of index at which the query holds true. May be empty but not <code>null</code>.
		 */
		@Override
		public List<Short> call() throws Exception {
//			System.out.println("Thread " + containerIndex + " called for execution.");
			return findIdsInRangeInternal(containerIndex, fromValue, toValue, fromInclusive, toInclusive);
		}
		
		/**
		 * Queries the container with the given data.
		 * @param containerIndex
		 * @param fromValue
		 * @param toValue
		 * @param fromInclusive
		 * @param toInclusive
		 * @return List of indices at which the query holds true. May be empty but not <code>null</code>.
		 */
		private List<Short> findIdsInRangeInternal(int containerIndex, 
				long fromValue,
				long toValue,
				boolean fromInclusive,
				boolean toInclusive) {
		
			//uncomment to test - result is thrown out as soon as it is available
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			List<Short> result = new ArrayList<>();
			Long data;
			for (int i = 0; i<container.size(); i++) {
				data = container.get(i);
				if (((fromInclusive && data >= fromValue) || (!fromInclusive && data > fromValue))
						&&
						((toInclusive && data <= toValue) || (!toInclusive && data < toValue))) {
					result.add((short)(RangeContainerImpl.CONTAINER_SIZE*containerIndex + i));
				}
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append('{');
			for (Short l2 : result) {
				sb.append(l2.longValue()).append(',');
			}
			sb.append("},");
//			System.out.println("Result from Thread "+ containerIndex + " is : " +sb.toString() );
			
			return result;
		}
		
	}
	

}
