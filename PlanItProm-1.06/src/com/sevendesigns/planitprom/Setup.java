package com.sevendesigns.planitprom;

import java.util.Calendar;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;
import com.sevendesigns.planitprom.utilities.ThemeManager.Theme;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Setup extends Activity
{
	static final int DATE_DIALOG_ID = 0;
	static final int GENDER_DIALOG_ID = 1;
	
	String m_name;
	Integer m_budget;
	Calendar m_eventDate;
	
	String[] m_genderArray;
	String m_gender;
	int m_genderChosen = 0;
	
	boolean m_integration;
	
	Button m_dateButton;
	Button m_genderButton;
	
	EditText m_nameEntry;
	EditText m_budgetEntry;
	
	ImageButton m_theme1;
	ImageButton m_theme2;
	ImageButton m_theme3;
	ImageButton m_theme4;
	
	private int m_year;    
	private int m_month;    
	private int m_day;   
	
	private boolean m_startedIntegration = false;
	
	private DatePickerDialog.OnDateSetListener m_dateSetListener =
	    	new DatePickerDialog.OnDateSetListener() 
	{                
		public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) 
	    {                    
	    	m_year = year;
	    	m_month = monthOfYear;                    
	    	m_day = dayOfMonth;  
	    	
	    	m_eventDate.set(Calendar.YEAR, year);
	    	m_eventDate.set(Calendar.MONTH, monthOfYear);
	    	m_eventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    	m_eventDate.set(Calendar.HOUR, 0);
	    	m_eventDate.set(Calendar.MINUTE, 0);
	    	m_eventDate.set(Calendar.SECOND, 0);  
	    	
	    	updateCalendarDisplay();                
	    }            
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		
		m_dateButton = (Button)findViewById(R.id.eventDateButton);
		m_genderButton = (Button)findViewById(R.id.genderButton);
		m_nameEntry = (EditText)findViewById(R.id.nameEntry);
		m_budgetEntry = (EditText)findViewById(R.id.budgetEntry);
		
		boolean isFacebook = getIntent().getBooleanExtra("isfacebook", false);
		
		m_eventDate = Calendar.getInstance();
		
		m_genderArray = this.getResources().getStringArray(R.array.gender);
		
		if (isFacebook)
		{
			m_name = getIntent().getStringExtra("name");
			m_gender = getIntent().getStringExtra("gender");
			if (m_gender.equalsIgnoreCase("female"))
			{
				m_genderChosen = 1;
			}
			else if (m_gender.equalsIgnoreCase("male"))
			{
				m_genderChosen = 2;
			}
			m_gender = m_genderArray[m_genderChosen-1];
			m_nameEntry.setText(m_name);
			m_genderButton.setText(m_gender);
		}
		else
		{
			m_gender = m_genderArray[m_genderChosen];
		}
		
		Calendar cal = Calendar.getInstance();
        m_month = cal.get(Calendar.MONTH);
        m_day = cal.get(Calendar.DAY_OF_MONTH);
        m_year = cal.get(Calendar.YEAR);
        
        m_theme1 = (ImageButton)findViewById(R.id.theme1);
    	m_theme2 = (ImageButton)findViewById(R.id.theme2);
    	m_theme3 = (ImageButton)findViewById(R.id.theme3);
    	m_theme4 = (ImageButton)findViewById(R.id.theme4);
        
        ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.SetupParentView), false);
        ThemeManager.SetHeader(this, findViewById(R.id.header), false);
        
        
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.setupHeaderText));
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.nameText));
        ThemeManager.SetFont(this, m_nameEntry);
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetText));
        ThemeManager.SetFont(this, m_budgetEntry);
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.eventDateText));
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.eventDateButton));
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.genderText));
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.genderButton));
        ThemeManager.SetFont(this, (TextView)findViewById(R.id.calendarIntegrationText));
        
        m_theme1.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 1, true));
    	m_theme2.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 2, false));
    	m_theme3.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 3, false));
    	m_theme4.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 4, false));
    	
    	if (App.PreviouslySetup)
    	{
    		m_nameEntry.setText(App.Name);
    		m_budgetEntry.setText(App.Budget.toString());
    		
    		m_year = App.EventDate.get(Calendar.YEAR);
	    	m_month = App.EventDate.get(Calendar.MONTH);
	    	m_day = App.EventDate.get(Calendar.DAY_OF_MONTH); 
	    	
	    	m_eventDate.set(Calendar.YEAR, m_year);
	    	m_eventDate.set(Calendar.MONTH, m_month);
	    	m_eventDate.set(Calendar.DAY_OF_MONTH, m_day);
	    	m_eventDate.set(Calendar.HOUR, 0);
	    	m_eventDate.set(Calendar.MINUTE, 0);
	    	m_eventDate.set(Calendar.SECOND, 0);  
	    	
	    	m_gender = App.Gender;
	    	
	    	if (m_gender.equals(m_genderArray[1]))
	    	{
	    		m_genderChosen = 1;
	    	}
	    	else
	    	{
	    		m_genderChosen = 2;
	    	}
	    	
	    	ToggleButton tb = (ToggleButton)findViewById(R.id.integrationToggle);
	    	if (App.DoCalendarIntegration)
	    	{
	    		m_integration = true;
	    	}
	    	else
	    	{
	    		m_integration = false;
	    	}
	    	tb.setChecked(m_integration);
	    	
	    	updateCalendarDisplay();
	    	updateGenderDisplay();
    	}
	}
	
	private void updateCalendarDisplay() 
    {    
    	StringBuilder sb = new StringBuilder();
    	if(ThemeManager.isMMDDYYYYDate())
    	{
    	   	sb.append(m_month+1); 
    	   	sb.append("."); 
    	   	sb.append(m_day);
    	   	sb.append(".");
    	   	sb.append(m_year);
    	}
    	else
    	{
    		sb.append(m_day);
    	   	sb.append(".");
    		sb.append(m_month+1); 
    	   	sb.append("."); 
    	   	sb.append(m_year);
    	}
        
        m_dateButton.setText(sb.toString());
    }
	
	private void updateGenderDisplay()
	{
		 m_genderButton.setText(m_gender);
	}

	@Override
    protected Dialog onCreateDialog(int id) 
    {    
    	switch (id) 
    	{    
    	case DATE_DIALOG_ID: 
    		DatePickerDialog dp = new DatePickerDialog(this,
    				m_dateSetListener,
    				m_year, 
    				m_month, 
    				m_day)
    		
    		 { 
                public void onDateChanged(DatePicker view, int year,int month, int day) 
                { 
                	Calendar now = Calendar.getInstance();
                    Calendar chosen = Calendar.getInstance();
                    
                    now.set(Calendar.HOUR, 0);
                    now.set(Calendar.MINUTE, 0);
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);
                    
                    chosen.set(Calendar.YEAR, year);
                    chosen.set(Calendar.MONTH, month);
                    chosen.set(Calendar.DAY_OF_MONTH, day);
                    chosen.set(Calendar.HOUR, 0);
                    chosen.set(Calendar.MINUTE, 0);
                    chosen.set(Calendar.SECOND, 0);   
                    chosen.set(Calendar.MILLISECOND, 0);

                    if (chosen.before(now))
                    {
                    	updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));	
                    }
                    
                    setTitle(Setup.this.getString(R.string.pick_date_title));
                } 
               
            }; 
    		dp.setTitle(Setup.this.getString(R.string.pick_date_title));
    		return dp;
    	case GENDER_DIALOG_ID:
    		Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Gender");
            builder.setSingleChoiceItems(m_genderArray, m_genderChosen, new DialogInterface.OnClickListener() 
            {    
        	    public void onClick(DialogInterface _dialog, int _item) 
        	    {        
        	    	m_genderChosen = _item;
        	    	m_gender = m_genderArray[_item];
        	    	_dialog.dismiss();
        	    	updateGenderDisplay();
        	    }
            });
            return builder.create();
    	}
    	return null;
    }
	
	public void doStartPlanning(View _view)
	{
		m_name = m_nameEntry.getText().toString();
		String budgetString = m_budgetEntry.getText().toString();
		
		if (m_name.length() == 0)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please enter a name")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			})
			.create()
			.show();
			
			return;
		}
		
		if (budgetString.length() == 0)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please enter a budget amount")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			})
			.create()
			.show();
			
			return;
		}
		
		if (m_gender.equals("TBD"))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please select a gender")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			})
			.create()
			.show();
			
			return;
		}
		
		App.DoCalendarIntegration = m_integration;
		
		
		m_budget = Integer.parseInt(budgetString);
		
		App.finishSetup(m_eventDate, m_name, m_gender, m_budget);
		
		if (m_integration)
		{
			m_startedIntegration = true;
			App.SetUpIntegration(this, m_eventDate);
		}
		else
		{
			Intent next = new Intent(this, Home.class);
			startActivity(next);
			finish();
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (m_startedIntegration)
		{
			Intent next = new Intent(this, Home.class);
			startActivity(next);
			finish();
		}
	}
	public void doPickGender(View _view)
	{
		showDialog(GENDER_DIALOG_ID);
	}
	
	public void doPickDate(View _view)
	{
		showDialog(DATE_DIALOG_ID);
	}
	
	public void doIntegration(View _view)
	{
		m_integration = ((ToggleButton) _view).isChecked();        
	}
	
	public void doTheme1(View _view)
	{
		ThemeManager.ThemeSelected = Theme.THEME_1;
		m_theme1.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 1, true));
    	m_theme2.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 2, false));
    	m_theme3.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 3, false));
    	m_theme4.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 4, false));
	}
	
	public void doTheme2(View _view)
	{
		ThemeManager.ThemeSelected = Theme.THEME_2;
		m_theme1.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 1, false));
    	m_theme2.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 2, true));
    	m_theme3.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 3, false));
    	m_theme4.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 4, false));
	}
	
	public void doTheme3(View _view)
	{
		ThemeManager.ThemeSelected = Theme.THEME_3;
		m_theme1.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 1, false));
    	m_theme2.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 2, false));
    	m_theme3.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 3, true));
    	m_theme4.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 4, false));
	}
	
	public void doTheme4(View _view)
	{
		ThemeManager.ThemeSelected = Theme.THEME_4;
		m_theme1.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 1, false));
    	m_theme2.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 2, false));
    	m_theme3.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 3, false));
    	m_theme4.setImageDrawable(ThemeManager.GetThemeButtonDrawable(this, 4, true));
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_api_key));
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	
	public void showPrivacyPolicy(View _view)
	{
		Utils.ShowInstructions(this, getString(R.string.privacy_policy));
	}
}
