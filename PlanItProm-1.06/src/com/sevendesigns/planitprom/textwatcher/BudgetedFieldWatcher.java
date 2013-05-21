package com.sevendesigns.planitprom.textwatcher;

import com.sevendesigns.planitprom.BudgetDetail;

import android.text.Editable;
import android.text.TextWatcher;

public class BudgetedFieldWatcher implements TextWatcher 
{
	private boolean isInAfterTextChanged = false;
	String before;
	String after;
	
	@Override
	public void afterTextChanged(Editable s) 
	{
		if (!isInAfterTextChanged)
		{
			BudgetDetail.updateTotalBudgetRemaining();
			isInAfterTextChanged = false;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	{
		before = s.toString(); 
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		after = s.toString();
	}

}
