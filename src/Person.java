import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import WikiParser.*;
import java.util.List;

public class Person {
    String firstName, lastName;
    String mainWikiPage;
    String listOfWorksWikiPage;

    int id;
    protected static int countPeople = 0;
    private WebDriver driver;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        id = countPeople ++;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(options);

        findMainWikiPage();
        findListOfWorksWikiPage();
        parseListOfWorks();

        driver.close();
    }

    private void findMainWikiPage(){
        driver.get("https://www.wikipedia.org");

        WebElement search = driver.findElement(By.name("search"));
        search.sendKeys(this.toString());
        search.submit();

        mainWikiPage = driver.getCurrentUrl();
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
        System.out.println(title);

        driver.get("https://en.wikipedia.org/wiki/Special:Export/" + title);

        String contents = driver.findElement(By.cssSelector("#folder4 > div.opened > div:nth-child(16) > span:nth-child(2)")).getText();
        //System.out.println(contents);
        WikiParser parser = new WikiParser(contents);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
