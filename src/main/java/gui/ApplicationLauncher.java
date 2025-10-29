package gui;

import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;

public class ApplicationLauncher {

	private static final Logger logger = Logger.getLogger(KotxeaGehituGUI.class.getName());
	
	public static void main(String[] args) {

		ConfigXML c = ConfigXML.getInstance();
		BLFactory blf = new BLFactory(); 
		
		logger.info(c.getLocale());

		Locale.setDefault(new Locale(c.getLocale()));

		System.out.println("Locale: " + Locale.getDefault());

		try {

			BLFacade appFacadeInterface;
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
			appFacadeInterface = blf.getBusinessLogicFactory(c.isBusinessLogicLocal());

			MainGUI.setBussinessLogic(appFacadeInterface);
			MainGUI a = new MainGUI();
			a.setVisible(true);

		} catch (Exception e) {
			// a.jLabelSelectOption.setText("Error: "+e.toString());
			// a.jLabelSelectOption.setForeground(Color.RED);

			System.out.println("Error in ApplicationLauncher: " + e.toString());
		}
		// a.pack();

	}

}
