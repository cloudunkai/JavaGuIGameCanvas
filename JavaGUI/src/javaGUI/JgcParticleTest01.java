package javaGUI;

//描画などの為に必要
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import jgc.JsGameCanvas;

public class JgcParticleTest01 extends JsGameCanvas {
	//シリアルIDの宣言と初期化
	private static final long serialVersionUID = 1L;

	//	private int fc;//フレームカウンタ
	private float x;//パーティークルの中心点のｘ座標
	private float y;//パーティークルの中心点のｙ座標
	private float r;//パーティークルの半径r
	private float dx;//パーティークルの移動量のｘ成分
	private float dy;//パーティークルの移動量のｙ成分
	private float agl;//パーティークルの移動量方向角度
	private float sp;//パーティークルの移動スピード
	private float gv;//パーティークルにかかる重力

	//ゲーム初期化
	protected void jgcGameInit() {

		//		fc = 0;
		x = this.getWidth() / 2.0f;
		y = this.getHeight() - 10.0f;
		r = 20.0f;
		agl = 0.0f;
		dx = 0.0f;
		dy = 0.0f;
		sp = 0.0f;
		gv = 0.05f;

	}

	//ゲームメイン
	protected void jgcGameMain() {

		if (this.jgcGetKeyTrigger(KeyEvent.VK_ENTER)) {
			x = this.getWidth() / 2.0f;
			y = this.getHeight() - 10.0f;
			dx = sp * (float) Math.cos(Math.toRadians(agl));
			dy = sp * -(float) Math.sin(Math.toRadians(agl));
			sp = (float) (Math.random() * 4.0) + 3.0f;
			agl = (float) (Math.random() * 40.0) + 70.0f;
		}
		if(y>=this.getHeight())
		{
			x = this.getWidth() / 2.0f;
			y = this.getHeight() - 10.0f;
			dx = sp * (float) Math.cos(Math.toRadians(agl));
			dy = sp * -(float) Math.sin(Math.toRadians(agl));
			sp = (float) (Math.random() * 4.0) + 3.0f;
			agl = (float) (Math.random() * 40.0) + 70.0f;
		}
		//		fc = fc + 1;
		x = x + dx;
		dy = dy + gv;
		y = y + dy;
	}

	//描画
	protected void jgcGameDraw(Graphics g) {

		g.setColor(Color.YELLOW);
		//引数(左上ｘ、左上ｙ、横ｗ、纵ｈ)
		g.fillOval((int) (x - r), (int) (y - r), (int) (r * 2.0f), (int) (r * 2.0f));
		//		g.drawString("FC:" + fc, 10, 15);
	}

	//終了
	protected void jgcGameFinish() {
	}

	public static void main(String args[]) {
		JsGameCanvas.jgcStart("Javaパー", new JgcParticleTest01(), JsGameCanvas.SCREEN_WINDOW);

	}
}
