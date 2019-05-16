package jgc;

import jfj.Joystick;

/**
 * JsGameCanvasJP
 * JavaSwingApplication版ゲームを簡単に作るためのフレームワーク
 * ジョイスティック使用可能バージョン
 * ＰＳ２用コントローラに完全対応（H19/12/09）
 * コントローラ関連メソッドを改良（H20/01/25）
 * ＰＳ２用コントローラに本当に完全対応（H20/03/03）
 * @version 1.5 (H20/03/03)
 * @author HARADA Toshinobu
 */
public abstract class JsGameCanvasJP extends JsGameCanvas {
	public static final int BUTTON1 = Joystick.BUTTON1;
	public static final int BUTTON2 = Joystick.BUTTON2;
	public static final int BUTTON3 = Joystick.BUTTON3;
	public static final int BUTTON4 = Joystick.BUTTON4;
	/** BUTTON_TRIは△ボタンを定義したクラスフィールド */
	public static final int BUTTON_TRI = Joystick.BUTTON_TRI;
	/** BUTTON_CIRは○ボタンを定義したクラスフィールド */
	public static final int BUTTON_CIR = Joystick.BUTTON_CIR;
	/** BUTTON_CRSは×ボタンを定義したクラスフィールド */
	public static final int BUTTON_CRS = Joystick.BUTTON_CRS;
	/** BUTTON_RCTは□ボタンを定義したクラスフィールド */
	public static final int BUTTON_RCT = Joystick.BUTTON_RCT;
	/** BUTTON_L2はL2ボタンを定義したクラスフィールド */
	public static final int BUTTON_L2 = Joystick.BUTTON_L2;
	/** BUTTON_R2はR2ボタンを定義したクラスフィールド */
	public static final int BUTTON_R2 = Joystick.BUTTON_R2;
	/** BUTTON_L1はL1ボタンを定義したクラスフィールド */
	public static final int BUTTON_L1 = Joystick.BUTTON_L1;
	/** BUTTON_R1はR1ボタンを定義したクラスフィールド */
	public static final int BUTTON_R1 = Joystick.BUTTON_R1;
	/** BUTTON_STARTはSTARTボタンを定義したクラスフィールド */
	public static final int BUTTON_START = Joystick.BUTTON_START;
	/** BUTTON_SELECTはSELECTボタンを定義したクラスフィールド */
	public static final int BUTTON_SELECT = Joystick.BUTTON_SELECT;
	/** BUTTON_L3はL3ボタンを定義したクラスフィールド */
	public static final int BUTTON_L3 = Joystick.BUTTON_L3;
	/** BUTTON_R3はR3ボタンを定義したクラスフィールド */
	public static final int BUTTON_R3 = Joystick.BUTTON_R3;

	public static final int BUTTON_01 = Joystick.BUTTON_TRI;
	public static final int BUTTON_02 = Joystick.BUTTON_CIR;
	public static final int BUTTON_03 = Joystick.BUTTON_CRS;
	public static final int BUTTON_04 = Joystick.BUTTON_RCT;
	public static final int BUTTON_05 = Joystick.BUTTON_L2;
	public static final int BUTTON_06 = Joystick.BUTTON_R2;
	public static final int BUTTON_07 = Joystick.BUTTON_L1;
	public static final int BUTTON_08 = Joystick.BUTTON_R1;
	public static final int BUTTON_09 = Joystick.BUTTON_START;
	public static final int BUTTON_10 = Joystick.BUTTON_SELECT;
	public static final int BUTTON_11 = Joystick.BUTTON_L3;
	public static final int BUTTON_12 = Joystick.BUTTON_R3;

