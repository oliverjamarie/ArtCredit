package WikiParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiData {
    protected StringBuffer data;
    protected String preamble;
    protected String name;
    protected List<WikiData> subsections;

    public WikiData(String name, String data){
        this.name = new String(name);
        this.data = new StringBuffer(data);
        subsections = new ArrayList<WikiData>();
        parse();
        System.out.println(this.data);
    }

    private void parse(){
        // Find the links \|\<ref.+?</ref>
        // Find the data after the header \{\|class=".+?".+?\|\}

        String stringPattern = "\\{\\|\\s?class=\".+?\".+?\\|\\}";
        String parsed = data.substring(name.length());

        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(parsed);

        if (!matcher.find()){
            System.out.println("NO MATCH");
        }
        else {
            parsed = matcher.group();
        }

        parsed = removeLinks(parsed);

        data = new StringBuffer(parsed);
    }

    private String removeLinks(String in){
        List<String> stringPatterns = new ArrayList<>();
        stringPatterns.add("\\|\\<ref.+?</ref>");
        stringPatterns.add("\\<ref>.+?</ref>");
        stringPatterns.add("\\{cite.+?\\}");

        for (String str : stringPatterns) {
            in = in.replaceAll(str,"");
        }

        return in;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Name : \t" + name + "\n");

        buffer.append(data);

        for (WikiData wikiData: subsections) {
            buffer.append("Section : \t" + wikiData.name + "\n");
        }

        return buffer.toString();
    }
}
