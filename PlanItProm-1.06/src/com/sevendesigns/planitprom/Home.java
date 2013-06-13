package com.sevendesigns.planitprom;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;
import com.sevendesigns.planitprom.widgets.BudgetHealthWidget;

public class Home extends Activity
{
	TextView m_amountRemaning;
	EditText m_daysLeft; 
	
	BudgetHealthWidget mBudgetHealthWidget;
	
	String[] mCategories;
	HashMap<String,Integer> mCategoryIdMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		m_amountRemaning = (TextView)findViewById(R.id.budgetSpentText);
		m_daysLeft = (EditText)findViewById(R.id.daysLeft);
		mBudgetHealthWidget = (BudgetHealthWidget)findViewById(R.id.budgetHealthWidget);
		
		m_daysLeft.setKeyListener(null);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.homeParent), true);
		ThemeManager.SetHomeButtons(this, 
						(ImageButton)findViewById(R.id.homeBudgetButton), 
						(ImageButton)findViewById(R.id.tips), 
						(ImageButton)findViewById(R.id.timeline), 
						(ImageButton)findViewById(R.id.photogallery),
						(ImageButton)findViewById(R.id.takephoto));
		ThemeManager.SetHeader(this, findViewById(R.id.headerHome), false);
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.eventDateText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetSpentText));
		
		//setup data for category list for Take Photo option
		ArrayList<BudgetCategoryItem> categories=App.getBudgetCategoryItems();
		mCategories = new String[categories.size()];
		mCategoryIdMap = new HashMap<String, Integer>();
		
		for(int i=0;i<categories.size();i++){
			BudgetCategoryItem item = categories.get(i); 
			mCategoryIdMap.put(item.Name, item.CategoryId);
			mCategories[i]=item.Name;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setUpDaysTilEvent();
		setUpBudgetRemaining();
		
		mBudgetHealthWidget.refreshData();
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
	
	public void doTakePhoto(View _view)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setTitle("Select the gallery where you wish to add a photo.");
	    
	    dialog.setItems(mCategories, new DialogInterface.OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	            
	            Intent intent = new Intent();
            	intent.setClass(getApplicationContext(),CameraScreen.class);
            	intent.putExtra("categoryid", mCategoryIdMap.get(mCategories[which]));
	            startActivity(intent);
	        }

	    });

	    dialog.show();
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

