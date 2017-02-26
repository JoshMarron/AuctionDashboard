package DataStructures;

import DataStructures.CsvInterfaces.Gender;
import DataStructures.CsvInterfaces.IImpressionLog;
import DataStructures.CsvInterfaces.Income;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.time.*;

/**
 * Created by rhys on 26/02/17.
 */
public class ImpressionLog implements IImpressionLog {
    String rawDate;
    Long secondsEpoch;
    Long ID;
    Gender gender;
    int AgeMin;
    int AgeMax;
    Income income;
    String Context;
    Double Impression_Cost;

    public ImpressionLog setAge(String x) {
        if (x.contains("-")) {
            String[] ages = x.split("-");
            this.AgeMin = Integer.parseInt(ages[0]);
            this.AgeMax = Integer.parseInt(ages[1]);
        } else if (x.contains(">")) {
            this.AgeMin = Integer.parseInt(x.replace(">", ""));
            this.AgeMax = Integer.MAX_VALUE;
        } else if (x.contains("<")) {
            this.AgeMin = Integer.MAX_VALUE;
            this.AgeMax = Integer.parseInt(x.replace("<", ""));

        } else
            throw new ValueException("age dosn't conform to min - max or > min or < max");
        return this;
    }

    public ImpressionLog setImpression_Cost(String x) {
        this.Impression_Cost = Double.parseDouble(x);
        return this;
    }

    @Override
    public String getDate() {
        return this.rawDate;
    }

    public ImpressionLog setDate(String x) {
        LocalDate localDate = LocalDate.parse(x.split(" ")[0]);
        LocalDateTime localDateTime = localDate.atTime(LocalTime.parse(x.split(" ")[1]));
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        rawDate = x;
        secondsEpoch = instant.getEpochSecond();
        return this;
    }

    @Override
    public Long getID() {
        return this.ID;
    }

    public ImpressionLog setID(String x) {
        this.ID = Long.parseLong(x);
        return this;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    public ImpressionLog setGender(String x) {
        switch (x.toLowerCase()) {
            case "male":
                this.gender = Gender.Male;
                break;
            case "m":
                this.gender = Gender.Male;
                break;
            case "female":
                this.gender = Gender.Female;
                break;
            case "f":
                this.gender = Gender.Female;
                break;
            default:
                this.gender = Gender.valueOf(x);
                break;
        }
        return this;
    }

    @Override
    public Integer getMinAge() {
        return this.AgeMin;
    }

    @Override
    public Integer getMaxAge() {
        return this.AgeMax;
    }

    @Override
    public Income getIncome() {
        return this.income;
    }

    public ImpressionLog setIncome(String x) {
        switch (x.toLowerCase()) {
            case "high":
                this.income = Income.High;
                break;
            case "h":
                this.income = Income.High;
                break;
            case "medium":
                this.income = Income.Medium;
                break;
            case "m":
                this.income = Income.Medium;
                break;
            case "low":
                this.income = Income.Low;
                break;
            case "l":
                this.income = Income.Low;
                break;
            default:
                this.income = Income.valueOf(x);
                break;
        }
        return this;
    }

    @Override
    public String getContext() {
        return this.Context;
    }

    public ImpressionLog setContext(String x) {
        this.Context = x;
        return this;
    }

    @Override
    public Double getImpressionCost() {
        return this.Impression_Cost;
    }

    @Override
    public String toString() {
        return "ImpressionLog{" +
                "rawDate='" + rawDate + '\'' +
                ", secondsEpoch=" + secondsEpoch +
                ", ID=" + ID +
                ", gender=" + gender +
                ", AgeMin=" + AgeMin +
                ", AgeMax=" + AgeMax +
                ", income=" + income +
                ", Context='" + Context + '\'' +
                ", Impression_Cost=" + Impression_Cost +
                '}';
    }
}
