package com.mobyadvert.money;

public class PointObject {
	public static final String TAG = PointObject.class.getSimpleName();
	
	public String offer_name;
	public String create_date;
	public String point;
	
	public PointObject(String offer_name, String point, String create_date) {
		this.offer_name = offer_name;
		this.point = point;
		this.create_date = create_date;
	}
}
