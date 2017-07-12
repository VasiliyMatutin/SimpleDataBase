package view;

import model.TableBook;
import view.ReadBox;
import view.MultipleReadBox;
import view.ConfirmBox;
import view.AlertBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import collections.CollectionType;
import hash.HashType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;

public class Controller { 

	private TableView<ArrayList<String>> table;
	private Main mainApp;
	private String current_file;
	private TableBook tables_list;
	private String current_table;
	private ObservableList<ArrayList<String>> unfiltered_table;
	private FilteredList<ArrayList<String>> filtered_data;

	public Controller(){
		current_file = "";
		current_table = "";
	}

	@FXML
	private AnchorPane table_edit;

	@FXML
	private Text table_name;

	@FXML
	private Text collection_name;

	@FXML
	private Text hash_name;

	@FXML
	private Menu table_change;

	@FXML
	private Menu tables_menu;

	@FXML
	private Menu edit_menu;

	@FXML
	private MenuItem save_button;

	@FXML
	private MenuItem close_button;

	@FXML
	private Button delete_button;

	@FXML
	private ComboBox<String> switch_search_column;

	@FXML
	private TextField search_field;
	
	@FXML
	private TextArea history;

	@FXML
	private void initialize() {
		table = new TableView<ArrayList<String>>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(true);
		unfiltered_table = table.getItems();

		switch_search_column.setOnAction(e -> {
			String curr_value = search_field.getText();
			search_field.clear();
			search_field.setText(curr_value);
		});
		
		filtered_data = new FilteredList<>(unfiltered_table, p -> true);
		search_field.textProperty().addListener((observable, oldValue, newValue) -> {
			if (switch_search_column.getValue().equals(tables_list.getColumnsName(current_table).get(0)) && newValue.isEmpty() == false){
				try{
					long start = System.nanoTime();
					tables_list.searchRowByKey(current_table, newValue);
					long time = System.nanoTime() - start;
					history.appendText("Time: " + time + "(ns) " + "Find node " + newValue + " in collection:" + collection_name.getText() + " with hashfunction:" + hash_name.getText() +  '\n');		
				}catch(RuntimeException ex){}
			}
			filtered_data.setPredicate(row -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				if (row.get(tables_list.getColumnNumber(current_table, switch_search_column.getValue())).toLowerCase().equals(newValue.toLowerCase())) {
					return true; 
				} 
				return false;
			});
		});
	}

	@FXML
	private void addNewTable(){
		while (true)
		{
			try{
				String table_name = ReadBox.display("Create new table", "Enter table name:");
				if (ReadBox.isClosedManually()==true){
					return;
				}
				if (table_name.isEmpty()==true){
					throw new RuntimeException("Empty field");
				}
				if (tables_list.isTableExist(table_name) == true){
					throw new RuntimeException("Table with such name already exists!");
				}
				String Key = ReadBox.display("Create key value", "Enter key name:");
				if (ReadBox.isClosedManually()==true){
					return;
				}
				if (table_name.isEmpty()==true){
					throw new RuntimeException("Empty field");
				}
				ArrayList<String> table_columns = MultipleReadBox.display("Add column", "Enter column name:", false, Key);
				if (MultipleReadBox.isClosedManually()==true){
					return;
				}
				if (table_columns.isEmpty()==true){
					throw new RuntimeException("No columns has been created!");
				}
				table_columns.set(0, Key + " (unique key)");
				tables_list.addNewTable(table_name, table_columns);
				current_table=table_name;
				collection_name.setText(tables_list.getCollectionName(current_table));
				hash_name.setText(tables_list.getHashName(current_table));
				this.table_name.setText(current_table);

				MenuItem menu_item = new MenuItem(table_name);
				menu_item.setOnAction(e -> switchTable(menu_item.getText()));
				table_change.getItems().add(menu_item);
				if (table_change.isDisable()==true)
				{
					if (table_change.getItems().size()>1){
						table_change.setDisable(false);
					}
					mainApp.getRootLayout().setRight(table_edit);

					if (table_change.getItems().size()>0){
						edit_menu.setDisable(false);
					}
					drawTable();
				}
				return;
			} catch (RuntimeException e){
				AlertBox.display("Error", e.getMessage());
			} catch (Exception e){
				AlertBox.display("Error", "Enable to create new table");
				return;
			}
		}
	}

	@FXML
	private void newMenuClicked(){
		if (current_file.isEmpty()==false){
			closeMenuClicked();
		}
		while (true)
		{
			String file_name = ReadBox.display("Create", "Enter filename without extension:");
			if (ReadBox.isClosedManually()==true){
				return;
			}
			try {
				if (file_name.isEmpty()) throw new NullPointerException();
				File file = new File(file_name + ".txt");
				if(file.exists() == true){
					if (ConfirmBox.display("Warning", "This file already exists! Sure you want to rewrite it?") == false || ConfirmBox.isCancelled()==true){
						return;
					}
					file.delete();
				}
				file.createNewFile();
				current_file=file_name + ".txt";
				tables_list= new TableBook();
				activateMenuButton();
				saveMenuClicked();
				return;
			} catch (NullPointerException e) {
				AlertBox.display("Error", "Empty field!");
			} catch (IOException | SecurityException e) {
				AlertBox.display("Error", "Unexpected error");
				return;
			}
		}
	}

	@FXML
	private void openMenuClicked(){
		if (current_file.isEmpty()==false){
			closeMenuClicked();
		}
		while (true)
		{
			String file_name = ReadBox.display("Open", "Enter filename without extension:");
			if (ReadBox.isClosedManually()==true){
				return;
			}
			try(
					FileInputStream fin = new FileInputStream(file_name + ".txt");
					ObjectInputStream fse = new ObjectInputStream(fin);
					) {
				tables_list = (TableBook)fse.readObject();
				current_file=file_name + ".txt";
				activateMenuButton();
				Set<String> table_names = tables_list.getTablesName();
				if (table_names.isEmpty()==false){
					fillTableSwitchMenu(table_names);
				}
				return;
			} catch (NullPointerException | SecurityException | ClassNotFoundException e) {
				AlertBox.display("Error", "Enable to read a file");
			} catch (FileNotFoundException e) {
				AlertBox.display("Error", "File '" + file_name + ".txt"+"' not found");
			} catch (IOException e) {
				AlertBox.display("Error", "Enable to open this file");
			} catch (Exception e) {
				AlertBox.display("Error", "File is corrupted");
				return;
			}
		}
	}

	@FXML
	private void saveMenuClicked(){
		try(
				FileOutputStream fot = new FileOutputStream(current_file);
				ObjectOutputStream fseo = new ObjectOutputStream(fot);
				) {
			fseo.writeObject(tables_list);
			AlertBox.display("Save", "File has been successfully written to the local disk");
		} catch (FileNotFoundException e) {
			try(
					FileOutputStream fot = new FileOutputStream(current_file);
					ObjectOutputStream fseo = new ObjectOutputStream(fot);
					) {
				File file = new File(current_file);
				file.createNewFile();
				fseo.writeObject(tables_list);
				fseo.close();
				AlertBox.display("Save", "File has been successfully written to the local disk");
			} catch (IOException e1) {
				AlertBox.display("Error", "File is corrupted");
				return;
			}
		} catch (IOException e) {
			AlertBox.display("Error", "File is corrupted");
			return;
		}
	}

	@FXML
	private void closeMenuClicked(){
		if (ConfirmBox.display("Close", "You want to save the file?") == true){
			if (ConfirmBox.isCancelled()==true){
				return;
			}
			saveMenuClicked();
		}
		deactivateMenuButton();
		current_file = "";
		tables_list = null;
		current_table = "";
		mainApp.getRootLayout().setCenter(null);
		mainApp.getRootLayout().setRight(null);
	}

	@FXML
	private void exitMenuClicked(){
		if (current_file.isEmpty()==false){
			if (ConfirmBox.display("Close", "You want to save the file?") == true){
				if (ConfirmBox.isCancelled()==true){
					return;
				}
				saveMenuClicked();
			}
			mainApp.getPrimaryStage().close();
		}
		else
		{
			if (ConfirmBox.display("Exit", "Sure you want to exit?") == true && ConfirmBox.isCancelled()==false){
				mainApp.getPrimaryStage().close();
			}
		}
	}

	@FXML
	private void deleteTableClicked(){
		if (ConfirmBox.display("Delete", "Sure you want to delete this table?") == true && ConfirmBox.isCancelled()==false){
			tables_list.deleteTable(current_table);
			MenuItem temp_menu_item = null;
			for (MenuItem menu_item : table_change.getItems()){
				if (menu_item.getText().equals(current_table)==true){
					temp_menu_item = menu_item;
				}
			}
			table_change.getItems().remove(temp_menu_item);
			Set<String> table_names = tables_list.getTablesName();
			if (table_names.isEmpty()==true){
				current_table="";
				mainApp.getRootLayout().setRight(null);
				edit_menu.setDisable(true);
			} else{
				if (table_names.size()==1){
					table_change.setDisable(true);
				}
				Iterator<String> iter = table_names.iterator();
				current_table = iter.next();
			}
			drawTable();
		}
	}

	@FXML
	private void addRow(){
		ArrayList<String> rows_value = new ArrayList<String>();
		ArrayList<String> columns_name = tables_list.getColumnsName(current_table);
		for (String column_name : columns_name){
			while(true){
				try{
					String value = ReadBox.display("Add value", "Enter the: " + column_name);
					if (ReadBox.isClosedManually()==true){
						return;
					}
					if (value.isEmpty()==true){
						throw new RuntimeException("Empty field");
					}
					rows_value.add(value);
					break;
				}
				catch(RuntimeException e){
					AlertBox.display("Error", e.getMessage());
				}
			}
		}
		try{
			long start = System.nanoTime();
			tables_list.addRowToTable(current_table, rows_value);
			long time = System.nanoTime() - start;
			history.appendText("Time: " + time + "(ns) " + "Add in collection:" + collection_name.getText() + " with hashfunction:" + hash_name.getText() +  '\n');
			unfiltered_table.add(rows_value);
			if (delete_button.isDisable()==true){
				delete_button.setDisable(false);
				search_field.setDisable(false);
			}
		}
		catch (RuntimeException ex){
			AlertBox.display("Error", ex.getMessage());
		}
		return;
	}

	@FXML
	private void deleteRow(){
		ObservableList<ArrayList<String>> rowSelected;
		rowSelected = table.getSelectionModel().getSelectedItems();
		for (ArrayList<String> row : rowSelected){
			tables_list.deleteRowFromTable(current_table, row);
		}
		rowSelected.forEach(unfiltered_table::remove);
		if (unfiltered_table.isEmpty()==true){
			delete_button.setDisable(true);
			search_field.setDisable(true);
		}
	}

	@FXML
	private void changeToSetCollection(){
		tables_list.changeCollection(current_table, CollectionType.SET, null);
		collection_name.setText(tables_list.getCollectionName(current_table));
	}

	@FXML
	private void changeToMapCollection(){
		tables_list.changeCollection(current_table, CollectionType.MAP, null);
		collection_name.setText(tables_list.getCollectionName(current_table));
	}

	@FXML
	private void changeToListCollection(){
		tables_list.changeCollection(current_table, CollectionType.LIST, null);
		collection_name.setText(tables_list.getCollectionName(current_table));
	}
	
	@FXML
	private void changeToStandartHash(){
		tables_list.changeHashInTable(current_table, HashType.STANDART);
		hash_name.setText(tables_list.getHashName(current_table));
	}

	@FXML
	private void changeToH37Hash(){
		tables_list.changeHashInTable(current_table, HashType.H37);
		hash_name.setText(tables_list.getHashName(current_table));
	}

	@FXML
	private void changeToLyHash(){
		tables_list.changeHashInTable(current_table, HashType.LY);
		hash_name.setText(tables_list.getHashName(current_table));
	}
	
	@FXML
	private void changeToRsHash(){
		tables_list.changeHashInTable(current_table, HashType.RS);
		hash_name.setText(tables_list.getHashName(current_table));
	}

	private void switchTable(String new_table){
		if (new_table.equals(current_table)){
			return;
		}
		current_table=new_table;
		collection_name.setText(tables_list.getCollectionName(current_table));
		hash_name.setText(tables_list.getHashName(current_table));
		drawTable();
	}

	private void drawTable(){
		delete_button.setDisable(true);
		search_field.setDisable(true);
		search_field.clear();
		switch_search_column.getItems().clear();
		unfiltered_table.clear();
		for (int i = 0; i<table.getColumns().size(); i++){
			table.getColumns().clear();
		}
		for (int i = 0; i<table.getItems().size(); i++){
			table.getItems().clear();
		}
		ArrayList<String> columns_name = tables_list.getColumnsName(current_table);
		if (columns_name.isEmpty()==true){
			mainApp.getRootLayout().setCenter(null);
			return;
		}

		for (int i=0; i<columns_name.size(); i++){
			TableColumn<ArrayList<String>, String> column = new TableColumn<ArrayList<String>, String>(columns_name.get(i));
			int ind=i;
			column.setCellValueFactory(row -> new SimpleStringProperty(row.getValue().get(ind)));
			table.getColumns().add(column);
			column.setCellFactory(TextFieldTableCell.<ArrayList<String>>forTableColumn());
			column.setOnEditCommit(
					(CellEditEvent<ArrayList<String>, String> t) -> {
						if (t.getTablePosition().getTableColumn().getText().equals(columns_name.get(0))){
							try{
								String old_key = t.getTableView().getItems().get(t.getTablePosition().getRow()).get(ind);
								tables_list.editKeyInTable(current_table, old_key, t.getNewValue());
								t.getTableView().getItems().get(t.getTablePosition().getRow()).set(ind, t.getNewValue());
							}catch(RuntimeException ex){
								AlertBox.display("Error", ex.getMessage());
								table.refresh();
							}
						}
						else{
							t.getTableView().getItems().get(t.getTablePosition().getRow()).set(ind, t.getNewValue());
							tables_list.editRowInTable(current_table, t.getTableView().getItems().get(t.getTablePosition().getRow()));
						}
					});
			switch_search_column.getItems().add(columns_name.get(ind));
		}
		switch_search_column.setValue(columns_name.get(0));
		ArrayList<ArrayList<String>> values = tables_list.getCurrentTableValue(current_table);		
		for (ArrayList<String> row : values){
			unfiltered_table.add(row);
		}
		
		table.setItems(filtered_data);

		if (values.isEmpty()==false){
			delete_button.setDisable(false);
			search_field.setDisable(false);
		}
		mainApp.getRootLayout().setCenter(table);
		table_name.setText(current_table);
	}

	private void fillTableSwitchMenu(Set<String> table_names){
		for (String table_name : table_names)
		{
			MenuItem menu_item = new MenuItem(table_name);
			menu_item.setOnAction(e -> switchTable(menu_item.getText()));
			table_change.getItems().add(menu_item);
		}
		if (table_names.size()>1){
			table_change.setDisable(false);
		}
		if (table_names.size()>0){
			edit_menu.setDisable(false);
		}
		Iterator<String> iter = table_names.iterator();
		current_table = iter.next();
		mainApp.getRootLayout().setRight(table_edit);
		collection_name.setText(tables_list.getCollectionName(current_table));
		hash_name.setText(tables_list.getHashName(current_table));
		drawTable();
	}

	private void activateMenuButton(){
		save_button.setDisable(false);
		close_button.setDisable(false);
		tables_menu.setDisable(false);
	}

	private void deactivateMenuButton(){
		save_button.setDisable(true);
		close_button.setDisable(true);
		tables_menu.setDisable(true);
		edit_menu.setDisable(true);
		table_change.getItems().clear();
		table_change.setDisable(true);
	}

	public void exit(){
		exitMenuClicked();
	}

	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void autoFill(){
		for (Integer i=0; i < 10000; i++){
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(i.toString());
			temp.add("def");
			temp.add("def");
			tables_list.addRowToTable(current_table, temp);
		}
	}
}