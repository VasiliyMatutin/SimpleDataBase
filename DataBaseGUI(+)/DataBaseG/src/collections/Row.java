package collections;

import java.io.Serializable;
import java.util.ArrayList;

import hash.HashFunction;

public class Row extends HashRowKey implements Serializable {
	private static final long serialVersionUID = -5858422273706321419L;
	private ArrayList<String> row_value = new ArrayList<String>();
	
	public Row (String _key, ArrayList<String> value, HashFunction _hf){
		super(_key,_hf);
		row_value = value;
	}
	
	public void setKey(String new_key){
		super.key = new_key;
	}
	
	public ArrayList<String> getValue(){
		return row_value;
	}
	
	public void setValue(ArrayList<String> value){
		row_value=value;
	}
}
