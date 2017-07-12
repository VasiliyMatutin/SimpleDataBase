package model;

import java.io.Serializable;
import java.util.ArrayList;

import collections.Table;

public class TableInfo implements Serializable {
	private static final long serialVersionUID = -6912095072459000404L;
	private ArrayList<String> columns_name;
	private Table table_value;
	
	public TableInfo(ArrayList<String> _columns_name, Table _table_value){
		columns_name=_columns_name;
		table_value=_table_value;
	}

	public ArrayList<String> getColumnsName() {
		return columns_name;
	}
	public void setColumnsName(ArrayList<String> columns_name) {
		this.columns_name = columns_name;
	}
	public Table getTableValue() {
		return table_value;
	}
	public void setTableValue(Table table_value) {
		this.table_value = table_value;
	}
}
