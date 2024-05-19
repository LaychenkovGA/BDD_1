package ru.netology.page;

import ru.netology.data.DataHelper;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Translation {

    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id='error-notification']");

    public Translation() {
        transferHead.shouldBe(visible);
    }

    public DashboardPage doValidTranslation(String amountToTranslation, DataHelper.CardInfo cardInfo) {
        makeTranslation(amountToTranslation, cardInfo);
        return new DashboardPage();
    }

    public void makeTranslation(String amountToTranslation, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTranslation);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void findErrorMessage(String exprectedText) {
        errorMessage.shouldHave(text(exprectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
