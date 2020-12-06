package bgu.spl.mics.application;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Config;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
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

		InitializeMicroservices(config);


	}

	private static void InitializeMicroservices(Config config) {
		Thread[] threads=new Thread[5];
		threads[0]=new Thread(new LeiaMicroservice(config.getAttacks()));
		threads[1]=new Thread(new C3POMicroservice());
		threads[2]=new Thread(new HanSoloMicroservice());
		threads[3]=new Thread(new R2D2Microservice(config.getR2D2()));
		threads[4]=new Thread(new LandoMicroservice(config.getLando()));

		for (Thread t:threads){
			//should I start or run???
			t.start();
		}
	}
}
