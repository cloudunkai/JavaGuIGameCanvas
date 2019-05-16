package jgc;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * JsGameCanvas JavaSwingApplication版ゲームを簡単に作るためのフレームワーク
 * @version 1.80 (H20/02/27)
 * @author HARADA Toshinobu, ISHIDUKA Ryuhei
 */
/*
 * H19/05/22 フルスクリーンモードを追加した。
 * H19/10/20 フルスクリーンモード追加に伴い、jgcStartメソッドを多重定義した。
 * H20/02/27 フルスクリーンモード時にマウスカーソルを表示しないようにした。
 * H20/02/27 ゲーム終了時jgcExitで、スレッド終了を待つのがよろしくないかも。
 * H20/05/25 時間計算にSystem.currentTimeMillisではなく、より精度の高いSystem.nanoTimeを使うことにした。
 */
public abstract class JsGameCanvas extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener {
	/** WAIT_10 10.000000msウェイト（FPS100） */
	public static final long WAIT_10 = 10000000L;
	/** WAIT_16 16.666667msウェイト（FPS 60） */
	public static final long WAIT_16 = 16666667L;
	/** WAIT_20 20.000000msウェイト（FPS 50） */
	public static final long WAIT_20 = 20000000L;
	/** WAIT_33 33.333333msウェイト（FPS 30） */
	public static final long WAIT_33 = 33333333L;
	/** WAIT_40 40.000000msウェイト（FPS 25） */
	public static final long WAIT_40 = 40000000L;
	/** WAIT_50 50.000000msウェイト（FPS 20） */
	public static final long WAIT_50 = 50000000L;
	/** FONT_DEF デフォルトフォント */
	public static final Font FONT_DEF = new Font("", Font.PLAIN, 12);
	/** SCREEN_WINDOW ウインドウモード */
	public static final int SCREEN_WINDOW = 0;
	/** SCREEN_FULL フルスクリーンモード（マウスカーソル無し） */
	public static final int SCREEN_FULL = 1;
	/** SCREEN_FULL2 フルスクリーンモード（マウスカーソル有り） */
	public static final int SCREEN_FULL2 = 2;

	private long fpsCalcTime; //FPS算出用時間計測変数
	private long fps; //FPS
	private long aWaitTime; //実ゲームウェイト
	private long iWaitTime; //想定ゲームウェイト

	private long gameTime; //ゲーム実行時間（単位ms）
	private long firstTime;
	private long frameCounter;
	private Thread gameThread;
	private boolean gameFlag;
	private boolean gameStateViewFlag;

	private MediaTracker mediaTracker;

	private long keyStateH;
	private long keyStateL;
	private long nowKeyStateH;
	private long nowKeyStateL;
	private long beforeKeyStateH;
	private long beforeKeyStateL;
	private int mouseState;
	private int nowMouseState;
	private int beforeMouseState;
	private int mouseCursorX;
	private int mouseCursorY;

	/**
	 * setWaitTime ゲームウェイト設定メソッド
	 * @param waitTime 設定したいゲームウェイト（WAIT_10, 16, 20, 33, 40, 50）
	 */
	public void setWaitTime(long waitTime) {
		if((WAIT_10 <= waitTime) && (waitTime <= WAIT_50) ) {
			iWaitTime = waitTime; //設定可能範囲で想定ゲームウェイトを設定
		}
	}
	/**
	 * getFPS 毎秒ごとのＦＰＳ値を返すメソッド
	 * @return FPS 毎秒ごとのＦＰＳ値
	 */
	public long getFPS() {   return fps;   }
	/**
	 * getGameTime ゲーム開始時からの経過時間を返すメソッド
	 * @return ゲーム経過時間
	 */
	public long getGameTime() { return gameTime; }
	/* アクセスメソッド */
	private final long getKeyStateH() { return keyStateH; }
	private final long getKeyStateL() { return keyStateL; }
	private final int getMouseState() { return mouseState; }

	public Thread getGameThread() { return this.gameThread; }

