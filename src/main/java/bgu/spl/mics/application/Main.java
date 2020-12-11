package bgu.spl.mics.application;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.Tester.Tester;
import bgu.spl.mics.application.passiveObjects.Config;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void otherMain(String[] args){
				Gson gson = new Gson();
		Reader reader = null;
		try {
			 reader = new FileReader(args[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Config config = gson.fromJson(reader, Config.class);
		Ewoks.getInstance().init(config.getEwoks());

		Thread[] threads=new Thread[5];

		InitializeMicroservices(config,threads);

		try {
			WaitForThreadsToFinish(threads);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		String output=gson.toJson(Diary.getInstance());
		try {
			Writer writer = new FileWriter(args[1]);
			writer.write(output);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Gson gson = new Gson();
		Reader reader = null;
		try {
			 reader = new FileReader(args[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Config config = gson.fromJson(reader, Config.class);
		Ewoks.getInstance().init(config.getEwoks());

		Thread[] threads=new Thread[5];

		InitializeMicroservices(config,threads);

		try {
			WaitForThreadsToFinish(threads);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		String output=gson.toJson(Diary.getInstance());
		try {
			Writer writer = new FileWriter(args[1]);
			writer.write(output);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Scanner in = new Scanner(System.in);
//		System.out.println("Type 3 to run logical tests");
//		System.out.println("Type 2 to generate tests into the tests.json file");
//		System.out.println("Type 1 to run the tests from the tests.json file");
//		int choosenOption = in.nextInt();
//		Tester myTester = new Tester();
//
//		if(choosenOption == 2)  myTester.generateTests();  else
//		if(choosenOption == 1)
//			myTester.runTestsFromFile();
//		else if(choosenOption == 3)
//			myTester.runLogicalTests();
//
	}

	private static void WaitForThreadsToFinish(Thread[] threads) throws InterruptedException {
		for(Thread t:threads){
			t.join();
		}
	}

	private static void InitializeMicroservices(Config config,Thread[] threads) {
		CountDownLatch count = new CountDownLatch((2));
		threads[0]=new Thread(new LeiaMicroservice(config.getAttacks(), count));
		threads[1]=new Thread(new C3POMicroservice(count));
		threads[2]=new Thread(new HanSoloMicroservice(count));
		threads[3]=new Thread(new R2D2Microservice(config.getR2D2()));
		threads[4]=new Thread(new LandoMicroservice(config.getLando()));

		for (Thread t:threads){
			t.start();
		}
	}
}
