package jfj;


/**
 * Joystick.java
 * @author Toshinobu HARADA
 * @version 1.1 2007/12/08
 * Original Program Satoshi KONNO 1999
 */
public class Joystick {
	public static final int BUTTON1 = 0x0001;
	public static final int BUTTON2 = 0x0002;
	public static final int BUTTON3 = 0x0004;
	public static final int BUTTON4 = 0x0008;

	public static final int BUTTON_01 = 0x0001;
	public static final int BUTTON_02 = 0x0002;
	public static final int BUTTON_03 = 0x0004;
	public static final int BUTTON_04 = 0x0008;
	public static final int BUTTON_05 = 0x0010;
	public static final int BUTTON_06 = 0x0020;
	public static final int BUTTON_07 = 0x0040;
	public static final int BUTTON_08 = 0x0080;
	public static final int BUTTON_09 = 0x0100;
	public static final int BUTTON_10 = 0x0200;
	public static final int BUTTON_11 = 0x0400;
	public static final int BUTTON_12 = 0x0800;
	public static final int BUTTON_13 = 0x1000;
	public static final int BUTTON_14 = 0x2000;
	public static final int BUTTON_15 = 0x4000;
	public static final int BUTTON_16 = 0x8000;

	public static final int BUTTON_TRI = 0x0001;
	public static final int BUTTON_CIR = 0x0002;
	public static final int BUTTON_CRS = 0x0004;
	public static final int BUTTON_RCT = 0x0008;
	public static final int BUTTON_L2 = 0x0010;
	public static final int BUTTON_R2 = 0x0020;
	public static final int BUTTON_L1 = 0x0040;
	public static final int BUTTON_R1 = 0x0080;
	public static final int BUTTON_START = 0x0100;
	public static final int BUTTON_SELECT = 0x0200;
	public static final int BUTTON_L3 = 0x0400;
	public static final int BUTTON_R3 = 0x0800;
	public static final int BUTTON_UP = 0x1000;
	public static final int BUTTON_RIGHT = 0x2000;
	public static final int BUTTON_DOWN = 0x4000;
	public static final int BUTTON_LEFT = 0x8000;
	

	static {
		System.loadLibrary("./res/dll/joystick");
	}		
	
	private int joyID = 0;
	
	public Joystick(int id) {
		joyID = id;
	}

	public native int getNumDevs();
	public native float getXPos(int id);
	public native float getYPos(int id);
	public native float getZPos(int id);
	public native int getButtons(int id);

	public float getXPos() {
		return getXPos(joyID);
	}
	
	public float getYPos() {
		return getYPos(joyID);
	}
	
	public float getZPos() {
		return getZPos(joyID);
	}
	
	public int getButtons() {
		return getButtons(joyID);
	}
	
	public String toString() {
		return "Joystick ID: " + this.joyID;
	}
	public int getID() {
		return this.joyID;
	}
}