	/* コンストラクタ */
	public JsGameCanvas() {
		firstTime = 0L;

		this.setWaitTime(WAIT_16); //初期設定は16.67msウェイトFPS60相当

		//各キー状態変数の初期化
		keyStateH = keyStateL = 0;
		nowKeyStateH = nowKeyStateL = beforeKeyStateH = beforeKeyStateL = 0;
		//マウス状態変数の初期化
		mouseState = nowMouseState = beforeMouseState = 0;
		mouseCursorX = this.jgcGetMouseCursorX();
		mouseCursorY = this.jgcGetMouseCursorY();

		this.addKeyListener(this); //キーリスナーの登録
		this.addMouseListener(this); //マウスリスナーの登録
		this.addMouseMotionListener(this); //マウスモーションリスナーの登録
		this.setFocusable(true); //フォーカスのセット
		this.setSize(640, 480);

		this.setBackground(Color.BLACK); //背景色のセット

		this.mediaTracker = new MediaTracker(this);

		this.gameStateViewFlag = false;

		this.jgcGameInitBefore(); //拡張用ゲーム初期化直前処理
		this.jgcGameInit(); //ゲーム初期化処理

		this.gameTime = 0;
		this.frameCounter = 0;

		this.fpsCalcTime = 0L;

		gameFlag = true;
		gameThread = new Thread(this);
		gameThread.start();
	}


	/**
	 * 指定したゲームキャンバスオブジェクトを実行する。
	 * @param title (タイトルバーに表示するゲームタイトル（String型）)
	 * @param jgc (実行するゲームキャンバス（JsGameCanvas）)
	 * @param screenMode （ウインドウモード（SCREEN_WINDOW）かフルスクリーンモード（SCREEN_FULL）か）
	 */
	public static final void jgcStart(String title, JsGameCanvas jgc, int screenMode) {
		//スクリーンモード設定のための処理
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		//フレームを生成
		JFrame jframe = new JFrame(title, gc);
		jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//右上×では終了しないように設定。エスケープキーのみで終了。
		jframe.setSize(646, 505); //ウインドウのサイズを決定
		jframe.setBounds(100, 100, 646, 505); //ウインドウの表示位置とサイズを設定
		jframe.setResizable(false); //ウインドウのサイズ変更を不可に設定
		if(screenMode == JsGameCanvas.SCREEN_WINDOW) {
			jframe.setUndecorated(false); //ウインドウの枠を表示に設定
		} else {
			jframe.setUndecorated(true); //ウインドウの枠を非表示に設定
		}
		jframe.getContentPane().add(jgc); //ゲームキャンバスを追加
		Image icon = jgc.jgcLoadImage("icon.gif", jgc);
		if(icon != null) {
			jframe.setIconImage(icon);
		}
		if( !(screenMode == SCREEN_WINDOW) ) {
			//フルスクリーン時は、マウスカーソルを表示しないようにする。
			if( screenMode == SCREEN_FULL ) {
				jframe.setCursor(
					Toolkit.getDefaultToolkit().createCustomCursor(
						jgc.jgcLoadImage("", jgc),
						new Point(0, 0),
						"NULL_CURSOR"
					)
				);
			}

			//フルスクリーンのための設定
			gd.setFullScreenWindow(jframe); //フレームをフルスクリーンモードに設定してから、
			gd.setDisplayMode( new DisplayMode(640, 480, 32, DisplayMode.REFRESH_RATE_UNKNOWN) ); //詳細（640*480, 32bit）を設定
		}
		jframe.setVisible(true); //ウインドウを表示
	}

	/**
	 * 指定したゲームキャンバスオブジェクトを実行する。
	 * @param title (タイトルバーに表示するゲームタイトル（String型）)
	 * @param jgc (実行するゲームキャンバス（JsGameCanvas）)
	 */
	public static final void jgcStart(String title, JsGameCanvas jgc) {
		JsGameCanvas.jgcStart(title, jgc, JsGameCanvas.SCREEN_WINDOW);
	}
	/**
	 * 指定したゲームキャンバスオブジェクトをフルスクリーンモードで実行する。
	 * @param title (タイトルバーに表示するゲームタイトル（String型）)
	 * @param jgc (実行するゲームキャンバス（JsGameCanvas）)
	 */
	public static final void jgcStartFullScreen(String title, JsGameCanvas jgc) {
		JsGameCanvas.jgcStart(title, jgc, JsGameCanvas.SCREEN_FULL);
	}

