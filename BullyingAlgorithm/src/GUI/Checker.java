package GUI;

public interface Checker {
	default boolean isNumeric(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
