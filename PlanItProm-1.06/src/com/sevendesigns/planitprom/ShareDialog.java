package com.sevendesigns.planitprom;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.flurry.android.FlurryAgent;
import com.sevendesigns.planitprom.data.ImageInfo;
import com.sevendesigns.planitprom.utilities.ThemeManager;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ShareDialog extends Activity
{
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions", "photo_upload", "publish_stream");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	UiLifecycleHelper m_uiHelper;
	
	Boolean m_pictureIncluded = false;
	Integer m_pictureId;
	
	EditText m_message;
	
	Bitmap m_image;
	ImageView m_preview;
	
	ProgressDialog m_dialog;
	
	ImageButton m_shareButton;
	
	private List<String> m_permissions = new ArrayList<String>();
	
	private Session.StatusCallback m_callback = new Session.StatusCallback() 
	{
	    @Override
	    public void call(Session session, SessionState state, Exception exception) 
	    {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_layout);
		
		m_uiHelper = new UiLifecycleHelper(this, m_callback);
   		m_uiHelper.onCreate(savedInstanceState);
   		
   		m_message = (EditText)findViewById(R.id.shareMessage);
   		m_preview = (ImageView)findViewById(R.id.shareImage);
   		
   		m_shareButton = (ImageButton)findViewById(R.id.share);
   		
   		m_pictureId = getIntent().getIntExtra("imageid", -1);
   		
   		ThemeManager.SetBackgroundImage(this, findViewById(R.id.shareParent), false);
   		ThemeManager.SetBarBackground(this, findViewById(R.id.shareBottomBar));
   		ThemeManager.SetFont(this, m_message);
   		
   		if (m_pictureId != -1)
   		{
   			m_pictureIncluded = true;
			
			ImageInfo info = App.getImageInfoByImageId(m_pictureId);
	        String name = getFilesDir() + "/" + info.FileName;

	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 4;
	        
	        m_image = BitmapFactory.decodeFile(name, options);
	        
	        Matrix matrix = new Matrix();
	        
	        matrix.postRotate(90);
	        m_image = Bitmap.createBitmap(m_image , 0, 0, m_image.getWidth(), m_image.getHeight(), matrix, true);
	        
	        m_preview.setImageBitmap(m_image);
   		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	    m_uiHelper.onSaveInstanceState(outState);
	}
	
	public void pickImage(View _view)
	{
		Intent next = new Intent(this, PictureGallery.class);
		next.putExtra("returntocaller", true);
		startActivityForResult(next, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				m_pictureIncluded = true;
				m_pictureId = data.getExtras().getInt("imageid");
				
				ImageInfo info = App.getImageInfoByImageId(m_pictureId);
		        String name = getFilesDir() + "/" + info.FileName;

		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inSampleSize = 4;
		        
		        m_image = BitmapFactory.decodeFile(name, options);
		        
		        Matrix matrix = new Matrix();
		        
		        matrix.postRotate(90);
		        m_image = Bitmap.createBitmap(m_image , 0, 0, m_image.getWidth(), m_image.getHeight(), matrix, true);
		        
		        m_preview.setImageBitmap(m_image);
			}
		}
		else
		{
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
	}
	
	public void share(View _view)
	{
		if ((m_dialog == null) || (!m_dialog.isShowing()))
		{
			if (_view != null)
			{
				m_dialog = ProgressDialog.show(this, null, getString(R.string.facebook_post_message));
			}
		}
		
		String message = m_message.getText().toString();
		
		if (message.length() == 0)
		{
			message = getString(R.string.share_default_message);
		}
		
		final Session localSession = Session.getActiveSession();
	
	    if ((localSession != null) && (localSession.isOpened()))
	    {
	        // Check for publish permissions    
	        List<String> permissions = localSession.getPermissions();
	        
	        if (!isSubsetOf(PERMISSIONS, permissions)) 
	        {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
	            localSession.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }
	
	        Request.Callback callback= new Request.Callback() 
	        {
	            public void onCompleted(Response response) 
	            {
	            	m_shareButton.setEnabled(true);
	            	m_dialog.dismiss();
	            	if (response.getGraphObject() != null)
	            	{
	            		finish();
	            	}
	            	else
	            	{
	            		AlertDialog.Builder builder = new AlertDialog.Builder(ShareDialog.this);
	            	    builder.setMessage(getString(R.string.facebook_post_error));
	            		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
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
	            }
	        };
	
	        Bundle postParams = new Bundle();
		    
	        Request request = null;
	        if (m_pictureIncluded)
	        {
	        	request = Request.newUploadPhotoRequest(Session.getActiveSession(), m_image, callback);
	        	postParams = request.getParameters();
	        	postParams.putString("name", message);
	        	 
	        	request.setParameters(postParams);
	        }
	        else
	        {
	        	request = Request.newStatusUpdateRequest(localSession, message, callback);
	        }
	
	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }
	    else
	    {
	    	m_permissions.clear();
	    	m_permissions.add("read_stream");
	    	//m_permissions.add("publish_actions");
			//m_permissions.add("photo_upload");
			//m_permissions.add("publish_stream");

			try
			{
				PackageInfo info = getPackageManager().getPackageInfo("com.sevendesigns.planitprom",  PackageManager.GET_SIGNATURES);
	
				for (Signature signature : info.signatures)
				{
					MessageDigest md = MessageDigest.getInstance("SHA");
				    md.update(signature.toByteArray());
				    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			}
			catch (Exception e)
			{
				
			}
				
			localSession.addCallback(new Session.StatusCallback() 
			{
            	@Override
				public void call(Session session, SessionState state, Exception exception) 
				{
            		if (session.isOpened()) 
					{
            			share(null);
					}
            		else if (state == SessionState.CLOSED_LOGIN_FAILED)
            		{
            			m_dialog.dismiss();
            			AlertDialog.Builder builder = new AlertDialog.Builder(ShareDialog.this);
	            	    builder.setMessage(getString(R.string.facebook_login_error));
	            		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
	            		{
	            			@Override
	            			public void onClick(DialogInterface dialog, int which) 
	            			{
	            				dialog.cancel();
	            			}
	            		});
	            	    builder.create().show();
	            	    
	            	    localSession.closeAndClearTokenInformation();
	            	    localSession.removeCallback(this);
	            	    
	            	    Session newSession = new Session(ShareDialog.this);
	            	    Session.setActiveSession(newSession);
	            	    
	            	    return;	
            		}
				}
			});
	    	
	    	Session.OpenRequest openRequest = new Session.OpenRequest(this);
	    	
	    	openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
            openRequest.setPermissions(m_permissions);
            openRequest.setLoginBehavior(null);
	    	
	    	localSession.openForRead(openRequest);
	    }
	}
	

	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) 
	{
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) 
	{
	    if (state.isOpened()) 
	    {
	    
	    }
	}
    
    public static String getMetadataApplicationId(Context context) 
    {
        try 
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo( context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) 
            {
                return ai.metaData.getString(Session.APPLICATION_ID_PROPERTY);
            }
        } 
        catch (PackageManager.NameNotFoundException e) 
        {
            // if we can't find it in the manifest, just return null
        }

        return null;
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
