package edu.perlstein.calendarActivity;

import java.util.Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarActivity extends Activity 
{
	
	// values for current date
	private int currentMonth;
	private int currentDay;
	private int currentYear;
	
	// values for selected date, used for event manipulation
	protected int selectedMonth;
	protected int selectedDay;
	protected int selectedYear;
	private int selectedMonthLength;
	private int previousMonthLength;
	private int selectedStartDay;

	
	private Button nextMonthButton;
	private Button nextYearButton;
	private Button backMonthButton;
	private Button backYearButton;
	 
	private CalendarAdapter calendarAdapter;
	
	private GridView calendarGrid;
	private GridView calendarTitleGrid;
	
	static final int DATE_DIALOG_ID = 0;
	
	private String[] monthNameArr;
	
	private TextView MonthYearTextView;
	
	private Calendar currentDate;
	private Calendar selectedDate;
	
	// Preference editing
	private String prefs_title;
	private SharedPreferences prefs;
	
	// Splash dialog
	protected Dialog mSplashDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        // Show splash
        showSplashScreen();
        setContentView(R.layout.main);        
     
        // Load correct date information
        setCurrentDate();
        resetSelectedDate();
    	setSelectedStartDay();
    	setSelectedStartDay();
    	setSelectedMonthLength();
    
    	// Load an array with month names
        monthNameArr = getResources().getStringArray( R.array.months_array);
        
        // Set the title month to the current value
        MonthYearTextView = (TextView) findViewById(R.id.monthYearTextView);
        MonthYearTextView.setText( monthNameArr[selectedMonth] + " " + selectedYear);        
        
        // Show dialog if the current date is pressed
        MonthYearTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				UpdateCalendar();
			}
			
		});
		
        
        backYearButton = (Button) findViewById(R.id.backYearButton);
        backYearButton.setOnClickListener(new OnClickListener() {
			
    		@Override
    		public void onClick(View n) {
    			
    			selectedYear--;
    			setSelectedMonthLength();
    			setSelectedStartDay();
    			
    			prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
    	       	prefs = getSharedPreferences( prefs_title, Context.MODE_PRIVATE );
    			
    			UpdateCalendar();
    		}
        });
        
        backMonthButton = (Button) findViewById(R.id.backMonthButton);
        backMonthButton.setOnClickListener(new OnClickListener() {
			
    		@Override
    		public void onClick(View n) {
    			
    			// Jump back a year if month is January
    			if( selectedMonth == 0 ) {
    				selectedMonth = 11;
    				selectedYear--;
    			}
    			else
    				selectedMonth--;
    			
    			setSelectedMonthLength();
    			setSelectedStartDay();
    			
    			prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
    	       	prefs = getSharedPreferences( prefs_title, Context.MODE_PRIVATE );
    			
    			UpdateCalendar();
    		}
        });
        
        nextYearButton = (Button) findViewById(R.id.nextYearButton);
        nextYearButton.setOnClickListener(new OnClickListener() {
			
    		@Override
    		public void onClick(View n) {
    			selectedYear++;
    			setSelectedMonthLength();
    			setSelectedStartDay();
    			
    			prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
    	       	prefs = getSharedPreferences( prefs_title, Context.MODE_PRIVATE );
    			
    			UpdateCalendar();
    		}
        });
        
        nextMonthButton = (Button) findViewById(R.id.nextMonthButton);
        nextMonthButton.setOnClickListener(new OnClickListener() {
			
    		@Override
    		public void onClick(View n) {
    			
    			// Jump forward a year if month is December
    			if( selectedMonth == 11 ) {
    				selectedMonth = 0;
    				selectedYear++;
    			}
    			else
    				selectedMonth++;
    			
    			setSelectedMonthLength();
    			setSelectedStartDay();
    			
    			prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
    	       	prefs = getSharedPreferences( prefs_title, Context.MODE_PRIVATE );
    			
    			UpdateCalendar();
    		}
        });
        
        
        // Set adapters for the calendar grid objects for the days of the week and the
        // actual dates to be displayed
        calendarGrid = (GridView) findViewById(R.id.calendarGrid);
        calendarTitleGrid = (GridView) findViewById(R.id.calendarTitleGrid);
        calendarTitleGrid.setAdapter(new TitleAdapter(this));
        calendarGrid.setAdapter( new CalendarAdapter(this));
        
        // Set listner for press to view event information
        calendarGrid.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		
        		// Verify that the selected day falls within current month
        		if( position > (selectedStartDay - 1) && position < (selectedMonthLength + selectedStartDay)) {
        			
        			// Set selected day to apporiate value
        			selectedDay = position - selectedStartDay + 1;
        		
        			// Load intent with current date information, view event 
	        		Intent myIntent = new Intent( calendarGrid.getContext(), EventActivity.class);
	            	myIntent.putExtra( "selectedDay", selectedDay );
	            	myIntent.putExtra( "selectedMonth", selectedMonth );
	            	myIntent.putExtra( "selectedYear", selectedYear );
	            	CalendarActivity.this.startActivity(myIntent);
        		}

        	}
		});
		

        		 
   }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	UpdateCalendar();
    }
        
    
    // Sets the current date
    public void setCurrentDate()
    {
    	currentDate 	= Calendar.getInstance();
    	currentDay 		= currentDate.get(Calendar.DAY_OF_MONTH);
        currentMonth 	= currentDate.get(Calendar.MONTH);
        currentYear 	= currentDate.get(Calendar.YEAR);
    }
    
    // Resets selected date values to the current date
    public void resetSelectedDate()
    {
    	selectedDate 	= Calendar.getInstance();
    	selectedDay 	= currentDay;
    	selectedMonth 	= currentMonth;
    	selectedYear 	= currentYear;
    	
    	prefs_title = selectedYear + "_" + selectedMonth + "_" + selectedDay;
       	prefs = getSharedPreferences( prefs_title, Context.MODE_PRIVATE );
    }
    
    public void setSelectedMonthLength()
    {
    	String[] monthLengthArr = getResources().getStringArray(R.array.month_length);
    	selectedMonthLength = Integer.parseInt(monthLengthArr[selectedMonth]);
    	
    	// Set month lengths
    	if( selectedMonth == 0 )
    		previousMonthLength = Integer.parseInt(monthLengthArr[11]);
    	else 
    		previousMonthLength = Integer.parseInt(monthLengthArr[selectedMonth-1]);
    	
    	// Add day for leap year
    	if( selectedYear%4 == 0 && selectedMonth == 1 )
    		selectedMonthLength++;
    	
    	if( selectedYear%4 == 0 && selectedMonth == 2 )
    		previousMonthLength++;
    }
    
    public void setSelectedStartDay()
    {
    	
    	selectedDate.set( selectedYear, selectedMonth, selectedDay );
    	selectedDate.set( Calendar.DAY_OF_MONTH, 1 );
    	selectedStartDay = selectedDate.get( Calendar.DAY_OF_WEEK ) - 1;
    	if( selectedStartDay == 0 )
    		selectedStartDay = 7;
  
    }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
    new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, 
                              int monthOfYear, int dayOfMonth) {
            selectedYear = year;
            selectedMonth = monthOfYear;
            selectedDay = dayOfMonth;
            
            MonthYearTextView.setText( monthNameArr[selectedMonth] + " " + selectedYear);
    		
        	calendarAdapter = new CalendarAdapter(view.getContext());
    		calendarAdapter.notifyDataSetChanged();
    		calendarGrid.invalidateViews();
    		calendarGrid.setAdapter( calendarAdapter );
        }
    };
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        selectedYear, selectedMonth, selectedDay);
            
        }
        return null;
    }
    
    
    private void UpdateCalendar()
    {
    	MonthYearTextView.setText( monthNameArr[selectedMonth] + " " + selectedYear);
		
    	calendarAdapter = new CalendarAdapter(getApplicationContext());
		calendarAdapter.notifyDataSetChanged();
		calendarGrid.invalidateViews();
		calendarGrid.setAdapter( calendarAdapter );
    }

    
    public class TitleAdapter extends BaseAdapter
    {
    	private Context context;
    	private String[] daysTitleArr = getResources().getStringArray(R.array.days_abbr_array);

    	
    	public TitleAdapter( Context context) {
    		this.context = context;
    	}
    	
    	public int getCount() {
    		return 7;
    	}
    	
    	public Object getItem(int position) {
    		return null;
    	}
    	
    	public long getItemId(int position) {
    		return position;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {
    		TextView tv;
    		
    		if( convertView == null ) {
    			tv = new TextView(context);
    			tv.setGravity(0x11);
    			tv.setTextColor(Color.DKGRAY);
    			tv.setTextSize(25);
    		}
    		else {
    			tv = (TextView) convertView;
    			tv.setGravity(0x11);
    			tv.setTextColor(Color.DKGRAY);
    			tv.setTextSize(25);
    		}
    		
    		tv.setText(daysTitleArr[position]);
    		
    		return tv;
    	}
    }
    
    public class CalendarAdapter extends BaseAdapter
    {
    	private Context context;
    	// Create array for the date values of a given instance
    	private String[] monthArr = new String[42];
    	
    	// Load prefs to update priority levels of dates

    	
    	public CalendarAdapter( Context context) {
    		this.context = context;
    		int previousMonthCounter = previousMonthLength;
    		int selectedMonthCounter = 1;
    		int nextMonthCounter = 1;
    		for( int i = selectedStartDay; i > 0; i--) {
        		monthArr[i-1] = Integer.toString(previousMonthCounter);
        		previousMonthCounter--;
        	}
    		for( int i = selectedStartDay; i < selectedStartDay + selectedMonthLength; i++ ) {
    			monthArr[i] = Integer.toString(selectedMonthCounter);
    			selectedMonthCounter++;
    		}
    		for( int i = selectedStartDay + selectedMonthLength; i < getCount(); i++ ){
    			monthArr[i] = Integer.toString(nextMonthCounter);
    			nextMonthCounter++;
    		}
    	}
    	
    	public int getCount() {
    		
    		return 42;
    	}
    	
    	public Object getItem(int position) {
    		return null;
    	}
    	
    	public long getItemId(int position) {
    		return position;
    	}
    	
    	public View getView(final int position, View convertView, ViewGroup parent) {
    		TextView tv;
    		
    		prefs = getSharedPreferences( selectedYear + "_" + selectedMonth + "_" + (position-selectedStartDay+1), Context.MODE_PRIVATE );
    		
    		if( convertView == null ) {
    			tv = new TextView(context);
    			tv.setGravity(0x11);
    			
    			tv.setTextSize(25);
    			tv.setHeight(70);
    			
    			//Gray out days not in selected month
    			if( position < selectedStartDay || position > (selectedStartDay + selectedMonthLength - 1) )
    				tv.setTextColor(Color.GRAY);
    			else if( position == currentDay + selectedStartDay - 1 && selectedMonth == currentMonth && selectedYear == currentYear ) {
    				if( prefs.contains( "event_priority") ) {
    					if( prefs.getInt( "event_priority", 0) == 1 )
    						tv.setTextColor(Color.GREEN);
    					else if( prefs.getInt( "event_priority", 0) == 2 )
    						tv.setTextColor(Color.BLUE);
    					else if( prefs.getInt( "event_priority", 0) == 3 )
    						tv.setTextColor(Color.RED);
    					else
    						tv.setTextColor(Color.BLACK);
    				}
    				else
    				tv.setTextColor(Color.BLACK);
    				
    				tv.setTextSize(27);
    				tv.setTypeface(null, Typeface.BOLD);
    			}
    			else if( prefs.contains( "event_priority") ) {
    					if( prefs.getInt( "event_priority", 0) == 1 )
    						tv.setTextColor(Color.GREEN);
    					else if( prefs.getInt( "event_priority", 0) == 2 )
    						tv.setTextColor(Color.BLUE);
    					else if( prefs.getInt( "event_priority", 0) == 3 )
    						tv.setTextColor(Color.RED);
    					else
    						tv.setTextColor(Color.BLACK);
   
    			}
    			else {
    				tv.setTextColor(Color.BLACK);
    			}
    			
    			
    		}
    		else {
    			tv = (TextView) convertView;
    			tv.setGravity(0x11);
    			
    			//Gray out days not in selected month
    			if( position < selectedStartDay || position > (selectedStartDay + selectedMonthLength - 1) )
    				tv.setTextColor(Color.GRAY);
    			else if( position == currentDay + selectedStartDay - 1 && selectedMonth == currentMonth && selectedYear == currentYear ) {
    				if( prefs.contains( "event_priority") ) {
    					if( prefs.getInt( "event_priority", 0) == 1 )
    						tv.setTextColor(Color.GREEN);
    					else if( prefs.getInt( "event_priority", 0) == 2 )
    						tv.setTextColor(Color.BLUE);
    					else if( prefs.getInt( "event_priority", 0) == 3 )
    						tv.setTextColor(Color.RED);
    					else
    						tv.setTextColor(Color.BLACK);
    				}
    				else
    				tv.setTextColor(Color.BLACK);
    				
    				tv.setTextSize(27);
    				tv.setTypeface(null, Typeface.BOLD);
    			}
    			else if( prefs.contains( "event_priority") ) {
					if( prefs.getInt( "event_priority", 0) == 1 )
						tv.setTextColor(Color.GREEN);
					else if( prefs.getInt( "event_priority", 0) == 2 )
						tv.setTextColor(Color.YELLOW);
					else if( prefs.getInt( "event_priority", 0) == 3 )
						tv.setTextColor(Color.RED);
					else
						tv.setTextColor(Color.BLACK);

    			}
    			else
    				tv.setTextColor(Color.BLACK);
    			
    			
    		}
    		
    		
    		tv.setText(monthArr[position]);
    		tv.setPadding(1, 1,1, 1);
    		
    		return tv;
    	}
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