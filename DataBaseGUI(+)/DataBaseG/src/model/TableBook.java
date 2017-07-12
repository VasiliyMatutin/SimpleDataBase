package model;

import java.io.Serializable;

import collections.CollectionType;
import collections.HashMapTable;
import collections.ListTable;
import collections.SetTable;
import collections.Table;
import hash.HashEclipseGenerated;
import hash.HashFunction;
import hash.HashH37;
import hash.HashLy;
import hash.HashRs;
import hash.HashType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class TableBook implements Serializable {
	private static final long serialVersionUID = 7698994763840619598L;
	private LinkedHashMap<String, TableInfo> table_list = new LinkedHashMap<String, TableInfo>();

	public TableBook(){
	}

	public void addNewTable(String table_name, ArrayList<String> new_columns_name){
		HashFunction hf = new HashEclipseGenerated();
		Table new_table = new ListTable(hf);
		TableInfo new_table_sheet = new TableInfo(new_columns_name, new_table);
		table_list.put(table_name, new_table_sheet);
	}

	public ArrayList<String> getColumnsName(String table_name){
		if (table_list.isEmpty()==true){
			return new ArrayList<String>();
		}
		return table_list.get(table_name).getColumnsName();
	}

	public ArrayList<ArrayList<String>> getCurrentTableValue(String table_name){
		return table_list.get(table_name).getTableValue().getTable();
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

	/*public void setCurrentTableValue(ArrayList<ArrayList<String>> new_table_set, String table_name){
		table_list.get(table_name).getValue().fillTable(new_table_set);
	}*/

	public void deleteTable(String table_name){
		table_list.remove(table_name);
	}

	public int getColumnNumber(String table_name, String column_name){
		ArrayList<String> name = table_list.get(table_name).getColumnsName();
		for (int i=0; i<name.size(); i++){
			if (name.get(i)==column_name){
				return i;
			}
		}
		return 0;
	}

	public void addRowToTable(String table_name, ArrayList<String> row){
		String key = new String(row.get(0));
		ArrayList<String> new_row = new ArrayList<String>(row);
		new_row.remove(0);
		table_list.get(table_name).getTableValue().add(key, new_row);
	}

	public void deleteRowFromTable(String table_name, ArrayList<String> row){
		String key = new String(row.get(0));
		table_list.get(table_name).getTableValue().delete(key);
	}

	public void editRowInTable(String table_name, ArrayList<String> row){
		String key = new String(row.get(0));
		ArrayList<String> new_row = new ArrayList<String>(row);
		new_row.remove(0);
		table_list.get(table_name).getTableValue().edit(key, new_row);
	}

	public void editKeyInTable(String table_name, String old_key, String new_key){
		table_list.get(table_name).getTableValue().changeKey(old_key, new_key);
	}

	public ArrayList<String> searchRowByKey(String table_name, String key){
		return table_list.get(table_name).getTableValue().search(key);		
	}

	public void changeCollection(String table_name, CollectionType ctype, HashFunction htype){
		if (table_list.get(table_name).getTableValue().getCollectionType() == ctype){
			return;
		} else{
			if (ctype==null){
				ctype=table_list.get(table_name).getTableValue().getCollectionType();
			}
			if (htype==null){
				htype = table_list.get(table_name).getTableValue().getHashFunction();
			}
			ArrayList<ArrayList<String>> temp_storage = new ArrayList<ArrayList<String>>(table_list.get(table_name).getTableValue().getTable());
			switch (ctype){
			case LIST:
				table_list.get(table_name).setTableValue(new ListTable(htype));
				break;
			case MAP:
				table_list.get(table_name).setTableValue(new HashMapTable(htype));
				break;
			case SET:
				table_list.get(table_name).setTableValue(new SetTable(htype));
				break;
			default:
				break;
			}
			table_list.get(table_name).getTableValue().fillTable(temp_storage);
		}
	}

	public String getCollectionName(String table_name){
		switch (table_list.get(table_name).getTableValue().getCollectionType()){
		case LIST:
			return "LinkedList";
		case MAP:
			return "LinkedHashMap";
		case SET:
			return "LinkedHashSet";
		default:
			break;
		}
		return null;
	}

	public void changeHashInTable(String table_name, HashType type){
		if (table_list.get(table_name).getTableValue().getHashFunction().getHashType() == type){
			return;
		} else{
			switch (type){
			case H37:
				this.changeCollection(table_name, null, new HashH37());
				break;
			case LY:
				this.changeCollection(table_name, null, new HashLy());
				break;
			case RS:
				this.changeCollection(table_name, null, new HashRs());
				break;
			case STANDART:
				this.changeCollection(table_name, null, new HashEclipseGenerated());
				break;
			default:
				break;
			}
		}
	}
	
	public String getHashName(String table_name){
		switch (table_list.get(table_name).getTableValue().getHashFunction().getHashType()){
		case H37:
			return "H37";
		case LY:
			return "LY";
		case RS:
			return "RS";
		case STANDART:
			return "EclipseGenerated";
		default:
			break;
		
		}
		return null;
	}
}