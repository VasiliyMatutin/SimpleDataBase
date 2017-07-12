package hash;

import java.io.Serializable;

public class HashRs implements Serializable, HashFunction {
	
	private static final long serialVersionUID = 8261592060095251345L;

	public HashRs(){}

	@Override
	public int hashCode(String key) {
		final int b = 378551;
		int a = 63689;
		int hash = 0;
		for (int i = 0; i < key.length(); i++){
			hash = hash * a + key.charAt(i);
			a *= b;
		}
		return hash;
	}

	@Override
	public HashType getHashType() {
		return HashType.RS;
	}
}
