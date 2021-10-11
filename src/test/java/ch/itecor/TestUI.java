package ch.itecor;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TestUI
{
    @Test
    public void technicalTestUI()
    {
        System.setProperty("webdriver.chrome.driver","C:\\Temp\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        String baseUrl = "https://itecor-app.herokuapp.com/";
//        String baseUrl = "http://localhost:3000";
        String expectedTitle = "Itecor Test App";
        String actualTitle = "";

        // launch the driver and direct it to the Base URL
        driver.manage().window().maximize();
        driver.get(baseUrl);

        // get the actual value of the title
        actualTitle = driver.getTitle();

        // Login
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        username.sendKeys("User1");
        password.sendKeys("Password1234");
        WebElement connexionBtn = driver.findElement(By.id("connexion_btn"));
        connexionBtn.click();
        // Verify the username on the greetings message
        String usernameOnGreeting = driver.findElement(By.id("displayed_name")).getText();
        Assertions.assertThat(usernameOnGreeting).isEqualTo("User1");

        // Wait for spinner to disappear
        WebElement loader = driver.findElement(By.id("loader"));
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(loader));

        // Select Geneva office
        WebElement imageGeneva = driver.findElement(By.id("img_geneva_office"));
        imageGeneva.click();

        // Get number of free seats from summary
        WebElement elemNumOfFreeSeats = driver.findElement(By.xpath(".//p[@class='office-summary']/span"));
        int numOfFreeSeats = Integer.parseInt(elemNumOfFreeSeats.getText());
        Assertions.assertThat(numOfFreeSeats).isEqualTo(10);

        // Get the number of free seats in the HR Office
        List<WebElement> freeSeatsInHR = driver.findElements(By.xpath(".//div[@class='office-hr']//*[contains(@class,'free')]"));
        int numOfFreeSeatsInHR = freeSeatsInHR.size();
        Assertions.assertThat(numOfFreeSeatsInHR).isEqualTo(4);

        // Click on the third free seat in HR Office and then click on the 6th free seat among the remaining ones
        freeSeatsInHR.get(2).click();
        List<WebElement> totalFreeSeats = driver.findElements(By.xpath(".//*[contains(@class,'free')]"));
        totalFreeSeats.get(5).click();

        // Verify that the total number of free seats was decreased by 2
        WebElement newElemNumOfFreeSeats = driver.findElement(By.xpath(".//p[@class='office-summary']/span"));
        int newNumOfFreeSeats = Integer.parseInt(newElemNumOfFreeSeats.getText());
        Assertions.assertThat(newNumOfFreeSeats).isEqualTo(numOfFreeSeats-2);

        // Show that the seats in the conference room are lightblue -> rgb(236, 103, 7)
        List<WebElement> conferenceSeats = driver.findElements(By.xpath(".//div[contains(@class,'neutral-seat')]"));
        String color = conferenceSeats.get(0).getCssValue("color");
        Assertions.assertThat(color).isEqualTo("rgba(236, 103, 7, 1)");

        // Click on the HR chair and verify that a pop-up message saying "This seat cannot be booked" appears (add a check of display:block for the popup on the xpath)
        WebElement hrChair = driver.findElement(By.xpath(".//div[@class='office-hr']//div[contains(@class,'seat')]"));
        hrChair.click();
        WebElement popup = driver.findElement(By.xpath(".//div[@class='office-hr']//div[@id='popup_hr' and @style='display: block;']//div[@class='hide-description']"));
        String popupText = popup.getText();
        Assertions.assertThat(popupText).contains("This seat cannot be booked");

        // close the driver
        driver.close();

    }
}
