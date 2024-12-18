//package com.usth_connect.vpn_server_backend_usth.service;
//
//import com.usth_connect.vpn_server_backend_usth.entity.Student;
//import com.usth_connect.vpn_server_backend_usth.repository.StudentRepository;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chromium.ChromiumOptions;
//import org.openqa.selenium.chromium.ChromiumDriver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import java.time.Duration;
//
//
//@Service
//public class SipRegistrationService {
//    private static final String SIP2SIP_REGISTRATION_URL = "https://mdns.sipthor.net/register_sip_account.phtml";
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    public String registerSipAccount(String studentId, String username, String name, String password, String email) {
//        // Setup Chrome options for headless operation
//        ChromeOptions options = new ChromiumOptions();
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");
//        System.setProperty("webdriver.chrome.driver", "D:\\Download\\chrome-headless-shell-win64\\chrome-headless-shell.exe");
//
//        // Create a WebDriver instance
//        WebDriver driver = new ChromiumDriver(options);
//        try {
//            // Open the Linphone registration page
//            driver.get(SIP2SIP_REGISTRATION_URL);
//
//            // Fill out the form with the provided email and password
//            driver.findElement(By.name("username")).sendKeys(username);
//            driver.findElement(By.name("password")).sendKeys(password);
//            driver.findElement(By.name("password_confirm")).sendKeys(password);
//            driver.findElement(By.name("first_name")).sendKeys(name);
//            driver.findElement(By.name("email")).sendKeys(email);
//
//            // Submit the form
//            driver.findElement(By.name("submit")).click();
//
//            // Wait for the registration to complete, based on a page change or success message
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".success-message"))); // Adjust selector as needed
//
//            // If successful, update the student entity with SIP information
//            Student student = studentRepository.findById(studentId)
//                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
//
//            student.setSipUsername(username);
//            student.setSipPassword(password);
//            student.setSipDomain("sip2sip.info");
//
//            studentRepository.save(student);
//
//            return "SIP account registered successfully and saved to the database!";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to register SIP account via web automation: " + e.getMessage());
//        } finally {
//            driver.quit();  // Close the browser
//        }
//    }
//}
