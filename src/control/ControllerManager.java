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
	
	private ArrayList<ProductVendor> allowedProductVendorList;
	private ArrayList<UsbController> controllerList;
	private DeviceList deviceList;
	private Context libUsbContext;
	
	private ControllerManager(){
		libUsbContext = new Context();
		int result = LibUsb.init(libUsbContext);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to initialize libusb.", result);		
		}
		
		loadAllowedUsbDeviceList(DEFAULT_USB_PRODUCT_VENDOR_FILE);
		loadUsbDevices();
		filterUsbDevices();
	}
	
	public void destroyManager(){
		if (controllerList != null){
			for (UsbController controller : controllerList){
				controller.stopController();
			}
		}
		if (deviceList != null){
			LibUsb.freeDeviceList(deviceList, true);
		}
		LibUsb.exit(libUsbContext);
	}
	
	public static ControllerManager getInstance(){
		return INSTANCE;
	}
	
	public ArrayList<UsbController> getUsbControllerList(){
		if (controllerList == null){
			filterUsbDevices();
		}
		return controllerList;
	}
	
	public void loadAllowedUsbDeviceList(String path){
		allowedProductVendorList = new ArrayList<ProductVendor>();
		
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
				allowedProductVendorList.add(new ProductVendor(product, vendor));
			}
			br.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public String printUsbDeviceList(boolean isPrintedToStdOut)
	{
		StringBuilder deviceListStringBuilder = new StringBuilder();
		for (Device device: deviceList){
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
	
	private void filterUsbDevices(){
		if (controllerList == null){
			controllerList = new ArrayList<UsbController>();
		}
		if (allowedProductVendorList == null){
			allowedProductVendorList = new ArrayList<ProductVendor>();
			loadAllowedUsbDeviceList(DEFAULT_USB_PRODUCT_VENDOR_FILE);
		}
		
		for (Device device : deviceList){
			DeviceDescriptor descriptor = new DeviceDescriptor();
			int result = LibUsb.getDeviceDescriptor(device, descriptor);
			if (result != LibUsb.SUCCESS){
				throw new LibUsbException("Unable to read device descriptor", result);
			}
			
			for (ProductVendor pair : allowedProductVendorList){
				if (descriptor.idProduct() == pair.getProduct() && descriptor.idVendor() == pair.getVendor()){
					String bp = LibUsb.getBusNumber(device) + ":" + LibUsb.getPortNumber(device);
					controllerList.add(new UsbController(bp, libUsbContext, device, pair));
				}
			}
		}
	}
	
	private void loadUsbDevices(){
		if (deviceList != null){
			LibUsb.freeDeviceList(deviceList, true);
		}
		
		deviceList = new DeviceList();
	    int result = LibUsb.getDeviceList(libUsbContext, deviceList);
	    if (result < 0){
	    	throw new LibUsbException("Unable to get device list", result);
	    }
	}
}