	/**
	 * ゲームを終了する。
	 */
	public final void jgcExit() {
		if(this.gameFlag) {
			this.gameFlag = false;
		}
		//たとえウインドウモードでも、とりあえずフルスクリーンモードを強制解除し、
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(null);
		//強制終了
		System.exit(0);
	}

	/**
	 * 拡張用初期化処理直前処理を定義できる。
	 */
	protected void jgcGameInitBefore() {

	}

	/**
	 * 各種データのロードなど、必要な初期化処理を行う。
	 */
	protected abstract void jgcGameInit();


	/**
	 * ゲームのシーン遷移やキャラクタの動作など、メイン処理を行う。
	 */
	protected abstract void jgcGameMain();

	/**
	 * ゲームメイン処理の直前に行う処理を定義できる。
	 *
	 */
	protected void jgcGameMainBefore() {

	}

	/**
	 * ゲームメイン処理の直後に行う処理を定義できる。
	 *
	 */
	protected void jgcGameMainAfter() {

	}

	/**
	 * ゲームの描画処理を行う。
	 * @param g (描画を行うグラフィックオブジェクト)
	 */
	protected abstract void jgcGameDraw(Graphics g);

	/**
	 * オーディオの停止やデータのセーブなど、必要な終了処理を行う。
	 */
	protected abstract void jgcGameFinish();

	/**
	 * ゲーム実行時間などのデバック情報表示のオンオフを設定する。
	 * @param flag
	 */
	public final void jgcSetGameStateView(boolean flag) {
		this.gameStateViewFlag = flag;
	}

	/**
	 * ゲーム実行時間などのデバック情報表示フラグを返す。
	 */
	public final boolean jgcGetGameStateView() {
		return this.gameStateViewFlag;
	}

	/**
	 * 現在のマウスカーソルのｘ座標を返す。
	 * @return 現在のマウスカーソルのx座標の値をint型で返す。
	 */
	public final int jgcGetMouseCursorX() {
		return this.mouseCursorX;
	}

	/**
	 * 現在のマウスカーソルのｙ座標を返す。
	 * @return 現在のマウスカーソルのy座標の値をint型で返す。
	 */
	public final int jgcGetMouseCursorY() {
		return this.mouseCursorY;
	}

	/**
	 * 現在のマウスカーソルの座標（ｘ，ｙ）を返す。
	 * @return 現在のマウスカーソルの座標（x, y）をPoint型で返す。
	 */
	public final Point jgcGetMouseCursor() {
		return new Point(this.jgcGetMouseCursorX(), this.jgcGetMouseCursorY());
	}

	/**
	 * なんらかのマウスボタンが押されているかを返す。
	 * @return なんらかのマウスボタンが押されているかを論理値で返す。
	 */
	public final boolean jgcGetMouseState() {
		return 0 != nowMouseState;
	}

	/**
	 * 指定したマウスボタンが押されているかを返す。
	 * @param mButton (MouseEventクラスのフィールドBUTTON1～3を指定する。（int型）)
	 * @return 指定したマウスボタンが押されているかを論理値で返す。
	 */
	public final boolean jgcGetMouseState(int mButton) {
		return ( 0 != ( (1<<mButton-1) & nowMouseState ) );
	}

	/**
	 * なんらかのマウスボタンがそのとき押されたかを返す。
	 * @return なんらかのマウスボタンがそのとき押されたかを論理値で返す。
	 */
	public final boolean jgcGetMouseTrigger() {
		return jgcGetMouseState() && (nowMouseState != beforeMouseState);
	}

