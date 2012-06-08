package edu.perlstein.arbor;

import java.util.ArrayList;
import java.util.List;

public class TreeInfo {
	
	// Name
	private String name;
	// Bark
	private String bark_color;
	private String bark_texture;
	private String bark_thickness;
	// Leaves
	private String leaves_color;
	private String leaves_texture;
	private String leaves_length;
	// Fruit
	private String fruit_shape;
	private String fruit_color;
	private String fruit_length;
	private String fruit_seeds;
	// Elevation
	private String elevation_max;
	private String elevation_min;
	// Height
	private String height_max;
	private String height_min;
	// Habitat
	private String habitat_region;
	private String habitat_temperature;
	// Fire
	private String fire_susceptibility;
	
	// Data
	private String google_uri;
	private int imageID;
	
	public TreeInfo( String treeName ) {
		name = treeName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getBarkColor()
	{
		return bark_color;
	}
	
	public String getBarkTexture()
	{
		return bark_texture;
	}
	
	public String getBarkThickness()
	{
		return bark_thickness;
	}
	
	public String getLeavesTexture()
	{
		return leaves_texture;
	}
	
	public String getLeavesLength()
	{
		return leaves_length;
	}
	
	public String getLeavesColor()
	{
		return leaves_color;
	}
	
	public String getFruitShape()
	{
		return fruit_shape;
	}
	
	public String getFruitColor()
	{
		return fruit_color;
	}
	
	public String getFruitLength()
	{
		return fruit_length;
	}
	
	public String getFruitSeeds()
	{
		return fruit_seeds;
	}
	
	public String getElevationMax()
	{
		return elevation_max;
	}
	
	public String getElevationMin()
	{
		return elevation_min;
	}
	
	public String getHeightMax()
	{
		return height_max;
	}
	
	public String getHeightMin()
	{
		return height_min;
	}
	
	public String getHabitatRegion()
	{
		return habitat_region;
	}
	
	public String getHabitatTemperature()
	{
		return habitat_temperature;
	}
	
	public String getFireSusceptability()
	{
		return fire_susceptibility;
	}
	
	public String getgoogle_uri()
	{
		return google_uri;
	}
	
	public int getImageID()
	{
		return imageID;
	}
	
	public void setBarkColor( String color )
	{
		bark_color = color;
	}
	
	public void setBarkTexture( String texture )
	{
		bark_texture = texture;
	}
	
	public void setBarkThickness( String thickness )
	{
		bark_thickness = thickness;
	}
	
	public void setLeavesTexture( String texture )
	{
		leaves_texture = texture;
	}
	
	public void setLeavesLength( String length )
	{
		leaves_length = length;
	}
	
	public void setLeavesColor( String color)
	{
		leaves_color = color;
	}
	
	public void setFruitShape( String shape )
	{
		fruit_shape = shape;
	}
	
	public void setFruitColor( String color )
	{
		fruit_color = color;
	}
	
	public void setFruitLength( String length )
	{
		fruit_length = length;
	}
	
	public void setFruitSeeds( String seeds )
	{
		fruit_seeds = seeds;
	}
	
	public void setElevationMax( String max )
	{
		elevation_max = max;
	}
	
	public void setElevationMin( String min )
	{
		elevation_min = min;
	}
	
	public void setHeightMax( String max )
	{
		height_max = max;
	}
	
	public void setHeightMin( String min )
	{
		height_min = min;
	}
	
	public void setHabitatRegion( String region )
	{
		habitat_region = region;
	}
	
	public void setHabitatTemperature( String temp)
	{
		habitat_temperature = temp;
	}
	
	public void setFireSusceptability( String fire )
	{
		fire_susceptibility = fire;
	}
	
	public void setgoogle_uri( String address )
	{
		google_uri = address;
	}
	
	public void setImageID( int img )
	{
		imageID = img;
	}


}
