package com.sevendesigns.planitprom;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.textwatcher.BudgetedFieldWatcher;
import com.sevendesigns.planitprom.utilities.ThemeManager;
import com.sevendesigns.planitprom.utilities.Utils;
import com.sevendesigns.planitprom.widgets.BudgetHealthWidget;

public class SubItemListActivity extends ListActivity
{

	private final String TAG = this.getClass().getSimpleName();
	TextView m_itemName;
	EditText m_budgeted;
	EditText m_cost;
	EditText m_merchant;
	
	int mParentId;
	String mCategoryName;
	
	String m_imageName;
	
	BudgetHealthWidget mBudgetHealthWidget;
	
	List<BudgetCategoryItem> mSubItems;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		m_imageName = getIntent().getStringExtra("image");
		mParentId = getIntent().getIntExtra("parentId", 0);
		mCategoryName = getIntent().getStringExtra("categoryName");
		
		setContentView(R.layout.budget_subitem_list);
		
		mSubItems = App.getSubItems(mParentId);
		
		mBudgetHealthWidget = (BudgetHealthWidget)findViewById(R.id.budgetHealthWidget);
   		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.budgetDetailParent), false);
   		ThemeManager.SetHeader(this, findViewById(R.id.budgetDetailHeader), false);
   		ThemeManager.SetUpBudgetDetailButtonBar(this, findViewById(R.id.budgetDetailsButtons));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.budgetDetailHeaderText));
   		ThemeManager.SetFont(this, (TextView)findViewById(R.id.categoryName));
   		
   		setImage();
   		TextView categoryNameView = (TextView)findViewById(R.id.categoryName);
		categoryNameView.setText(mCategoryName+"\nSub-Item(s):");   		
   		
   		setListAdapter(new SubItemListAdapter(this, mParentId));
   		
	}

	public void doDone(View _view)
	{
		finish();
	}
	

	public void doCalc(View _view)
	{
		Intent next = new Intent(this, Calculator.class);
		startActivity(next);
	}
	
	public void doTips(View _view)
	{
		Intent next = new Intent(this, Tips.class);
		next.putExtra("categoryId", mParentId);
		startActivity(next);
	}
	
	public void doPic(View _view)
	{
		Intent next = new Intent(this, CameraScreen.class);
		next.putExtra("categoryid", mParentId);
		startActivity(next);
	}
	
	public void doGallery(View _view)
	{
		Intent next = new Intent(this, PictureGallery.class);
		next.putExtra("categoryId", mParentId);
		startActivity(next);
	}
	
	private void setImage()
	{
		ImageView image = (ImageView)findViewById(R.id.categoryImage);
		image.setImageResource(getResources().getIdentifier(m_imageName, "drawable", getPackageName()));
	}
	
	public void shareToFacebook(View _view)
	{
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
	
	private class SubItemListAdapter extends ArrayAdapter<BudgetCategoryItem>{
		Context mContext;

		public SubItemListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			
			mContext = context;
		}
		
		@Override
		public int getCount(){
			return mSubItems.size();
		}
		
		@Override
		public BudgetCategoryItem getItem(int position){
			return mSubItems.get(position);
		}
		
		 @Override
		    public View getView(int position, View v, ViewGroup parent) {
		        SubItemViewHolder viewHolder;
		 
		        if (v == null) {
		            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
		                    Context.LAYOUT_INFLATER_SERVICE);
		            v = inflater.inflate(R.layout.subitem_list_item, parent, false);
		 
		            viewHolder = new SubItemViewHolder();
		            viewHolder.name = (TextView) v.findViewById(R.id.itemNameEntry);
		            viewHolder.budgeted = (TextView) v.findViewById(R.id.budgetedEntry);
		            viewHolder.cost= (TextView) v.findViewById(R.id.costEntry);
		            viewHolder.merchant = (TextView) v.findViewById(R.id.merchantEntry);
		            
		       		ThemeManager.SetFont(mContext, viewHolder.name);
		       		ThemeManager.SetFont(mContext,  viewHolder.budgeted);
		       		ThemeManager.SetFont(mContext, viewHolder.cost);
		       		ThemeManager.SetFont(mContext, viewHolder.merchant);
		       		
		       		ThemeManager.SetFont(mContext, (TextView)v.findViewById(R.id.itemNameTitle));
		       		ThemeManager.SetFont(mContext, (TextView)v.findViewById(R.id.budgetedTitle));
		       		ThemeManager.SetFont(mContext, (TextView)v.findViewById(R.id.costText));
		       		ThemeManager.SetFont(mContext, (TextView)v.findViewById(R.id.merchantText));
		       		

		 
		            v.setTag(viewHolder);
		        } else {
		            viewHolder = (SubItemViewHolder) v.getTag();
		        }
		 
		        BudgetCategoryItem subItem = mSubItems.get(position);
		        if (subItem != null) {
		            viewHolder.name.setText(subItem.Name);
		            
		            if(subItem.Budgeted>0) viewHolder.budgeted.setText(subItem.Budgeted.toString());
		            if(subItem.Actual>0) viewHolder.cost.setText(subItem.Actual.toString());
		            viewHolder.merchant.setText(subItem.Merchant);
		        }
		        return v;
		    }
		 
		    class SubItemViewHolder {
		        TextView name;
		        TextView budgeted;
		        TextView cost;
		        TextView merchant;
		    }
		
	}
}
