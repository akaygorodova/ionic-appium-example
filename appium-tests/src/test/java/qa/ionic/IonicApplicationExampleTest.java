package qa.ionic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class IonicApplicationExampleTest {

    private static Logger logger;
    private AndroidDriver driver;

    @BeforeClass
    public static void setUpBeforeClass() {
        logger = LoggerFactory.getLogger(IonicApplicationExampleTest.class);
    }

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "My device");
        caps.setCapability(
            AndroidMobileCapabilityType.APP_PACKAGE,
            "com.example.tester.ionic_test_app"
        );
        caps.setCapability(
            AndroidMobileCapabilityType.APP_ACTIVITY,
            ".MainActivity"
        );
        caps.setCapability(CapabilityType.VERSION, "1.1");
        caps.setCapability("automationName", "UiAutomator2");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Set contextHandles = driver.getContextHandles();
        logger.debug("size: {}", contextHandles);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void findElementByXPath_nativeContext() {
        //works only when "automationName" is "UiAutomator2"
        WebElement element = driver.findElement(By.xpath(
            "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget" +
                ".FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view" +
                ".View/android.view.View/android.view.View/android.view.View/android.view" +
                ".View/android.view.View[2]/android.view.View/android.view.View[2]/android.view" +
                ".View[2]/android.view.View[1]"));
        assertThat(element.getText()).contains("Ionic Documentation");
    }

    @Test
    public void findElementsByTagName_webViewContext() {
        driver.context("WEBVIEW_com.example.tester.ionic_test_app");
        List<WebElement> elements = driver.findElements(By.tagName("ion-label"));
        assertThat(elements)
            .extracting(WebElement::getText)
            .areAtLeastOne(containsText("Ionic Documentation"));
    }

    private Condition<String> containsText(String expected) {
        return new Condition<>(
            element -> element.contains(expected),
            "contains <%s>", expected);
    }
}