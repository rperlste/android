package edu.perlstein.animals;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AnimalList extends ListActivity {

	public static final String NAME_EXTRA = "NAME";
	
	static List<SlideshowInfo> slideshowList;
	private ListView slideshowListView;
	private SlideshowAdapter slideshowAdapter;
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		
		//Load slideshowList with approriate animals
		slideshowList = new ArrayList<SlideshowInfo>();
		makeSlideshowList( getIntent().getExtras().getChar("animal_letter", '0') );

		
		slideshowListView = getListView();
		slideshowAdapter = new SlideshowAdapter( this, slideshowList );
		slideshowListView.setAdapter( slideshowAdapter );
		
	}

	public static class ViewHolder
	{
		TextView nameTextView;
		ImageView imageView;
		Button playButton;
		Button infoButton;
		Button paintButton;
	}
	
	private class SlideshowAdapter extends ArrayAdapter< SlideshowInfo >
	{
		private List< SlideshowInfo > items;
		private LayoutInflater inflater;
		
		public SlideshowAdapter( Context context, List< SlideshowInfo > items )
		{			
			super( context, -1, items );
			this.items = items;
			inflater = ( LayoutInflater ) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		}
		
		@Override
		public View getView( int position, View convertView, ViewGroup parent )
		{
			ViewHolder viewHolder;
			
			if( convertView == null ) {
				convertView = inflater.inflate( R.layout.slideshow_list_item, null);
				
				viewHolder = new ViewHolder();
				viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.slideshowImageView);
				viewHolder.playButton = (Button) convertView.findViewById(R.id.playButton);
				viewHolder.infoButton = (Button) convertView.findViewById(R.id.infoButton);
				viewHolder.paintButton = (Button) convertView.findViewById(R.id.paintButton);
				
				convertView.setTag( viewHolder );
			}
			else
				viewHolder = (ViewHolder) convertView.getTag();
			
			SlideshowInfo slideshowInfo = items.get(position);
			viewHolder.nameTextView.setText( slideshowInfo.getName() );
			
			// Set image thumbnail usign resource
			if( slideshowInfo.size() > 0 ) {
				String firstItem = slideshowInfo.getImageAt(0);
				viewHolder.imageView.setImageResource( getResources().getIdentifier( firstItem, null, null));
			}
						
			viewHolder.playButton.setTag(slideshowInfo);
			viewHolder.playButton.setOnClickListener(playButtonListener);
			
			viewHolder.infoButton.setTag(slideshowInfo);
			viewHolder.infoButton.setOnClickListener(infoButtonListener);
			
			viewHolder.paintButton.setTag(slideshowInfo);
			viewHolder.paintButton.setOnClickListener(paintButtonListener);
			
			return convertView;
		}
	}
	
	
	OnClickListener playButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent playSlideshow = new Intent(AnimalList.this, AnimalPlayer.class);
			playSlideshow.putExtra( NAME_EXTRA, ((SlideshowInfo) v.getTag()).getName() );
			startActivity( playSlideshow );
		}
	};
	
	OnClickListener infoButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load intent with URI saved to the SlideshowInfo object
			Intent getURL = new Intent( Intent.ACTION_VIEW, Uri.parse(((SlideshowInfo) v.getTag()).getURI()));
			startActivity(getURL);
		}
	};
	
	OnClickListener paintButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// load paint, pass the animals name was extra
			Intent paintAnimal = new Intent(AnimalList.this, AnimalPaint.class);
			paintAnimal.putExtra( NAME_EXTRA, ((SlideshowInfo) v.getTag()).getName() );
			startActivity( paintAnimal );
		}
	};
	
	public static SlideshowInfo getSlideshowInfo( String name )
	{
		for( SlideshowInfo slideshowInfo : slideshowList )
			if( slideshowInfo.getName().equals(name))
				return slideshowInfo;
		
		return null;
	}
	
	
	private void makeSlideshowList( char animalLetter )
	{
		// Load array information to create a SlideshowInfo object
		String[] animalArray = getResources().getStringArray(R.array.animal_names);
		String[] URIArray = getResources().getStringArray(R.array.animal_uris);
		
		// Loop arrays, extract appropriate animal types
		for( int i = 0; i < animalArray.length; i++ ) {
			if( animalArray[i].startsWith( Character.toString(animalLetter) ) ) {
				slideshowList.add( new SlideshowInfo(animalArray[i].toString()) );
				slideshowList.get( slideshowList.size() - 1 ).setURI( URIArray[i].toString() );
				for( int j = 1; j < 5; j++ ) {
					// adds images to list
					slideshowList.get( slideshowList.size() - 1 ).addImage("edu.perlstein.animals:drawable/img_" + animalArray[i].toString().toLowerCase() + "_" + j);
					
				}
			}
		}
					
	}
	
}
