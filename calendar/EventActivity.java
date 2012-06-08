package edu.perlstein.calendarActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends Activity
{
	
	private int selectedYear;
	private int selectedDay;
	private int selectedMonth;
	
	private Button addEventButton;
	
	TextView eventDateTextView;
	
	private SharedPreferences prefs;
	private String prefs_title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        
        
        // Load current date values
        Bundle extras = getIntent().getExtras();
       	selectedYear = extras.getInt( "selectedYear" );
       	selectedDay = extras.getInt( "selectedDay" );
       	selectedMonth = extras.getInt( "selectedMonth" );
       	
       	// Load preferences for this date
       	prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
       	prefs = getSharedPreferences( prefs_title, Context.MODE_PRIVATE );
       	
       	// Set current date information
       	eventDateTextView = (TextView) findViewById(R.id.eventDayTextView);
       	String[] monthArr = getResources().getStringArray(R.array.months_array);
       	eventDateTextView.setText(monthArr[selectedMonth] + " " + selectedDay + ", " + selectedYear);
       	
        // Add an event
        addEventButton = (Button) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), EventInfoActivity.class);
				myIntent.putExtra( "selectedDay", selectedDay );
            	myIntent.putExtra( "selectedMonth", selectedMonth );
            	myIntent.putExtra( "selectedYear", selectedYear );
				v.getContext().startActivity(myIntent);
				
			}
		});
		
        
        refreshEventList();
        
    }
	
	@Override
	public void onResume() {
		super.onResume();
        refreshEventList();	
        
	}
	
	private void clearEventList()
	{
		// Clear title
		TextView eventItemTextView = (TextView) findViewById( R.id.eventItemTextView );
		eventItemTextView.setText( "" );
		// Clear description
		TextView eventDescriptionTextView = (TextView) findViewById( R.id.eventDescriptionTextView );
		eventDescriptionTextView.setText( "");
		// Clear time
		TextView eventTimeTextView = (TextView) findViewById(R.id.eventTimeTextView);
		eventTimeTextView.setText( "");
		// Clear priority
		ImageView eventItemImageView = (ImageView) findViewById(R.id.eventItemImageView);
		eventItemImageView.setImageDrawable( null );
	}
	
	private void refreshEventList()
	{
		if( prefs.contains( "event_name" ) || prefs.contains("event_description") ) {    	
        	eventGUIInflater();	
        }
        else
        	clearEventList();	
	}
	
	
	private void eventGUIInflater()
	{
		
    	addEventButton.setText("Edit or Delete Event");
		// Load current even title
		TextView eventItemTextView = (TextView) findViewById( R.id.eventItemTextView );
		eventItemTextView.setText( "Title: " + prefs.getString( "event_name", "") );
		
		// Load current event description
		TextView eventDescriptionTextView = (TextView) findViewById( R.id.eventDescriptionTextView );
		eventDescriptionTextView.setText( "Description: " + prefs.getString( "event_description", ""));
		
		// Load current event time
		TextView eventTimeTextView = (TextView) findViewById(R.id.eventTimeTextView);
		eventTimeTextView.setText( "Time:  " + prefs.getInt( "event_hour", 0) + ":" + prefs.getInt( "event_minute", 0) + "                   ");
		
		// Set priority image
		int eventItemPriority = prefs.getInt( "event_priority", 0);
		ImageView eventItemImageView = (ImageView) findViewById(R.id.eventItemImageView);
		if( eventItemPriority == 1 )
			eventItemImageView.setImageResource( R.drawable.low_prior_item );
		else if( eventItemPriority == 2 )
			eventItemImageView.setImageResource( R.drawable.med_prior_img );
		else if( eventItemPriority == 3 )
			eventItemImageView.setImageResource( R.drawable.high_prior_img );
		else
			eventItemImageView.setImageDrawable( null );
		

	}

}
