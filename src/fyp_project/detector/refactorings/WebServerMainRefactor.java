package fyp_project.detector.refactorings;

public class WebServerMainRefactor {

    private String originalLine;
    private String deletion;

    public WebServerMainRefactor(String line) {
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {

        String replacement = originalLine;

        replacement = replacement + " // Unsafe";
        deletion = "";
        return replacement;
    }
}
