package test;

import org.testng.annotations.Test;
import page.*;
import utils.Utils;

public class CreateMovieTest extends TestBase{
    @Test
    public void testCreateMovie(){

        int random = Utils.randomEmail(0, 10000);

        MainPage mainPage = new MainPage(androidDriver);
        mainPage.goToLoginPage();

        PageLogin loginPage = new PageLogin(androidDriver);
        loginPage.gotoSignUp();

        PageSignUp signUpPage = new PageSignUp(androidDriver);
        signUpPage.createUser(random);

//        mainPage = new MainPage(androidDriver);
//        mainPage.goToProfile();
//
//        PageProfile pageProfile = new PageProfile((androidDriver));
//        pageProfile.logout();
//
//        loginPage = new PageLogin(androidDriver);
//        loginPage.testLoginSuccess(random);

        PageNotLogin pageNotLogin = new PageNotLogin(androidDriver);
        pageNotLogin.verifySuccessfulLogin();

        mainPage = new MainPage(androidDriver);
        mainPage.goToCreateMovie();

        PageCreateMovie createMovie = new PageCreateMovie(androidDriver);
        createMovie.createMovie();



        createMovie = new PageCreateMovie(androidDriver);
        createMovie.updateCreateMovie();

//        createMovie = new PageCreateMovie(androidDriver);
//        createMovie.setImageCamera();
    }
}
