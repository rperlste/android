package edu.perlstein.arbor;

import java.util.ArrayList;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Perlstein_Final extends Activity {
    /** Called when the activity is first created. */
	
	protected Dialog mSplashDialog;
	
	private String[] treeArr;
	
	private Button searchButton;
	private Button browseButton;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        showSplashScreen();
        setContentView(R.layout.main);
        
        // Allow for browsing all trees
        browseButton = (Button) findViewById(R.id.browse_button);
        browseButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				treeArr = getResources().getStringArray(R.array.tree_names);
				

				Intent intent = new Intent( v.getContext(), ResultsView.class);
				intent.putExtra( "trees", treeArr);
				v.getContext().startActivity(intent);
			}
		});
        
        // Search by parameters
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent( v.getContext(), Search.class );
				v.getContext().startActivity(intent);
			
			}
		});
    }
    
    
    protected void showSplashScreen () {
    	mSplashDialog = new Dialog( this, R.style.SplashScreen );
    	mSplashDialog.setContentView( R.layout.splash );
    	mSplashDialog.setCancelable( false );
    	mSplashDialog.show();
    	
    	final Handler handler = new Handler();
    	handler.postDelayed( new Runnable() {
			
			@Override
			public void run() {
				removeSplashScreen();
				
			}
		}, 3000);
    }
    
    protected void removeSplashScreen() {
    	if( mSplashDialog != null ) {
    		mSplashDialog.dismiss();
    		mSplashDialog = null;
    	}
    }
}