	/**
	 * 指定したマウスボタンがそのとき押されたかを返す。
	 * @param mButton (MouseEventクラスのフィールドBUTTON1～3を指定する。（int型）)
	 * @return 指定したマウスボタンがそのときに押されたかを論理値で返す。
	 */
	public final boolean jgcGetMouseTrigger(int mButton) {
		return !( 0 != ( (1<<mButton-1)  & beforeMouseState ) ) & this.jgcGetMouseState(mButton);
	}

	/**
	 * なんらかのキーが押されているかを返す。ただし、一部のキーは判定不可。
	 * @return なんらかのキーが押されているかを論理値で返す
	 * */
	public final boolean jgcGetKeyState() {
		boolean flag = false;
		if( (0 != nowKeyStateL) || (0 != nowKeyStateH) ) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 指定したキーが押されているかを返す。ただし、一部のキーは判定不可。
	 * @param vkCode (KeyEVentクラスのフィールドVK_*を指定する。（int型）)
	 * @return 指定したキーが押されているかを論理値で返す
	 * */
	public final boolean jgcGetKeyState(int vkCode) {
		boolean flag = false;
		if(vkCode < 64) {
			if( 0 != ( (1<<vkCode) & nowKeyStateL )  ) {
				flag = true;
			}
		} else if(vkCode < 128) {
			if( 0 != ( (1<<(vkCode-64)) & nowKeyStateH )  ){
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * なんらかのキーがそのときに押されたかを返す。ただし、一部のキーは判定不可。
	 * @return なんらかのキーがそのときに押されたかを論理値で返す
	 * */
	public final boolean jgcGetKeyTrigger() {
		boolean flag = false;
		if(
			( (nowKeyStateL != 0) || (nowKeyStateH != 0) )
			&& ( (nowKeyStateL != beforeKeyStateL) || (nowKeyStateH != beforeKeyStateH))
		) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 指定したキーがそのときに押されたかを返す。ただし、一部のキーは判定不可。
	 * @param vkCode (KeyEVentクラスのフィールドVK_*を指定する。（int型）)
	 * @return 指定したキーがそのときに押されたかを論理値で返す
	 * */
	public final boolean jgcGetKeyTrigger(int vkCode) {
		boolean flag = false;
		if(vkCode < 64) {
			if( 0 != ( (1<<vkCode) & beforeKeyStateL )  ) {
				flag = true;
			}
		} else if(vkCode < 128) {
			if( 0 != ( (1<<(vkCode-64)) & beforeKeyStateH )  ) {
				flag = true;
			}
		}
		return !flag & this.jgcGetKeyState(vkCode);
	}

	/**
	 * 指定したイメージ配列をJsGameCanvasの持つMediaTrackerに登録する。
	 * @param image (登録するイメージ配列（Image[] 型）)
	 * @return 指定したイメージがすべて登録されたかどうかの論理値を返す。
	 * @deprecated jgcLoadImageの改良によりjgcSetMediaTrackerによる登録が不要になりました。
	 */
	public final boolean jgcSetMediaTracker(Image[] image) {
		for(int i=0; i<image.length; i=i+1) {
			mediaTracker.addImage(image[i], 0);
		}
		try {
			mediaTracker.waitForAll();
		} catch(InterruptedException ie) {
			System.out.println(ie);
		}
		return mediaTracker.isErrorAny();
	}
	/**
	 * 指定したイメージをJsGameCanvasの持つMediaTrackerに登録する。
	 * @param image (登録するイメージ（Image 型）)
	 * @return 指定したイメージが登録されたかどうかの論理値を返す。
	 * @deprecated jgcLoadImageの改良によりjgcSetMediaTrackerによる登録が不要になりました。
	 */
	public final boolean jgcSetMediaTracker(Image image) {
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForAll();
		} catch(InterruptedException ie) {
			System.out.println(ie);

		}
		return mediaTracker.isErrorAny();
	}


	/**
	 * 指定した画像ファイルからイメージを取得し、そのイメージを返す。
	 * @param dataFileName (イメージを取得したい画像ファイル名（String型）)
	 * @param jgc (利用対象となるオブジェクト，通常はthisでよい)
	 * @return 指定した画像ファイルのイメージを取得し、そのイメージを返す
	 */
	public final Image jgcLoadImage(String dataFileName, JsGameCanvas jgc) {
		Image img;
		try {
			URL url = new URL(this.jgcGetPathName(dataFileName, jgc));
			img = new ImageIcon(url).getImage();
			//img = Toolkit.getDefaultToolkit().getImage(url);
		} catch(MalformedURLException e) {
			System.err.println(e);
			System.err.println("イメージファイル入力エラー");
			img = null;
		}
		return img;
	}

	/**
	 * 指定したオーディオファイルからオーディオを取得し、そのオーディオクリップを返す。
	 * @param dataFileName (オーディオを取得したいオーディオファイル名（String型）)
	 * @param jgc (利用対象となるオブジェクト，通常はthisでよい)
	 * @return 指定したオーディオファイルのイメージを取得し、そのオーディオクリップを返す
	 */
	public final AudioClip jgcLoadAudioClip(String dataFileName, JsGameCanvas jgc) {
		AudioClip ac;
		try {
			URL url = new URL(this.jgcGetPathName(dataFileName, jgc));
			ac = Applet.newAudioClip(url);
		} catch(Exception e) {
			System.err.println("オーディオファイル入力エラー: "+e);
			ac = null;
		}
		return ac;
	}

	/**
	 * 指定したテキストファイルの文字列データを取得し、String型配列で返す。
	 * @param dataFileName (データを取得したいテキストファイル名（String型）)
	 * @param jgc (利用対象となるオブジェクト，通常はthisでよい)
	 * @return 指定したテキストファイルから取得した文字列配列を返す
	 */
	public final String[] jgcLoadTextFile(String dataFileName, JsGameCanvas jgc) {
		ArrayList<String> stringList = new ArrayList<String>();
		try {
			URL url = new URL(this.jgcGetPathName(dataFileName, jgc));
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader br =new BufferedReader(isr);
			String inStr = null;
			do {
				inStr = br.readLine();
				if(inStr != null) {
					stringList.add(inStr);
				}
			} while(inStr != null);
			br.close();
		} catch(Exception e) {
			System.err.println("テキストファイル入力エラー: "+e);
			stringList = null;
		}
		String[] stringArray;
		if(stringList != null) {
			stringArray = new String[ stringList.size() ];
			for(int i=0; i<stringArray.length; i=i+1) {
				stringArray[i] = (String)(stringList.get(i));
			}
		} else {
			stringArray = new String[0];
		}
		return stringArray;
	}

	/**
	 * 指定したファイルの絶対パスを返す。
	 * @param dataFileName (パスを取得したいデータファイル名（String型）)
	 * @param jgc (利用対象となるオブジェクト，通常はthisでよい)
	 * @return そのファイルのパスを文字列で返す
	 */
	public final String jgcGetPathName(String dataFileName, JsGameCanvas jgc) {
		StringBuffer classStrBuf = new StringBuffer( jgc.getClass().getName() );
		int dotIndex = classStrBuf.lastIndexOf(".");
		if(0 <= dotIndex) {
			classStrBuf.replace(0, dotIndex+1, "");
		}
		URL url = jgc.getClass().getResource(classStrBuf.append(".class").toString());
		StringBuffer urlStrBuf = new StringBuffer(url.toString());
		int startIndex = urlStrBuf.lastIndexOf("/bin/");
		if(startIndex < 0) {
			startIndex = urlStrBuf.lastIndexOf("!/");
			startIndex = startIndex + 1;
			urlStrBuf.replace(startIndex, urlStrBuf.length(), "/");
		} else {
			urlStrBuf.replace(startIndex, urlStrBuf.length(), "/res/");
		}
		urlStrBuf.append(dataFileName);
		return urlStrBuf.toString();
	}

	/**
	 * 指定したGraphicsオブジェクトに指定したアルファ値を設定する。
	 * @param g アルファ値を設定したいGraphicsオブジェクト
	 * @param alpha 設定するアルファ値
	 * @return アルファ値を正しく設定できたかどうかを返す
	 */
	public static final boolean jgcDrawAlphaOn(Graphics g, float alpha) {
		boolean flag = false;
		if((0.0f<=alpha) && (alpha<=1.0f)) {
			( (Graphics2D)g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			flag = true;
		}
		return flag;
	}

	/**
	 * 指定したGraphicsオブジェクトのアルファ値を解除する。
	 * @param g アルファ値設定を解除したいGraphicsオブジェクト
	 */
	public static final void jgcDrawAlphaOff(Graphics g) {
		( (Graphics2D)g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}



	/* paintメソッド */
	public final void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(gameFlag) {
			this.jgcGameDraw(g); //ゲーム描画処理
		}
	}
	/* runメソッド */
	public final void run() {
		this.firstTime = System.nanoTime();
		this.fpsCalcTime = System.nanoTime();
		this.repaint();
		while(gameFlag) {
			long startTime = System.nanoTime(); //フレーム開始時間の取得
			nowKeyStateH = this.getKeyStateH(); //現在のキー状態の取得
			nowKeyStateL = this.getKeyStateL();
			nowMouseState = this.getMouseState(); //現在のマウス状態の取得

			this.jgcGameMainBefore(); //ゲームメイン直前処理

			this.jgcGameMain(); //ゲームメイン処理
			this.repaint(); //再描画

			this.jgcGameMainAfter(); //ゲームメイン直後処理

			beforeKeyStateH = nowKeyStateH; //キー状態の更新
			beforeKeyStateL = nowKeyStateL;
			beforeMouseState = nowMouseState; //マウス状態の更新


			long frameTime = System.nanoTime() - startTime;
			//実ゲームウェイトを算出（単位はナノ秒 （1ns = 1000000ms））
			//実ゲームウェイト ＝ 想定ゲームウェイト － 毎フレーム処理時間
			aWaitTime = iWaitTime - frameTime;
			if(aWaitTime < 0) { //実ゲームウェイトが０未満なら、ウェイトを０にする。
				aWaitTime = 0;
			}

			//実待ち時間ウェイト処理
			long sTime = System.nanoTime();
			while( ((System.nanoTime() - sTime)) < aWaitTime);

			//現在のゲーム実行時間を算出
			gameTime = (System.nanoTime() - firstTime) / 1000000L;

			//FPS算出処理
			frameCounter = frameCounter + 1;
			if( (System.nanoTime() - this.fpsCalcTime) > 1000000000L ) {
				//System.out.println("FPS: "+frameCounter + "   GAMETIME:"+gameTime);
				fps = frameCounter; //毎秒ごとのFPSを更新
				frameCounter = 0;
				fpsCalcTime = System.nanoTime();
			}
		}
		this.jgcGameFinish(); //ゲーム終了処理
	}
	/* イベントメソッド */
	public final void mousePressed(MouseEvent me) {
		mouseState = mouseState | (1<<me.getButton()-1);
	}
	public final void mouseReleased(MouseEvent me) {
		mouseState = mouseState & ~(1<<me.getButton()-1);
	}
	public final void mouseClicked(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mouseDragged(MouseEvent me) {
		this.mouseCursorX = me.getX();
		this.mouseCursorY = me.getY();
	}
	public final void mouseMoved(MouseEvent me) {
		this.mouseCursorX = me.getX();
		this.mouseCursorY = me.getY();
	}
	public final void keyTyped(KeyEvent ke) {}
	public final void keyPressed(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		if(keyCode != KeyEvent.VK_ESCAPE) {
			if( keyCode < 64) {
				keyStateL = keyStateL | (1<<keyCode);
			}
			else if( keyCode < 128 ) {
				keyStateH = keyStateH | (1<<(keyCode-64));
			}
		}
	}
	public final void keyReleased(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		if(keyCode != KeyEvent.VK_ESCAPE) {
			if( keyCode < 64 ) {
				keyStateL = keyStateL & ~(1<<keyCode);
			} else if( keyCode < 128 ) {
				keyStateH = keyStateH & ~(1<<(keyCode-64));
			}
		}
		//ESCキーによってのみゲームを終了する。
		else if( keyCode == KeyEvent.VK_ESCAPE ) {
			this.jgcExit();
		}
	}
}