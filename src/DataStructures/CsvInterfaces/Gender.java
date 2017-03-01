package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 *
 * Categorisation of the genders in the database
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female");
    
    private final String genderText;
    
    private Gender(final String text) {
    	genderText = text;
    }
	
	@Override
	public String toString() {
		return genderText;
	}
}
