import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiSection {
    protected StringBuffer data;
    protected String preamble;
    protected String name;
    protected List<WikiSection> subsections;

    public WikiSection(String name, String data){
        this.name = new String(name);
        this.data = new StringBuffer(data);
        subsections = new ArrayList<WikiSection>();
        filter();
        //System.out.println(this.data);
    }

    private void filter(){
        // Find the links \|\<ref.+?</ref>
        // Find the data after the header \{\|class=".+?".+?\|\}

        String stringPattern = "\\{\\|\\s?class=\".+?\".+?\\|\\}";
        String parsed = data.substring(name.length());

        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(parsed);

        if (!matcher.find()){
            data = new StringBuffer(parsed);
            return;
        }
        else {
            parsed = matcher.group();
        }

        parsed = removeLinks(parsed);

        data = new StringBuffer();
        data.append(parsed);
    }

    /**
     * Removes hyperlinks from input string
     * @param in String to remove hyperlinks from
     * @return String with removed hyperlinks
     */
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

    private void filterSubsections(){

    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Section : \t" + name + "\n");

        buffer.append(data);

        for (WikiSection wikiSection : subsections) {
            buffer.append("SubSection : \t" + wikiSection.name + "\n");
        }

        return buffer.toString();
    }

    public WikiSection getSection(String sectionTitle){
        return null;
    }
}
