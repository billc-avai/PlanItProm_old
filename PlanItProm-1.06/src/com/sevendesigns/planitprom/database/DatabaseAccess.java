package com.sevendesigns.planitprom.database;

import java.util.ArrayList;

import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.data.ImageInfo;
import com.sevendesigns.planitprom.data.PictureGalleryItemInfo;
import com.sevendesigns.planitprom.data.TimeLineItem;
import com.sevendesigns.planitprom.data.TimeLineSubItem;
import com.sevendesigns.planitprom.utilities.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccess 
{	
	DatabaseAccessHelper m_helper;
	Context m_context;
	
	public DatabaseAccess(Context _context)
	{
		m_context = _context;
		m_helper = new DatabaseAccessHelper(m_context);
	}
	
	public ArrayList<BudgetCategoryItem> getBudgetCategoryItems()
	{
		ArrayList<BudgetCategoryItem> items = new ArrayList<BudgetCategoryItem>();
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "CategoryId", "Name", "Image", "ListImage", "Budgeted", "Actual", "Merchant", "RecommendedSpendingPercent"};
		
		Cursor results = db.query("BudgetCategoryItems", columns, null, null, null, null, null);
		
		while (results.moveToNext())
		{
			BudgetCategoryItem newItem = new BudgetCategoryItem();
			
			newItem.CategoryId					= results.getInt(results.getColumnIndex("CategoryId"));
			newItem.Name 						= results.getString(results.getColumnIndex("Name")); 
			newItem.Image						= results.getString(results.getColumnIndex("Image"));
			newItem.ListImage					= results.getString(results.getColumnIndex("ListImage"));
			newItem.Budgeted 					= results.getInt(results.getColumnIndex("Budgeted"));
			newItem.Actual 						= results.getDouble(results.getColumnIndex("Actual"));
			newItem.Merchant					= results.getString(results.getColumnIndex("Merchant"));
			newItem.RecommendedSpendingPercent  = results.getFloat(results.getColumnIndex("RecommendedSpendingPercent"));
			
			items.add(newItem);
		}
		results.close();
		db.close();
		
		return items;
	}
	
	public void populateBudget(String[] _categories, Float[] _categoryPercs)
	{
		SQLiteDatabase db = m_helper.getWritableDatabase();
		
		for (int i = 0; i < _categories.length; i++)
		{
			String[] s = _categories[i].split("\\|");
			Float perc = _categoryPercs[i];
			
			ContentValues values = new ContentValues(); 
			values.put("CategoryId", i);
			values.put("Name", s[0]);
			values.put("Image", s[1]);
			values.put("ListImage", s[2]);
			values.put("Budgeted", -1);
			values.put("Actual", -1.0);
			values.put("Merchant", "");
			values.put("RecommendedSpendingPercent", perc);
			
			db.insert("BudgetCategoryItems", null, values);
		}
		
		db.close();
	}
	
	public void updateCategory(int _categoryId, Integer _budgeted, Float _actual, String _merchant)
	{
		SQLiteDatabase db = m_helper.getWritableDatabase();
		
		
		ContentValues values = new ContentValues(); 
		if (_budgeted == null)
		{
			values.put("Budgeted", -1);
		}
		else
		{
			values.put("Budgeted", _budgeted);
		}
		
		if (_actual == null)
		{
			values.put("Actual", -1.0);
		}
		else
		{
			values.put("Actual", _actual);
		}
		
		if (_merchant == null)
		{
			values.put("Merchant", "");
		}
		else
		{
			values.put("Merchant", _merchant);
		}

		String where = "CategoryId=?";
		String[] whereArgs = new String[] {Integer.toString(_categoryId)};
		
		db.update("BudgetCategoryItems", values, where, whereArgs);
		
		db.close();
	}
	
	public void saveTimeLineInfo(ArrayList<TimeLineItem> _timeLineList)
	{
		SQLiteDatabase db = m_helper.getWritableDatabase();
		
		for (int i = 0; i < _timeLineList.size(); i++)
		{
			TimeLineItem item = _timeLineList.get(i);
			
			ContentValues values = new ContentValues(); 
			values.put("TimeLineGroupID", i);
			values.put("Date", item.DaysFromEvent);
			
			db.insert("TimeLineItems", null, values);
			
			for (int j = 0; j < item.SubItems.size(); j++)
			{
				TimeLineSubItem subItem = item.SubItems.get(j);
				
				ContentValues subValues = new ContentValues();
				subValues.put("TimeLineGroupID", i);
				subValues.put("Content", subItem.Body); 
				subValues.put("Checked", 0);
				
				db.insert("TimeLineContent", null, subValues);
			}
		}
		
		db.close();
	}
	
	public ArrayList<TimeLineItem> getTimeLineInfo()
	{
		ArrayList<TimeLineItem> items = new ArrayList<TimeLineItem>();
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "TimeLineGroupID", "Date"};
		
		Cursor results = db.query("TimeLineItems", columns, null, null, null, null, null);
		
		while (results.moveToNext())
		{
			TimeLineItem newItem = new TimeLineItem();
			
			newItem.ID			  = results.getInt(results.getColumnIndex("TimeLineGroupID"));
			newItem.DaysFromEvent = results.getInt(results.getColumnIndex("Date")); 
			
			items.add(newItem);
		}
		results.close();
		db.close();
		
		db = m_helper.getReadableDatabase();
		columns = new String[] { "TimeLineContentID", "TimeLineGroupID", "Content", "Checked"};
		String query = "TimeLineGroupId = ?";
		
		for (int i = 0; i < items.size(); i++)
		{
			TimeLineItem item = items.get(i);
			String[] queryArgs = new String[] {item.ID.toString()};
			
			results = db.query("TimeLineContent", columns, query, queryArgs, null, null, null);
		
			while (results.moveToNext())
			{
				TimeLineSubItem newSubItem = new TimeLineSubItem();
				
				newSubItem.ID 			= results.getInt(results.getColumnIndex("TimeLineContentID"));
				newSubItem.CategoryID 	= results.getInt(results.getColumnIndex("TimeLineGroupID"));
				newSubItem.Body 		= results.getString(results.getColumnIndex("Content"));
				newSubItem.Checked 		= Utils.IntToBoolean(results.getInt(results.getColumnIndex("Checked")));
				
				item.SubItems.add(newSubItem);
			}
			results.close();
		}
			
		db.close();
		
		return items;
	}
	
	public void updateTimeLineInfo(Integer _id, Boolean _checked)
	{
		SQLiteDatabase db = m_helper.getWritableDatabase();
		
		ContentValues values = new ContentValues(); 
		values.put("Checked", Utils.BooleanToInt(_checked));
		
		String where = "TimeLineContentID=?";
		String[] whereArgs = new String[] {Integer.toString(_id)};
		
		db.update("TimeLineContent", values, where, whereArgs);
		
		db.close();
	}

	public void saveImageInfo(Integer _categoryId, Integer _itemId, String _fileName)
	{
		SQLiteDatabase db = m_helper.getWritableDatabase();
				
		ContentValues values = new ContentValues();
		if (_itemId != -1)
		{
			values.put("ImageId", _itemId);
		}
		values.put("CategoryId", _categoryId);
		values.put("FileName", _fileName);
					
		db.insert("ImageGalleryInfo", null, values);
				
		db.close();
	}
	
	public ArrayList<ImageInfo> getImageInfoByCategoryId(Integer _categoryId)
	{
		ArrayList<ImageInfo> items = new ArrayList<ImageInfo>();
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "ImageId", "CategoryId" ,"FileName"};
		String query = "CategoryId = ?";
		String[] queryArgs = {_categoryId.toString()};
		
		Cursor results = db.query("ImageGalleryInfo", columns, query, queryArgs, null, null, null);
		
		while (results.moveToNext())
		{
			ImageInfo item = new ImageInfo();
			
			item.ImageId 	= results.getInt(results.getColumnIndex("ImageId"));
			item.CategoryId = results.getInt(results.getColumnIndex("CategoryId"));
			item.FileName 	= results.getString(results.getColumnIndex("FileName"));
			
			items.add(item);
		}
		results.close();
		db.close();
		
		return items;
	}
	
	public ImageInfo getImageInfoByImageId(Integer _imageId)
	{
		ImageInfo item = new ImageInfo();
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "ImageId", "CategoryId" ,"FileName"};
		String query = "ImageId = ?";
		String[] queryArgs = {_imageId.toString()};
		
		Cursor results = db.query("ImageGalleryInfo", columns, query, queryArgs, null, null, null);
		
		if (results.moveToNext())
		{	
			item.ImageId 	= results.getInt(results.getColumnIndex("ImageId"));
			item.CategoryId = results.getInt(results.getColumnIndex("CategoryId"));
			item.FileName 	= results.getString(results.getColumnIndex("FileName"));
		}
		results.close();
		db.close();
		
		return item;
	}
	
	public Integer getImageIdByFileName(String _fileName)
	{
		Integer imageId = -1;
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "ImageId", "CategoryId" ,"FileName"};
		String query = "FileName = ?";
		String[] queryArgs = {_fileName};
		
		Cursor results = db.query("ImageGalleryInfo", columns, query, queryArgs, null, null, null);
		
		if (results.moveToNext())
		{	
			imageId = results.getInt(results.getColumnIndex("ImageId"));
		}
		
		results.close();
		db.close();
		
		return imageId;
	}

	public void saveGalleryItem(PictureGalleryItemInfo _item, boolean _update)
	{
		SQLiteDatabase db = m_helper.getWritableDatabase();
		
		ContentValues values = new ContentValues(); 
		
		values.put("CategoryId", _item.CategoryId);
		values.put("Item", _item.Item);
		values.put("Cost", _item.Cost);
		values.put("Merchant", _item.Merchant);
		values.put("Notes", _item.Notes);
		values.put("Date", _item.Date.getTime().toString());
		
		if (_update)
		{
			String where = "ItemId = ?";
			String[] whereArgs = {_item.ItemId.toString()};
			db.update("PictureGalleryItem", values, where, whereArgs);
		}
		else
		{
			db.insert("PictureGalleryItem", null, values);
		}
		
		if (_update)
		{
			db.execSQL("Delete from PictureGalleryItemImageList where ItemId = " + _item.ItemId.toString());
		}
		
		for (int i = 0; i < _item.ImageId.size(); i++)
		{
			values = new ContentValues();
			values.put("ItemId", _item.ItemId);
			values.put("ImageId", _item.ImageId.get(i));
			
			db.insert("PictureGalleryItemImageList", null, values);
		}
		
		db.close();
	}
	
	public PictureGalleryItemInfo getGalleryItemByItemId(Integer _itemId)
	{
		PictureGalleryItemInfo item = new PictureGalleryItemInfo();
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "ItemId", "ImageId", "CategoryId", "Item", "Cost", "Merchant" ,"Notes", "Date"};
				
		String query = "ItemId = ?";
		String[] queryArgs = {_itemId.toString()};
		
		Cursor results = db.query("PictureGalleryItem", columns, query, queryArgs, null, null, null);
		
		if (results.moveToNext())
		{	
			item.ItemId = results.getInt(results.getColumnIndex("ItemId"));
			item.CategoryId = results.getInt(results.getColumnIndex("CategoryId")); 
			item.Item = results.getString(results.getColumnIndex("Item"));
			item.Cost = results.getString(results.getColumnIndex("Cost"));
			item.Merchant = results.getString(results.getColumnIndex("Merchant"));
			item.Notes = results.getString(results.getColumnIndex("Notes"));
			String dateString = results.getString(results.getColumnIndex("Date"));
			item.Date = Utils.formatDateTime(dateString);
			
			String temp = item.Date.getTime().toString();
			temp += temp;
		}
		
		results.close();
		
		columns = new String[] { "ItemId", "ImageId"};
		
		results = db.query("PictureGalleryItemImageList", columns, query, queryArgs, null, null, null);
		
		while (results.moveToNext())
		{	
			item.ImageId.add( results.getInt(results.getColumnIndex("ImageId")));
		}
		
		results.close();
		db.close();
		
		return item;
	}
	
	public ArrayList<PictureGalleryItemInfo> getGalleryItemsByCategoryId(Integer _categoryId)
	{
		ArrayList<PictureGalleryItemInfo> items = new ArrayList<PictureGalleryItemInfo>();
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		String[] columns = new String[] { "ItemId", "ImageId", "CategoryId", "Item", "Cost", "Merchant" ,"Notes", "Date"};
				
		String query = "CategoryId = ?";
		String[] queryArgs = {_categoryId.toString()};
		
		Cursor results = db.query("PictureGalleryItem", columns, query, queryArgs, null, null, null);
		
		while (results.moveToNext())
		{
			PictureGalleryItemInfo item = new PictureGalleryItemInfo();
			
			item.ItemId = results.getInt(results.getColumnIndex("ItemId"));
			item.CategoryId = results.getInt(results.getColumnIndex("CategoryId")); 
			item.Item = results.getString(results.getColumnIndex("Item"));
			item.Cost = results.getString(results.getColumnIndex("Cost"));
			item.Merchant = results.getString(results.getColumnIndex("Merchant"));
			item.Notes = results.getString(results.getColumnIndex("Notes"));
			String dateString = results.getString(results.getColumnIndex("Date"));
			item.Date = Utils.formatDateTime(dateString);
			
			items.add(item);
		}
		
		results.close();
		
		for (int i = 0; i < items.size(); i++)
		{
			PictureGalleryItemInfo item = items.get(i);
		
			columns = new String[] { "ItemId", "ImageId"};
			query = "ItemId = ?";
			String[] queryArgs2 = { item.ItemId.toString() };
			
			results = db.query("PictureGalleryItemImageList", columns, query, queryArgs2, null, null, null);
			
			while (results.moveToNext())
			{	
				item.ImageId.add( results.getInt(results.getColumnIndex("ImageId")));
			}
			
			results.close();
		}
		
		db.close();
		
		return items;
	}

	public PictureGalleryItemInfo getGalleryItemByImageId(Integer _imageId)
	{
		PictureGalleryItemInfo item = new PictureGalleryItemInfo();
		Integer itemId = -1;
		
		SQLiteDatabase db = m_helper.getReadableDatabase();
		
		
		String[] columns2 = new String[] { "ItemId", "ImageId"};
		
		String query2 = "ImageId = ?";
		String[] queryArgs2 = {_imageId.toString()};
		
		Cursor results = db.query("PictureGalleryItemImageList", columns2, query2, queryArgs2, null, null, null);
		
		while (results.moveToNext())
		{	
			itemId = results.getInt(results.getColumnIndex("ItemId"));
			item.ImageId.add( results.getInt(results.getColumnIndex("ImageId")));
		}

		results.close();
		
		String[] columns = new String[] { "ItemId", "ImageId", "CategoryId", "Item", "Cost", "Merchant" ,"Notes", "Date"};
				
		String query = "ItemId = ?";
		String[] queryArgs = {itemId.toString()};
		
		results = db.query("PictureGalleryItem", columns, query, queryArgs, null, null, null);
		
		if (results.moveToNext())
		{	
			item.ItemId = results.getInt(results.getColumnIndex("ItemId"));
			item.CategoryId = results.getInt(results.getColumnIndex("CategoryId")); 
			item.Item = results.getString(results.getColumnIndex("Item"));
			item.Cost = results.getString(results.getColumnIndex("Cost"));
			item.Merchant = results.getString(results.getColumnIndex("Merchant"));
			item.Notes = results.getString(results.getColumnIndex("Notes"));
			String dateString = results.getString(results.getColumnIndex("Date"));
			item.Date = Utils.formatDateTime(dateString);
			
			String temp = item.Date.getTime().toString();
			temp += temp;
		}
		
		
		results.close();
		db.close();
		
		return item;
	}
}
