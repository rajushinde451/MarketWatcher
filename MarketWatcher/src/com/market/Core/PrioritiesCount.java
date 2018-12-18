package com.market.Core;

public class PrioritiesCount {
	public int highPriorityCount;
	public int mediumPriorityCount;
	public int lowPriorityCount;
	
	public int getTotalCount() 
	{
		return highPriorityCount+mediumPriorityCount+lowPriorityCount;
	}
}
