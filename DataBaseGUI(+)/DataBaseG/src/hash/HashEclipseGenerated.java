package hash;

import java.io.Serializable;

public class HashEclipseGenerated implements Serializable, HashFunction {
	
	private static final long serialVersionUID = -1460322181414785309L;

	public HashEclipseGenerated(){}

	@Override
	public int hashCode(String key) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public HashType getHashType() {
		return HashType.STANDART;
	}

}
