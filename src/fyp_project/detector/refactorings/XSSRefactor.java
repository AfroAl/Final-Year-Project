package fyp_project.detector.refactorings;

public class XSSRefactor {

    private String originalLine;
    private String deletion;

    public XSSRefactor(String line) {
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {

        String replacement = originalLine;

        deletion = "";
        return replacement + " //Potentially vulnerable. Ensure the input is validated.";
    }
}
