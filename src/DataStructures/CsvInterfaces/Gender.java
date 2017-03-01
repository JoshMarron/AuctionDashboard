package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public enum Gender {
    MALE("male"),
    FEMALE("female");
    
    private final String genderText;
    
    private Gender(final String text) {
    	genderText = text;
    }
	
	@Override
	public String toString() {
		return genderText;
	}
}
