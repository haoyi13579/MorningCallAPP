package application;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainController implements Initializable {

	private Stage primaryStage;

	Alert alert = new Alert(AlertType.INFORMATION);
	EditController editcontroller = new EditController();

	@FXML
	public ComboBox<String> combobox;

	@FXML
	public DatePicker datepicker;

	@FXML
	public TextField minutefield;

	@FXML
	public TextField roomfield;

	@FXML
	public Button addbtn;

	@FXML
	public Button editbtn;

	@FXML
	public TableView<Calls> table;
	@FXML public TableColumn<Calls, String> date;
	@FXML public TableColumn<Calls, String> time;
	@FXML public TableColumn<Calls, String> room;

	@FXML
	public Button refreshbtn;


	public ObservableList<Calls> calllist = FXCollections.observableArrayList();

	public ObservableList<Calls> Call() throws IOException {

		ArrayList<String> list = new ArrayList<String>();
		Model model = new Model();
		model.delete();
		model.readcall();
		list = model.getCalls();
		int callnum = list.size()/3;
		for(int i=0;i<callnum;i++) {
			calllist.add(new Calls(list.get(3*i),list.get(3*i+1),list.get(3*i+2)));
		}
		return calllist;
	}






	ObservableList<String> hourlist = FXCollections.observableArrayList("0","1","2","3","4","5","6","7","8","9","10","11","12",
			"13","14","15","16","17","18","19","20","21","22","23");

// Initialize the app
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<Calls> list = null;
		try {
			list = Call();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		combobox.setItems(hourlist);





		date.setCellValueFactory(new PropertyValueFactory<Calls, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<Calls, String>("time"));
		room.setCellValueFactory(new PropertyValueFactory<Calls, String>("room"));
		table.setItems(list);

		Calls calls = new Calls(null, null, null);
		calls.dateProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				table.refresh();
			}
		});

		calls.timeProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				table.refresh();
			}
		});

		calls.roomProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				table.refresh();
			}
		});

	}


	public void add(ActionEvent event) {

		if(inputvalid()) {

			LocalDate localdate = datepicker.getValue();
			String date = localdate.toString();
			String hour = combobox.getValue();
			String minute = minutefield.getText();
			String room = roomfield.getText();
			if(Integer.valueOf(minute)<10) {
				minute = "0"+Integer.valueOf(minute);
			}
			String time = hour+":"+minute;

			try {
				Model model = new Model();
				model.writecall(date, time, room);
				calllist.add(new Calls(date,time,room));
				table.refresh();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void edit(ActionEvent event) throws IOException {
		Calls selectedcall = table.getSelectionModel().getSelectedItem();
		if(selectedcall != null) {

			try {
				// Load the fxml file and create a new stage for the popup dialog.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/application/Edit.fxml"));
				AnchorPane page = (AnchorPane) loader.load();

				// Create the dialog Stage.
				Stage stage = new Stage();
				stage.setTitle("Edit Calls");
				stage.initModality(Modality.WINDOW_MODAL);

				stage.initOwner(primaryStage);
				Scene scene = new Scene(page);
				stage.setScene(scene);

				// Set the person into the controller.
				EditController editcontroller = loader.getController();
				editcontroller.setcall(selectedcall);


				// Show the dialog and wait until the user closes it
				stage.showAndWait();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			alert.setTitle("No Selection");
			alert.setHeaderText(null);
			alert.setContentText("No call item is selected.");
			alert.showAndWait();

		}
	}




// Two try bun
	public void refreshbtn(ActionEvent event) throws IOException {




		calllist.clear();
		table.getItems().clear();
		Call();

//		Model model1 = new Model();
//		model1 .readcall();
//		ArrayList<String> list = model1.getCalls();
//		for(int i=0; i<list.size();i++) {
//
//			System.out.println(list.get(i));
//		}
//		int callnum = list.size()/3;
//		System.out.println(callnum);
//		table.getItems().clear();
//		calllist.clear();
//		for(int i=0;i<callnum;i++) {
//			calllist.add(new Calls(list.get(3*i),list.get(3*i+1),list.get(3*i+2)));
//		}


//		table.setItems(list);

//		table.refresh();




	}

	public void watchbtn(ActionEvent event) throws IOException, InterruptedException {
		calllist.clear();
		table.getItems().clear();
	}






// Valid input
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
