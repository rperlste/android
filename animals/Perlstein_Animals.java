package edu.perlstein.animals;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Perlstein_Animals extends Activity {
	
	private GridView animalListGridView;
	private MediaPlayer farmMP;
	protected Dialog mSplashDialog;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        showSplashScreen();
        
        setContentView(R.layout.main);
        
        // Load grid of letters
        animalListGridView = (GridView) findViewById( R.id.animal_list_grid_view );
        animalListGridView.setAdapter( new AnimalGridAdapter(this) );
    }
    
    
    
    public class AnimalGridAdapter extends BaseAdapter {
    	private Context context;
    	private char currentLetter;

    	
    	public AnimalGridAdapter( Context context) {
    		this.context = context;
    	}
    	
    	public int getCount() {
    		return 26;
    	}
    	
    	public Object getItem(int position) {
    		return null;
    	}
    	
    	public long getItemId(int position) {
    		return position;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		// Output error, must declare A on first position, then increment
    		if( position == 0 )
    			currentLetter = 'A';
    		
    		// Create a button with the current animals letter
    		Button button = new Button(context);
    		button.setText( Character.toString( currentLetter ) );
    		button.setTextSize( 20 );
    		
    		// Set listener to load list of animals of certain type
    		button.setOnClickListener(new animalSelectListener(position));
    		
    		// Increment the current letter once the button is created
    		currentLetter ++;
    		
    		return button;
    	}
    }
    
    // Load list of animals of certain letter
    class animalSelectListener implements OnClickListener
    {
    	private final int position;
    	
    	public animalSelectListener( int position )
    	{
    		this.position = position;
    	}
    	
    	public void onClick( View v )
    	{
    		// Load the correct animal letter type
    		char temp_ch = 'A';
    		temp_ch += position;
    		
    		Intent myIntent = new Intent( animalListGridView.getContext(), AnimalList.class );
    		myIntent.putExtra( "animal_letter", temp_ch );
    		v.getContext().startActivity( myIntent );
    		
    	}
    }
    
    protected void showSplashScreen () {
    	mSplashDialog = new Dialog( this, R.style.SplashScreen );
    	mSplashDialog.setContentView( R.layout.splash );
    	mSplashDialog.setCancelable( false );
    	mSplashDialog.show();
    	farmMP = MediaPlayer.create( getApplicationContext(), R.raw.moo );
    	farmMP.start();
    	
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