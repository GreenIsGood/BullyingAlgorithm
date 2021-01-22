package App;

import java.awt.EventQueue;
import GUI.GUIForApp;

public class AppStarter extends GUIForApp {
	public static void main(String args[]) {
		GUIForApp window = new GUIForApp();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
