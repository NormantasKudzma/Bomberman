package control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class ControllerManager{
	public static class ProductVendor {
		private short product;
		private short vendor;
		
		public ProductVendor(short p, short v){
			product = p;
			vendor = v;
		}
		
		public short getProduct(){
			return product;
		}
		
		public short getVendor(){
			return vendor;
		}
	}
	
	private static final String DEFAULT_USB_PRODUCT_VENDOR_FILE = "config\\AllowedDevices.dat";
	private static final ControllerManager INSTANCE = new ControllerManager();
	
	private ArrayList<ProductVendor> allowedUsbProductVendorList;
	private ArrayList<UsbController> usbControllerList;
	private DeviceList usbDeviceList;
	private Context libUsbContext;
	private LwjglKeyboardController lwjglKeyboardController;
	private ArrayList<AbstractController> allControllers = new ArrayList<AbstractController>();
	
	private ControllerManager(){
		
	}
	
	public void destroyManager(){
		for (AbstractController c : allControllers){
			c.stopController();
		}
		
		if (usbControllerList != null){
			for (UsbController controller : usbControllerList){
				controller.stopController();
			}
		}
		if (usbDeviceList != null){
			LibUsb.freeDeviceList(usbDeviceList, true);
			LibUsb.exit(libUsbContext);
		}
		if (lwjglKeyboardController != null){
			lwjglKeyboardController.destroyController();
		}
	}
	
	private void filterUsbDevices(){
		if (usbControllerList == null){
			usbControllerList = new ArrayList<UsbController>();
		}
		if (allowedUsbProductVendorList == null){
			allowedUsbProductVendorList = new ArrayList<ProductVendor>();
			loadAllowedUsbDeviceList(DEFAULT_USB_PRODUCT_VENDOR_FILE);
		}
		
		for (Device device : usbDeviceList){
			DeviceDescriptor descriptor = new DeviceDescriptor();
			int result = LibUsb.getDeviceDescriptor(device, descriptor);
			if (result != LibUsb.SUCCESS){
				throw new LibUsbException("Unable to read device descriptor", result);
			}
			
			for (ProductVendor pair : allowedUsbProductVendorList){
				if (descriptor.idProduct() == pair.getProduct() && descriptor.idVendor() == pair.getVendor()){
					String bp = LibUsb.getBusNumber(device) + ":" + LibUsb.getPortNumber(device);
					usbControllerList.add(new UsbController(bp, libUsbContext, device, pair));
				}
			}
		}
	}
	
	public AbstractController getController(EController type){
		return getController(type, 0);
	}
	
	public AbstractController getController(EController type, int index){
		switch (type){
			case LWJGLKEYBOARDCONTROLLER:{
				if (lwjglKeyboardController == null){
					lwjglKeyboardController = new LwjglKeyboardController();
					allControllers.add(lwjglKeyboardController);
				}
				return lwjglKeyboardController;
			}
			case USBCONTROLLER:{
				if (usbDeviceList == null){
					libUsbContext = new Context();
					int result = LibUsb.init(libUsbContext);
					if (result != LibUsb.SUCCESS){
						throw new LibUsbException("Unable to initialize libusb.", result);		
					}
					
					loadAllowedUsbDeviceList(DEFAULT_USB_PRODUCT_VENDOR_FILE);
					loadUsbDevices();
					filterUsbDevices();
					
					for (UsbController u : usbControllerList){
						allControllers.add(u);
					}
				}
				if (usbDeviceList != null && index >= 0 && index < usbControllerList.size()){
					return usbControllerList.get(index);
				}
				return null;
			}
			default:{
				// Invalid request,
				return null;
			}
		}
	}
	
	public static ControllerManager getInstance(){
		return INSTANCE;
	}
	
	public ArrayList<UsbController> getUsbControllerList(){
		if (usbControllerList == null){
			filterUsbDevices();
		}
		return usbControllerList;
	}
	
	public void loadAllowedUsbDeviceList(String path){
		allowedUsbProductVendorList = new ArrayList<ProductVendor>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path == null ? DEFAULT_USB_PRODUCT_VENDOR_FILE : path));
			String line;
			while ((line = br.readLine()) != null){
				if (line.isEmpty() || line.startsWith("#")){
					continue;
				}
				
				String [] params = line.split("=");
				if (params.length != 2){
					continue;
				}
				
				short product = Short.parseShort(params[0].substring(params[0].length() - 4), 16);
				short vendor = Short.parseShort(params[1].substring(params[1].length() - 4), 16);
				allowedUsbProductVendorList.add(new ProductVendor(product, vendor));
			}
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private void loadUsbDevices(){
		if (usbDeviceList != null){
			LibUsb.freeDeviceList(usbDeviceList, true);
		}
		
		usbDeviceList = new DeviceList();
	    int result = LibUsb.getDeviceList(libUsbContext, usbDeviceList);
	    if (result < 0){
	    	throw new LibUsbException("Unable to get device list", result);
	    }
	}
	
	public void pollControllers(){
		for (AbstractController c : allControllers){
			c.pollController();
		}
	}
	
	public String printUsbDeviceList(boolean isPrintedToStdOut)
	{
		StringBuilder deviceListStringBuilder = new StringBuilder();
		for (Device device: usbDeviceList){
			DeviceDescriptor descriptor = new DeviceDescriptor();
			int result = LibUsb.getDeviceDescriptor(device, descriptor);
			if (result != LibUsb.SUCCESS){
				throw new LibUsbException("Unable to read device descriptor", result);
			}
			deviceListStringBuilder.append(descriptor.toString());
        }
		if (isPrintedToStdOut){
			System.out.print(deviceListStringBuilder.toString());
		}
		return deviceListStringBuilder.toString();
	}
}
