package page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestReport;

import java.util.concurrent.ThreadLocalRandom;

import static utils.TestReport.testReport;

public class PageLogin extends PageBase {
    private AppiumDriver appiumDriver;
    private WebDriverWait wait;

    @FindBy(id = "edtEmail")
    WebElement edtEmail;

    @FindBy(id = "edtPassword")
    WebElement edtPassword;

    @FindBy(id = "btnSignIn")
    WebElement btnSignIn;

    @FindBy(id = "btnISignUp")
    WebElement btnISignUp;

    @FindBy(id = "tvResetPassword")
    WebElement tvResetPassword;

    public PageLogin(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
        PageFactory.initElements(appiumDriver, this);
        this.wait = new WebDriverWait(appiumDriver, 25);
    }

    public void testEmailFail(){
        WebElement fabCreateMovie = appiumDriver.findElement(By.id("myFabCreateMovie"));
        fabCreateMovie.click();

        WebElement button1 = appiumDriver.findElement(By.id("button1"));
        button1.click();

    }

    public void gotoSignUp(){
        WebElement btnISignUp = appiumDriver.findElement(By.id("btnISignUp"));
        btnISignUp.click();
    }

    public void testLoginSuccess(int random){
        edtEmail.sendKeys("ngochuong"+ random +"@gmail.com");
        edtPassword.sendKeys("123456");
        btnSignIn.click();
        testReport(appiumDriver,true,"Đăng nhập thành công",true);
    }


}
