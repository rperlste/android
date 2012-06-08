package edu.perlstein.arbor;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ResultsView extends ListActivity {
public static final String NAME_EXTRA = "NAME";
	
	static List<TreeInfo> treeList;
	private ListView treeListView;
	private treeAdapter treeAdapter;
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		treeListView = getListView();
		
		treeList = new ArrayList<TreeInfo>();
		// Load tree list with results
		maketreeList( getIntent().getExtras().getStringArray("trees") );

		treeAdapter = new treeAdapter( this, treeList );
		treeListView.setAdapter( treeAdapter );
		
	}
	
	public static class ViewHolder
	{
		TextView nameTextView;
		ImageView imageView;
		Button imageButton;
		Button infoButton;
	}
	
	private class treeAdapter extends ArrayAdapter< TreeInfo >
	{
		private List< TreeInfo > items;
		private LayoutInflater inflater;
		
		public treeAdapter( Context context, List< TreeInfo > items )
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
				convertView = inflater.inflate( R.layout.tree_list_item, null);
				
				viewHolder = new ViewHolder();
				viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.slideshowImageView);
				viewHolder.imageButton = (Button) convertView.findViewById(R.id.imageButton);
				viewHolder.infoButton = (Button) convertView.findViewById(R.id.infoButton);
				
				convertView.setTag( viewHolder );
			}
			else
				viewHolder = (ViewHolder) convertView.getTag();
			
			TreeInfo TreeInfo = items.get(position);
			viewHolder.nameTextView.setText( TreeInfo.getName() );
			

			// Parse tree image location information
			String tempTreeStr = TreeInfo.getName();
			tempTreeStr = tempTreeStr.toLowerCase();
			tempTreeStr = tempTreeStr.replace( ' ', '_');
			tempTreeStr = tempTreeStr.replace( '-', '_');
		
			// Load thumbnail to image
			viewHolder.imageView.setImageResource( getResources().getIdentifier("edu.perlstein.arbor:drawable/img_" + tempTreeStr, null, null ) );
			viewHolder.imageView.setTag(TreeInfo);
			
			viewHolder.imageButton.setTag(TreeInfo);
			viewHolder.imageButton.setOnClickListener(imageButtonListener);
			
			viewHolder.infoButton.setTag(TreeInfo);
			viewHolder.infoButton.setOnClickListener(infoButtonListener);
			
			return convertView;
		}
	}

	
	OnClickListener imageButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Load intent with URI saved to the TreeInfo object
			Intent getURL = new Intent( Intent.ACTION_VIEW, Uri.parse(((TreeInfo) v.getTag()).getgoogle_uri()));
			startActivity(getURL);
		}
	};
	
	OnClickListener infoButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent( ResultsView.this, InfoView.class);
			intent.putExtra( "name", ( (TreeInfo) v.getTag()).getName() );
			intent.putExtra( "img", ( (TreeInfo) v.getTag()).getImageID());
			v.getContext().startActivity(intent);
		}
	};
	
	
	public static TreeInfo getTreeInfo( String name )
	{
		for( TreeInfo TreeInfo : treeList )
			if( TreeInfo.getName().equals(name))
				return TreeInfo;
		
		return null;
	}
	
	
	private void maketreeList( String[] treesInput )
	{
		String[] treeSourceArr = getResources().getStringArray(R.array.tree_names);
		
		for( int i = 0; i < treesInput.length; i++ ) {
			for( int j = 0; j < treeSourceArr.length; j++ ) {
				if( treesInput[i].equals(treeSourceArr[j].toString()) ){
					// Add tree and image uri if in the list
					treeList.add( new TreeInfo(treesInput[i].toString()));
					treeList.get(i).setgoogle_uri( getResources().getStringArray(R.array.tree_uris)[j].toString() );
				}
			}
		}
		
	}
}
