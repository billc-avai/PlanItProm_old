package com.sevendesigns.planitprom;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.textwatcher.BudgetedFieldWatcher;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BudgetDetail extends Activity
{

	TextView m_itemName;
	
	EditText m_recommendedSpending;
	EditText m_budgeted;
	EditText m_totalBudgetRemaining;
	EditText m_cost;
	EditText m_merchant;
	
	int m_id;
	
	String m_imageName;
	
	ImageButton m_calc;
	ImageButton m_tips;
	ImageButton m_pic;
	ImageButton m_gallery;
	ImageButton m_facebook;
	
	private static BudgetDetail Instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		float recommended;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget_detail);
		
		m_id = getIntent().getIntExtra("id", 0);
		
		m_itemName = (TextView)findViewById(R.id.budgetDetailHeaderText);
		m_itemName.setText(getIntent().getStringExtra("name"));
			
		m_imageName = getIntent().getStringExtra("image");
		
		m_recommendedSpending = (EditText)findViewById(R.id.recommendedSpendingEntry); 
		m_budgeted = (EditText)findViewById(R.id.budgetedEntry);
		m_totalBudgetRemaining = (EditText)findViewById(R.id.totalRemainingEntry);
		m_cost = (EditText)findViewById(R.id.costEntry);
		m_merchant = (EditText)findViewById(R.id.merchantEntry);
   		
		m_calc = (ImageButton)findViewById(R.id.calcButton);
		m_tips = (ImageButton)findViewById(R.id.tipsButton);
		m_pic = (ImageButton)findViewById(R.id.picButton);
		m_gallery = (ImageButton)findViewById(R.id.galleryButton);
		m_facebook = (ImageButton)findViewById(R.id.facebookButton);
		
		Integer budgeted = getIntent().getIntExtra("budgeted", -1);
		Double actual = getIntent().getDoubleExtra("actual", -1.0);

		if (budgeted != -1)
		{
			m_budgeted.setText(budgeted.toString());
			
			Integer remain = App.Budget - budgeted;
			
			m_totalBudgetRemaining.setText(remain.toString());
		}
		
		if (actual != -1)
		{
			m_cost.setText(String.format("%.2f", actual));
		}
   		
		recommended = getIntent().getFloatExtra("recomended", 0.0f);
		Integer recTemp = (int) (App.Budget * recommended);
		m_recommendedSpending.setText(recTemp.toString());
		
   		m_recommendedSpending.setKeyListener(null);
   		m_totalBudgetRemaining.setKeyListener(null);
   		
   		if (!App.BudgetDetailsInstructionsSeen)
   		{
   			showInstructions(null);
   			App.setBudgetDetailsInstructionsSeen();
   		}
   			
   		m_budgeted.addTextChangedListener(new BudgetedFieldWatcher());
   		
   		String merchant = getIntent().getStringExtra("merchant");
   		m_merchant.setText(merchant);
   		
   		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.budgetDetailParent), false);
   		ThemeManager.SetHeader(this, findViewById(R.id.budgetDetailHeader), false);
   		
   		ThemeManager.SetUpBudgetDetailButtonBar(this, findViewById(R.id.budgetDetailsButtons));
   		
   		ThemeManager.SetFont(this, m_recommendedSpending);
   		ThemeManager.SetFont(this, m_budgeted);
   		ThemeManager.SetFont(this, m_totalBudgetRemaining);
   		ThemeManager.SetFont(this, m_cost);
   		ThemeManager.SetFont(this, m_merchant);
   		
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetDetailHeaderText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.recommendedSpendingText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetedTitle));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.totalRemainingText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.costText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.merchantText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetInstructionsText));
   		
   		ThemeManager.SetLogo(this, (ImageView)findViewById(R.id.logo));
   		
   		setImage();
   		
   		Instance = this;

	}
	
	@Override
	public void onBackPressed()
	{
		updateCategoryInfo();
		super.onBackPressed();
	}
	
	void updateCategoryInfo()
	{
		Integer b = -1;
		Float c = -1.0f;
	
		String budgetedString = m_budgeted.getText().toString();
		if (budgetedString.length() != 0)
		{
			b = Integer.parseInt(budgetedString);
		}
		
		String costString = m_cost.getText().toString();
		if (costString.length() != 0)
		{
			c = Float.parseFloat(costString);
		}
		
		App.updateBudgetCategory(m_id, b, c, m_merchant.getText().toString());
	}
	
	public void doDone(View _view)
	{
		updateCategoryInfo();
		finish();
	}
	
	public void showInstructions(View _view)
	{
		Utils.ShowInstructions(this, getString(R.string.budget_detail_instructions));
	}
	
	public static void updateTotalBudgetRemaining()
	{
		EditText budgeted = (EditText)Instance.findViewById(R.id.budgetedEntry);
		String b = budgeted.getText().toString();
		
		if (b.length() == 0)
		{
			return;
		}
		
		EditText totalBudgetRemaining = (EditText)Instance.findViewById(R.id.totalRemainingEntry);
		
		Integer bInt = Integer.parseInt(b);
		Integer remaining = App.Budget - bInt;
		
		totalBudgetRemaining.setText(remaining.toString());
	}
	
	public void doCalc(View _view)
	{
		updateCategoryInfo();
		Intent next = new Intent(this, Calculator.class);
		startActivity(next);
	}
	
	public void doTips(View _view)
	{
		updateCategoryInfo();
		Intent next = new Intent(this, Tips.class);
		next.putExtra("categoryId", m_id);
		startActivity(next);
	}
	
	public void doPic(View _view)
	{
		updateCategoryInfo();
		Intent next = new Intent(this, CameraScreen.class);
		next.putExtra("categoryid", m_id);
		startActivity(next);
	}
	
	public void doGallery(View _view)
	{
		updateCategoryInfo();
		Intent next = new Intent(this, PictureGallery.class);
		next.putExtra("categoryId", m_id);
		startActivity(next);
	}
	
	private void setImage()
	{
		updateCategoryInfo();
		ImageView image = (ImageView)findViewById(R.id.categoryImage);
		image.setImageResource(getResources().getIdentifier(m_imageName, "drawable", getPackageName()));
	}
	
	public void shareToFacebook(View _view)
	{
		updateCategoryInfo();
		Intent next = new Intent(this, ShareDialog.class);
		startActivity(next);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() 
	{
	    super.onResume();
	}

	@Override
	public void onPause() 
	{
	    super.onPause();
	}

	@Override
	public void onDestroy() 
	{
	    super.onDestroy();
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
