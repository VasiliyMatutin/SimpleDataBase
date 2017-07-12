package hash;

import java.io.Serializable;

public class HashLy implements Serializable,HashFunction {

	private static final long serialVersionUID = -2964907411003977410L;

	public HashLy(){}
	
	@Override
	public int hashCode(String key) {
		int hash = 0;
		for (int i = 0; i < key.length(); i++){
			hash = (hash * 1664525) + key.charAt(i) + 1013904223;
		}
		return hash;

	}

	@Override
	public HashType getHashType() {
		return HashType.LY;
	}

}
