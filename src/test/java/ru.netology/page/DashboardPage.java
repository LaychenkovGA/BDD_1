package ru.netology.page;

import ru.netology.data.DataHelper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement header = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        header.shouldBe(visible);
    }

    public int getBalansCard(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(text(cardInfo.getCardNumber().substring(15))).getText();
        return extractBalance(text);
    }

    public Translation selectCardToTranslation(DataHelper.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestID())).$("button").click();
        return new Translation();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}