package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Calls {

	private final SimpleStringProperty date;
	private final SimpleStringProperty time;
	private final SimpleStringProperty room;

	public Calls(String date, String time, String room) {
		super();
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.room = new SimpleStringProperty(room);
	}

	public String getDate() {
		return date.get();
	}

	public void setDate(String date) {
		this.date.set(date);;
	}

	public StringProperty dateProperty() {
		return date;
	}

	public String getTime() {
		return time.get();
	}

	public void setTime(String time) {
		this.time.set(time);;
	}

	public StringProperty timeProperty() {
		return time;
	}

	public String getRoom() {
		return room.get();
	}

	public void setRoom(String room) {
		this.room.set(room);;
	}

	public StringProperty roomProperty() {
		return room;
	}






}
