package edu.perlstein.arbor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Search extends Activity {
	
	private Button searchViewButton;
	private Button bark_colorButton;
	private Button bark_textureButton;
	private Button bark_thicknessButton;
	private Button fruit_shapeButton;
	private Button fruit_colorButton;
	private Button fruit_lengthButton;
	private Button fruit_seedButton;
	private Button leaf_colorButton;
	private Button leaf_lengthButton;
	private Button leaf_textureButton;
	private Button elevationButton;
	private Button heightButton;
	private Button habitat_regionButton;
	private Button habitat_temperatureButton;
	private Button fire_susceptibilityButton;
	
	private TextView bark_colorInfo;
	private TextView bark_textureInfo;
	private TextView bark_thicknessInfo;
	private TextView fruit_shapeInfo;
	private TextView fruit_colorInfo;
	private TextView fruit_lengthInfo;
	private TextView fruit_seedInfo;
	private TextView leaf_colorInfo;
	private TextView leaf_lengthInfo;
	private TextView leaf_textureInfo;
	private TextView elevationInfo;
	private TextView heightInfo;
	private TextView habitat_regionInfo;
	private TextView habitat_temperatureInfo;
	private TextView fire_susceptibilityInfo;
	
	private List<String> bark_colorList;
	private List<String> bark_textureList;
	private List<String> fruit_shapeList;
	private List<String> fruit_colorList;
	private List<String> fruit_seedList;
	private List<String> leaf_colorList;
	private List<String> leaf_textureList;
	private List<String> habitat_regionList;
	
	private List<String> bark_thicknessList;
	private List<String> leaf_lengthList;
	private List<String> fruit_lengthList;
	private List<String> habitat_temperatureList;
	private List<String> elevationList;
	private List<String> heightList;
	private List<String> fire_susceptibilityList;
	
	private HashMap<String, List<String>> searchFieldsUsed;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_view);
		
		
		//Initialize lists
		bark_colorList = new ArrayList<String>();
		bark_textureList = new ArrayList<String>();
		fruit_shapeList = new ArrayList<String>();
		fruit_colorList = new ArrayList<String>();
		fruit_seedList = new ArrayList<String>();
		leaf_colorList = new ArrayList<String>();
		leaf_textureList = new ArrayList<String>();
		habitat_regionList = new ArrayList<String>();
		bark_thicknessList = new ArrayList<String>();
		leaf_lengthList = new ArrayList<String>();
		fruit_lengthList = new ArrayList<String>();
		habitat_temperatureList = new ArrayList<String>();
		elevationList = new ArrayList<String>();
		heightList = new ArrayList<String>();
		fire_susceptibilityList = new ArrayList<String>();
		
		// Search button
		searchViewButton = (Button) findViewById(R.id.searchViewButton);
		searchViewButton.setOnClickListener( searchQuery );
		
		// Set button listeners
		bark_colorButton = (Button) findViewById(R.id.barkColorButton);
		bark_colorButton.setOnClickListener( bark_colorListener );
		
		bark_textureButton = (Button) findViewById(R.id.barkTextureButton);
		bark_textureButton.setOnClickListener( bark_textureListener );
		
		bark_thicknessButton = (Button) findViewById(R.id.barkThicknessButton);
		bark_thicknessButton.setOnClickListener( bark_thicknessListener );
		
		leaf_colorButton = (Button) findViewById(R.id.leafColorButton);
		leaf_colorButton.setOnClickListener( leaf_colorListener );
		
		leaf_textureButton = (Button) findViewById(R.id.leafTypeButton);
		leaf_textureButton.setOnClickListener( leaf_textureListener );
		
		leaf_lengthButton = (Button) findViewById(R.id.leafLengthButton);
		leaf_lengthButton.setOnClickListener( leaf_lengthListener );
		
		fruit_shapeButton = (Button) findViewById(R.id.fruitShapeButton);
		fruit_shapeButton.setOnClickListener( fruit_shapeListener );
		
		fruit_colorButton = (Button) findViewById(R.id.fruitColorButton);
		fruit_colorButton.setOnClickListener( fruit_colorListener );
		
		fruit_lengthButton = (Button) findViewById(R.id.fruitLengthButton);
		fruit_lengthButton.setOnClickListener( fruit_lengthListener );
		
		fruit_seedButton = (Button) findViewById(R.id.fruitSeedsButton);
		fruit_seedButton.setOnClickListener( fruit_seedListener );
		
		elevationButton = (Button) findViewById(R.id.elevationButton);
		elevationButton.setOnClickListener( elevationListener );
		
		heightButton = (Button) findViewById(R.id.heightButton);
		heightButton.setOnClickListener( heightListener );
		
		habitat_regionButton = (Button) findViewById(R.id.habitatRegionButton);
		habitat_regionButton.setOnClickListener( habitat_regionListener );
		
		habitat_temperatureButton = (Button) findViewById(R.id.habitatTempButton);
		habitat_temperatureButton.setOnClickListener( habitat_temperatureListener );
		
		fire_susceptibilityButton = (Button) findViewById(R.id.fireButton);
		fire_susceptibilityButton.setOnClickListener( fire_susceptibilityListener );
		
		
		// Initialize search field data
		searchFieldsUsed = new HashMap<String, List<String>>(15);
		
		// Load blank fields for the first time
		refreshFields();
		
		// Print warning for the over-zealous
		welcomeDialog();


	}
	
	// Clear selections
	@Override
	public void onResume()
	{
		super.onResume();
		refreshFields();
	}
	
	// Allow for clearing fields
	@Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    	menu.add( Menu.NONE, 0, 0, "Clear Selections" );
    	return super.onCreateOptionsMenu( menu );
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	refreshFields();
    	return true;
    } 
    
    public void welcomeDialog()
    {
    	AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
		builder.setTitle("Notice to user!");
		builder.setMessage("It is wise to search with fewer attribute selections, " +
				"than more. What you see may not reflect the scientific records we " +
				"have on file. If your search turns up with no results, reduce the" +
				" amount of data you are selecting." );
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
    }
	
	//Refresh all settings
	private void refreshFields()
	{
		searchFieldsUsed.clear();
		
		bark_colorInfo = (TextView) findViewById(R.id.barkColorInfoText);
		bark_colorInfo.setText("No entry");
		bark_colorList.clear();
		
		bark_textureInfo = (TextView) findViewById(R.id.barkTextureInfoText);
		bark_textureInfo.setText("No entry");
		bark_textureList.clear();
		
		bark_thicknessInfo = (TextView) findViewById(R.id.barkThicknessInfoText);
		bark_thicknessInfo.setText("No entry");
		bark_thicknessList.clear();
		
		leaf_colorInfo = (TextView) findViewById(R.id.leafColorInfoText);
		leaf_colorInfo.setText("No entry");
		leaf_colorList.clear();
		
		leaf_textureInfo = (TextView) findViewById(R.id.leafTypeInfoText);
		leaf_textureInfo.setText("No entry");
		leaf_textureList.clear();
		
		leaf_lengthInfo = (TextView) findViewById(R.id.leafLengthInfoText);
		leaf_lengthInfo.setText("No entry");
		leaf_lengthList.clear();
		
		fruit_shapeInfo = (TextView) findViewById(R.id.fruitShapeInfoText);
		fruit_shapeInfo.setText("No entry");
		fruit_shapeList.clear();
		
		fruit_colorInfo = (TextView) findViewById(R.id.fruitColorInfoText);
		fruit_colorInfo.setText("No entry");
		fruit_colorList.clear();
		
		fruit_lengthInfo = (TextView) findViewById(R.id.fruitLengthInfoText);
		fruit_lengthInfo.setText("No entry");
		fruit_lengthList.clear();
		
		fruit_seedInfo = (TextView) findViewById(R.id.fruitSeedsInfoText);
		fruit_seedInfo.setText("No entry");
		fruit_seedList.clear();
		
		elevationInfo = (TextView) findViewById(R.id.elevationInfoText);
		elevationInfo.setText("No entry");
		elevationList.clear();
		
		heightInfo = (TextView) findViewById(R.id.heightInfoText);
		heightInfo.setText("No entry");
		heightList.clear();
		
		habitat_regionInfo = (TextView) findViewById(R.id.habitatRegionInfoText);
		habitat_regionInfo.setText("No entry");
		habitat_regionList.clear();
		
		habitat_temperatureInfo = (TextView) findViewById(R.id.habitatTempInfoText);
		habitat_temperatureInfo.setText("No entry");
		habitat_temperatureList.clear();
		
		fire_susceptibilityInfo = (TextView) findViewById(R.id.fireInfoText);
		fire_susceptibilityInfo.setText("No entry");
		fire_susceptibilityList.clear();
	}
	
	// ## Create OnClickListeners for all the buttons ##
	
	// ############# SEARCH #####################
	OnClickListener searchQuery = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			// Variables
			String[] resultsArr;
			String[] searchTerms;
			boolean firstTerm = true;
			
			// load possible search fields
			searchTerms = getResources().getStringArray(R.array.searchable_terms);
			
			//############# LOAD DATABASE ##############
			// Credit - Juan-Manuel Fluxa of Reign Design
			//
			DatabaseHelper myDbHelper;
	        myDbHelper = new DatabaseHelper(Search.this);
	        try {
	        	myDbHelper.createDataBase();
		 	} catch (IOException ioe) {
		 		throw new Error("Unable to create database");
		 	}
		 	try {
		 		myDbHelper.openDataBase();
		 	}catch(SQLException sqle){
		 		throw sqle;
		 	}
		 	//
			//########################################### 	 	
		 	
		 	// Load database and cursor
			SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/edu.perlstein.arbor/databases/COarbor.db",null, SQLiteDatabase.OPEN_READONLY);
			Cursor cur;
			
			// prime sqlQuery
			String sqlQuery = "SELECT * FROM COarbor ";			
			
			if( searchFieldsUsed.size() > 0) {
				
				// Check all data types
				for( int i = 0; i < searchTerms.length; i++) {
					
					// Check for numeric range entries
					if( 	searchTerms[i].equals("leaf_length") ||
							searchTerms[i].equals("fruit_length") ||
							searchTerms[i].equals("elevation") ||
							searchTerms[i].equals("height") ){
						
						// Verify there was a selection in a given field
						if( searchFieldsUsed.get(searchTerms[i]) != null ) {
						
							Iterator<String> itr = searchFieldsUsed.get(searchTerms[i]).iterator();
							String tempNumStr = itr.next();
							
							// Test for first term
							if( firstTerm ) {
								sqlQuery = sqlQuery.concat( "WHERE ");
								firstTerm = false;
							}
							else
								sqlQuery = sqlQuery.concat( "AND ");
							
							// Check range of values
							sqlQuery = sqlQuery.concat( searchTerms[i] + "_min <= " + tempNumStr + " AND " + searchTerms[i] + "_max >= " + tempNumStr + " ");
						}
					}
					else {
						// Verify there was a selection in a given field
						if( searchFieldsUsed.get(searchTerms[i]) != null ) {
							
							// Test for first term
							if( firstTerm ) {
								sqlQuery = sqlQuery.concat( "WHERE ");
								firstTerm = false;
							}
							else
								sqlQuery = sqlQuery.concat( "AND ");
							
							// Add table name
							sqlQuery = sqlQuery.concat(searchTerms[i]);
							Iterator<String> itr = searchFieldsUsed.get(searchTerms[i]).iterator();
							while( itr.hasNext() ) {
								sqlQuery = sqlQuery.concat(" LIKE '%" + itr.next() + "%' ");
							}
						}
					}
				}
			}
			
			// Query the database
			cur = db.rawQuery( sqlQuery, null);
			cur.moveToFirst();
			
			// There were no results
			if( cur.getCount() == 0 ) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
				builder.setTitle("Search Error");
				builder.setMessage("There are no results that meet your search parameters." +
						" Please try again with less selections.");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();
				
			}
			// Load results
			else {
				resultsArr = new String[cur.getCount()];
				for( int i = 0; i < cur.getCount(); i++ ) {
					resultsArr[i] = cur.getString(0);
					cur.moveToNext();
				}
				
				// Excessive closing for safety's sake
				cur.close();
				db.close();
				myDbHelper.close();
				
				
				Intent intent = new Intent( Search.this, ResultsView.class);
				intent.putExtra( "trees", resultsArr );
				v.getContext().startActivity(intent);
			}
			
			
		}
	};
	
	// ############## BARK COLOR #################
	OnClickListener bark_colorListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Light (shade)", "Dark (shade)", "Brown", "Gray", "Green", "Orange", "Red", "White"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select bark color(s):");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( bark_colorList.isEmpty() ) {
						bark_colorInfo.setText("No entry");
						searchFieldsUsed.remove("bark_color");
					}
					// Parse data list
					else {
						Iterator<String> itr = bark_colorList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("bark_color", bark_colorList);
						// Load display with values
						bark_colorInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  bark_colorList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !bark_colorList.contains( items[itemOffset].toString() )){
							bark_colorList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( bark_colorList.contains( items[itemOffset].toString() )){
							bark_colorList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## BARK TEXTURE ###############
	OnClickListener bark_textureListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Small scales", "Large scales", "Furrowed Scales", "Flat Scales", "Irregular Scales", "Flaky Scales", 
					"Corky Ridges", "Scaly Ridges", "Interlacing Ridges", "Furrowed Ridges", "Smooth", "Fibrous"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select bark texture:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Parse data list
					if( bark_textureList.isEmpty() ) {
						bark_textureInfo.setText("No entry");
						searchFieldsUsed.remove("bark_texture");
					}
					else {
						Iterator<String> itr = bark_textureList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("bark_texture", bark_textureList);
						// Load display with values
						bark_textureInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  bark_textureList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					if( bool == true )
						if( !bark_textureList.contains( items[itemOffset].toString() )){
							bark_textureList.add(items[itemOffset].toString());
						}
					if( bool == false )
						if( bark_textureList.contains( items[itemOffset].toString() )){
							bark_textureList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	
	// ############## BARK THICKNESS ##############
	OnClickListener bark_thicknessListener = new OnClickListener() {
		
		// temp values
		String bark_thicknessTemp;
		int offset = 0;
		
		@Override
		public void onClick(View v) {
			final CharSequence[] items = { "No entry", "Thin", "Thick" };
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select thickness:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
										
					if( bark_thicknessTemp.equals("No entry")){
						bark_thicknessInfo.setText(bark_thicknessTemp);
						bark_thicknessList.clear();
						searchFieldsUsed.remove("bark_thickness");
					}
					else {
						bark_thicknessList.clear();
						bark_thicknessList.add(bark_thicknessTemp);
						searchFieldsUsed.put("bark_thickness", bark_thicknessList);
						bark_thicknessInfo.setText(bark_thicknessTemp);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++ ){
				if( bark_thicknessList.contains(items[i].toString())) {
					offset = i;
					break;
				}
			}
			
			builder.setSingleChoiceItems( items, offset, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					bark_thicknessTemp = (String) items[which];	
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## LEAF COLOR ###############
	OnClickListener leaf_colorListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Light (shade)", "Dark (shade)", "Blue", "Gray", "Green", "Yellow", "White Lines", "Silver Lines"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select leaf color(s):");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( leaf_colorList.isEmpty() ) {
						leaf_colorInfo.setText("No entry");
						searchFieldsUsed.remove("leaf_color");
					}
					// Parse data list
					else {
						Iterator<String> itr = leaf_colorList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("leaf_color", leaf_colorList);
						// Load display with values
						leaf_colorInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  leaf_colorList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !leaf_colorList.contains( items[itemOffset].toString() )){
							leaf_colorList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( leaf_colorList.contains( items[itemOffset].toString() )){
							leaf_colorList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## LEAF TEXTURE/TYPE ###############
	OnClickListener leaf_textureListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Needle", "Stiff", "Cracked", "Stout", "Flat", 
					"Slender", "Broad", "Serrated", "Curved"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select leaf attributes:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( leaf_textureList.isEmpty() ) {
						leaf_textureInfo.setText("No entry");
						searchFieldsUsed.remove("leaf_texture");
					}
					// Parse data list
					else {
						Iterator<String> itr = leaf_textureList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("leaf_texture", leaf_textureList);
						// Load display with values
						leaf_textureInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  leaf_textureList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !leaf_textureList.contains( items[itemOffset].toString() )){
							leaf_textureList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( leaf_textureList.contains( items[itemOffset].toString() )){
							leaf_textureList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## LEAF LENGTH ###############
	OnClickListener leaf_lengthListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			final Dialog dialog = new Dialog(Search.this);
			dialog.setContentView(R.layout.numeric_search);
			dialog.setTitle("Enter leaf length (inches):");
			
			final EditText numText = (EditText) dialog.findViewById(R.id.numberEditText);
			
			// Load value into EditText if one exists
			if( !leaf_lengthList.isEmpty() )
				numText.setText(leaf_lengthList.get(0));
			
			// Button to confirm choice
			Button okButton = (Button) dialog.findViewById(R.id.ok_button);
			okButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( numText.getText().toString().length() != 0 ) {
						leaf_lengthList.clear();
						leaf_lengthList.add( numText.getText().toString() );
						leaf_lengthInfo.setText(leaf_lengthList.get(0) + " inches");
						
							searchFieldsUsed.put("leaf_length", leaf_lengthList);
					}
					else {
						if( numText.getText().toString().length() == 0 ) {
							leaf_lengthList.clear();
							searchFieldsUsed.remove("leaf_length");
							leaf_lengthInfo.setText("No Entry");
						}
					}
					dialog.dismiss();
				}
			});
			
			// Button to cancel selection
			Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			

			dialog.show();			
		}
	};
	
	// ############## FRUIT SHAPE ###############
	OnClickListener fruit_shapeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Cone", "Capsule", "Egg-Shaped", "Cylindrical", "Short-Stalked", "Oblong", "Catkin", "Berry"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select fruit shape:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( fruit_shapeList.isEmpty() ) {
						fruit_shapeInfo.setText("No entry");
						searchFieldsUsed.remove("fruit_shape");
					}
					// Parse data list
					else {
						Iterator<String> itr = fruit_shapeList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("fruit_shape", fruit_shapeList);
						// Load display with values
						fruit_shapeInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  fruit_shapeList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !fruit_shapeList.contains( items[itemOffset].toString() )){
							fruit_shapeList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( fruit_shapeList.contains( items[itemOffset].toString() )){
							fruit_shapeList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## FRUIT COLOR ###############
	OnClickListener fruit_colorListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Light (shade)", "Dark (shade)", "Blue", "Brown", "Gray", "Green", "Purple", "Red", "Yellow"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select fruit color(s):");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( fruit_colorList.isEmpty() ) {
						fruit_colorInfo.setText("No entry");
						searchFieldsUsed.remove("fruit_color");
					}
					// Parse data list
					else {
						Iterator<String> itr = fruit_colorList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("fruit_color", fruit_colorList);
						// Load display with values
						fruit_colorInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  fruit_colorList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !fruit_colorList.contains( items[itemOffset].toString() )){
							fruit_colorList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( fruit_colorList.contains( items[itemOffset].toString() )){
							fruit_colorList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## FRUIT LENGTH ###############
	OnClickListener fruit_lengthListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			final Dialog dialog = new Dialog(Search.this);
			dialog.setContentView(R.layout.numeric_search);
			dialog.setTitle("Enter fruit length (inches):");
			
			final EditText numText = (EditText) dialog.findViewById(R.id.numberEditText);
			
			// Load value into EditText if one exists
			if( !fruit_lengthList.isEmpty() )
				numText.setText(fruit_lengthList.get(0));
			
			// Button to confirm choice
			Button okButton = (Button) dialog.findViewById(R.id.ok_button);
			okButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( numText.getText().toString().length() != 0 ) {
						fruit_lengthList.clear();
						fruit_lengthList.add( numText.getText().toString() );
						fruit_lengthInfo.setText(fruit_lengthList + " inches");
						
						searchFieldsUsed.put("fruit_length", fruit_lengthList);
					}
					else {
						if( numText.getText().toString().length() == 0 )
							fruit_lengthList.clear();
							fruit_lengthInfo.setText("No Entry");
							searchFieldsUsed.remove("fruit_length");
					}
				}
			});
			
			// Button to cancel selection
			Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			

			dialog.show();			
		}
	};
	
	// ############## FRUIT SEED ###############
	OnClickListener fruit_seedListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Broad Wing", "Short Wing", "Long Wing", "Brown", "Black", "Long", "Pair", "Three-Point", "Rounded"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select seed attributes:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( fruit_seedList.isEmpty() ) {
						fruit_seedInfo.setText("No entry");
						searchFieldsUsed.remove("fruit_seed");
					}
					// Parse data list
						else {
						Iterator<String> itr = fruit_seedList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
						
							searchFieldsUsed.put("fruit_seed", fruit_seedList);
						// Load display with values
						fruit_seedInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if(  fruit_seedList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !fruit_seedList.contains( items[itemOffset].toString() )){
							fruit_seedList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( fruit_seedList.contains( items[itemOffset].toString() )){
							fruit_seedList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## EVELVATION ###############
	OnClickListener elevationListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			final Dialog dialog = new Dialog(Search.this);
			dialog.setContentView(R.layout.numeric_search);
			dialog.setTitle("Enter elevation (feet):");
			
			final EditText numText = (EditText) dialog.findViewById(R.id.numberEditText);
			
			// Load value into EditText if one exists
			if( !elevationList.isEmpty())
				numText.setText(elevationList.get(0));
			
			// Button to confirm choice
			Button okButton = (Button) dialog.findViewById(R.id.ok_button);
			okButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( numText.getText().toString().length() != 0 ) {
						elevationList.clear();
						elevationList.add( numText.getText().toString() );
						elevationInfo.setText(elevationList.get(0) + " inches");
							searchFieldsUsed.put("elevation", elevationList);
					}
					else {
						if( numText.getText().toString().length() == 0) {
							elevationList.clear();
							elevationInfo.setText("No Entry");
							searchFieldsUsed.remove("elevation");
						}
					}
					dialog.dismiss();
				}
			});
			
			// Button to cancel selection
			Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			

			dialog.show();			
		}
	};
	
	// ############## HEIGHT ###############
	OnClickListener heightListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			final Dialog dialog = new Dialog(Search.this);
			dialog.setContentView(R.layout.numeric_search);
			dialog.setTitle("Enter height (feet):");
			
			final EditText numText = (EditText) dialog.findViewById(R.id.numberEditText);
			
			// Load value into EditText if one exists
			if( !heightList.isEmpty() )
				numText.setText(heightList.get(0));
			
			// Button to confirm choice
			Button okButton = (Button) dialog.findViewById(R.id.ok_button);
			okButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( numText.getText().toString().length() != 0 ) {
						heightList.clear();
						heightList.add( numText.getText().toString() );
						heightInfo.setText(heightList.get(0) + " inches");
						searchFieldsUsed.put("height", heightList);
					}
					else {
						if( numText.getText().toString().length() == 0 ) {
							heightList.clear();
							heightInfo.setText("No Entry");
							searchFieldsUsed.remove("height");
						}
					}
					dialog.dismiss();
				}
			});
			
			// Button to cancel selection
			Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
			cancelButton.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			

			dialog.show();			
		}
	};
	
	// ############## HABITAT REGION ###############
	OnClickListener habitat_regionListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load values
			final CharSequence[] items = { "Drained", "Dry", "Gravel", "Moist", "Plains", "Rocky", "Stream", "Sandy"  };
			boolean[] b = new boolean[ items.length ];
			Arrays.fill(b, false);
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select habitat/soil attributes:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String tempStr = "";
					// Verify if selections are made
					if( habitat_regionList.isEmpty() ) {
						habitat_regionInfo.setText("No entry");
						searchFieldsUsed.remove("habitat_region");
					}
					// Parse data list
					else {
						Iterator<String> itr = habitat_regionList.iterator();
						while( itr.hasNext() )
							tempStr = tempStr.concat( itr.next() + ", " );
							searchFieldsUsed.put("habitat_region", habitat_regionList);
						// Load display with values
						habitat_regionInfo.setText(tempStr);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++) {
				if( habitat_regionList.contains(items[i].toString()) )
					b[i] = true;
			}
			
			// Load checkbox dialog
			builder.setMultiChoiceItems( items, b, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int itemOffset, boolean bool) {
					
					// Data to be included
					if( bool == true )
						if( !habitat_regionList.contains( items[itemOffset].toString() )){
							habitat_regionList.add(items[itemOffset].toString());
						}
					// Data to be removed (or ignored)
					if( bool == false )
						if( habitat_regionList.contains( items[itemOffset].toString() )){
							habitat_regionList.remove(items[itemOffset].toString());
						}
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## HABITAT TEMPERATURE ###############
	OnClickListener habitat_temperatureListener = new OnClickListener() {
		
		// temp values
		String tempStr;
		int offset = 0;
		
		@Override
		public void onClick(View v) {
			final CharSequence[] items = { "No entry", "Cold" };
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select temperature type:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if( tempStr.equals("No entry")) {
						habitat_temperatureList.clear();
						searchFieldsUsed.remove("habitat_temperature");
					}
					else {
						habitat_temperatureList.clear();
						habitat_temperatureList.add(tempStr);
						searchFieldsUsed.put("habitat_temperature", habitat_temperatureList);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++ ){
				if( habitat_temperatureList.contains(items[i].toString())) {
					offset = i;
					break;
				}
			}
			
			builder.setSingleChoiceItems( items, offset, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tempStr = (String) items[which];	
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};
	
	// ############## FIRE SUSCEPTIBILITY ###############
	OnClickListener fire_susceptibilityListener = new OnClickListener() {
		
		// temp values
		String tempStr;
		int offset = 0;
		
		@Override
		public void onClick(View v) {
			final CharSequence[] items = { "No entry", "Low", "Medium", "High" };
			
			// Build dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
			builder.setTitle("Select susceptibility to fire:");
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if( tempStr.equals("No entry")) {
						fire_susceptibilityList.clear();
						searchFieldsUsed.remove("fire_susceptibility");
					}
					else {
						fire_susceptibilityList.clear();
						fire_susceptibilityList.add(tempStr);
						searchFieldsUsed.put("fire_susceptibility", fire_susceptibilityList);
					}
					
					dialog.dismiss();
				}
			});
			builder.setCancelable( false );
			
			// Determine current value
			for( int i = 0; i < items.length; i++ ){
				if( fire_susceptibilityList.contains(items[i].toString())) {
					offset = i;
					break;
				}
			}
			
			builder.setSingleChoiceItems( items, offset, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tempStr = (String) items[which];	
				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

		}
	};

}
