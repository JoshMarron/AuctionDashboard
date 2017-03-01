package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public enum Income {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");
    
    private final String incomeString;
    
    private Income(final String text) {
        incomeString = text;
    }
    
    @Override
    public String toString() {
        return incomeString;
    }
}
