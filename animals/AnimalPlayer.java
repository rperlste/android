package edu.perlstein.animals;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class AnimalPlayer extends Activity {
	
	public static final String TAG = "SLIDESHOW";
	
	private static final String IMAGE_INDEX = "IMAGE_INDEX";
	private static final String SLIDESHOW_NAME = "SLIDESHOW_NAME";
	
	private static final int DURATION = 5000;
	
	private ImageView imageView;
	private String slideshowName;
	private SlideshowInfo slideshow;
	private BitmapFactory.Options options;
	private Handler handler;
	private int nextItemIndex;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow_player);
        
        imageView = (ImageView) findViewById(R.id.imageView);
        
        if( savedInstanceState == null ){
        	slideshowName = getIntent().getStringExtra(AnimalList.NAME_EXTRA);
        	nextItemIndex = 0;
        }
        else {
        	nextItemIndex = savedInstanceState.getInt(IMAGE_INDEX);
        	slideshowName = savedInstanceState.getString(SLIDESHOW_NAME);
        }
        
        slideshow = AnimalList.getSlideshowInfo(slideshowName);
        
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        
        handler = new Handler();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		handler.post(updateSlideshow);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		handler.removeCallbacks(updateSlideshow);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		outState.putInt(IMAGE_INDEX, nextItemIndex - 1);
		outState.putString(SLIDESHOW_NAME, slideshowName);
	}
	
	private Runnable updateSlideshow = new Runnable()
	{
		@Override
		public void run()
		{
			if( nextItemIndex >= slideshow.size() ) {
				finish();
			}
			else
			{
				String item = slideshow.getImageAt(nextItemIndex);
				new LoadImageTask().execute(item);
				++nextItemIndex;
			}
		}
		
		class LoadImageTask extends AsyncTask<String, Object, Bitmap>
		{
			@Override
			protected Bitmap doInBackground(String... params)
			{
				// Replaced getBitmap with this to create the bitmap froma resource, instead of the SDcard
				return BitmapFactory.decodeResource( getResources(), getResources().getIdentifier(params[0], null, null));  
			}
			
			@Override
			protected void onPostExecute(Bitmap result)
			{
				super.onPostExecute(result);
				BitmapDrawable next = new BitmapDrawable(result);
				next.setGravity(android.view.Gravity.CENTER);
				Drawable previous = imageView.getDrawable();
				
				if(previous instanceof TransitionDrawable)
					previous = ((TransitionDrawable) previous).getDrawable(1);
				
				if( previous == null )
					imageView.setImageDrawable(next);
				else
				{
					Drawable[] drawables = { previous, next };
					TransitionDrawable transition = new TransitionDrawable(drawables);
					imageView.setImageDrawable(transition);
					transition.startTransition(1000);
				}
				
				handler.postDelayed(updateSlideshow, DURATION);
			}
		}
	
	};

}
