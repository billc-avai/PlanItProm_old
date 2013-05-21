package com.sevendesigns.planitprom.utilities;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils
{
	final static long msPerDay = 1000*60*60*24; 
	
	public static void ShowInstructions(Context _context, String _instructions)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(_instructions);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
        builder.create().show();
	}
	
	public static Integer GetDaysDifference(Calendar _date1, Calendar _date2)
	{
		long msDate1 = _date1.getTimeInMillis();
		long msDate2 = _date2.getTimeInMillis();
		
		long diff = msDate2 - msDate1;
		
		Integer daysTotal = (int) (diff / msPerDay);
		
		if (daysTotal > 999)
		{
			daysTotal = 999;
		}
		if (daysTotal < 0)
		{
			daysTotal = 0;
		} 
		
		return daysTotal;
	}
	
	public static Integer GetDaysDifferenceFromToday(Calendar _date)
	{
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		long msToday = today.getTimeInMillis();
		long msEvent = _date.getTimeInMillis();
		
		long diff = msEvent - msToday;
		
		Integer daysTotal = (int) Math.floor(diff / msPerDay);

		
		if (daysTotal > 999)
		{
			daysTotal = 999;
		}
		if (daysTotal < 0)
		{
			daysTotal = 0;
		} 
		
		return daysTotal;
	}
	
	public static int BooleanToInt(boolean _bool)
	{
		if (_bool)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public static boolean IntToBoolean(int _int)
	{
		if (_int > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static Calendar formatDateTime(String timeToFormat) 
	{
	    Calendar date = Calendar.getInstance();
	    
		Date.parse(timeToFormat);
			
		date.setTimeInMillis(Date.parse(timeToFormat));

		return date;
	}
	
	public static int DpToPixels(Context _context, int _dp) 
	{
		float Scale = _context.getResources().getDisplayMetrics().density;
		return (int) (_dp * Scale + 0.5f);
	}
}
