package com.sevendesigns.planitprom.data;

import java.util.ArrayList;

public class TipsData
{
	public String ImageName;
	public String Header;
	public ArrayList<TipsSubData> SubTopics;
	
	public TipsData()
	{
		SubTopics = new ArrayList<TipsSubData>();
	}
}
