package Controllers;

/**
 * A non-initialisable class to store some settings of the application. All settings should go here
 */
public class ProjectSettings {

    private static int bounceMinutes = Integer.MAX_VALUE; //Default assumes no timeout for bounces
    private static int bouncePages = 1; //Default bounce definition is one page viewed

    private ProjectSettings() {

    }

    public static int getBounceMinutes() {
        return bounceMinutes;
    }

    public static void setBounceMinutes(int bounceMinutes) {
        ProjectSettings.bounceMinutes = bounceMinutes;
    }

    public static int getBouncePages() {
        return bouncePages;
    }

    public static void setBouncePages(int bouncePages) {
        ProjectSettings.bouncePages = bouncePages;
    }

    //Resets the bounce definition to its default values
    public static void setDefaultBounceRate() {
        ProjectSettings.bounceMinutes = Integer.MAX_VALUE;
        ProjectSettings.bouncePages = 1;
    }
}
