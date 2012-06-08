package edu.perlstein.animals;

import java.util.ArrayList;
import java.util.List;

public class SlideshowInfo {
	
	private String name;
	private String uri;
	private List<String> imageList;
	
	public SlideshowInfo( String slideshowName ) {
		name = slideshowName;
		imageList = new ArrayList<String>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<String> getImageList()
	{
		return imageList;
	}
	
	public String getURI()
	{
		return uri;
	}
	
	public void addImage(String path) 
	{
		imageList.add(path);
	}
	
	public void setURI( String address )
	{
		uri = address;
	}
	
	public String getImageAt( int index )
	{
		if( index >= 0 && index < imageList.size() )
			return imageList.get( index );
		else
			return null;
	}
	
	public void clearImageList()
	{
		imageList.clear();
	}
	
	public int size()
	{
		return imageList.size();
	}

}
