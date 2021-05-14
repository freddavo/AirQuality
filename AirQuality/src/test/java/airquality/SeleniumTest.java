package airquality;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import static org.assertj.core.api.Assertions.assertThat;

public class SeleniumTest {

     // Teste funcionais à interface web



    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp(){
        System.setProperty("webdriver.gecko.driver","geckodriver");
        driver = new FirefoxDriver();
        baseUrl = "https://www.katalon.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String error_string= verificationErrors.toString();
        if (!"".equals(error_string)) {
            Assertions.fail(error_string);
        }
    }


    @Test
    public void testPossibleCities() throws Exception { // se é possível consultar os dados para todas as cidade escolhendo cada cidade disponível e carregando no botão ok ao lado direito)
        boolean var = false;
        driver.get("http://localhost:8080");
        List<String> nomecidade = new ArrayList<>();
        String [] cidades ={"Shanghai", "Paris", "London", "Lisbon", "Berlin","Helsinki", "Stockholm", "Moscow","Madrid","Beijing","Porto","HongKong","Barcelona","Manchester","Braga","Liverpool","Roma","Lille","Bern","Linz","New-York"};


        for (String c : cidades ) {
            nomecidade.add(c);
        }

        for(int i=0;i<nomecidade.size();i++){
            new Select(driver.findElement(By.id("city"))).selectByVisibleText(nomecidade.get(i));
            driver.findElement(By.id("ok")).click();
        }
        //Se passou o for anterior significa que está tudo OK
        var = true;
        assertThat(var).isEqualTo(true);
    }


    private boolean isElementPresent(By by) {
        driver.get("http://localhost:8080");
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


}