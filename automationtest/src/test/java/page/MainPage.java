package page;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage extends PageBase {
    AppiumDriver appiumDriver;
    private WebDriverWait wait;

    @FindBy(id = "myFabCreateMovie")
    WebElement myFabCreateMovie;

    @FindBy(id = "myFabUser")
    WebElement myFabUser;

    public MainPage(AppiumDriver appiumDriver){
        this.appiumDriver = appiumDriver;
        PageFactory.initElements(appiumDriver, this);
        this.wait = new WebDriverWait(appiumDriver, 30);
    }

    public void goToLoginPage() {
        WebElement fabCreateMovie = appiumDriver.findElement(By.id("myFabCreateMovie"));
        fabCreateMovie.click();

        WebElement button1 = appiumDriver.findElement(By.id("button1"));
        button1.click();



//        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtNameSU")).sendKeys("huong");
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtEmailSU")).sendKeys("ngochuong"+ random +"@gmail.com");
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtPasswordSU")).sendKeys("123456");
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtConfirmPasswordSU")).sendKeys("123456");
//
//        WebElement btnSignUp = appiumDriver.findElement(By.id("btnSignUp"));
//        btnSignUp = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnSignUp")));
//        btnSignUp.click();


//        WebElement goToLogin = appiumDriver.findElement(By.id("btDialogYes"));
//        goToLogin.click();
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("")));
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/myFabCreateMovie")).sendKeys("abc");
//        appiumDriver.findElement(By.name("com.huong.mycinema:id/action_search")).click();
    }

    public void goToProfile(){
        myFabUser.click();
    }

    public void goToCreateMovie(){
        try {
            if(myFabUser.isDisplayed() ) {
              myFabCreateMovie.click();
            }else {

            }
        }
        catch(Exception e) {

        }
    }

    private int checkLogin(){
        if (myFabUser.isDisplayed()) {
            return 1;
        }else {
            return 0;
        }
    }
}
