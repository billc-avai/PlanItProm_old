package com.sevendesigns.planitprom;

import java.text.DecimalFormat;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.textwatcher.CreditAPRFieldWatcher;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CostOfCredit extends Activity
{
	EditText m_itemCost;
	EditText m_interestRate;
	EditText m_3MonthTotal;
	EditText m_6MonthTotal;
	EditText m_12MonthTotal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cost_of_credit);
		
		m_itemCost = (EditText)findViewById(R.id.itemCostEntry);
		m_interestRate = (EditText)findViewById(R.id.creditAprEntry);
		m_3MonthTotal = (EditText)findViewById(R.id.threeMonthTotalEntry);
		m_6MonthTotal = (EditText)findViewById(R.id.sixMonthTotalEntry);
		m_12MonthTotal = (EditText)findViewById(R.id.twelveMonthTotalEntry);
		
		m_interestRate.addTextChangedListener(new CreditAPRFieldWatcher());
		
		m_3MonthTotal.setKeyListener(null);
		m_6MonthTotal.setKeyListener(null);
		m_12MonthTotal.setKeyListener(null);
		
		if (!App.CostOfCreditInstructionsSeen)
		{
			showInstructions(null);
			App.setCostOfCreditInstructionsSeen();
		}
		
		String cost_carryover = getIntent().getStringExtra("cost");
		if(cost_carryover!=null){
			m_itemCost.setText(cost_carryover);
		}
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.costOfCreditParent), false);
		ThemeManager.SetHeader(this, findViewById(R.id.costOfCreditHeader), false);
		
		ThemeManager.SetCalcButton(this, (ImageButton)findViewById(R.id.calculateButton));
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.costOfCreditHeaderText));
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.itemCostText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.creditAprText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.creditBlurb));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.threeMonthTotalText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.sixMonthTotalText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.twelveMonthTotalText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.costOfCreditInstructionsText));
		
		ThemeManager.SetFont(this, m_itemCost);
		ThemeManager.SetFont(this, m_interestRate);
		ThemeManager.SetFont(this, m_3MonthTotal);
		ThemeManager.SetFont(this, m_6MonthTotal);
		ThemeManager.SetFont(this, m_12MonthTotal);
	}

	public void showInstructions(View _view)
	{
		Utils.ShowInstructions(this, getString(R.string.cost_of_credit_instructions));
	}
	
	public void doCalculate(View _view)
	{
		String itemCostString;
		String interestRateString;
		
		itemCostString = m_itemCost.getText().toString();
		interestRateString = m_interestRate.getText().toString();
		
		if ((itemCostString.length() == 0) || (interestRateString.length() == 0))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(getString(R.string.cost_of_credit_data_missing));
			builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			});
	        builder.create().show();
	        return;
		}

		//((([Interest Rate]/12)/100)*[Item Cost])/(1-(1+(([Interest Rate]/12)/100))^(-1*[Duration]))*[Duration]
		
		Double itemCost = Double.parseDouble(itemCostString);
		Double intrestRate = Double.parseDouble(interestRateString);
		
		Double result3month;
		Double result6month;
		Double result12month;

		result3month = (((intrestRate/12.0)/100.0)*itemCost)/(1-Math.pow((1+((intrestRate/12.0)/100.0)),(-1*3))) * 3;
		result6month = (((intrestRate/12.0)/100.0)*itemCost)/(1-Math.pow((1+((intrestRate/12.0)/100.0)),(-1*6))) * 6;
		result12month = (((intrestRate/12.0)/100.0)*itemCost)/(1-Math.pow((1+((intrestRate/12.0)/100.0)),(-1*12))) * 12;
		
		m_3MonthTotal.setText(new DecimalFormat("#.00").format(result3month));
		m_6MonthTotal.setText(new DecimalFormat("#.00").format(result6month));
		m_12MonthTotal.setText(new DecimalFormat("#.00").format(result12month));
	}
	
	public void doHome(View _view)
	{
		Intent home = new Intent(this, Home.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home);
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
