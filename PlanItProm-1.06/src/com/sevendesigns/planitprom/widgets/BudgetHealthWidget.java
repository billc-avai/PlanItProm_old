package com.sevendesigns.planitprom.widgets;

import java.math.RoundingMode;
import java.text.NumberFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.R;

public class BudgetHealthWidget extends LinearLayout {

	public BudgetHealthWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
		inflater.inflate(R.layout.budget_health_widget, this, true);
		
		
		ToggleButton toggleBtn = (ToggleButton) findViewById(R.id.btn_hide_table);
		final View budgetTable = findViewById(R.id.budget_health_table);
		
		if(toggleBtn!=null){
			toggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(!isChecked){
						budgetTable.setVisibility(View.GONE);
					}else{
						budgetTable.setVisibility(View.VISIBLE);
					}
					
				}
			});
		}
		
	}
	
	public void refreshData(){
		NumberFormat formatter = NumberFormat.getIntegerInstance();
		formatter.setRoundingMode(RoundingMode.HALF_UP);
		String totalSpent = formatter.format(App.BudgetSpent);
		String totalBudget = formatter.format(App.Budget);
		String budgetedToDate = formatter.format(App.BudgetedToDate);
		
		TextView totalBudgetView = (TextView)findViewById(R.id.totalBudget);
		TextView totalSpentView = (TextView)findViewById(R.id.totalSpent);
		TextView budgetedToDateView = (TextView)findViewById(R.id.budgetedToDate);
		
		totalBudgetView.setText("$"+totalBudget);
		totalSpentView.setText("$"+totalSpent);
		budgetedToDateView.setText("$"+budgetedToDate);
		
		BudgetHealthMeter meter = (BudgetHealthMeter)findViewById(R.id.meter);
		meter.setHealthInfo(App.getBudgetHealth());
				
	}

}
