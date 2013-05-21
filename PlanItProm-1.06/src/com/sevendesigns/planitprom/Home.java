package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;
import com.sevendesigns.planitprom.widgets.BudgetHealthMeter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home extends Activity
{
	TextView m_amountRemaning;
	EditText m_daysLeft; 
	
	BudgetHealthMeter m_meter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		m_amountRemaning = (TextView)findViewById(R.id.budgetSpentText);
		m_daysLeft = (EditText)findViewById(R.id.daysLeft);
		m_meter = (BudgetHealthMeter)findViewById(R.id.meter);
		
		m_daysLeft.setKeyListener(null);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.homeParent), true);
		ThemeManager.SetHomeButtons(this, 
						(ImageButton)findViewById(R.id.homeBudgetButton), 
						(ImageButton)findViewById(R.id.tips), 
						(ImageButton)findViewById(R.id.timeline), 
						(ImageButton)findViewById(R.id.photogallery));
		ThemeManager.SetHeader(this, findViewById(R.id.headerHome), false);
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.eventDateText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetSpentText));
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setUpDaysTilEvent();
		setUpBudgetRemaining();
		
		m_meter.setHealthInfo(App.getBudgetHealth());
	}

	public void doBudget(View _view)
	{
		Intent next = new Intent (this, Budget.class);
		startActivity(next);
	}
	
	public void doTips(View _view)
	{
		Intent next = new Intent (this, Tips.class);
		startActivity(next);
	}
	
	public void doTimeline(View _view)
	{
		Intent next = new Intent (this, TimeLine.class);
		startActivity(next);
	}
	
	public void doPhotoGallery(View _view)
	{
		Intent next = new Intent (this, PictureGallery.class);
		startActivity(next);
	}
	
	public void doSettings(View _view)
	{
		Intent next = new Intent (this, Setup.class);
		startActivity(next);
		finish();
	}
	
	void setUpDaysTilEvent()
	{
		Integer daysTotal = Utils.GetDaysDifferenceFromToday(App.EventDate);
		
		m_daysLeft.setText(daysTotal.toString());
	}

	void setUpBudgetRemaining()
	{
		String amount = getString(R.string.budget_remaining_template);
		
	
		amount = String.format(amount, App.BudgetSpent, App.Budget);
		
		m_amountRemaning.setText(amount);
	}
	
	public void showPrivacyPolicy(View _view)
	{
		Utils.ShowInstructions(this, getString(R.string.privacy_policy));
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
}

