package com.sevendesigns.planitprom;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.data.ImageInfo;
import com.sevendesigns.planitprom.data.PictureGalleryItemInfo;
import com.sevendesigns.planitprom.data.TimeLineItem;
import com.sevendesigns.planitprom.data.TimeLineSubItem;
import com.sevendesigns.planitprom.data.TipsData;
import com.sevendesigns.planitprom.data.TipsSubData;
import com.sevendesigns.planitprom.database.DatabaseAccess;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.ThemeManager.Theme;
import com.sevendesigns.planitprom.utilities.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

public class App extends Application
{
	private static App Instance;
	
	private static SharedPreferences m_settings;
	private static DatabaseAccess m_dbAccess;
	
	private static ArrayList<BudgetCategoryItem> m_budgetCategoryItems;
	private static ArrayList<TipsData> m_tipsData;

	public static Calendar EventDate = Calendar.getInstance();
	public static String Name;
	public static String Gender;
	public static Integer Budget;
	public static Float BudgetSpent;
	public static boolean PreviouslySetup;
	public static boolean FinishedSetup = false;
	public static boolean DoCalendarIntegration = false;
	
	public static boolean BudgetInstructionsSeen = false;
	public static boolean BudgetDetailsInstructionsSeen = false;
	public static boolean CostOfCreditInstructionsSeen = false;
	
	public static boolean UseFacebook = false;
	
	public void onCreate() 
    {
		Instance = this;
		
		m_settings = getSharedPreferences(this.getString(R.string.pref_file_name), 0);
		
		m_dbAccess = new DatabaseAccess(this);
		
		switch(m_settings.getInt("Theme", 1))
		{
			case 1:
				ThemeManager.ThemeSelected = Theme.THEME_1;
				break;
			case 2:
				ThemeManager.ThemeSelected = Theme.THEME_2;
				break;
			case 3:
				ThemeManager.ThemeSelected = Theme.THEME_3;
				break;
			case 4:
				ThemeManager.ThemeSelected = Theme.THEME_4;
				break;
			default:
				ThemeManager.ThemeSelected = Theme.THEME_1;
				break;
		}
		
		Name = m_settings.getString("Name", "");
		Gender = m_settings.getString("Gender", "Female");
		
		Budget = m_settings.getInt("Budget", 0);
		BudgetSpent = m_settings.getFloat("BudgetSpent", 0.0f);
		
		long time = m_settings.getLong("EventDate", Calendar.getInstance().getTimeInMillis());
		EventDate.setTimeInMillis(time);
		
		BudgetInstructionsSeen = m_settings.getBoolean("BudgetInstructionsSeen", false);
		BudgetDetailsInstructionsSeen = m_settings.getBoolean("BudgetDetailsInstructionsSeen", false);
		CostOfCreditInstructionsSeen = m_settings.getBoolean("CostOfCreditInstructionsSeen", false);
		UseFacebook = m_settings.getBoolean("UseFacebook", false);
		
		PreviouslySetup = m_settings.getBoolean("SetUpBefore", false);
		
		FinishedSetup = m_settings.getBoolean("FinishedSetup", false);
		
		if (PreviouslySetup)
		{
			m_budgetCategoryItems = m_dbAccess.getBudgetCategoryItems();
			m_tipsData = LoadTipsData();
			updateBudgetSpent();
		}
	}
		
