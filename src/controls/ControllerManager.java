package controls;

import java.util.ArrayList;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import utils.Config;
import utils.ConfigManager;
import utils.Pair;
import utils.Paths;

public class ControllerManager {
	private static final ControllerManager INSTANCE = new ControllerManager();

	private ArrayList<AbstractController> allControllers = new ArrayList<AbstractController>();
	private ArrayList<Pair<Short, Short>> allowedUsbProductVendorList;
	private Context libUsbContext;
	private LwjglKeyboardController lwjglKeyboardController;
	private LwjglMouseController lwjglMouseController;
	private ArrayList<UsbController> usbControllerList;
	private DeviceList usbDeviceList;

	private ControllerManager() {

	}

	public void destroyManager() {
		for (AbstractController c : allControllers) {
			c.stopController();
		}

		if (usbControllerList != null) {
			for (UsbController controller : usbControllerList) {
				controller.stopController();
			}
		}
		if (usbDeviceList != null) {
			LibUsb.freeDeviceList(usbDeviceList, true);
			LibUsb.exit(libUsbContext);
		}
		if (lwjglKeyboardController != null) {
			lwjglKeyboardController.destroyController();
		}
	}

	private void filterUsbDevices() {
		if (usbControllerList == null) {
			usbControllerList = new ArrayList<UsbController>();
		}
		if (allowedUsbProductVendorList == null) {
			allowedUsbProductVendorList = new ArrayList<Pair<Short, Short>>();
			loadAllowedUsbDeviceList(Paths.ALLOWED_DEVICES);
		}

		for (Device device : usbDeviceList) {
			DeviceDescriptor descriptor = new DeviceDescriptor();
			int result = LibUsb.getDeviceDescriptor(device, descriptor);
			if (result != LibUsb.SUCCESS) {
				throw new LibUsbException("Unable to read device descriptor", result);
			}

			for (Pair<Short, Short> pair : allowedUsbProductVendorList) {
				if (descriptor.idProduct() == pair.key && descriptor.idVendor() == pair.value) {
					String bp = LibUsb.getBusNumber(device) + ":" + LibUsb.getPortNumber(device);
					usbControllerList.add(new UsbController(bp, libUsbContext, device, pair));
				}
			}
		}
	}

	public AbstractController getController(EController type) {
		return getController(type, 0);
	}

	public AbstractController getController(EController type, int index) {
		switch (type) {
		case LWJGLKEYBOARDCONTROLLER: {
			if (lwjglKeyboardController == null) {
				lwjglKeyboardController = new LwjglKeyboardController();
				allControllers.add(lwjglKeyboardController);
			}
			return lwjglKeyboardController;
		}
		case USBCONTROLLER: {
			if (usbDeviceList == null) {
				libUsbContext = new Context();
				int result = LibUsb.init(libUsbContext);
				if (result != LibUsb.SUCCESS) {
					throw new LibUsbException("Unable to initialize libusb.", result);
				}

				loadAllowedUsbDeviceList(Paths.ALLOWED_DEVICES);
				loadUsbDevices();
				filterUsbDevices();

				for (UsbController u : usbControllerList) {
					allControllers.add(u);
				}
			}
			if (usbDeviceList != null && index >= 0 && index < usbControllerList.size()) {
				return usbControllerList.get(index);
			}
			return null;
		}
		case LWJGLMOUSECONTROLLER: {
			if (lwjglMouseController == null) {
				lwjglMouseController = new LwjglMouseController();
				allControllers.add(lwjglMouseController);
			}
			return lwjglMouseController;
		}
		default: {
			// Invalid request,
			return null;
		}
		}
	}

	public static ControllerManager getInstance() {
		return INSTANCE;
	}

	public ArrayList<UsbController> getUsbControllerList() {
		if (usbControllerList == null) {
			filterUsbDevices();
		}
		return usbControllerList;
	}

	public void loadAllowedUsbDeviceList(String path) {
		if (path == null) {
			return;
		}

		allowedUsbProductVendorList = new ArrayList<Pair<Short, Short>>();
		Config<String, String> cfg = ConfigManager.loadConfigAsPairs(Paths.ALLOWED_DEVICES);
		for (Pair<String, String> pair : cfg.contents) {
			allowedUsbProductVendorList.add(new Pair<Short, Short>(Short.parseShort(pair.key, 16), Short.parseShort(pair.value, 16)));
		}
	}

	private void loadUsbDevices() {
		if (usbDeviceList != null) {
			LibUsb.freeDeviceList(usbDeviceList, true);
		}

		usbDeviceList = new DeviceList();
		int result = LibUsb.getDeviceList(libUsbContext, usbDeviceList);
		if (result < 0) {
			throw new LibUsbException("Unable to get device list", result);
		}
	}

	public void pollControllers() {
		for (AbstractController c : allControllers) {
			c.pollController();
		}
	}

	public String printUsbDeviceList(boolean isPrintedToStdOut) {
		StringBuilder deviceListStringBuilder = new StringBuilder();
		for (Device device : usbDeviceList) {
			DeviceDescriptor descriptor = new DeviceDescriptor();
			int result = LibUsb.getDeviceDescriptor(device, descriptor);
			if (result != LibUsb.SUCCESS) {
				throw new LibUsbException("Unable to read device descriptor", result);
			}
			deviceListStringBuilder.append(descriptor.toString());
		}
		if (isPrintedToStdOut) {
			System.out.print(deviceListStringBuilder.toString());
		}
		return deviceListStringBuilder.toString();
	}
}
