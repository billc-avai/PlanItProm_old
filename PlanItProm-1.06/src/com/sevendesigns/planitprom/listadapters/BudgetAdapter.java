package com.sevendesigns.planitprom.listadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.sevendesigns.planitprom.App;
import com.sevendesigns.planitprom.Budget;
import com.sevendesigns.planitprom.BudgetDetail;
import com.sevendesigns.planitprom.R;
import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.utilities.ThemeManager;

public class BudgetAdapter extends BaseAdapter
{
	Context m_context;
	Budget mBudgetActivity;
	ArrayList<BudgetCategoryItem> m_data;
	
    public BudgetAdapter(Activity _context) 
    {
        m_context = _context;
        m_data = App.getBudgetCategoryItems();
        
        try{
        	mBudgetActivity = (Budget)m_context;
        }catch(ClassCastException e){
        	e.printStackTrace();
        }
    }

	@Override
	public int getCount()
	{
        return m_data.size();
    }

	@Override
	public Object getItem(int position)
	{
		return m_data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
        return position;
    }

    @Override
	public View getView(int _position, View _convertView, ViewGroup _parent)
    {
    	final int position = _position;
    	final BudgetCategoryItem item = m_data.get(_position);
    	
    	View view;
		
		if (_convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.budget_list_item, _parent, false);
		}
		else
		{
			view = _convertView;
		}
    	
    	final ImageButton category = (ImageButton)view.findViewById(R.id.budgetCategoryButton);
    	EditText budgeted = (EditText)view.findViewById(R.id.budgetedEntry);
        EditText actual = (EditText)view.findViewById(R.id.actualEntry);
        
        ThemeManager.SetBudgetButton(m_context, category, item.ListImage);
        if (item.Budgeted != -1)
        {
        	budgeted.setText(m_context.getString(R.string.money_symbol) + item.Budgeted.toString());
        }
        else
        {
        	budgeted.setText(m_context.getString(R.string.money_symbol) + m_context.getString(R.string.no_amount_entered_budget));
        }
        
        if (item.Actual != -1)
        {
        	actual.setText(m_context.getString(R.string.money_symbol) + String.format("%.2f", item.Actual));
        }
        else
        {
        	actual.setText(m_context.getString(R.string.money_symbol) + m_context.getString(R.string.no_amount_entered_budget));
        }
        
        budgeted.setKeyListener(null);
        actual.setKeyListener(null);
        
        category.setOnClickListener(new OnClickListener() 
        {
           	public void onClick(View view) 
           	{      
           		Intent next = new Intent(m_context, BudgetDetail.class);
           		next.putExtra("id", item.CategoryId);
           		next.putExtra("image", item.Image);
           		next.putExtra("name", item.Name);
           		next.putExtra("budgeted", item.Budgeted);
           		next.putExtra("actual", item.Actual);
           		next.putExtra("merchant", item.Merchant);
           		next.putExtra("recomended", item.RecommendedSpendingPercent);
           		m_context.startActivity(next);
           	}
        });
        
    	ToggleButton checkBox = (ToggleButton)view.findViewById(R.id.budgetCategoryCheckbox);
		checkBox.setChecked(item.Active);
//    	category.setAlpha(new Float(0x87));
        
    	checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				ToggleButton checkBox = (ToggleButton)view;  
				
				boolean checked = checkBox.isChecked();;
				m_data.get(position).Active=checked;
				
//				if(isChecked){
//					category.setAlpha(new Float(0xFF));
//				}else{
//					category.setAlpha(new Float(0x87));
//				}
				
				App.updateBudgetActiveStatus(item.CategoryId,checked);
				
				if(mBudgetActivity!=null){
					mBudgetActivity.refreshBudgetHealth();
				}
			}
		});
    	
    	
        ThemeManager.SetFont(m_context, budgeted);
        ThemeManager.SetFont(m_context, actual);
        
        return view;
    }
}
