package com.sevendesigns.planitprom;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError.Category;
import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.BudgetCategoryItem;
import com.sevendesigns.planitprom.listadapters.PictureGalleryAdapter;
import com.sevendesigns.planitprom.utilities.ThemeManager;

public class PictureGallery extends Activity
{
	static final int SELECT_PICTURE = 1;
	ListView m_listView;
	PictureGalleryAdapter m_adapter;
	DisplayMetrics m_metrics;
	int mSelectedCategoryId;
	
	boolean m_returnImageIdToCaller = false;
	
	String[] mCategories;
	HashMap<String,Integer> mCategoryIdMap;
	String mSelectedImagePath;
	Bitmap mExistingPhoto; 
	
	boolean mUseExistingPhoto=false;
			
			
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_gallery);
		
		m_returnImageIdToCaller = getIntent().getBooleanExtra("returntocaller", false);
		
		ThemeManager.SetBackgroundImage(this, (View)findViewById(R.id.PictureGalleryParent), false);
		
		m_metrics = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(m_metrics);
		
		m_listView = (ListView)findViewById(R.id.pictureGalleryListView);
		m_adapter = new PictureGalleryAdapter(this, m_metrics, m_returnImageIdToCaller);
		
		m_listView.setAdapter(m_adapter);
		
		ThemeManager.SetHeader(this, findViewById(R.id.pictureGalleryHeader), false);
		ThemeManager.SetFont(this, (TextView)findViewById(R.id.pictureGalleryHeaderText));
		
		mCategoryIdMap = new HashMap<String, Integer>();
		mCategories = new String[m_adapter.getCount()];
		
		for(int i=0;i<m_adapter.getCount();i++){
			BudgetCategoryItem item = m_adapter.getData().get(i); 
			mCategoryIdMap.put(item.Name, item.CategoryId);
			mCategories[i]=item.Name;
		}
		
		Button btnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
		btnTakePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mUseExistingPhoto=false; 
				promptForGallery();		
				 
			}
		});
		
		Button btnAddExistingPhoto = (Button)findViewById(R.id.btnAddExistingPhoto);
		btnAddExistingPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mUseExistingPhoto=true;
				Intent intent = new Intent();
		        intent.setType("image/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent,
		                "Select Picture"), SELECT_PICTURE);
				
			}
		});
		
	}
	
	private void promptForGallery(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(PictureGallery.this);
	    dialog.setTitle("Select the gallery where you wish to add a photo.");
	    
	    dialog.setItems(mCategories, new DialogInterface.OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	            
	            Intent intent = new Intent();
	            
	            if(mUseExistingPhoto){
	            	intent.setClass(getApplicationContext(), PictureGalleryMultipleItem.class);
	            	Integer imageId = App.addImageToGallery(mSelectedCategoryId, -1, mSelectedImagePath);
	    			intent.putExtra("imageId", imageId);
	    			intent.putExtra("fileName", mSelectedImagePath);
	    			intent.putExtra("categoryId", mCategoryIdMap.get(mCategories[which]));
	            }else{
	            	intent.setClass(getApplicationContext(),CameraScreen.class);
	            	intent.putExtra("categoryid", mCategoryIdMap.get(mCategories[which]));
	            }
	            	            
	            startActivity(intent);
	        }

	    });

	    dialog.show();
		
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

        mExistingPhoto  = null;
		
        if (resultCode == RESULT_OK) {
        	Uri mImageCaptureUri = data.getData();
            mSelectedImagePath = getRealPathFromURI(mImageCaptureUri); //from Gallery
 
            if (mSelectedImagePath == null)
            	mSelectedImagePath = mImageCaptureUri.getPath(); //from File Manager
 
//            if (mSelectedImagePath != null)
//            	mExistingPhoto = BitmapFactory.decodeFile(mSelectedImagePath);
        }
        
        if(mSelectedImagePath!=null){
        	promptForGallery();        	
        }else{
        	Toast.makeText(this, "The selected photo could not be retrieved",Toast.LENGTH_SHORT).show();
        }
    }

	 public String getRealPathFromURI(Uri contentUri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri, proj, null, null,null);
 
        if (cursor == null) return null;
 
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
 
        cursor.moveToFirst();
 
        return cursor.getString(column_index);
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
