package com.sevendesigns.planitprom.data;

import java.util.ArrayList;

public class TimeLineItem
{
	public Integer ID;
	public Integer DaysFromEvent;
	public String Title;
	public ArrayList<TimeLineSubItem> SubItems;
	
	public TimeLineItem()
	{
		SubItems = new ArrayList<TimeLineSubItem>();
	}
}
