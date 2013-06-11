package com.sevendesigns.planitprom;

import java.text.DecimalFormat;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.textwatcher.TaxFieldWatcher;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Calculator extends Activity
{
	EditText m_price;
	EditText m_tax;
	EditText m_discount;
	EditText m_tip;
	EditText m_total;
	
	Boolean m_percentOff;
	Boolean m_amountOff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);
		
		m_price = (EditText)findViewById(R.id.priceEntry);
		m_tax = (EditText)findViewById(R.id.taxRateEntry);
		m_discount = (EditText)findViewById(R.id.discountEntry);
		m_tip = (EditText)findViewById(R.id.tipEntry);
		m_total = (EditText)findViewById(R.id.totalEntry);
		
		m_total.setKeyListener(null);
		m_tax.addTextChangedListener(new TaxFieldWatcher());
		
		m_percentOff = true;
		m_amountOff = false;
		
		String cost = getIntent().getStringExtra("cost");
		if(cost!=null) m_price.setText(cost);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.calculatorParent), false);
		ThemeManager.SetHeader(this, findViewById(R.id.calcHeader), false);
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.calcHeaderText));
		
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.priceText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.taxRateText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.discountText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.tipText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.totalCostText));
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.costOfCreditText));
		
		ThemeManager.SetFont(this, m_price);
		ThemeManager.SetFont(this, m_tax);
		ThemeManager.SetFont(this, m_discount);
		ThemeManager.SetFont(this, m_tip);
		ThemeManager.SetFont(this, m_total);
		
		ThemeManager.SetCalcButton(this, (ImageButton)findViewById(R.id.calculate));
	}

	public void doCalculate(View _view)
	{
		Float calcValue;
		
		String taxString = m_tax.getText().toString();
		String priceString = m_price.getText().toString();
		String discountString = m_discount.getText().toString();
		String tipString = m_tip.getText().toString();
		
		Float tax;
		Float price;
		Float discount;
		Float tip;
		
		if (taxString.length() == 0)
		{
			tax = 0f;
		}
		else
		{
			tax = Float.parseFloat(taxString);
		}
		
		if (priceString.length() == 0)
		{
			price = 0f;
		}
		else
		{
			price = Float.parseFloat(priceString);
		}
		
		if (discountString.length() == 0)
		{
			discount = 0f;
		}
		else
		{
			discount = Float.parseFloat(discountString);
		}
		
		if (tipString.length() == 0)
		{
			tip = 0f;
		}
		else
		{
			tip = Float.parseFloat(tipString);
		}
		
		Float discountAmount;
		if (m_percentOff)
		{
			discountAmount = price * (discount / 100);
		}
		else
		{
			discountAmount = discount;
		}
		
		if (tax == 0)
		{
			calcValue = ((price - discountAmount) + tip);
		}
		else
		{
			calcValue = ((price - discountAmount) * ( 1 + tax / 100)) + tip;
		}
		
		m_total.setText(new DecimalFormat("####.00").format(calcValue));
		
	}
	
	public void doCostOfCredit(View _view)
	{
		Intent next = new Intent(this, CostOfCredit.class);
		
		String cost="";
		
		if(m_total.getText().length()>0){
			cost = m_total.getText().toString(); 
		}else if( m_price.getText().length()>0){
			cost=m_price.getText().toString();
		}
		
		next.putExtra("cost", cost);
		startActivity(next);
	}
	
	public void doDiscountType(View _view)
	{
		Button button = (Button)_view;
		if (m_percentOff)
		{
			m_percentOff = false;
			m_amountOff = true;
			
			button.setText(R.string.dollar);
		}
		else
		{
			m_percentOff = true;
			m_amountOff = false;
			
			button.setText(R.string.percent);
		}
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
