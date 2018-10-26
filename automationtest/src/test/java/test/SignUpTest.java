package test;

import org.testng.annotations.Test;
import page.*;
import utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.testng.Assert.assertEquals;
import static utils.TestReport.handleExceptionAndMarkFailResult;
import static utils.TestReport.testReport;

public class SignUpTest extends TestBase{

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
