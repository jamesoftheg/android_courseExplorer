package ca.mohawk.gelfand.finalproject;

public class Courses {
    private String courseCode;
    private String courseDescription;
    private String courseTitle;
    private int program;
    private int hours;

    @Override
    public String toString() {
        return "Course Code: " + courseCode +
                "\nCourse Name: " + courseTitle +
                "\nProgram: " + program +
                "\nHours: " + hours +
                "\nDescription: " + courseDescription +
                "\n";
    }
}
