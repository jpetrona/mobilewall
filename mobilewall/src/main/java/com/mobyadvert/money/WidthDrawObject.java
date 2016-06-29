package com.mobyadvert.money;

public class WidthDrawObject {
	public static final String TAG = WidthDrawObject.class.getSimpleName();
	
	public String account;
	public String widthdrawDate;
	public String point;
	public String status;
	public String payment;
	
	public WidthDrawObject(String account, String widthdrawDate, 
			String point, String status,String payment) {
		this.account = account;
		this.widthdrawDate = widthdrawDate;
		this.point = point;
		this.status = status;
		this.payment = payment;
	}
}
