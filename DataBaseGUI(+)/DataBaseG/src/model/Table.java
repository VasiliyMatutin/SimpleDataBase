package model;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Table implements Serializable {
	private static final long serialVersionUID = 7698994763840619598L;
	private HashMap<String, AbstractMap.SimpleEntry<ArrayList<String>, ArrayList<ArrayList<String>>>> table_list = new HashMap<String, AbstractMap.SimpleEntry<ArrayList<String>, ArrayList<ArrayList<String>>>>();

	public Table(){
	}
	
	public void addNewTable(String table_name, ArrayList<String> new_columns_name){
		ArrayList<ArrayList<String>> new_table_set = new ArrayList<ArrayList<String>>();
		AbstractMap.SimpleEntry<ArrayList<String>, ArrayList<ArrayList<String>>> new_table = new AbstractMap.SimpleEntry<ArrayList<String>, ArrayList<ArrayList<String>>>(new_columns_name, new_table_set);
		table_list.put(table_name, new_table);
	}
	
	public ArrayList<String> getColumnsName(String table_name){
		if (table_list.isEmpty()==true){
			return new ArrayList<String>();
		}
		return table_list.get(table_name).getKey();
	}
	
	public ArrayList<ArrayList<String>> getCurrentTableValue(String table_name){
		return table_list.get(table_name).getValue();
	}
	
	public Boolean isTableExist(String table_name){
		if (table_list.containsKey(table_name)==true){
			return true;
		}
		return false;
	}
	
	public Set<String> getTablesName(){
		return table_list.keySet();
	}
	
	public void setCurrentTableValue(ArrayList<ArrayList<String>> new_table_set, String table_name){
		table_list.get(table_name).setValue(new_table_set);
	}
	
	public void deleteTable(String table_name){
		table_list.remove(table_name);
	}
	
	public int getColumnNumber(String table_name, String column_name){
		ArrayList<String> name = table_list.get(table_name).getKey();
		for (int i=0; i<name.size(); i++){
			if (name.get(i)==column_name){
				return i;
			}
		}
		return 0;
	}
}
