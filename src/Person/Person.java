package Person;

import ArtWork.IArtWork;
import Parser.WikiParser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class Person {

    String firstName, lastName;
    String mainWikiPage;
    String listOfWorksWikiPage;

    int id;
    protected static int countPeople = 0;
    private WebDriver driver;
    protected List<Occupation> knownForBeing;
    protected List<IArtWork> artWorks;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        id = ++ countPeople;

        knownForBeing = new ArrayList<Occupation>();
        artWorks = new ArrayList<>();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(options);

        findMainWikiPage();
        findOccupations();
        findListOfWorksWikiPage();
        parseListOfWorks();

        //driver.close();
    }

    private void findMainWikiPage(){
        driver.get("https://www.wikipedia.org");

        WebElement search = driver.findElement(By.name("search"));
        search.sendKeys(firstName + " " + lastName);
        search.submit();

        mainWikiPage = driver.getCurrentUrl();
    }

    private void findOccupations(){
        WebElement biography = driver.findElement(By.cssSelector("#mw-content-text > div.mw-parser-output > table > tbody"));
        List<WebElement> children = biography.findElements(By.xpath("./*"));
        WebElement occupationWebElement = null;

        for(WebElement child : children){
            if (child.getText().contains("Person.Occupation")){
                occupationWebElement = child;
                break;
            }
        }

        if (occupationWebElement == null){
            System.out.println("No occupation found");
            return;
        }

        children = occupationWebElement.findElements(By.xpath("./*"));
        occupationWebElement = children.get(1);

        parseOccupations(occupationWebElement.getText());
    }

    /**
     * Finds the person's occupation and adds them to occupationList
     * @param occupationIn String containing the list of the person's occupations
     */
    private void parseOccupations(String occupationIn){
        Occupation occupation;

        if (occupationIn == null){
            return;
        }

        occupationIn = occupationIn.toUpperCase();

        occupation = parseOccupation(occupationIn);

        while (occupation != Occupation.UNDEFINED){
            knownForBeing.add(occupation);
            occupationIn = filterOccupations(occupation, occupationIn);
            occupation = parseOccupation(occupationIn);
        }

    }

    /**
     * Finds single occupation from string
     * @param occupation String containing an occupation
     * @return First occupation found or UNDEFINED if none found
     */
    private Occupation parseOccupation(String occupation){

        if (occupation.contains("ACTOR") || occupation.contains("ACTRESS")){
            return Occupation.Actor;
        }

        if (occupation.contains("FILMMAKER") || occupation.contains("DIRECTOR")){
            return Occupation.FilmMaker;
        }

        if (occupation.contains("PRODUCER")){
            return Occupation.Producer;
        }
        System.out.println("Unkown Person.Occupation " + occupation);

        return Occupation.UNDEFINED;
    }

    /**
     * Removes instances of the specified occupation from the string
     * @param occupation The occupation to remove from the string
     * @param occupationStr The string you want to filter
     * @return Filtered string
     */
    private String filterOccupations(Occupation occupation, String occupationStr){
        String regexPattern;
        if (occupation == Occupation.Actor){
            regexPattern = "(ACTOR)|(ACTRESS)";
        }
        else if (occupation == Occupation.FilmMaker){
            regexPattern = "(FILMMAKER)|(DIRECTOR)";
        }
        else if (occupation == Occupation.Producer){
            regexPattern = "(FILM\\s)?PRODUCER";
        }
        else {
            return null;
        }

        return occupationStr.replaceAll(regexPattern,"");
    }

    private void findListOfWorksWikiPage(){
        WebElement table = driver.findElement(By.cssSelector("#mw-content-text > div.mw-parser-output > table > tbody"));
        List<WebElement> children = table.findElements(By.xpath("./child::*"));
        WebElement listOfWorks = null;

        for (WebElement child :
                children) {
            if (child.getText().contains("Works")){
                listOfWorks  = child;
                break;
            }
        }

        if(listOfWorks == null){
            System.out.println("No link found");

            WebElement filmography = driver.findElement(By.cssSelector("#toc > ul > li.toclevel-1.tocsection-10 > a > span.toctext"));

            if (filmography == null){
                List<WebElement> anchors = driver.findElements(By.tagName("a"));
            }
            else {
                filmography.click();
            }

            listOfWorksWikiPage = driver.getCurrentUrl();

            return;
        }

        children = listOfWorks.findElements(By.xpath("./child::*"));
        children.get(1).findElement(By.xpath("./child::*")).click();
        listOfWorksWikiPage = driver.getCurrentUrl();
    }

    private void parseListOfWorks(){
        String title = driver.getTitle().replace(" - Wikipedia", "");
        System.out.println("TITLE\t" + title);
        System.out.println(driver.getCurrentUrl());

        driver.get("https://en.wikipedia.org/wiki/Special:Export/" + title);

        List<WebElement> webElements = driver.findElements(By.className("line"));
        WebElement text = null;

        for (WebElement element :
                webElements) {
            WebElement searchedElement = element.findElement(By.className("html-tag"));
            if (searchedElement.getText().contains("<text")){
                text = element;
            }
        }

        if (text == null){
            return;
        }

        String contents = text.findElements(By.tagName("span")).get(7).getText();

        //System.out.println("WAKA WAKA\t" + contents);
        WikiParser parser = new WikiParser(contents);
        //parser.findArtWorks(knownForBeing);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(firstName + " " + lastName + "\n");

        buffer.append("Person.Occupation(s)\n\t");
        for (Occupation occupation :
                knownForBeing) {
            buffer.append(occupation);
            buffer.append("\n\t");
        }
        return buffer.toString();
    }

}
