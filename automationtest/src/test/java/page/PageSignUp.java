package page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.ThreadLocalRandom;

public class PageSignUp extends PageBase {
    private AppiumDriver appiumDriver;
    private WebDriverWait wait;

    @FindBy(id = "edtNameSU")
    WebElement btnLogin;

    @FindBy(id = "edtEmailSU")
    WebElement edtEmailSU;

    @FindBy(id = "edtPasswordSU")
    WebElement edtPasswordSU;

    @FindBy(id = "edtConfirmPasswordSU")
    WebElement edtConfirmPasswordSU;

    @FindBy(id = "btnISignIn")
    WebElement btnISignIn;

    @FindBy(id = "btnSignUp")
    WebElement btnSignUp;

    public PageSignUp(AppiumDriver appiumDriver){
        this.appiumDriver = appiumDriver;
        PageFactory.initElements(appiumDriver,this);
        this.wait = new WebDriverWait(appiumDriver,30);
    }

    public void createUser(int random) {
        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtNameSU")).sendKeys("huong");
        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtEmailSU")).sendKeys("ngochuong"+ random +"@gmail.com");
        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtPasswordSU")).sendKeys("123456");
        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtConfirmPasswordSU")).sendKeys("123456");

        WebElement btnSignUp = appiumDriver.findElement(By.id("btnSignUp"));
        btnSignUp = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSignUp")));
        btnSignUp.click();



//        WebElement goToLogin = appiumDriver.findElement(By.id("btDialogYes"));
//        goToLogin.click();
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("")));
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/myFabCreateMovie")).sendKeys("abc");
//        appiumDriver.findElement(By.name("com.huong.mycinema:id/action_search")).click();
    }
}
