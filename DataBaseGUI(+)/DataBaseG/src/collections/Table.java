package collections;

import java.util.ArrayList;

import hash.HashFunction;

public interface Table {
	void add(String key, ArrayList<String> new_row);
	void delete(String key);
	void edit(String key, ArrayList<String> new_row);
	void changeKey(String old_key, String new_key);
	ArrayList<String> search(String key);
	ArrayList<ArrayList<String>> getTable();
	void fillTable(ArrayList<ArrayList<String>> table_value);
	CollectionType getCollectionType();
	HashFunction getHashFunction();
}
