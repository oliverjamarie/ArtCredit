package Parser;

import ArtWork.IArtWork;
import Person.Occupation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiParser {
    String fileContents;
    List<WikiSection> wikiDataList;

    public WikiParser(String data) {
        fileContents = new String(data);
        wikiDataList = new ArrayList<WikiSection>();
        parse();
    }

    private void parse(){
        //System.out.println("Parsing");

        //Regex to find different sections in wiki page : ={2,}[A-Z][a-zA-Z|\s]*={2,}
        //Regex Pattern to extract tables : \{\|class="[\w\s]*"\s([!="%':;,#~?&\—‘’\–\.\<\>\/\[\]\(\)\{\}\|\-\s\d\w])+?\|\}
        String stringPattern = "={2}\\s?[A-Z][a-zA-Z|\\s]*\\s?={2}";

        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(fileContents);

        List<Integer> startIndeces = new ArrayList<>();

        while(matcher.find()){
            startIndeces.add(matcher.start());
        }

        matcher.reset();
        for (int i = 0; i < startIndeces.size(); i++) {
            if(!matcher.find()){
                break;
            }
            String name = matcher.group();
            String data;
            int startIndex, endIndex;

            startIndex = startIndeces.get(i);

            if (i + 1 < startIndeces.size()){
                endIndex = startIndeces.get(i+1) - 1;

                data = fileContents.substring(startIndex,endIndex);
            }
            else {
                data = fileContents.substring(startIndex);
            }

            //FIXME Doesn't extract plays for Hugh Jackman
            wikiDataList.add(new WikiSection(name,data));
        }
    }

    public List<IArtWork> findArtWorks(List<Occupation> occupationList){
        ArrayList<IArtWork> IArtWorks = new ArrayList<>();

        System.out.println("Sections");

        return IArtWorks;
    }
}
