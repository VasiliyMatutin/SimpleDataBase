package hash;

import java.io.Serializable;

public class HashH37 implements Serializable,HashFunction {

	private static final long serialVersionUID = -210242437391118009L;

	public HashH37(){}
	
	@Override
	public int hashCode(String key) {
		int hash = 2139062143;
		//for (int i = 0; i < key.length(); i++){
			hash = 37 * hash + key.charAt(0);
		//}
		return hash;
	}

	@Override
	public HashType getHashType() {
		return HashType.H37;
	}

}
