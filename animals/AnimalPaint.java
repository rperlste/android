package edu.perlstein.animals;


import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AnimalPaint extends Activity
{
   private AnimalPaintView doodleView; // drawing View
   private SensorManager sensorManager; // monitors accelerometer
   private float acceleration; // acceleration
   private float currentAcceleration; // current acceleration
   private float lastAcceleration; // last acceleration
   private AtomicBoolean dialogIsVisible = new AtomicBoolean(); // false

   // create menu ids for each menu option 
   private static final int COLOR_MENU_ID = Menu.FIRST;
   private static final int WIDTH_MENU_ID = Menu.FIRST + 1;
   private static final int ERASE_MENU_ID = Menu.FIRST + 2;
   private static final int CLEAR_MENU_ID = Menu.FIRST + 3;
   
   // value used to determine whether user shook the device to erase
   private static final int ACCELERATION_THRESHOLD = 15000;
   
   // variable that refers to a Choose Color or Choose Line Width dialog
   private Dialog currentDialog; 

   // called when this Activity is loaded
   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.paint_main); // inflate the layout
      
      // Extract bitmap information

      int drawableOffset = 0;
      String[] animalArray = getResources().getStringArray(R.array.animal_names);
      String animalSelected = getIntent().getExtras().getString( "NAME");
      while( !animalSelected.equals(animalArray[drawableOffset]) )
    	  drawableOffset++;


      // get reference to the DoodleView
      doodleView = (AnimalPaintView) findViewById(R.id.doodleView);
      
      doodleView.setBackgroundResource(R.drawable.draw_alligator+drawableOffset);

      
      // initialize acceleration values
      acceleration = 0.00f; 
      currentAcceleration = SensorManager.GRAVITY_EARTH;    
      lastAcceleration = SensorManager.GRAVITY_EARTH;    
     
       // listen for shake
   } // end method onCreate
 
   // displays configuration options in menu
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu); // call super's method

      // add options to menu
      menu.add(Menu.NONE, COLOR_MENU_ID, Menu.NONE, 
         R.string.menuitem_color);
      menu.add(Menu.NONE, WIDTH_MENU_ID, Menu.NONE, 
         R.string.menuitem_line_width);
      menu.add(Menu.NONE, ERASE_MENU_ID, Menu.NONE, 
         R.string.menuitem_erase);
      menu.add(Menu.NONE, CLEAR_MENU_ID, Menu.NONE, 
         R.string.menuitem_clear);

      return true; // options menu creation was handled
   } // end onCreateOptionsMenu

   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      // switch based on the MenuItem id
      switch (item.getItemId()) 
      {
         case COLOR_MENU_ID:
            showColorDialog(); // display color selection dialog
            return true; // consume the menu event
         case WIDTH_MENU_ID:
            showLineWidthDialog(); // display line thickness dialog
            return true; // consume the menu event
         case ERASE_MENU_ID:
            doodleView.setDrawingColor(Color.WHITE); // line color white
            return true; // consume the menu event
         case CLEAR_MENU_ID:
            doodleView.clear(); // clear doodleView
            return true; // consume the menu event
      } // end switch
      
      return super.onOptionsItemSelected(item); // call super's method
   } // end method onOptionsItemSelected
     
   // display a dialog for selecting color
   private void showColorDialog()
   {
      // create the dialog and inflate its content
      currentDialog = new Dialog(this);
      currentDialog.setContentView(R.layout.color_dialog);
      currentDialog.setTitle(R.string.title_color_dialog);
      currentDialog.setCancelable(true);
      
      // get the color SeekBars and set their onChange listeners
      final SeekBar alphaSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
      final SeekBar redSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
      final SeekBar greenSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
      final SeekBar blueSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

      // register SeekBar event listeners
      alphaSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
      redSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
      greenSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
      blueSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
     
      // use current drawing color to set SeekBar values
      final int color = doodleView.getDrawingColor();
      alphaSeekBar.setProgress(Color.alpha(color));
      redSeekBar.setProgress(Color.red(color));
      greenSeekBar.setProgress(Color.green(color));
      blueSeekBar.setProgress(Color.blue(color));        
      
      // set the Set Color Button's onClickListener
      Button setColorButton = (Button) currentDialog.findViewById(
         R.id.setColorButton);
      setColorButton.setOnClickListener(setColorButtonListener);
 
      dialogIsVisible.set(true); // dialog is on the screen
      currentDialog.show(); // show the dialog
   } // end method showColorDialog
   
   // OnSeekBarChangeListener for the SeekBars in the color dialog
   private OnSeekBarChangeListener colorSeekBarChanged = 
     new OnSeekBarChangeListener() 
   {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
         boolean fromUser) 
      {
         // get the SeekBars and the colorView LinearLayout
         SeekBar alphaSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
         SeekBar redSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
         SeekBar greenSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
         SeekBar blueSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);
         View colorView = 
            (View) currentDialog.findViewById(R.id.colorView);

         // display the current color
         colorView.setBackgroundColor(Color.argb(
            alphaSeekBar.getProgress(), redSeekBar.getProgress(), 
            greenSeekBar.getProgress(), blueSeekBar.getProgress()));
      } // end method onProgressChanged
      
      // required method of interface OnSeekBarChangeListener
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) 
      {
      } // end method onStartTrackingTouch
      
      // required method of interface OnSeekBarChangeListener
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) 
      {
      } // end method onStopTrackingTouch
   }; // end colorSeekBarChanged
   
   // OnClickListener for the color dialog's Set Color Button
   private OnClickListener setColorButtonListener = new OnClickListener() 
   {
      @Override
      public void onClick(View v) 
      {
         // get the color SeekBars
         SeekBar alphaSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
         SeekBar redSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
         SeekBar greenSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
         SeekBar blueSeekBar = 
            (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

         // set the line color
         doodleView.setDrawingColor(Color.argb(
            alphaSeekBar.getProgress(), redSeekBar.getProgress(), 
            greenSeekBar.getProgress(), blueSeekBar.getProgress()));
         dialogIsVisible.set(false); // dialog is not on the screen
         currentDialog.dismiss(); // hide the dialog
         currentDialog = null; // dialog no longer needed
      } // end method onClick
   }; // end setColorButtonListener
   
   // display a dialog for setting the line width
   private void showLineWidthDialog()
   {
      // create the dialog and inflate its content
      currentDialog = new Dialog(this);
      currentDialog.setContentView(R.layout.width_dialog);
      currentDialog.setTitle(R.string.title_line_width_dialog);
      currentDialog.setCancelable(true);
      
      // get widthSeekBar and configure it
      SeekBar widthSeekBar = 
         (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
      widthSeekBar.setOnSeekBarChangeListener(widthSeekBarChanged);
      widthSeekBar.setProgress(doodleView.getLineWidth()); 
       
      // set the Set Line Width Button's onClickListener
      Button setLineWidthButton = 
         (Button) currentDialog.findViewById(R.id.widthDialogDoneButton);
      setLineWidthButton.setOnClickListener(setLineWidthButtonListener);
      
      dialogIsVisible.set(true); // dialog is on the screen
      currentDialog.show(); // show the dialog      
   } // end method showLineWidthDialog

   // OnSeekBarChangeListener for the SeekBar in the width dialog
   private OnSeekBarChangeListener widthSeekBarChanged = 
      new OnSeekBarChangeListener() 
      {
         Bitmap bitmap = Bitmap.createBitmap( // create Bitmap
            400, 100, Bitmap.Config.ARGB_8888);
         Canvas canvas = new Canvas(bitmap); // associate with Canvas
         
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) 
         {  
            // get the ImageView
            ImageView widthImageView = (ImageView) 
               currentDialog.findViewById(R.id.widthImageView);
            
            // configure a Paint object for the current SeekBar value
            Paint p = new Paint();
            p.setColor(doodleView.getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);
            
            // erase the bitmap and redraw the line
            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
         } // end method onProgressChanged
   
         // required method of interface OnSeekBarChangeListener
         @Override
         public void onStartTrackingTouch(SeekBar seekBar) 
         {
         } // end method onStartTrackingTouch
   
         // required method of interface OnSeekBarChangeListener
         @Override
         public void onStopTrackingTouch(SeekBar seekBar) 
         {
         } // end method onStopTrackingTouch
      }; // end widthSeekBarChanged

   // OnClickListener for the line width dialog's Set Line Width Button
   private OnClickListener setLineWidthButtonListener = 
      new OnClickListener() 
      {
         @Override
         public void onClick(View v) 
         {
            // get the color SeekBars
            SeekBar widthSeekBar = 
               (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
   
            // set the line color
            doodleView.setLineWidth(widthSeekBar.getProgress());
            dialogIsVisible.set(false); // dialog is not on the screen
            currentDialog.dismiss(); // hide the dialog
            currentDialog = null; // dialog no longer needed
         } // end method onClick
      }; // end setColorButtonListener
} // end class Doodlz


/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/
