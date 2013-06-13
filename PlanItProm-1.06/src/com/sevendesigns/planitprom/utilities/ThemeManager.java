package com.sevendesigns.planitprom.utilities;

import java.util.Locale;

import com.sevendesigns.planitprom.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ThemeManager
{
	private static Typeface m_font = null; 
	
	public enum Theme
	{
		THEME_1,
		THEME_2,
		THEME_3,
		THEME_4
	}

	public static Theme ThemeSelected = Theme.THEME_1;
	
	private static String GetColorString()
	{
		switch (ThemeSelected)
		{
		case THEME_1:
			return "blue";
		case THEME_2:
			return "green";
		case THEME_3:
			return "orange";
		case THEME_4:
			return "pink";
		default:
			return "";
		}
	}
	
	private static String GetColorStringById(int _id)
	{
		switch (_id)
		{
		case 1:
			return "blue";
		case 2:
			return "green";
		case 3:
			return "orange";
		case 4:
			return "pink";
		default:
			return "";
		}
	}
	
	private static int GetColorValue()
	{
		switch (ThemeSelected)
		{
		case THEME_1:
			return Color.parseColor("#cce5f5");
		case THEME_2:
			return Color.parseColor("#b3ddda");
		case THEME_3:
			return Color.parseColor("#fbce9a");
		case THEME_4:
			return Color.parseColor("#ed96b6");
		default:
			return 0;
		}
	}
	
	private static int GetColorValueDark()
	{
		switch (ThemeSelected)
		{
		case THEME_1:
			return Color.parseColor("#bf1d3156");
		case THEME_2:
			return Color.parseColor("#bf265d4f");
		case THEME_3:
			return Color.parseColor("#bff0532f");
		case THEME_4:
			return Color.parseColor("#bfcb2243");
		default:
			return 0;
		}
	}
	
	public static String GetLocale()
	{
		Locale location = Locale.getDefault();
		
		if ((location.equals(Locale.CANADA)) || (location.equals(Locale.CANADA_FRENCH)))
		{
			return "_canada";
		}
		else
		{
			return "";
		}
	}
	
	public static boolean isMMDDYYYYDate()
	{
		Locale location = Locale.getDefault();
		
		if ((location.equals(Locale.CANADA)) || (location.equals(Locale.CANADA_FRENCH)))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static void SetSplashImage(Context _context, ImageView _view)
	{
		String loc = GetLocale();
		
		int id = _context.getResources().getIdentifier("splash" + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("splash", "drawable", _context.getPackageName());
		}
		
		_view.setImageResource(id);
	}
	
	public static void SetBackgroundImage(Context _context, View _view, Boolean _withPeople)
	{
		String color = GetColorString();
		String people = "";
		String loc = GetLocale();
		int id;
		
		if (_withPeople)
		{
			people = "_people";
		}
		
		id = _context.getResources().getIdentifier("bg_" + color + people + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("bg_" + color + people, "drawable", _context.getPackageName());
		}
		
		_view.setBackgroundResource(id);
	}

	public static void SetHeader(Context _context, View _view, Boolean _withLogo)
	{
		String color = GetColorString();
		String people = "";
		String loc = GetLocale();
		int id;
		
		if (_withLogo)
		{
			people = "_logo";
		}
		
		id = _context.getResources().getIdentifier("navbar_" + color + people + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("navbar_" + color + people, "drawable", _context.getPackageName());
		}
		
		_view.setBackgroundResource(id);
	}
	
	public static void SetFont(Context _context, TextView _view)
	{
		if (m_font == null)
		{
			m_font = Typeface.createFromAsset(_context.getAssets(), "OPTIMA.TTF");
		}
		_view.setTypeface(m_font);  
	}
	
	public static void SetHomeButtons(Context _context, ImageButton _budget, 
			ImageButton _tips, ImageButton _timeLine, ImageButton _photoGallery, ImageButton _takePhoto)
	{
		String color = GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier("button_budget_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("button_budget_" + color, "drawable", _context.getPackageName());
		}
		
		_budget.setImageResource(id);
		
		
		id = _context.getResources().getIdentifier("button_tips_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("button_tips_" + color, "drawable", _context.getPackageName());
		}
		
		_tips.setImageResource(id);
		
		
		id = _context.getResources().getIdentifier("button_timeline_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("button_timeline_" + color, "drawable", _context.getPackageName());
		}
		
		_timeLine.setImageResource(id);
		
		
		id = _context.getResources().getIdentifier("button_photo_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("button_photo_" + color, "drawable", _context.getPackageName());
		}
		
		_photoGallery.setImageResource(id);
		
		id = _context.getResources().getIdentifier("button_takephoto_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("button_takephoto_" + color, "drawable", _context.getPackageName());
		}
		
		_takePhoto.setImageResource(id);
	}
	
	public static void SetListItemHeaderColor(TextView _view)
	{
		_view.setBackgroundColor(GetColorValue());
	}
	
	public static void SetHeaderColorDark(TextView _view)
	{
		_view.setBackgroundColor(GetColorValueDark());
	}
	
	public static Drawable GetCheckMarkUnselected(Context _context)
	{
		return _context.getResources().getDrawable(R.drawable.checkmark_off);
	}
	
	public static Drawable GetCheckMarkSelected(Context _context)
	{
		String color = GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier("checkmark_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("checkmark_" + color, "drawable", _context.getPackageName());
		}

			
		return _context.getResources().getDrawable(id);
	}
	
	
	public static Drawable getThemedDrawable(Context context, String drawableBaseName){
		String colorSuffix = "_"+GetColorString();
		int id = context.getResources().getIdentifier(drawableBaseName+colorSuffix , "drawable", context.getPackageName());
		
		if (id == 0){
			id = context.getResources().getIdentifier(drawableBaseName, "drawable", context.getPackageName());
		}
		
		if(id==0) return null;

		return context.getResources().getDrawable(id);
		
	}
	
	public static Drawable getToggleCheckboxBackground(Context context){
		
		String baseName="btn_toggle_checkbox";
		
		return getThemedDrawable(context, baseName);
	}
	
	public static Drawable GetThemeButtonDrawable(Context _context, int _id, boolean _selected)
	{
		String image = "thumbnail_";
		String loc = GetLocale();
		int id;
		
		image += GetColorStringById( _id);
		
		if (_selected)
		{
			image += "_on";
		}
		else
		{
			image += "_off";
		}
		
		id = _context.getResources().getIdentifier(image + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(image, "drawable", _context.getPackageName());
		}

		return _context.getResources().getDrawable(id);
	}

	public static Drawable GetTipsImage(Context _context, String _imageName)
	{
		String image = _imageName + "_"  + GetColorString();
		
		int id = _context.getResources().getIdentifier(image, "drawable", _context.getPackageName());
		return _context.getResources().getDrawable(id);
	}

	public static void SetUpBudgetDetailButtonBar(Context _context, View _view)
	{
		String color = GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier("bottom_navbar_" + color + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier("bottom_navbar_" + color, "drawable", _context.getPackageName());
		}
		
		_view.setBackgroundResource(id);
	}

	public static void SetLogo(Context _context, ImageView _view)
	{
		String imageName = "logo_" + GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier(imageName + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(imageName, "drawable", _context.getPackageName());
		}
		
		_view.setImageResource(id);
	}

	public static void SetCalcButton(Context _context, ImageButton _view)
	{
		String imageName = "button_calculate_" + GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier(imageName + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(imageName, "drawable", _context.getPackageName());
		}
		
		_view.setImageResource(id);
	}

	public static void SetBarBackground(Context _context, View _view)
	{
		String image = GetColorString() + "_bar";
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier(image + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(image, "drawable", _context.getPackageName());
		}
		
		_view.setBackgroundDrawable(_context.getResources().getDrawable(id));
	}
	
	public static void SetBudgetButton(Context _context, ImageButton _view, String name)
	{
		String image = name + "_button_" + GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier(image + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(image, "drawable", _context.getPackageName());
		}
		
		_view.setImageDrawable(_context.getResources().getDrawable(id));
	}

	public static void SetInstructionsBackground(Context _context, View _background)
	{
		String background = "instructions_bg_" + GetColorString();
		String loc = GetLocale();
		int id;
		
		id = _context.getResources().getIdentifier(background + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(background, "drawable", _context.getPackageName());
		}
		
		_background.setBackgroundDrawable(_context.getResources().getDrawable(id));
	}

	public static void setToggleButtonBackground(Context _context, Button _toggle, boolean _selected)
	{
		String background = "button_arrow";
		String loc = GetLocale();
		int id;
		
		if (_selected)
		{
			background += "down";
		}
		
		background = background + "_" + GetColorString();

		id = _context.getResources().getIdentifier(background + loc, "drawable", _context.getPackageName());
		
		if (id == 0)
		{
			id = _context.getResources().getIdentifier(background, "drawable", _context.getPackageName());
		}
		
		_toggle.setBackgroundDrawable(_context.getResources().getDrawable(id));
	}
}
