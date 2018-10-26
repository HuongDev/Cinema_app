package page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static utils.TestReport.testReport;

public class PageNotLogin extends PageBase{
    private AppiumDriver appiumDriver;
    private WebDriverWait wait;

    @FindBy(id = "myFabUser")
    WebElement myFabUser;


    public PageNotLogin(AppiumDriver appiumDriver){
        this.appiumDriver = appiumDriver;
        PageFactory.initElements(appiumDriver,this);
        this.wait = new WebDriverWait(appiumDriver,30);
    }

    public int verifySuccessfulLogin(){
        boolean result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myFabUser"))).isDisplayed();
        testReport(this.appiumDriver, result, "Logout SUCCESS", "Logout FAIL", true);
        if (result)
            return 1;
        return 0;
    }
}
