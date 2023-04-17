package sel1;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExcelExample2 {
	WebDriver driver;
	WebDriverWait wait;
	
	String appURL="https://www.linkedin.com/";
	private By bySignInLink = By.linkText("Sign in");
	private By byEmail = By.name("session_key");
	private By byPassword = By.name("session_password");
	private By bySignIn = By.xpath("//button[@type='submit']");
	private By byError=By.id("error-for-username");
	
	
	@BeforeClass
	public void testSetup() {
		System.setProperty("webdriver.gecko.driver", "./src/test/resources/drivers/geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait=new WebDriverWait(driver,Duration.ofSeconds(10));
	}
	
	
	@DataProvider(name="inputData")
	public Object[][] getCellData() throws IOException{
		
		//locatte the excel file
		FileInputStream file = new FileInputStream(".\\src\\test\\resources\\sampledoc.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(file); //creating the workbook instance
		//go to desired sheet
		XSSFSheet s=wb.getSheet("Sheet1");
		
		int rowcount = s.getLastRowNum()+1;
		int cellcount = s.getRow(0).getLastCellNum();
		
		Object data[][]	= new Object[rowcount][cellcount];
		for(int i=1;i<rowcount;i++) {
			Row r = s.getRow(i);
			for(int j=0;j<cellcount;j++) {
				Cell c = r.getCell(j);
				data[i][j]=c.getStringCellValue();
			}
		}
		wb.close();
		return data;
	}
	
	@Test(dataProvider="inputData")
	public void verifyInvalidLogin(String username, String password) {
		
		driver.get(appURL);
		driver.findElement(bySignInLink).click();
		driver.findElement(byEmail).sendKeys(username);
		driver.findElement(byPassword).sendKeys(password);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(bySignIn));
		driver.findElement(bySignIn).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(byError));
		String actualErrorMessage=driver.findElement(byError).getText();
		String requiredMessage="Please enter a valid username";
		Assert.assertEquals(actualErrorMessage, requiredMessage);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
