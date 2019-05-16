package javaGUI;

import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class JavaGUITimer {

	private JFrame frmPetittimer;
	private JLabel label, label_1, label_2;
	private JButton btnStart, btnStop;
	private JTextField textField;
	
	private Timer countTimer;//カウント用タイマーcountTimerを宣言
	private long startTime;//開始時刻startTimeを宣言
	
	JFrame frame = new JFrame("HelloWorldSwing");
}
