package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditController {

	Alert alert = new Alert(AlertType.INFORMATION);
	Model model = new Model();
	String originaldate;
	String originaltime;
	String originalroom;

	@FXML
	public ComboBox<String> combobox;

	@FXML
	public DatePicker datepicker;

	@FXML
	public TextField minutefield;

	@FXML
	public TextField roomfield;

	@FXML
	public Button okbtn;

	@FXML
	public Button cancelbtn;


	ObservableList<String> hourlist = FXCollections.observableArrayList("0","1","2","3","4","5","6","7","8","9","10","11","12",
			"13","14","15","16","17","18","19","20","21","22","23");

	private Calls call;

    @FXML
    private void initialize() {
    	combobox.setItems(hourlist);


    }

    public void setcall(Calls call) {
    	this.call = call;
        String time = call.getTime();
        String[] temp = time.split(":");
        String hour = temp[0];
        String minute = temp[1];
        String room = call.getRoom();
        String date = call.getDate();
        LocalDate dateformat = LocalDate.parse(date);
        originaldate = date;
        originaltime = time;
        originalroom = room;

        combobox.setValue(hour);
        roomfield.setText(room);
        minutefield.setText(minute);
        datepicker.setValue(dateformat);

    }


    public void eidtOK(ActionEvent event) {

    	if(inputvalid()) {

    		String minute = minutefield.getText();
    		String hour = combobox.getValue();
    		if(Integer.valueOf(minute)<10) {
				minute = "0"+Integer.valueOf(minute);
			}
			String time = hour+":"+minute;
			String date = datepicker.getValue().toString();
			String room = roomfield.getText();

			call.setDate(date);
    		call.setTime(time);
    		call.setRoom(room);

    		String oldcall = originaldate+","+originaltime+","+originalroom;
    		String newcall = date+","+time+","+room;

			try {
				model.modifycsv(oldcall, newcall);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}

    	Stage stage = (Stage) okbtn.getScene().getWindow();
    	stage.close();

    }

    public void editcancel(ActionEvent event) {
    	Stage stage = (Stage) okbtn.getScene().getWindow();
    	stage.close();
    }

	public boolean inputvalid() {
		String errormessage = "";
		ChronoLocalDate datenow = LocalDate.now();
		String min = minutefield.getText();
		String room = roomfield.getText();
		int minint = -1;

		try{
			minint = Integer.valueOf(min);
			if(minint<0 || minint>59) {
				errormessage += "No valid minute!\n";
			}

		} catch(Exception e) {
			e.printStackTrace();
			errormessage += "No valid minute!\n";
		}

		if(datepicker.getValue() == null || datepicker.getValue().isBefore(datenow)){
			errormessage += "No valid date!\n";
		}
		if(combobox.getValue() == null) {
			errormessage += "No valid hour!\n";
		}

		if(room == null) {
			errormessage += "No valid room!\n";
		} else {
			try {
				int roomint = Integer.valueOf(room);
			} catch(Exception e) {
				e.printStackTrace();
				errormessage += "No valid room!\n";
			}
		}

        if (errormessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
			alert.setTitle("Invalid Fields");
			alert.setHeaderText(null);
			alert.setContentText(errormessage);
			alert.showAndWait();
            return false;
        }
	}


















}
