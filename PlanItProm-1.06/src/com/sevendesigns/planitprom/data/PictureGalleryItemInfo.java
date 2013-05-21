package com.sevendesigns.planitprom.data;

import java.util.ArrayList;
import java.util.Calendar;

public class PictureGalleryItemInfo
{
	public Integer ItemId;
	public ArrayList<Integer> ImageId;
	public Integer CategoryId;
	public String Item;
	public String Cost;
	public String Merchant;
	public String Notes;
	public Calendar Date;
	
	public PictureGalleryItemInfo()
	{
		ImageId = new ArrayList<Integer>();
	}

	public void partialClone(PictureGalleryItemInfo _toClone)
	{
		ItemId = _toClone.ItemId; 
		CategoryId = _toClone.CategoryId;
		Item = _toClone.Item;
		Cost = _toClone.Cost;
		Merchant = _toClone.Merchant;
		Notes = _toClone.Notes;
		Date = _toClone.Date;
	}
}
