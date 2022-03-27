package carsharing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        DB db = new DB(getDbName(args));
        CLI cli = new CLI(db);
    }

    private static String getDbName(String[] args) {
        Pattern pattern = Pattern.compile("-databaseFileName (\\w+)");
        Matcher matcher = pattern.matcher(String.join(" ", args));
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "carsharing";
    }
}