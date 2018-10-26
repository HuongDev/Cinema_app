package page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PageCreateMovie extends PageBase {
    private AppiumDriver appiumDriver;
    private WebDriverWait wait;

    @FindBy(id = "imvCinema")
    WebElement imvCinema;

    @FindBy(id = "btnSelectPhoto")
    WebElement btnSelectPhoto;

    @FindBy(id = "edtMovie")
    WebElement edtMovie;

    @FindBy(id = "spGenreMovie")
    WebElement spGenreMovie;

    @FindBy(id = "edtOpeningDay")
    WebElement edtOpeningDay;

    @FindBy(id = "edtDes")
    WebElement edtDes;

    @FindBy(id = "btnCreateMovie")
    WebElement btnCreateMovie;

    @FindBy(id = "gl_root_view")
    WebElement gl_root_view;

    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget." +
            "FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v7.widget." +
            "LinearLayoutCompat/android.widget.FrameLayout/android.widget.ListView/android.widget.TextView[1]")
    WebElement selectPhoto;

    @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget." +
            "FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v7.widget." +
            "LinearLayoutCompat/android.widget.FrameLayout/android.widget.ListView/android.widget.TextView[2]")
    WebElement selectCamera;

    public PageCreateMovie(AppiumDriver appiumDriver){
        this.appiumDriver = appiumDriver;
        PageFactory.initElements(appiumDriver,this);
        this.wait = new WebDriverWait(appiumDriver,30);
    }

    public void createMovie(){
        btnSelectPhoto.click();
    }

    public void setImageGarelly(){
        selectPhoto.click();

        appiumDriver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.view.View[1]")).click();
//        appiumDriver.findElement(By.xpath("//android.widget.GridView//android.widget.GridView//android.widget.ImageView[contains(@resource-id,'echo')]")).click();

        waitForLoading(5);
    }

    public void setImageCamera(){
        selectCamera.click();
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/shutter_button_photo")).click();
        appiumDriver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Shutter button\"]")).click();
        appiumDriver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"OK\"]")).click();
//        appiumDriver.findElement(By.id("com.huong.mycinema:id/btn_done")).click();
        waitForLoading(5);
    }

    public void updateCreateMovie(){

        appiumDriver.findElement(By.id("com.huong.mycinema:id/edtMovie")).sendKeys("huong");
        try {
            appiumDriver.hideKeyboard();
        } catch (Exception e) {
            System.out.println("Virtual keyboard is not shown.");
        }
        WebElement spinner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.RelativeLayout[1]/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.RelativeLayout[2]/android.widget.Spinner")));
        spinner.click();
        waitForLoading(2);
        appiumDriver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.TextView[2]")).click();

        appiumDriver.findElement(By.xpath("//android.view.View[@content-desc=\"11 October 2018\"]")).click();

        WebElement btnISignUp = appiumDriver.findElement(By.id("button1"));
        btnISignUp.click();

        edtDes.sendKeys("phim hay nhat");
        try {
            appiumDriver.hideKeyboard();
        } catch (Exception e) {
            System.out.println("Virtual keyboard is not shown.");
        }

        btnCreateMovie = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnCreateMovie")));
        btnCreateMovie.click();
    }
}
