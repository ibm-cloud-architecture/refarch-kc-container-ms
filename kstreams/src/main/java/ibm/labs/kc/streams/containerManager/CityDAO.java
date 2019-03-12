package ibm.labs.kc.streams.containerManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CityDAO {
	private Map<String,Location[]> cities;
	
	public CityDAO() {
		cities = new ConcurrentHashMap<String,Location[]>();
		this.populateCities();
	}
	
	
	public void populateCities() {
		Location[] rect = new Location[2];
		rect[0] = new Location(37.82932,-122.33771);
		rect[1] = new Location(37.76529,-122.24024);
		cities.put("Oakland", rect);
	}
	
	public String getCity(double la, double lo) {
		// TODO Auto-generated method stub
		
		for (String c: this.cities.keySet()) {
			if (within(la,lo,cities.get(c))) {
				return c;
			}
		}
		return null;
	}
	
	private boolean within(double la, double lo,Location[] rect) {
		return ( ( la >= rect[1].latitude && la <= rect[0].latitude ) 
				 && (lo <= rect[1].longitude && lo >= rect[0].longitude));
	}
	
	public class Location {
		protected double latitude;
		protected double longitude;
		
		public Location(double la, double lo) {
			this.latitude = la;
			this.longitude = lo;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		@Override
		public boolean equals(Object o) {
			Location p = (Location)o;
			return (this.getLatitude() == p.getLatitude() && this.getLongitude() == p.getLongitude());
			
		}
	}


}