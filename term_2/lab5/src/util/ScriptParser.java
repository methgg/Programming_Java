package util;

public class ScriptParser {

    public static String convertToJson(String input) {
        
    input = input.trim();
    if (input.startsWith("{") && input.endsWith("}")) {
        input = input.substring(1, input.length() - 1);
    }

    input = input.replace("=", ":");

    input = input.replaceAll("(\\w+)\\s*:", "\"$1\":");

    input = input.replace("'", "\"");

   
    input = input.replaceAll("Coordinates\\s*\\{", "{");
    input = input.replaceAll("Person\\s*\\{", "{");

    return "{" + input + "}";
    }
}