package runjar;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class runAppiumTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		
		System.out.println("Test Running...");
		
		//Grab file - device info
		File appDir = new File("src");
		File app = new File(appDir, "1-1app-debug.apk");
//		testNo = 4000 + testNo;
		
		//Setup as android device
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
		
		//Run app
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "20");  //only wait 20 sec
		cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		AndroidDriver driver = null;
		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + 4723 + "/wd/hub"), cap);
			//Close app
			waitInSeconds(2);
			driver.closeApp();
			waitInSeconds(2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitInSeconds(15);
		System.out.println("Test Finished Running");
	}

    static void waitInSeconds(int seconds) {

        // Need to wait for command to finish
        try {
            Thread.sleep(1000 * seconds); // 1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
	
}
