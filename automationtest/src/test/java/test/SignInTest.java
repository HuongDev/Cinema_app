package test;

import org.testng.annotations.Test;
import page.MainPage;
import page.PageLogin;
import page.PageProfile;
import page.PageSignUp;
import utils.Utils;

public class SignInTest extends TestBase {

    @Test
    public void testLogin() {
        int random = Utils.randomEmail(0, 10000);

        MainPage mainPage = new MainPage(androidDriver);
        mainPage.goToLoginPage();

        PageLogin loginPage = new PageLogin(androidDriver);
        loginPage.gotoSignUp();

        PageSignUp signUpPage = new PageSignUp(androidDriver);
        signUpPage.createUser(random);

        mainPage = new MainPage(androidDriver);
        mainPage.goToProfile();

        PageProfile pageProfile = new PageProfile((androidDriver));
        pageProfile.logout();

        loginPage = new PageLogin(androidDriver);
        loginPage.testLoginSuccess(random);
    }

}
