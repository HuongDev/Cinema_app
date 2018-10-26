package page;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageProfile extends PageBase {
    private AppiumDriver appiumDriver;
    private WebDriverWait wait;

    @FindBy(id = "imvBack")
    WebElement imvBack;

    @FindBy(id = "imvCreateMovie")
    WebElement imvCreateMovie;

    @FindBy(id = "imvAvatar")
    WebElement imvAvatar;

    @FindBy(id = "tvName")
    WebElement tvName;

    @FindBy(id = "tvEmail")
    WebElement tvEmail;

    @FindBy(id = "btnChangePass")
    WebElement btnChangePass;

    @FindBy(id = "btnLogout")
    WebElement btnLogout;

    @FindBy(id = "button1")
    WebElement button1;

    public PageProfile(AppiumDriver appiumDriver){
        this.appiumDriver = appiumDriver;
        PageFactory.initElements(appiumDriver,this);
        this.wait = new WebDriverWait(appiumDriver,30);
    }

    public void logout(){
        btnLogout.click();
        button1.click();
    }
}
