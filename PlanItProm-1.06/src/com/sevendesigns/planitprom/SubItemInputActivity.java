package com.sevendesigns.planitprom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.textwatcher.BudgetedFieldWatcher;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;
import com.sevendesigns.planitprom.widgets.BudgetHealthWidget;

public class SubItemInputActivity extends Activity
{

	TextView m_itemName;
	
	EditText m_itemNameEntry;
	TextView m_categoryName;
	EditText m_budgeted;
	EditText m_totalBudgetRemaining;
	EditText m_cost;
	EditText m_merchant;
	
	int m_parentId;
	String mCategoryName;
	
	String m_imageName;

	BudgetHealthWidget mBudgetHealthWidget;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget_detail);
		
		m_parentId=getIntent().getIntExtra("parentId",0);
		m_imageName = getIntent().getStringExtra("image");
		mCategoryName = getIntent().getStringExtra("categoryName");
		
		View nameInputSection = findViewById(R.id.nameInputSection);
		nameInputSection.setVisibility(View.VISIBLE);
		
		m_itemName = (TextView)findViewById(R.id.budgetDetailHeaderText);
		m_itemName.setText(R.string.title_activity_add_subitem);
		m_itemNameEntry = (EditText)findViewById(R.id.itemNameEntry);
		m_categoryName = (TextView)findViewById(R.id.recommendedSpending); 
		m_budgeted = (EditText)findViewById(R.id.budgetedEntry);
		m_totalBudgetRemaining = (EditText)findViewById(R.id.totalRemainingEntry);
		m_cost = (EditText)findViewById(R.id.costEntry);
		m_merchant = (EditText)findViewById(R.id.merchantEntry);
   		
		mBudgetHealthWidget = (BudgetHealthWidget)findViewById(R.id.budgetHealthWidget);
		
		m_totalBudgetRemaining.setText(Integer.toString(App.Budget-App.BudgetedToDate));
   		m_totalBudgetRemaining.setKeyListener(null);
   		
   		if (!App.BudgetDetailsInstructionsSeen)
   		{
   			showInstructions(null);
   			App.setBudgetDetailsInstructionsSeen();
   		}
   			
   		m_budgeted.addTextChangedListener(new BudgetedFieldWatcher());
   		
   		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.budgetDetailParent), false);
   		ThemeManager.SetHeader(this, findViewById(R.id.budgetDetailHeader), false);
   		
   		ThemeManager.SetUpBudgetDetailButtonBar(this, findViewById(R.id.budgetDetailsButtons));
   		
   		ThemeManager.SetFont(this, m_categoryName);
   		ThemeManager.SetFont(this, m_itemNameEntry);
   		ThemeManager.SetFont(this, m_budgeted);
   		ThemeManager.SetFont(this, m_totalBudgetRemaining);
   		ThemeManager.SetFont(this, m_cost);
   		ThemeManager.SetFont(this, m_merchant);
   		
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.itemNameTitle));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetDetailHeaderText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.recommendedSpending));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetedTitle));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.totalRemainingText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.costText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.merchantText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetInstructionsText));
   		
   		setImage();
		m_categoryName.setText(mCategoryName+"\nSub-Item:");
	}
	
	@Override
	public void onBackPressed(){
		promptToSaveAndFinish(null);
	}
	
	private void promptToSaveAndFinish(Intent intent){
		if(isFormDirty()){
			AlertDialog.Builder dialog = getSaveDialog(intent);
			dialog.show();
		}else if(intent!=null){
			startActivity(intent);
		}else{
			finish();
		}
	}
	
	
	private AlertDialog.Builder getSaveDialog(final Intent intent){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Save Sub-Item?");
		dialog.setMessage("You are about to navigate away from this screen. Would you like to save this Sub-Item first?");
		
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(validateInput()){
					saveSubItem();
					finish();
					
					if(intent!=null){
						startActivity(intent);
					}
				}else{
					showIncompleteInputDialog();
				}
			}
		});
		
		
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(intent!=null){
					startActivity(intent);
				}else{
					finish();
				}
			}
		});
		
		dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		return dialog;
		
	}
	
	private boolean isFormDirty(){
		if(m_itemNameEntry.getText().length()>0 ||  m_budgeted.getText().length()>0 || 
				m_cost.getText().length()>0 || m_merchant.getText().length()>0){
			return true;
		}
		
		return false;
	}
	
	private boolean validateInput(){
		
		if(m_itemNameEntry.getText().length()==0 || m_budgeted.getText().length()==0 || 
				m_cost.getText().length()==0 || m_merchant.getText().length()==0){

			return false;
		}
		
		return true;
	}
	
	
	private void showIncompleteInputDialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Incomplete Entry");
		dialog.setMessage("All fields are required.");
		dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		dialog.show();
	}
	
	private void saveSubItem()
	{
		BudgetCategoryItem newSubItem = new BudgetCategoryItem();

		String name = m_itemNameEntry.getText().toString();
		newSubItem.Name=name;
	
		String budgetedString = m_budgeted.getText().toString();
		newSubItem.Budgeted = Integer.parseInt(budgetedString);
		
		String costString = m_cost.getText().toString();
		newSubItem.Actual = Double.parseDouble(costString);

		newSubItem.Merchant = m_merchant.getText().toString();

		App.addCategorySubItem(m_parentId, newSubItem);
		
		Toast.makeText(this, "Sub-item saved", Toast.LENGTH_SHORT).show();
	}
	
	public void doDone(View _view){
		
		if(!isFormDirty()){
			finish();
		}else{
			if(validateInput()){
				saveSubItem();
				finish();
			}else{
				showIncompleteInputDialog();
			}
		}
	}
	
	public void showInstructions(View _view)
	{
		Utils.ShowInstructions(this, getString(R.string.budget_detail_instructions));
	}
	
	public void updateTotalBudgetRemaining()
	{
		String b = m_budgeted.getText().toString();
		
		if (b.length() == 0)
		{
			return;
		}
		
		Integer bInt = Integer.parseInt(b);
		Integer remaining = App.Budget -App.BudgetedToDate - bInt;
		
		m_totalBudgetRemaining.setText(remaining.toString());
	}
	
	public void doCalc(View _view)
	{
		Intent next = new Intent(this, Calculator.class);
		next.putExtra("cost", m_cost.getText().toString());
		promptToSaveAndFinish(next);
	}
	
	public void doTips(View _view)
	{
		Intent next = new Intent(this, Tips.class);
		next.putExtra("categoryId", m_parentId);
		promptToSaveAndFinish(next);
	}
	
	public void doPic(View _view)
	{
		Intent next = new Intent(this, CameraScreen.class);
		next.putExtra("categoryid", m_parentId);
		
	}
	
	public void doGallery(View _view)
	{
		Intent next = new Intent(this, PictureGallery.class);
		next.putExtra("categoryId", m_parentId);
		promptToSaveAndFinish(next);
	}
	
	private void setImage()
	{
		ImageView image = (ImageView)findViewById(R.id.categoryImage);
		image.setImageResource(getResources().getIdentifier(m_imageName, "drawable", getPackageName()));
	}
	
	public void shareToFacebook(View _view)
	{
		Intent next = new Intent(this, ShareDialog.class);
		promptToSaveAndFinish(next);
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
		refreshBudgetHealth();
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
	
	public void refreshBudgetHealth(){
		mBudgetHealthWidget.refreshData();
		mBudgetHealthWidget.invalidate();
	}
}
