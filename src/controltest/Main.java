package controltest;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import control.ControllerEventListener;
import control.UsbController;
import control.UsbControllerManager;

public class Main extends JFrame {
	class EvtSingleButton implements ControllerEventListener {
		@Override
		public void handleEvent(long eventArg) {
			label2.setText("" + eventArg);
		}
	}
	
	class EvtH1 implements ControllerEventListener {
		@Override
		public void handleEvent(long eventArg) {
			String formattedEvent = String.format("%016x", eventArg);
			label.setText(formattedEvent);
			if (eventArg != 0){
				c1.addKeybind(eventArg, new EvtSingleButton());
				c1.removeUnmaskedCallback();
			}
		}
	}
	
	class EvtH2 implements ControllerEventListener {
		@Override
		public void handleEvent(long eventArg) {
			label2.setText("" + eventArg);
		}
	}
	
	UsbControllerManager manager;
	JLabel label;
	JLabel label2;
	UsbController c1;
	
	public Main(){
		manager = new UsbControllerManager();
		label = new JLabel("Loading..");
		label.setFont(new Font("Courier new", Font.BOLD, 34));
		label2 = new JLabel("Loading..");
		label2.setFont(new Font("Courier new", Font.BOLD, 34));
		this.setLayout(new BorderLayout());
		this.setSize(620, 240);
		this.add(label, BorderLayout.NORTH);
		this.add(label2, BorderLayout.SOUTH);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        manager.destroyManager();
		        System.exit(0);
		    }
		});
		
		c1 = manager.getUsbControllerList().get(0);
		c1.startController();
		c1.setUnmaskedCallback(new EvtH1());
		if (manager.getUsbControllerList().size() > 1){
			UsbController c2 = manager.getUsbControllerList().get(1);
			c2.setUnmaskedCallback(new EvtH2());
			c2.startController();
		}
		System.out.println(c1.getDefaultBitmaskValue());
	}
	
	public static void main(String [] args){
		new Main();
	}
}
