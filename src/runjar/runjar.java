package runjar;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import junit.framework.TestResult;
import junit.textui.TestRunner;

public class runjar {

	static File target = new File("C:/Work/Github/mcb-adbservice/logs");
	static File nodeHome = new File("C:/Work/Appium/Appium");
	static File appiumHome = new File("C:/Work/Appium/Appium/node_modules/appium");
	
	@SuppressWarnings("resource")
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub

//		String DeviceId = "0915f940d8211d04"; //Samsung Note 5
//		int Port = 1;
//		
//		//Start Appium
//		try {
//			startAppium(DeviceId, Port);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		//New run junit code - works good time to move on.
//		JUnitCore junit = new JUnitCore();
//		Result result = junit.run(runAppiumTest.class);  //Run appium test here
//		System.out.println(result.wasSuccessful());
		
		

//		URL url = new URL("file:///c:/Work/runAppiumTest.jar");
//		URLClassLoader loader = new URLClassLoader(new URL[]{url});
//		loader.loadClass("runjar.runAppiumTest");
//		TestRunner runner = new TestRunner();
//		TestResult result = runner.start(new String[]{"runjar.runAppiumTest"});
//		System.out.println(result.toString());
		
		// Getting the jar URL which contains target class
		URL[] classLoaderUrls = new URL[]{new URL("file:///c:/Work/simpleappiumtest2.jar"),
				new URL("file:///c:/Work/simpleappiumtest2.jar")};
		//Code to run junit test
		URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);
		 // Load the target class
		Class<?> appiumClass = urlClassLoader.loadClass("simpleapp.simpleappium");
		// Run the class using JUnitCore
		JUnitCore junit = new JUnitCore();
		Result result = junit.run(appiumClass);
		//Display if test was successful
		System.out.println(result.wasSuccessful());
		
		System.out.println("Tests run: " + result.getRunCount());
		System.out.println("Tests failed: " + result.getFailureCount());
        for (Failure failure : result.getFailures()) {
        	System.out.println("Failure: " + failure.toString());
        	System.out.println("Failure desc: " + failure.getDescription());
        	System.out.println("Failure excp: " + failure.getException());
        	System.out.println("Failure trace: " + failure.getTrace());
        }
		
		
////		Code to run app not test
//		//Create a new URLClassLoader
//		URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);
//		// Load the target class
//		Class<?> beanClass = urlClassLoader.loadClass("simpleapp.simpleappium");
//		// Create a new instance from the loaded class
//		Constructor<?> constructor = beanClass.getConstructor();
//		Object beanObj = constructor.newInstance();
//		// Getting a method from the loaded class and invoke it
//		Method method = beanClass.getMethod("test");
//		method.invoke(beanObj);

		
		

	}

	@Test
	static void runAppiumTest(int testNo){
		
		System.out.println("Test Running...");
		
		//Grab file - device info
		File appDir = new File("src");
		File app = new File(appDir, "1-1app-debug.apk");
		testNo = 4000 + testNo;
		
		//Setup as android device
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
		
		//Run app
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "20");  //only wait 20 sec
		cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		AndroidDriver driver = null;
		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + testNo + "/wd/hub"), cap);
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
	
    static void startAppium(String deviceID, int serverPort) throws Exception {
        try {
            System.out.println("Starting Appium server...");
            System.out.println("Node home: " + nodeHome);
            System.out.println("Appium home: " + appiumHome);
            String nodeBin;
            nodeBin = "node.exe";
            serverPort = 4000 + serverPort;
            int bootstrapPort = 2000 + serverPort;
             
            if (nodeHome != null) {
                File nodeBinFile = new File(nodeHome, nodeBin);
                if (!nodeBinFile.exists()) {
                    throw new Exception("Node binary does not exist: " + nodeBinFile);
                }
                nodeBin = nodeBinFile.getAbsolutePath();
            }
            System.out.println("Node bin: " + nodeBin);
            File appiumJsFile = new File(appiumHome.getAbsoluteFile(), "bin" + File.separator + "appium.js");
            System.out.println("Appium js: " + appiumJsFile);
            if (!appiumJsFile.exists()) {
                throw new Exception("Appium js does not exist: " + appiumJsFile);
            }
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(nodeBin, appiumJsFile.getAbsolutePath(), "--log-timestamp", "--log", 
            		new File(target, "appiumMCBLogger.txt").getAbsolutePath(), 
            		"-p " + serverPort + " -bp " + bootstrapPort + " -U " + deviceID);
            
            processBuilder.redirectError(new File(target, "appiumErrorMCBLogger.txt"));
            processBuilder.redirectOutput(new File(target, "appiumOutputMCBLogger.txt"));
            System.out.println("Appium server commands " + processBuilder.command());
            Process process = processBuilder.start();
//            if (!Processes.newPidProcess(process).isAlive()) {
//                throw new Exception("Failed to start Appium server");
//            }
//            int pid = PidUtil.getPid(process);
//            this.pid = pid;
            System.out.println("Appium server started");
//            System.out.println("Appium server PID " + pid);
            //Dumb way to sleep until appium starts - file watcher would be better
            Thread.sleep(5000);
        } catch (Exception ex) {
            throw new Exception("Failed to start Appium server", ex);
        }
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























//System.out.println("Runjar has run");
//String s;
//Scanner in = new Scanner(System.in);

//  System.out.println("Enter a string");
//  s = in.nextLine();
//  System.out.println("You entered string "+s);
  
// Run a java app in a separate system process
//  Process proc = null;
//try {
//	proc = Runtime.getRuntime().exec("java -jar androidtest.jar");
//} catch (IOException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//  // Then retreive the process output
//  InputStream in = proc.getInputStream();
//  InputStream err = proc.getErrorStream();






















