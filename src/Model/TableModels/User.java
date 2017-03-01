package Model.TableModels;

import DataStructures.CsvInterfaces.Gender;
import DataStructures.CsvInterfaces.Income;

/**
 * User class models an entry in the User table after it comes out of the database
 */
public class User {

    private long userID;
    private String ageRange;
    private Gender gender;
    private Income income;

    public User(long userID, String ageRange, Gender gender, Income income) {
        this.userID = userID;
        this.ageRange = ageRange;
        this.gender = gender;
        this.income = income;
    }

    public long getUserID() {
        return userID;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public Gender getGender() {
        return gender;
    }

    public Income getIncome() {
        return income;
    }
}
