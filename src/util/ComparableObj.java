package util;

public class ComparableObj<String,Integer> implements Comparable<ComparableObj> {
	public String input;
	public int occurrences;

	public ComparableObj(String input, int occurrences) {
		super();
		this.input = input;
		this.occurrences = occurrences;
	}

	@Override
	public int compareTo(ComparableObj arg0) {
		int compare = this.occurrences > arg0.occurrences ? -1 : this.occurrences < arg0.occurrences ? +1 : 0;
		return compare;
	}

	@Override
	public int hashCode() {
		final int x = 19;
		int result = 9;
		result = x * result + occurrences;
		result = x * result + ((input == null) ? 0 : input.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object compareObj) {
		if (this == compareObj)
			return true;
		if (compareObj == null)
			return false;
		if (getClass() != compareObj.getClass())
			return false;
		ComparableObj other = (ComparableObj) compareObj;
		if (occurrences != other.occurrences)
			return false;
		return true;
	}
	
	@Override
    public java.lang.String toString() {
        return "(" + input + ", " + occurrences + ")";
    }
}
