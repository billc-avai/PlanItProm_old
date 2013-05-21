package com.sevendesigns.planitprom.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;

public class CreditAPRFieldWatcher implements TextWatcher 
{
	private boolean isInAfterTextChanged = false;
	String before;
	String after;
	int beforeDecimal;	
	
	@Override
	public void afterTextChanged(Editable s) 
	{
		if (!isInAfterTextChanged)
		{
			isInAfterTextChanged = true;
		
			StringBuilder sb = new StringBuilder();
			
			String test = s.toString();
			
			if (test.length() > 6)
			{
				test = test.substring(1);
			}
			
			int decimalLoc = test.indexOf(".");
			if (decimalLoc == -1)
			{
				if (test.length() == 1)
				{
					sb.append("00.00");
				}
				else if (test.length() == 2)
				{
					sb.append("00.0");
				}
				else if (test.length() == 3)
				{
					sb.append("00.");
				}
				sb.append(test);
			}
			else
			{
				String clean = test.substring(0, decimalLoc) + test.substring(decimalLoc+1);
				
				String front = clean.substring(0,2);
				String end = clean.substring(2);
				clean = front + "." + end;
				sb.append(clean);
			}
			
			s.clear();
			s.insert(0, sb.toString());
			
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
