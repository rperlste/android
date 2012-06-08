package edu.perlstein.arbor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoView extends Activity {
	
	private ImageView imageView;
	private TextView textView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_view);
		
		// Parse data
		String tempTreeStr = getIntent().getExtras().getString("name");
		tempTreeStr = tempTreeStr.toLowerCase();
		tempTreeStr = tempTreeStr.replace( ' ', '_');
		tempTreeStr = tempTreeStr.replace( '-', '_');
	
		// Load thumbnail to image
		imageView = (ImageView) findViewById(R.id.infoImageView);
		imageView.setImageResource( getResources().getIdentifier("edu.perlstein.arbor:drawable/img_" + tempTreeStr, null, null ) );
		
		// Determine correct info index
		String[] strA = getResources().getStringArray(R.array.tree_names);
		int index = 0;
		for( int i = 0; i < strA.length; i++ ){
			if( strA[i].equals(getIntent().getExtras().getString("name"))){
				index = i;
				break;
			}
		}
			
		// Load info
		textView = (TextView) findViewById(R.id.infoTextView);
		textView.setText( getResources().getStringArray(R.array.tree_info)[index] );
	}

}