	public static void SetUpIntegration(Activity _activity, Calendar _date)
	{
		ArrayList<TimeLineItem> unSortedList = m_dbAccess.getTimeLineInfo();
		
		Integer daysTilEvent = Utils.GetDaysDifferenceFromToday(App.EventDate);
		
		/*
		19.1.1.	Date user completed setup is Start Date
		19.1.2.	Prom Date entered in setup is Prom Date
		
		19.1.3.	If [Prom Date] – [Start Date] is greater than or equal to 75
		19.1.3.1.	Header: Three Months Until Prom
		19.1.3.1.1.	Items: Three Month Items
		19.1.3.1.2.	Calendar Date: ([Prom Date] – 90 Days) if ([Prom Date] - [Start Date]) is greater than or equal to 90
		19.1.3.1.3.	Calendar Date: ([Start Date] + 1) if ([Prom Date] – [Start Date]) is less than 90
		19.1.3.2.	Header: Two Months Until Prom
		19.1.3.2.1.	Items: Two Month Items 
		19.1.3.2.2.	Calendar Date: [Prom Date] – 60 Days
		19.1.3.3.	Header: One Month Until Prom
		19.1.3.3.1.	Items: One Month Items 
		19.1.3.3.2.	Calendar Date: [Prom Date] – 30 Days
		19.1.3.4.	Header: Two Weeks Until Prom
		19.1.3.4.1.	Items: Two Week Items 
		19.1.3.4.2.	Calendar Date: [Prom Date] – 14 Days
		19.1.3.5.	Header: One Week Until Prom
		19.1.3.5.1.	Items: One Week Items 
		19.1.3.5.2.	Calendar Date: [Prom Date] – 7 Days
		19.1.3.6.	Header: Day of Prom
		19.1.3.6.1.	Items: Day Of Prom Content 
		19.1.3.6.2.	Calendar Date: [Prom Date]
		*/
		if (daysTilEvent > 75)
		{
			Calendar date;
			ArrayList<TimeLineSubItem> list;

			if (daysTilEvent >= 90)
			{
				date = (Calendar) App.EventDate.clone();
				date.roll(Calendar.MONTH, -3);
			}
			else
			{
				date = Calendar.getInstance();
				date.roll(Calendar.DAY_OF_MONTH, 1);
			}
			
			list = getItemsForTimePeriod(unSortedList, 90);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.three_months_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.MONTH, -2);
			list = getItemsForTimePeriod(unSortedList, 60);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.two_months_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.MONTH, -1);
			list = getItemsForTimePeriod(unSortedList, 30);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.one_months_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.WEEK_OF_YEAR, -3);
			list = getItemsForTimePeriod(unSortedList, 21);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.three_weeks_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.WEEK_OF_YEAR, -2);
			list = getItemsForTimePeriod(unSortedList, 14);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.two_weeks_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.WEEK_OF_YEAR, -1);
			list = getItemsForTimePeriod(unSortedList, 7);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.one_week_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -1);
			list = getItemsForTimePeriod(unSortedList, 1);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_before_event_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			list = getItemsForTimePeriod(unSortedList, 0); 
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_of_event_calendar), getEventsList(list));
		}
		
		/*
		19.1.4.	If [Prom Date] – [Start Date] is less than 75 and greater than or equal to 45
		19.1.4.1.	Header: Prom To-Do ASAP
		19.1.4.1.1.	Items: Three Month Items and Two Month Items 
		19.1.4.1.2.	Calendar Date: [Start Date] + 1 Day
		19.1.4.2.	Header: One Month Until Prom
		19.1.4.2.1.	Items: One Month Items 
		19.1.4.2.2.	Calendar Date: [Prom Date] – 30 Days
		19.1.4.3.	Header: Two Weeks Until Prom
		19.1.4.3.1.	Items: Two Week Items 
		19.1.4.3.2.	Calendar Date: [Prom Date] – 14 Days
		19.1.4.4.	Header: One Week Until Prom
		19.1.4.4.1.	Items: One Week Items 
		19.1.4.4.2.	Calendar Date: [Prom Date] – 7 Days
		19.1.4.5.	Header: Day of Prom
		19.1.4.5.1.	Items: Day Of Prom Items 
		19.1.4.5.2.	Calendar Date: [Prom Date]
		*/
		
		else if ((daysTilEvent <= 75) && (daysTilEvent > 45))
		{
			Calendar date;
			ArrayList<TimeLineSubItem> list;


			
			date = Calendar.getInstance();
			date.roll(Calendar.DAY_OF_YEAR, 1);
			list = getItemsForTimePeriod(unSortedList, 90);
			list.addAll(getItemsForTimePeriod(unSortedList, 60));
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.now_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -30);
			list = getItemsForTimePeriod(unSortedList, 30);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.one_months_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -21);
			list = getItemsForTimePeriod(unSortedList, 21);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.three_weeks_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -14);
			list = getItemsForTimePeriod(unSortedList, 14);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.two_weeks_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -7);
			list = getItemsForTimePeriod(unSortedList, 7);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.one_week_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -1);
			list = getItemsForTimePeriod(unSortedList, 1);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_before_event_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			list = getItemsForTimePeriod(unSortedList, 0); 
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_of_event_calendar), getEventsList(list));
			
		}
		
		/*
		19.1.5.	If [Prom Date] – [Start Date] is less than 45 and greater than or equal to 15
		19.1.5.1.	Header: Prom To-Do ASAP
		19.1.5.1.1.	Items: Three Month Items, Two Month Items and One Month Items 
		19.1.5.1.2.	Calendar Date: [Start Date] + 1 Day
		19.1.5.2.	Header: Two Weeks Until Prom
		19.1.5.2.1.	Items: Two Week Items 
		19.1.5.2.2.	Calendar Date: [Prom Date] – 14 Days
		19.1.5.3.	Header: One Week Until Prom
		19.1.5.3.1.	Items: One Week Items 
		19.1.5.3.2.	Calendar Date: [Prom Date] – 7 Days
		19.1.5.4.	Header: Day of Prom
		19.1.5.4.1.	Items: Day Of Prom Items 
		19.1.5.4.2.	Calendar Date: [Prom Date]
		*/
		
		else if ((daysTilEvent < 45) && (daysTilEvent >= 15))
		{
			Calendar date;
			ArrayList<TimeLineSubItem> list;

			date = Calendar.getInstance();
			date.roll(Calendar.DAY_OF_YEAR, 1);
			list = getItemsForTimePeriod(unSortedList, 90);
			list.addAll(getItemsForTimePeriod(unSortedList, 60));
			list.addAll(getItemsForTimePeriod(unSortedList, 30));
			list.addAll(getItemsForTimePeriod(unSortedList, 21));
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.now_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -14);
			list = getItemsForTimePeriod(unSortedList, 14);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.two_weeks_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -7);
			list = getItemsForTimePeriod(unSortedList, 7);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.one_week_away_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			date.roll(Calendar.DAY_OF_YEAR, -1);
			list = getItemsForTimePeriod(unSortedList, 1);
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_before_event_calendar), getEventsList(list));
			
			date = (Calendar) App.EventDate.clone();
			list = getItemsForTimePeriod(unSortedList, 0); 
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_of_event_calendar), getEventsList(list));
		}
		
		/*
		19.1.6.	If [Prom Date] – [Start Date] is less than 15 and greater than or equal to 1
		19.1.6.1.	Header: Prom To-Do ASAP
		19.1.6.1.1.	All items except Day of Prom Items 
		19.1.6.1.2.	Calendar Date: [Start Date] + 1 Day
		19.1.6.2.	Header: Day of Prom
		19.1.6.2.1.	Items: Day Of Prom Items 
		19.1.6.2.2.	Calendar Date: [Prom Date]
		*/
		
		else if ((daysTilEvent < 15) && (daysTilEvent >= 1))
		{
			Calendar date;
			ArrayList<TimeLineSubItem> list;

			date = Calendar.getInstance();
			date.roll(Calendar.DAY_OF_YEAR, 1);
			list = getItemsForTimePeriod(unSortedList, 90);
			list.addAll(getItemsForTimePeriod(unSortedList, 60));
			list.addAll(getItemsForTimePeriod(unSortedList, 30));
			list.addAll(getItemsForTimePeriod(unSortedList, 21));
			list.addAll(getItemsForTimePeriod(unSortedList, 14));
			list.addAll(getItemsForTimePeriod(unSortedList, 7));
			list.addAll(getItemsForTimePeriod(unSortedList, 1));
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.now_calendar), getEventsList(list));

			date = (Calendar) App.EventDate.clone();
			list = getItemsForTimePeriod(unSortedList, 0); 
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_of_event_calendar), getEventsList(list));
		}
		
		/*
		
		
		19.1.7.	If [Prom Date] – [Start Date] is 0 or negative
		19.1.7.1.	Header: Day of Prom
		19.1.7.1.1.	Items: Day Of Prom Items 
		19.1.7.1.2.	Calendar Date: [Prom Date]
		*/
		else
		{
			Calendar date;
			ArrayList<TimeLineSubItem> list;
			
			date = (Calendar) App.EventDate.clone();
			list = getItemsForTimePeriod(unSortedList, 0); 
			setUpCalendarIntent(_activity, date, _activity.getString(R.string.day_of_event_calendar), getEventsList(list));
		}
	}
	
	private static String getEventsList(ArrayList<TimeLineSubItem> _list)
	{
		String rc = "";
		
		for (int i = 0; i < _list.size(); i++)
		{
			rc += _list.get(i).Body;
			if (i + 1 !=  _list.size())
			{
				rc += "\n";
			}
		}
		
		return rc;
	}
	
	private static void setUpCalendarIntent(Activity _activity, Calendar _date, String _name, String _description)
	{
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", _date.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("endTime", _date.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", _name);
		intent.putExtra("description", _description);
		intent.putExtra("hasAlarm", true);
		intent.putExtra("minutes", 120);
		_activity.startActivity(intent);
	}
	
	public static void finishSetup(Calendar _eventDate, String _name, String _gender, Integer _budget)
	{
		SharedPreferences.Editor editor = m_settings.edit();
		
		switch(ThemeManager.ThemeSelected)
		{
			case THEME_1:
				editor.putInt("Theme", 1);
				break;
			case THEME_2:
				editor.putInt("Theme", 2);
				break;
			case THEME_3:
				editor.putInt("Theme", 3);
				break;
			case THEME_4:
				editor.putInt("Theme", 4);
				break;
			default:
				editor.putInt("Theme", 1);
				break;
		}
		
		Name = _name;
		Gender = _gender;
		editor.putString("Name", Name);
		editor.putString("Gender", Gender);
		
		Budget = _budget;
		editor.putInt("Budget", Budget);
		
		EventDate = _eventDate;
		long time = EventDate.getTimeInMillis();
		editor.putLong("EventDate", time);
		
		FinishedSetup = true;
		editor.putBoolean("FinishedSetup", true);
		
		if (!PreviouslySetup)
		{
			setUpCategories();
			setUpTimeline();
		}
		
		editor.commit();
		
		m_budgetCategoryItems = m_dbAccess.getBudgetCategoryItems();
		m_tipsData = LoadTipsData();
	}
	
	public static void updateBudgetSpent()
	{
		BudgetSpent = 0.0f;
		
		for (int i = 0; i < m_budgetCategoryItems.size(); i++)
		{
			BudgetCategoryItem item = m_budgetCategoryItems.get(i);
			
			if (item.Actual != -1)
			{
				BudgetSpent += item.Actual.floatValue();
			}
		}
		
		SharedPreferences.Editor editor = m_settings.edit();
		
		editor.putFloat("BudgetSpent", BudgetSpent);
		
		editor.commit();
	}
	
	public static void setBudgetInstructionsSeen()
	{
		BudgetInstructionsSeen = true;
		
		SharedPreferences.Editor editor = m_settings.edit();
		
		editor.putBoolean("BudgetInstructionsSeen", true);
		
		editor.commit();
	}
	
	public static void setUseFacebook()
	{
		UseFacebook = true;
		
		SharedPreferences.Editor editor = m_settings.edit();
		
		editor.putBoolean("UseFacebook", true);
		
		editor.commit();
	}
	
	public static void setBudgetDetailsInstructionsSeen()
	{
		BudgetDetailsInstructionsSeen = true;
		
		SharedPreferences.Editor editor = m_settings.edit();
		
		editor.putBoolean("BudgetDetailsInstructionsSeen", true);
		
		editor.commit();
	}
	
	public static void setCostOfCreditInstructionsSeen()
	{
		CostOfCreditInstructionsSeen = true;
		
		SharedPreferences.Editor editor = m_settings.edit();
		
		editor.putBoolean("CostOfCreditInstructionsSeen", true);
		
		editor.commit();
	}
	
	public static ArrayList<BudgetCategoryItem> getBudgetCategoryItems()
	{
		return m_budgetCategoryItems;
	}
	
	public static void updateBudgetCategory(int _id, Integer _budgeted, Float _actual, String _merchant)
	{
		m_dbAccess.updateCategory(_id, _budgeted, _actual, _merchant);
		m_budgetCategoryItems = m_dbAccess.getBudgetCategoryItems();
		updateBudgetSpent();
	}
	
	public static ArrayList<TipsData> getTipsData()
	{
		return m_tipsData;
	}
	
	private static ArrayList<TipsData> LoadTipsData()
	{
		ArrayList<TipsData> data = new ArrayList<TipsData>();
		
		int id;
		String loc = ThemeManager.GetLocale();
		String resourceBaseName;
		String[] rawTipData;
		
		if (Gender.equals("Female"))
		{
			resourceBaseName = "tips_female";
		}
		else
		{
			resourceBaseName = "tips_male";
		}
		
		id = Instance.getResources().getIdentifier(resourceBaseName + loc, "array", Instance.getPackageName());
		
		if (id == 0)
		{
			id = Instance.getResources().getIdentifier(resourceBaseName, "array", Instance.getPackageName());
		}
		
		rawTipData = Instance.getResources().getStringArray(id);
		
		for (int i = 0; i < rawTipData.length; i++)
		{
			TipsData item = new TipsData();
		
			String[] splitEntry = rawTipData[i].split("\\|");
			
			item.ImageName = splitEntry[0];
			item.Header = splitEntry[1];
			
			for (int j = 2; j < splitEntry.length; j++)
			{
				String[] split2 = splitEntry[j].split("\\~");
				
				TipsSubData subItem = new TipsSubData();
				
				subItem.Header = split2[0];
				subItem.Text = split2[1];
				
				item.SubTopics.add(subItem);
			}
			data.add(item);
		}
		
		return data;
	}
	
	private static void setUpCategories()
	{
		String[] categories;
		TypedArray tempTypedArray;
		int id;
		String loc = ThemeManager.GetLocale();
		String budgetCategoriesBaseName;
		String percValuesBaseName;
		
		if (Gender.equals("Female"))
		{
			budgetCategoriesBaseName = "budget_categories_female";
			percValuesBaseName = "perc_values_female";
		}
		else
		{
			budgetCategoriesBaseName = "budget_categories_male";
			percValuesBaseName = "perc_values_male";
		}
		
		id = Instance.getResources().getIdentifier(budgetCategoriesBaseName + loc, "array", Instance.getPackageName());
		
		if (id == 0)
		{
			id = Instance.getResources().getIdentifier(budgetCategoriesBaseName, "array", Instance.getPackageName());
		}
		
		categories = Instance.getResources().getStringArray(id);
		
		
	
		id = Instance.getResources().getIdentifier(percValuesBaseName + loc, "array", Instance.getPackageName());
		
		if (id == 0)
		{
			id = Instance.getResources().getIdentifier(percValuesBaseName, "array", Instance.getPackageName());
		}
		
		tempTypedArray = Instance.getResources().obtainTypedArray(id);
		
		
		 
		ArrayList<Float> temp = new ArrayList<Float>();
		
		for (int i = 0; i < tempTypedArray.length(); i++)
		{
			Float fTemp = tempTypedArray.getFloat(i, 0);
			temp.add(fTemp);
		}
			
		Float[] percs = new Float[temp.size()];
		percs = temp.toArray(percs);
		
		m_dbAccess.populateBudget(categories, percs);
		
		tempTypedArray.recycle();
		
		PreviouslySetup = true;
		
		m_settings.edit().putBoolean("SetUpBefore", true).commit();
	}
	
	private static void setUpTimeline()
	{
		ArrayList<TimeLineItem> timeLineList = new ArrayList<TimeLineItem>();
		String[] tipItems;
		int id;
		String loc = ThemeManager.GetLocale();
		String resourceBaseName;
		
		if (Gender.equals("Female"))
		{
			resourceBaseName = "time_line_female";
		}
		else
		{
			resourceBaseName = "time_line_male";
		}
		
		id = Instance.getResources().getIdentifier(resourceBaseName + loc, "array", Instance.getPackageName());
		
		if (id == 0)
		{
			id = Instance.getResources().getIdentifier(resourceBaseName, "array", Instance.getPackageName());
		}
		
		tipItems = Instance.getResources().getStringArray(id);
		
		for (int i = 0; i < tipItems.length; i++)
		{
			TimeLineItem item = new TimeLineItem();
			String[] splitTipInfo;
			String timeBase;
			String timeSplit[];
			String timeAmountString;
			String timePeriodString;
			Integer timeAmount;
			Integer timeMultiplier;
			
			splitTipInfo = tipItems[i].split("\\|");
			
			timeBase = splitTipInfo[0]; 
			timeSplit = timeBase.split("\\~");
			timeAmountString = timeSplit[0];
			timePeriodString = timeSplit[1];
			
			timeAmount = Integer.parseInt(timeAmountString);
			
			if (timePeriodString.equalsIgnoreCase("m"))
			{
				timeMultiplier = 30;
			}
			else if (timePeriodString.equalsIgnoreCase("w"))
			{
				timeMultiplier = 7;
			}
			else
			{
				timeMultiplier = 1;
			}

			timeAmount = timeAmount * timeMultiplier;
			
			
			item.DaysFromEvent = timeAmount;
			item.Title = "";
			
			for (int j = 1; j < splitTipInfo.length; j++)
			{
				TimeLineSubItem subItem = new TimeLineSubItem();
				
				subItem.Body = splitTipInfo[j];
				subItem.Checked = false;
			
				item.SubItems.add(subItem);
			}
			
			timeLineList.add(item);
		}
		
		m_dbAccess.saveTimeLineInfo(timeLineList);
	}
	
	public static ArrayList<TimeLineItem> getTimeLineItems()
	{
		ArrayList<TimeLineItem> unSortedList = m_dbAccess.getTimeLineInfo();
		ArrayList<TimeLineItem> sortedList = new ArrayList<TimeLineItem>();
		
		Integer daysTilEvent = Utils.GetDaysDifferenceFromToday(App.EventDate);
		
		if (daysTilEvent >= 75)
		{
			/*
			15.4.3.	If [Prom Date] – [Start Date] is greater than or equal to 75
			15.4.3.1.	Header: Three Months Away
			15.4.3.1.1.	Items: Three Month Items
			15.4.3.2.	Header: Two Months Away
			15.4.3.2.1.	Items: Two Month Items
			15.4.3.3.	Header: One Month Away
			15.4.3.3.1.	Items: One Month Items
			15.4.3.4.	Header: Two Weeks Away
			15.4.3.4.1.	Items: Two Week Items
			15.4.3.5.	Header: One Week Away
			15.4.3.5.1.	Items: One Week Items
			15.4.3.6.	Header: Day of Prom
			15.4.3.6.1.	Items: Day Of Prom Content
			*/
			
			TimeLineItem item = new TimeLineItem();
			item.Title = Instance.getString(R.string.three_months_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 90);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.two_months_away);
			item.SubItems= getItemsForTimePeriod(unSortedList, 60);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.one_months_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 30);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.three_weeks_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 21);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.two_weeks_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 14);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.one_months_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 7);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_before_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 1);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_of_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 0); 
			sortedList.add(item);
		}
		else if ((daysTilEvent < 75) && (daysTilEvent >= 45))
		{
			/*
			15.4.4.	If [Prom Date] – [Start Date] is less than 75 and greater than or equal to 45
			15.4.4.1.	Header: Now
			15.4.4.1.1.	Items: Three Month Items and Two Month Items
			15.4.4.2.	Header: One Month Away
			15.4.4.2.1.	Items: One Month Items
			15.4.4.3.	Header: Two Weeks Away
			15.4.4.3.1.	Items: Two Week Items
			15.4.4.4.	Header: One Week Away
			15.4.4.4.1.	Items: One Week Items
			15.4.4.5.	Header: Day of Prom
			15.4.4.5.1.	Items: Day Of Prom Items
			*/
			
			TimeLineItem item = new TimeLineItem();
			item.Title = Instance.getString(R.string.now_time_line);
			item.SubItems = getItemsForTimePeriod(unSortedList, 90);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.two_months_away);
			item.SubItems= getItemsForTimePeriod(unSortedList, 60);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.one_months_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 30);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.three_weeks_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 21);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.two_weeks_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 14);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.one_months_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 7);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_before_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 1);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_of_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 0); 
			sortedList.add(item);
			
		}
		else if ((daysTilEvent < 45) && (daysTilEvent >= 15))
		{
			/*
			15.4.5.	If [Prom Date] – [Start Date] is less than 45 and greater than or equal to 15
			15.4.5.1.	Header: Now
			15.4.5.1.1.	Items: Three Month Items, Two Month Items and One Month Items
			15.4.5.2.	Header: Two Weeks Away
			15.4.5.2.1.	Items: Two Week Items
			15.4.5.3.	Header: One Week Away
			15.4.5.3.1.	Items: One Week Items
			815.4.5.4.	Header: Day of Prom
			15.4.5.4.1.	Items: Day Of Prom Items
			*/
			
			TimeLineItem item = new TimeLineItem();
			item.Title = Instance.getString(R.string.now_time_line);
			item.SubItems = getItemsForTimePeriod(unSortedList, 90);
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 60));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 30));
			item.SubItems = getItemsForTimePeriod(unSortedList, 21);
			sortedList.add(item);
						
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.two_weeks_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 14);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.one_months_away);
			item.SubItems = getItemsForTimePeriod(unSortedList, 7);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_before_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 1);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_of_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 0); 
			sortedList.add(item);
			
		}
		else if ((daysTilEvent < 15) && (daysTilEvent >= 1))
		{
			/*
			15.4.6.	If [Prom Date] – [Start Date] is less than 15 and greater than or equal to 1
			15.4.6.1.	Header: Now
			15.4.6.1.1.	All items except Day of Prom Items
			15.4.6.2.	Header: Day of Prom
			15.4.6.2.1.	Items: Day Of Prom Items
			*/
			TimeLineItem item = new TimeLineItem();
			item.Title = Instance.getString(R.string.now_time_line);
			item.SubItems = getItemsForTimePeriod(unSortedList, 90);
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 60));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 30));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 21));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 14));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 7));
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_before_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 1);
			sortedList.add(item);
			
			item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_of_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 0); 
			sortedList.add(item);
		}
		else
		{
			/*
			15.4.7.	If [Prom Date] – [Start Date] is 0 or negative
			15.4.7.1.	Header: Day of Prom
			15.4.7.1.1.	Items: Day Of Prom Items
			//change as of 3/13/2013, add all the previous items as well
			*/
			
			TimeLineItem item = new TimeLineItem();
			item.Title = Instance.getString(R.string.day_of_event);
			item.SubItems = getItemsForTimePeriod(unSortedList, 90);
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 60));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 30));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 21));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 14));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 7));
			item.SubItems.addAll( getItemsForTimePeriod(unSortedList, 1));
			item.SubItems.addAll(getItemsForTimePeriod(unSortedList, 0)); 
			sortedList.add(item);
		}
		
		return sortedList;
	}
	
	static ArrayList<TimeLineSubItem> getItemsForTimePeriod(ArrayList<TimeLineItem> _timeLineItems, Integer _daysFromEvent)
	{
		ArrayList<TimeLineSubItem> returnItems = new ArrayList<TimeLineSubItem>();
		
		for (int i = 0; i < _timeLineItems.size(); i++)
		{
			TimeLineItem item = _timeLineItems.get(i);
			
			if (item.DaysFromEvent == _daysFromEvent)
			{
				for (int j = 0; j < item.SubItems.size(); j++)
				{
					returnItems.add(item.SubItems.get(j));
				}
				break;
			}
		}
		
		return returnItems;
	}

	public static void updateTimeLineSubItem(Integer _id, Boolean _checked)
	{
		m_dbAccess.updateTimeLineInfo(_id, _checked);
	}
	
	public static Integer addImageToGallery(Integer _categoryId, Integer _itemId, String _fileName)
	{
		m_dbAccess.saveImageInfo(_categoryId, _itemId, _fileName);
		return m_dbAccess.getImageIdByFileName(_fileName);
	}

	public static void saveGalleryItem(PictureGalleryItemInfo _newItem, Boolean _update)
	{
		m_dbAccess.saveGalleryItem(_newItem, _update);
	}
	
	public static ArrayList<PictureGalleryItemInfo> getPictureGalleryItems(Integer _categoryId)
	{
		return m_dbAccess.getGalleryItemsByCategoryId(_categoryId);
	}
	
	public static PictureGalleryItemInfo getPictureGalleryItem(Integer _itemId)
	{
		return m_dbAccess.getGalleryItemByItemId(_itemId);
	}
	
	public static ImageInfo getImageInfoByImageId(Integer _imageId)
	{
		return m_dbAccess.getImageInfoByImageId(_imageId);
	}
	
	public static Integer getImageIdByFileName(String _fileName)
	{
		return m_dbAccess.getImageIdByFileName(_fileName);
	}
	
	public static PictureGalleryItemInfo getGallerItemByImageId(Integer _imageId)
	{
		return m_dbAccess.getGalleryItemByImageId(_imageId);
	}
	
	public static Double getBudgetHealth()
	{
		Double budgetHealthSum = 0.0;
		DecimalFormat format = new DecimalFormat("#.##");
		
		for (int i = 0; i < m_budgetCategoryItems.size(); i++)
		{
			BudgetCategoryItem item = m_budgetCategoryItems.get(i);
			
			if ((item.Actual != -1) && (item.Budgeted != -1))
			{
				Double actual = item.Actual;

				if (actual > 0)
				{
					Double itemPercent = (Double) (actual / (double)Budget);
					Double delta = itemPercent - item.RecommendedSpendingPercent;
					
					delta = Double.parseDouble(format.format(delta));
					
					
					budgetHealthSum += delta;
				}
			}
		}
		
		return budgetHealthSum;
	}
}
