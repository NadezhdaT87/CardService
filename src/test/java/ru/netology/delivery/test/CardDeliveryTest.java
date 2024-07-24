package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {
    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("test successful plan meeting")
    void testSuccessfulPlanMeeting() {
//        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
//        var firstMeetingDay = DataGenerator.generateData(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
//        var secondMeetingDay = DataGenerator.generateData(daysToAddForSecondMeeting);

        $("[data-test-id='city'] .input__control").setValue(DataGenerator.generateCity("ru"));
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] .input__control").setValue(DataGenerator
                .generateData(daysToAddForFirstMeeting, "dd.MM.yyyy"));
        $("[data-test-id='name'] [name='name']").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] [name='phone']").setValue(DataGenerator.generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на "
                        + DataGenerator.generateData(daysToAddForFirstMeeting, "dd.MM.yyyy")), Duration.ofSeconds(15))
                .shouldBe(visible);
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] .input__control").setValue(DataGenerator
                .generateData(daysToAddForSecondMeeting, "dd.MM.yyyy"));
        $(".button").click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] .button__text").click();
        $("[data-test-id='success-notification']").shouldHave(text("Встреча успешно запланирована на "
                + DataGenerator.generateData(daysToAddForSecondMeeting, "dd.MM.yyyy")));

    }
    @Test
    @DisplayName("test get error if wrong phone")
    void testGetErrorIfWrongPhone() {
        var daysToAddForFirstMeeting = 4;

        $("[data-test-id='city'] .input__control").setValue(DataGenerator.generateCity("ru"));
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] .input__control").setValue(DataGenerator
                .generateData(daysToAddForFirstMeeting, "dd.MM.yyyy"));
        $("[data-test-id='name'] [name='name']").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] [name='phone']").setValue(DataGenerator.generateWrongPhone("en"));
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(exactText("Неверный формат номера мобильного телефона"));
    }
}
