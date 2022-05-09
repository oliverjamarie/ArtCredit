package Parser;

import ArtWork.IArtWork;
import ArtWork.Play;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiDataTable implements IWikiData {
    // Regex Expression to filter the table by year
    // \|-\s!scope=\"row\"(\srowspan=\d\s?){0,1}\|\d{4}\s\|
    String data;
    List<String> fields;

    public enum ArtWorkType {
        PLAY,
        FILM,
        UNKNOWN
    }

    ArtWorkType artWorkType;

    //TODO Generate json file containing parsed and extracted from a table
    public WikiDataTable(String data, String name) {
        this.data = data;
        artWorkType = getArtWorkType(name);

        if (artWorkType == ArtWorkType.UNKNOWN){
            return;
        }

        fields = new ArrayList<>();
        parse();
    }

    protected ArtWorkType getArtWorkType(String name){
        if (name.contains("Stage")){
            return ArtWorkType.PLAY;
        }
        return ArtWorkType.UNKNOWN;
    }

    @Override
    public void setData(String dataIN) {
        data = dataIN;
    }

    @Override
    public String getData() {
        return data;
    }

    protected void parse(){
        String regexPattern = RegexPatternFinder.findRegexPattern(RegexPatternFinder.Mode.FIND_FIELDS, data);

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()){
            fields = extractFields(data.substring(0, matcher.start()));
            data = data.substring(matcher.start());
        }
        else{
            System.out.println("SOME TOMFOOLERY IS AFOOT");
            System.out.println(data);
            return;
        }

        List<String> dataStrings = extractDataStr(data);
        parseDataStr(dataStrings);
    }

    //TODO Extract fields from the table
    protected List<String> extractFields(String dataIn){
        String regexPattern = RegexPatternFinder.findRegexPattern(RegexPatternFinder.Mode.PARSE_FIELDS,dataIn);
        ArrayList<String> fields = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(dataIn);


        while (matcher.find()){
            String str = matcher.group();
            String field = null;
            try{
                field = str.split("\\|")[1];
            }
            catch (Exception e){
                if (str.contains("!")){
                    field = str.replaceAll("!+\\s","");
                }
            }


            fields.add(field);
        }

        return fields;
    }

    protected List<String> extractDataStr(String dataIn){
        List<String> dataStrings = new ArrayList<>();
        List<Integer> startIndeces = new ArrayList<>();
        String regex = RegexPatternFinder.findRegexPattern(RegexPatternFinder.Mode.FIND_DATA, dataIn);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dataIn);

        while (matcher.find()){
            startIndeces.add(matcher.start());
        }

        matcher.reset();
        int i = 0;

        while(matcher.find()){
            String descriptor = matcher.group();
            String dataStr;
            int startIndex, endIndex;

            startIndex = startIndeces.get(i);

            if (i + 1 < startIndeces.size()){
                endIndex = startIndeces.get(i+1) - 1;
                dataStr = dataIn.substring(startIndex,endIndex);
            }
            else{
                dataStr = dataIn.substring(startIndex);
            }
            dataStr = removeMetaData(dataStr);
            dataStrings.add(dataStr);
            i ++;
        }

        return dataStrings;
    }

    protected List<IArtWork> parseDataStr(List<String> extractedDataStrings){
        System.out.println(artWorkType);
        switch (artWorkType){
            case PLAY:
                return parsePlays(extractedDataStrings);
            default:
                System.out.println("UNDEFINED");
                return null;
        }
    }

    protected List<IArtWork> parsePlays(List<String> extractedDataString){
        ArrayList<IArtWork> plays = new ArrayList<>();
        System.out.println("Parsing Plays");

        //FIXME a list is not being generated for Hugh Jackman
        for (String dataString :
                extractedDataString) {
            Play play;
            HashMap<String, String> splitData = splitData(dataString);


        }

        return plays;
    }

    private String removeMetaData(String dataIn){
        dataIn = dataIn.replaceAll("align=\"center\"","");
        dataIn = dataIn.replaceAll("style=\"text-align:center;\"", "");
        dataIn = dataIn.replaceAll("\\|- !scope=\\\"row\\\"( rowspan=\\d\\s?)?", "");
        dataIn = dataIn.replaceAll("\\|- !scope=\"row\"?", "");
        dataIn = dataIn.replaceAll("\\|\\s+rowspan=", "");
        return dataIn;
    }

    private HashMap<String, String> splitData(String dataIn){
        HashMap<String,String> hashMap = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder(dataIn);



        return hashMap;
    }
}
