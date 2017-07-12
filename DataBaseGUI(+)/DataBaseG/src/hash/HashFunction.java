package hash;

public interface HashFunction {
	int hashCode(String key);
	HashType getHashType();
}