	/** BUTTON_UPは↑ボタンを定義したクラスフィールド */
	public static final int BUTTON_UP = Joystick.BUTTON_UP;
	/** BUTTON_RIGHTは→ボタンを定義したクラスフィールド */
	public static final int BUTTON_RIGHT = Joystick.BUTTON_RIGHT;
	/** BUTTON_DOWNは↓ボタンを定義したクラスフィールド */
	public static final int BUTTON_DOWN = Joystick.BUTTON_DOWN;
	/** BUTTON_LEFTは←ボタンを定義したクラスフィールド */
	public static final int BUTTON_LEFT = Joystick.BUTTON_LEFT;

	private Joystick joystick;
	private long nowJoyState;
	private long beforeJoyState;

	protected final void jgcGameInitBefore() {
		joystick = new Joystick(0);
		nowJoyState = 0L;
		beforeJoyState = 0L;
	}

	protected final void jgcGameMainBefore() {
		//現在のジョイスティックボタン状態の取得
		nowJoyState = 0L;
		nowJoyState = joystick.getButtons();
		if( joystick.getYPos(0) <= -0.4f ) {
			nowJoyState = BUTTON_UP | nowJoyState;
		}
		if( joystick.getYPos(0) >=  0.4f ) {
			nowJoyState = BUTTON_DOWN | nowJoyState;
		}
		if( joystick.getXPos(0) <= -0.4f ) {
			nowJoyState = BUTTON_LEFT | nowJoyState;
		}
		if( joystick.getXPos(0) >=  0.4f ) {
			nowJoyState = BUTTON_RIGHT | nowJoyState;
		}
	}

	protected final void jgcGameMainAfter() {
		beforeJoyState = nowJoyState; //ジョイスティックボタン状態の更新

		//スタート＆セレクトが両方押されていたらゲーム終了
		if( jgcGetJoyState(BUTTON_SELECT) && jgcGetJoyState(BUTTON_START) ) {
			this.jgcExit();
		}
	}

	/**
	 * 指定したボタンが押されているかを返す。JsGameCanvasJP.BUTTON_**
	 * △:_TRI, _01, BUTTON1, ○:_CIR, _02, BUTTON2
	 * ×:_CRS, _03, BUTTON3, □:_RCT, _04, BUTTON4
	 * L1:_L1, L2:_L2, L3:_L3, R1:_R1, R2:_R2, L3:_L3
	 * START:_START, SELECT:_SELECT
	 * ↑:_UP, →:_RIGHT, ↓:DOWN, ←:LEFT
	 * @return 指定したボタンが押されているかを論理値で返す
	 * */
	public final boolean jgcGetJoyState(int button) {
		return 0 != (button & nowJoyState) ;
	}

	/**
	 * 指定したボタンがそのとき押されたかを返す。
	 * △:_TRI, _01, BUTTON1, ○:_CIR, _02, BUTTON2
	 * ×:_CRS, _03, BUTTON3, □:_RCT, _04, BUTTON4
	 * L1:_L1, L2:_L2, L3:_L3, R1:_R1, R2:_R2, L3:_L3
	 * START:_START, SELECT:_SELECT
	 * ↑:_UP, →:_RIGHT, ↓:DOWN, ←:LEFT
	 * @return 指定したボタンがそのとき押されたかを論理値で返す
	 * */
	public final boolean jgcGetJoyTrigger(int button) {
		boolean flag = ( 0 != ( button & beforeJoyState ) );
		return !flag & this.jgcGetJoyState(button);
	}

	/**
	 * アナログスティックのｘ成分の傾き値を返す。
	 * ← -1.0f ～ 0.0f ～ 1.0f →
	 * @return ｘ成分の傾き
	 */
	public final float jgcGetJoyPosX() {
		return joystick.getXPos(0);
	}

	/**
	 * アナログスティックのｙ成分の傾き値を返す。
	 * ↑ -1.0f ～ 0.0f ～ 1.0f ↓
	 * @return ｙ成分の傾き
	 */
	public final float jgcGetJoyPosY() {
		return joystick.getYPos(0);
	}

	/**
	 * ボタンの状態値を返す。
	 * @return ボタンの状態値
	 */
	public final float jgcGetJoyButtons() {
		return joystick.getButtons(0);
	}
}