package collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import hash.HashFunction;

public class HashMapTable implements Serializable, Table {
	private static final long serialVersionUID = -3674661481196296569L;
	private LinkedHashMap<HashRowKey, ArrayList<String>> table_list = new LinkedHashMap<HashRowKey, ArrayList<String>>();
	private HashFunction hf;
	
	public HashMapTable(){}

	public HashMapTable(HashFunction _hf){
		hf = _hf;
	}
	
	@Override
	public void add(String key, ArrayList<String> new_row){
		HashRowKey cont_key = new HashRowKey(key, hf);
		if (table_list.containsKey(cont_key) == true){
			throw new RuntimeException("This key already exist!");
		}
		table_list.put(cont_key, new_row);
	}
	
	@Override
	public ArrayList<String> search(String key){
		HashRowKey cont_key = new HashRowKey(key, hf);
		ArrayList<String> temp = table_list.get(cont_key);
		if (temp == null){
			throw new RuntimeException("This key doesn't exist!");
		}
		return temp;
	}
	
	@Override
	public ArrayList<ArrayList<String>> getTable() {
		ArrayList<ArrayList<String>> ret_table = new ArrayList<ArrayList<String>>();
		for (Entry<HashRowKey, ArrayList<String>> entry : table_list.entrySet()){
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(entry.getKey().getKey());
			temp.addAll(entry.getValue());
			ret_table.add(temp);
		}
		return ret_table;
	}
	
	@Override
	public void fillTable(ArrayList<ArrayList<String>> table_value) {
		table_list.clear();
		for (ArrayList<String> single_row : table_value){
			HashRowKey key = new HashRowKey(single_row.get(0), hf);
			ArrayList<String> temp_row =  new ArrayList<String>(single_row);
			temp_row.remove(0);
			table_list.put(key, temp_row);
		}
	}

	@Override
	public void delete(String key) {
		HashRowKey cont_key = new HashRowKey(key, hf);
		table_list.remove(cont_key);
	}

	@Override
	public void edit(String key, ArrayList<String> new_row) {
		HashRowKey cont_key = new HashRowKey(key, hf);
		table_list.replace(cont_key, new_row);
		
	}

	@Override
	public CollectionType getCollectionType() {
		return CollectionType.MAP;
	}

	@Override
	public void changeKey(String old_key, String new_key) {
		HashRowKey cont_key = new HashRowKey(new_key, hf);
		if (table_list.containsKey(cont_key) == true){
			throw new RuntimeException("This key already exist!");
		}
		HashRowKey cont_key2 = new HashRowKey(old_key, hf);
		table_list.put(cont_key, table_list.get(cont_key2));
		table_list.remove(cont_key2);
	}
	
	@Override
	public HashFunction getHashFunction() {
		return hf;
	}
}
