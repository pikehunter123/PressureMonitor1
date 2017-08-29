package my.android;

import java.util.Date;

public class ResultP {
	private int id;
	private Date date;
	private int press;
	public ResultP(int id, Date date, int press, int temp) {
		super();
		this.id = id;
		this.date = date;
		this.press = press;
		this.temp = temp;
	}
	public int getId() {
		return id;
	}
	public Date getDate() {
		return date;
	}
	public int getPress() {
		return press;
	}
	public int getTemp() {
		return temp;
	}
	private int temp;
	

}
