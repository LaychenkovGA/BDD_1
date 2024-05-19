package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV1;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.data.DataHelper.*;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPageV1.class);

        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor();
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
    }

    @Test
    void shouldTranslationFromFerstCadr() {
        var firstCardBalance = dashboardPage.getBalansCard(firstCardInfo);
        var secondCardBalance = dashboardPage.getBalansCard(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var translation = dashboardPage.selectCardToTranslation(secondCardInfo);
        dashboardPage = translation.doValidTranslation(String.valueOf(amount), firstCardInfo);
        var actualBalansFirstCard = dashboardPage.getBalansCard(firstCardInfo);
        var actualBalansSecondCard = dashboardPage.getBalansCard(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalansFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalansSecondCard);
    }

    @Test
    void shouldTranslationFromSecondCadr() {
        var firstCardBalance = dashboardPage.getBalansCard(firstCardInfo);
        var secondCardBalance = dashboardPage.getBalansCard(secondCardInfo);
        var amount = generateValidAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var translation = dashboardPage.selectCardToTranslation(firstCardInfo);
        dashboardPage = translation.doValidTranslation(String.valueOf(amount), secondCardInfo);
        var actualBalansFirstCard = dashboardPage.getBalansCard(firstCardInfo);
        var actualBalansSecondCard = dashboardPage.getBalansCard(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalansFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalansSecondCard);
    }

    @Test
    void shouldErrorTranslation() {
        var firstCardBalance = dashboardPage.getBalansCard(firstCardInfo);
        var secondCardBalance = dashboardPage.getBalansCard(secondCardInfo);
        var amount = DataHelper.generateInvalidAmount(secondCardBalance);
        var translation = dashboardPage.selectCardToTranslation(firstCardInfo);
        translation.makeTranslation(String.valueOf(amount), secondCardInfo);
        translation.findErrorMessage("Ошибка");
        var actualBalansFirstCard = dashboardPage.getBalansCard(firstCardInfo);
        var actualBalansSecondCard = dashboardPage.getBalansCard(secondCardInfo);
        assertEquals(firstCardBalance, actualBalansFirstCard);
        assertEquals(secondCardBalance, actualBalansSecondCard);
    }
}
