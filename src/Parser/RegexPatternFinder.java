package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPatternFinder {
    protected enum Mode{
        PARSE_FIELDS,
        FIND_FIELDS,
        FIND_DATA,
        PARSE_DATA
    }

    protected static String findRegexPattern(Mode mode, String data){
        List<String> list;

        switch (mode){
            case FIND_FIELDS:
                list = findFields(data);
                break;
            case PARSE_FIELDS:
                list = parseFields(data);
                break;
            case FIND_DATA:
                list = findData(data);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }

        for (String regex :
                list) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()){
                //System.out.println(regex);
                return regex;
            }
        }

        System.out.println("NOT FOUND \tMode: " + mode);
        System.out.println(data);
        return null;
    }

    private static List<String> findFields(String data){
        List<String> list = new ArrayList<>();
        list.add("\\|-\\s!scope=\\\"row\\\"(\\srowspan=\\d\\s?){0,1}\\|\\d{4}\\s\\|");
        list.add("\\|-");

        return list;

    }

    private static List<String> parseFields(String data){
        List<String> list = new ArrayList<>();
        list.add("scope=\"col\"[\\s\\w=\":;]*\\|[\\w\\(\\)]*");
        list.add("!+\\s\\w+");

        return list;
    }


    private static List<String> findData(String data){
        List<String> list = new ArrayList<>();
        list.add("\\| ?\\d{4}");
        list.add("\\|- !scope=\"row\"( rowspan=\\d\\s?)?\\|\\d{4}");
        list.add("\\|-\\s\\|\\s?rowspan=\"?\\d\"?\\s?\\|\\s?\\d{4}");
        list.add("\\| rowspan=\\d\\|\\d{4}");

        return list;
    }

}
