package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiSection {
    protected StringBuffer dataStrBuff;
    protected String preamble;
    protected String name;
    protected List<WikiSection> subsections;
    protected List<IWikiData> wikiDataList;

    public WikiSection(String name, String data){
        this.name = new String(name);
        this.dataStrBuff = new StringBuffer(data);
        subsections = new ArrayList<WikiSection>();
        wikiDataList = new ArrayList<>();
        filterSubsections();
        filter();
    }

    private void filter(){
        // Find the links \|\<ref.+?</ref>
        // Find the data after the header \{\|class=".+?".+?\|\}

        String stringPattern = "\\{\\|\\s?class=\".+?\".+?\\|\\}";
        String parsed = dataStrBuff.substring(name.length());
        parsed = removeLinks(parsed);

        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(parsed);

        if (!matcher.find()){
            dataStrBuff = new StringBuffer(parsed);
            return;
        }
        else {
            parsed = matcher.group();
        }

        dataStrBuff = new StringBuffer(parsed);
        wikiDataList.add(new WikiDataTable(dataStrBuff.toString(), name));
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

    private String extractTable(String in){
        return null;
    }

    private void filterSubsections(){
        //System.out.println("Filtering Subsections");

        String stringPattern = "={3}[A-Z][a-zA-Z|\\s]*={3}";

        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(stringPattern);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(dataStrBuff);
        buffer.append("Section : \t" + name + "\n");

        buffer.append(dataStrBuff);

        for (WikiSection wikiSection : subsections) {
            buffer.append("SubSection : \t" + wikiSection.name + "\n");
        }

        return buffer.toString();
    }

    public WikiSection getSection(String sectionTitle){
        return null;
    }
}
