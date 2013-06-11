package com.sevendesigns.planitprom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccessHelper extends SQLiteOpenHelper 
{
	Context m_context;
	
	public DatabaseAccessHelper(Context context) 
	{
		super(context, "PlanItPromDatabase", null, 3);
		m_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE " 
				+ "BudgetCategoryItems ("
				+ "CategoryId INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "Name TEXT, "
				+ "Image TEXT, "
				+ "ListImage TEXT, "
				+ "Budgeted INTEGER, "
				+ "Actual REAL, "
				+ "Merchant TEXT, "
				+ "RecommendedSpendingPercent REAL, "
				+ "Active INTEGER, "
				+ "ParentId INTEGER);");
		
		db.execSQL("CREATE TABLE " 
				+ "TimeLineItems ("
				+ "TimeLineGroupID INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "Date INTEGER);");
		
		db.execSQL("CREATE TABLE " 
				+ "TimeLineContent ("
				+ "TimeLineContentID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "TimeLineGroupID INTEGER,"
				+ "Content TEXT,"
				+ "Checked INTEGER);");
		
		db.execSQL("CREATE TABLE "
				+ "ImageGalleryInfo ("
				+ "ImageId INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "CategoryId INTEGER, "
				+ "FileName TEXT);");
		
		db.execSQL("CREATE TABLE "
				+ "PictureGalleryItem ("
				+ "ItemId INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "ImageId INTEGER, "
				+ "CategoryId INTEGER, "
				+ "Item TEXT, "
				+ "Cost TEXT, "
				+ "Merchant TEXT, "
				+ "Notes TEXT, "
				+ "Date TEXT);");
		
		db.execSQL("CREATE TABLE "
				+ "PictureGalleryItemImageList ("
				+ "ItemId INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "ImageId INTEGER);");
	}

	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS BudgetCategoryItems");
		db.execSQL("DROP TABLE IF EXISTS TimeLineItems");
		db.execSQL("DROP TABLE IF EXISTS TimeLineContent");
		db.execSQL("DROP TABLE IF EXISTS ImageGalleryInfo");
		db.execSQL("DROP TABLE IF EXISTS PictureGalleryItem");
		db.execSQL("DROP TABLE IF EXISTS PictureGalleryItemImageList");
		onCreate(db);
	}
}
