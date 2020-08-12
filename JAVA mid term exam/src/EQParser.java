
import java.awt.Color;
import java.awt.image.ColorModel;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class EQParser implements Comparable<EQParser> {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	//public Date dateTime;
	public String dateTime;
	public double magnitude;
	public double latitude;
	public double longitude;
	public String location;
	public String link;
	public Color magnitudeColor;


	public EQParser() {
		this("", 0, 0, 0, "");
		
		BufferedReader br = null;
		String line;
		
		String path = "C:\\Users\\dkxmp\\Documents\\Java project\\2016037069 한재윤 JAVA 중간고사 대체과제\\EQdata.csv";
		

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			br.readLine();
			//br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null) {
				int i=0;
				String[] temp = line.split(","); // 쉼표로 구분
				//String[] location = null;

				String storeT = temp[0];
				String storeM = temp[1];
				String storeN = temp[2];
				String storeE = temp[3];
				String storeC = temp[4];
				
				String [] storeTime = storeT.split("");
				String [] storeMagnitude = storeM.split("");
				String [] storeNorth = storeN.split("N");
				String [] storeEast = storeE.split("E");
				String [] storeCity = storeC.split("");
				
				
				double m1 = Double.valueOf(storeMagnitude[i]);
				double x1 = Double.valueOf(storeNorth[i]);
				double y1 = Double.valueOf(storeEast[i]);
				
						
				this.dateTime = storeT;
		        this.magnitude = m1;
		        this.latitude = x1;
		        this.longitude = y1;
		        this.location = storeC;
		        
				i = i+1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public EQParser(String dateTime, double magnitude, double latitude, double longitude, String location) {
        this.dateTime = dateTime;
        this.magnitude = magnitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.magnitudeColor = computeARGBColor();

	}
	
    // compute color based on magnitude
    public Color computeARGBColor() {
        int r = 0, g = 0, b = 0;
        if (this.magnitude <= 1.5)
        {
            // BLUE
            r = 0; g = 0; b = 255;
        }
        else if ((this.magnitude > 1.5) && (this.magnitude <= 2.75))
        {
            // BLUE->CYAN
            r = 0; g = (int)(255 * (this.magnitude - 1.5) / 1.25); b = 255;
        }
        else if ((this.magnitude > 2.75) && (this.magnitude <= 4.0))
        {
            // CYAN->GREEN
            r = 0; g = 255; b = 255 - (int)(255 * (this.magnitude - 2.75) / 1.25);
        }
        else if ((this.magnitude > 4.0) && (this.magnitude <= 5.25))
        {
            // GREEN->YELLOW
            r = (int)(255 * (this.magnitude - 4.0) / 1.25); g = 255; b = 0;
        }
        else if ((this.magnitude > 5.25) && (this.magnitude <= 6.5))
        {
            // YELLOW->RED
            r = 255; g = 255 - (int)(255 * (this.magnitude - 5.25) / 1.25); b = 0;
        }
        else if (this.magnitude > 6.5)
        {
            // RED
            r = 255; g = 0; b = 0;
        }
        return new Color(r, g, b);
    }
    
    @Override
    public String toString() {
    	return "" + this.dateTime + "4545454545454" + this.magnitude + "," + this.latitude + "," + this.longitude + "," + this.location;
    }
    

    
    @Override
    public boolean equals(Object other)                             // Object.equals overriding
    {
    	if (other instanceof EQParser) {
    		EQParser that = (EQParser) other;
    		return that.canEqual(this) 
    				&& this.dateTime.equals(that.dateTime) 
    				&& this.magnitude == that.magnitude
    				&& this.latitude == that.latitude
    				&& this.longitude == that.longitude
    				&& this.location == that.location;
    	}
    	return false;
    }

    @Override
    public int hashCode() {
    	return (dateTime.hashCode() + Double.hashCode(magnitude) + Double.hashCode(latitude) + Double.hashCode(longitude) + location.hashCode());
    }

    public boolean canEqual(Object other) {
    	return (other instanceof EQParser);
    }
    
    public int compareTo(EQParser other) {
    	return dateTime.compareTo(other.dateTime);
    }

}