package collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import hash.HashFunction;

public class SetTable implements Serializable, Table{

	private static final long serialVersionUID = -3294926358308885160L;
	private LinkedHashSet<Row> table_list = new LinkedHashSet<Row>();
	private HashFunction hf;
	
	public SetTable(){}
	
	public SetTable(HashFunction _hf){
		hf = _hf;
	}

	@Override
	public void add(String key, ArrayList<String> new_row) {
		Row cont = new Row(key, new_row, hf);
		if (table_list.contains(cont) == true){
			throw new RuntimeException("This key already exist!");
		}
		table_list.add(cont);	
	}

	@Override
	public ArrayList<String> search(String key) {
		Row cont = new Row(key, null, hf);
		for (Row it : table_list){
			if (it.equals(cont)){
				return it.getValue();
			}
		}
		throw new RuntimeException("This key doesn't exist!");
	}

	@Override
	public ArrayList<ArrayList<String>> getTable() {
		ArrayList<ArrayList<String>> ret_table = new ArrayList<ArrayList<String>>();
		for (Row it : table_list){
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(it.getKey());
			temp.addAll(it.getValue());
			ret_table.add(temp);
		}
		return ret_table;
	}

	@Override
	public void fillTable(ArrayList<ArrayList<String>> table_value) {
		table_list.clear();
		for (ArrayList<String> single_row : table_value){
			String key = single_row.get(0);
			ArrayList<String> temp_row =  new ArrayList<String>(single_row);
			temp_row.remove(0);
			Row temp = new Row(key, temp_row, hf);
			table_list.add(temp);
		}
	}

	@Override
	public void delete(String key) {
		Row cont = new Row(key, null, hf);
		table_list.remove(cont);
	}

	@Override
	public void edit(String key, ArrayList<String> new_row) {
		Row cont = new Row(key, null, hf);
		for (Row it : table_list){
			if (it.equals(cont)){
				it.setValue(new_row);;
			}
		}	
	}

	@Override
	public CollectionType getCollectionType() {
		return CollectionType.SET;
	}

	@Override
	public void changeKey(String old_key, String new_key) {
		Row cont = new Row(new_key, null, hf);
		for (Row it : table_list){
			if (it.equals(cont)){
				throw new RuntimeException("This key already exist!");
			}
		}
		Row cont2 = new Row(old_key, null, hf);
		for (Row it : table_list){
			if (it.equals(cont2)){
				it.setKey(new_key);
			}
		}
	}

	@Override
	public HashFunction getHashFunction() {
		return hf;
	}
}
