package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.listadapters.BudgetAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;
import com.sevendesigns.planitprom.widgets.BudgetHealthMeter;
import com.sevendesigns.planitprom.widgets.BudgetHealthWidget;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Budget extends Activity
{
	ListView m_listView;
	BudgetAdapter m_adapter;
	BudgetHealthWidget mBudgetHealthWidget;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget);
		
		m_listView = (ListView)findViewById(R.id.budgetListView);
		
		if (!App.BudgetInstructionsSeen)
		{
			showInstructions(null);
			App.setBudgetInstructionsSeen();
		}
		
		mBudgetHealthWidget = (BudgetHealthWidget)findViewById(R.id.budgetHealthWidget);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.budgetParent), false);
		ThemeManager.SetHeader(this, findViewById(R.id.budgetHeader), false);
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.itemText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetedText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.tipText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetInstructionsText));
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetHeaderText));
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		m_adapter = new BudgetAdapter(this);
		m_listView.setAdapter(m_adapter);
		
		refreshBudgetHealth();
	}
	
	public void refreshBudgetHealth(){
		mBudgetHealthWidget.refreshData();
		mBudgetHealthWidget.invalidate();
	}
	
	public void showInstructions(View _view)
	{
		Utils.ShowInstructions(this, getString(R.string.budget_instructions));
	}
	
	public void doHome(View _view)
	{
		finish();
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
