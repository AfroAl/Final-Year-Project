package fyp_project.detector.refactorings;

public class Base64EncoderRefactor {

    private String originalLine;
    private String deletion;

    public Base64EncoderRefactor(String line) {
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
