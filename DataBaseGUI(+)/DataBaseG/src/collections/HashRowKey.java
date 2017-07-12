package collections;

import java.io.Serializable;

import hash.HashFunction;

public class HashRowKey implements Serializable {

	private static final long serialVersionUID = -3713771348340785813L;
	protected String key;
	private HashFunction hf;

	public HashRowKey (String _key, HashFunction _hf){
		hf=_hf;
		key = _key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (obj instanceof HashRowKey){
			if ( ((HashRowKey)obj).getKey().equals(key) ){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hf.hashCode(key);
	}

	public String getKey(){
		return key;
	}
}
