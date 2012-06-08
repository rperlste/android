package edu.perlstein.calendarActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;



public class EventInfoActivity extends Activity {
	
	private int selectedYear;
	private int selectedDay;
	private int selectedMonth;
	private int eventPriority;
	
	private String eventTitle;
	private String eventDescription;
	
	private TimePicker eventTimePicker;
	
	private TextView eventTitleTextView;
	private TextView descriptionTextView;
	private TextView eventDateTextView;

	private EditText eventTitleEditText;
	private EditText descriptionEditText;
	
	private Button editTitleSaveButton;
	private Button editDescriptionSaveButton;
	private Button saveEventButton;
	private Button clearEventButton;
	
	private Spinner prioritySpinner;
	private ArrayAdapter<CharSequence> priorityAdapter;
	
	private int eventHour;
	private int eventMinute;
	
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefs_edit;
	
	private String[] monthArr;

	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        
        
        
        
        Bundle extras = getIntent().getExtras();
       	selectedYear = extras.getInt( "selectedYear" );
       	selectedDay = extras.getInt( "selectedDay" );
       	selectedMonth = extras.getInt( "selectedMonth" );
       	
       	String prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
       	
       	prefs = getSharedPreferences( prefs_title, 0 );
       	prefs_edit = prefs.edit();
       	
       	eventPriority = 0;
       	
       	// Date event is for
       	monthArr = getResources().getStringArray(R.array.months_array);
       	eventDateTextView = (TextView) findViewById(R.id.eventDateTextView);
       	eventDateTextView.setText(monthArr[selectedMonth] + " " + selectedDay + ", " + selectedYear);
       	
       	// Time for event (defaults to current)
       	eventTimePicker = (TimePicker) findViewById(R.id.eventTimePicker);
       	eventHour = eventTimePicker.getCurrentHour();
       	eventMinute = eventTimePicker.getCurrentMinute();
       	if( prefs.contains( "event_hour") ) {
       		eventTimePicker.setCurrentHour( prefs.getInt( "event_hout", 12));
       		eventTimePicker.setCurrentMinute( prefs.getInt( "event_minute", 0));
       	}
       	eventTimePicker.setOnTimeChangedListener( new TimePicker.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				eventHour = hourOfDay;
				eventMinute = minute;
			}
		});
        
       	// Event title area
        eventTitleTextView = (TextView) findViewById(R.id.titleEventTextView);
        eventTitleTextView.setText("Title:");
        
        eventTitleEditText = (EditText) findViewById(R.id.titleEventEditText);
        eventTitle = prefs.getString("event_name", "");
        eventTitleEditText.setText(eventTitle);

        editTitleSaveButton = (Button) findViewById(R.id.editTitleSaveButton);
        editTitleSaveButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eventTitle = eventTitleEditText.getText().toString();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(eventTitleEditText.getWindowToken(), 0);
				
			}
		});
        
        // Event description area
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        descriptionTextView.setText("Event description:");
        
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        eventDescription = prefs.getString( "event_description", "");
        descriptionEditText.setText(eventDescription);
        
        editDescriptionSaveButton = (Button) findViewById(R.id.editDescriptionSaveButton);
        editDescriptionSaveButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eventDescription = descriptionEditText.getText().toString();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
			}
		});
        
        // Edit the priority level
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        eventPriority = prefs.getInt( "event_priority", 0);
        
        priorityAdapter = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);
        prioritySpinner.setSelection( eventPriority );
        prioritySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected( AdapterView<?> parent, View view, int pos, long id ) {
        		eventPriority = pos;
        	}
        	public void onNothingSelected(AdapterView parent) {
        	      // Do nothing.
        	} 
		});
		
        // Save the event to Shared Preferences
        saveEventButton = (Button) findViewById(R.id.saveEventButton);
        saveEventButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				prefs_edit.putString( "event_name", eventTitle );
				prefs_edit.putString( "event_description", eventDescription );
				prefs_edit.putInt( "event_priority", eventPriority);
				prefs_edit.putInt( "event_hour", eventHour);
				prefs_edit.putInt( "event_minute", eventMinute);
				prefs_edit.commit();
				
				finish();
			}
		});		
        
        // Clear preference file and exit event page
        clearEventButton = (Button) findViewById(R.id.clearEventButton);
        clearEventButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				prefs_edit.clear();
				prefs_edit.commit();
				finish();
			}
		});
	}

}
