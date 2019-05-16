package javaGUI;

//描画などの為に必要
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import jgc.JsGameCanvas;
import no02.Particle;

public class JgcParticleTest02 extends JsGameCanvas {
	//シリアルIDの宣言と初期化
	private static final long serialVersionUID = 1L;

	private Particle p;
	private Particle[] pa;
	private boolean red, blue, green = false;

	//ゲーム初期化
	protected void jgcGameInit() {
		pa = new Particle[3000];
		p = null;
	}

	//ゲームメイン
	protected void jgcGameMain() {

		if(this.jgcGetKeyTrigger(KeyEvent.VK_UP))
		{
			red=true;
		}
		else if(this.jgcGetKeyTrigger(KeyEvent.VK_SPACE))
		{
			red=false;
		}
		if (this.jgcGetKeyTrigger(KeyEvent.VK_ENTER)) {
			p = new Particle(this.getWidth() / 2.0f, this.getHeight() - 10.0f);
		}
		if (this.jgcGetKeyState(KeyEvent.VK_UP)) {
			//パーティークル配列の要素をチェックして、
			for (int i = 0; i < pa.length; i = i + 1) {
				//実体がNULLだったときに、
				if (pa[i] == null) {
					pa[i] = new Particle(this.getWidth() / 2.0f, this.getHeight() - 10.0f);
					break;
					//すべて生成
				}
			}
		}
		//spaceキー
		if (this.jgcGetKeyTrigger(KeyEvent.VK_SPACE)) {
			//パーティークル配列の要素をチェックして、
			for (int i = 0; i < pa.length; i = i + 1) {
				//実体がNULLだったときに、
				if (pa[i] == null) {
					pa[i] = new Particle(this.getWidth() / 2.0f, this.getHeight() - 10.0f);
					//パーティークルを生成。但し一つ生成した、breakで繰り返しを終了する。
					break;//しないと。実体がなかった要素すべてのパーティークルが生成されてしまう。
				}
			}
		}
		//パーティークル配列飛ばす処理
		for (int i = 0; i < pa.length; i = i + 1) {
			if (pa[i] != null) {
				pa[i].update(this);
				if (pa[i].getY() > this.getHeight()) {
					pa[i] = null;
				}
			}
		}
		if (p != null) {
			p.update(this);
			if (p.getY() > this.getHeight()) {
				//パーティークルが画面下に到達したら、
				//そのパーティークルはもう不要なのでnullクリアする
				//しないと無駄にずーっと下に落ち続ける。
				p = null;
			}
		}

	}

	//描画
	protected void jgcGameDraw(Graphics g) {

		if (p != null) {
			p.draw(g);

		}
		if (p != null) {
			g.setColor(Color.WHITE);
			g.drawString("PX:" + p.getX(), 10, 45);
			g.drawString("PY:" + p.getY(), 10, 60);
		}
		for (int i = 0; i < pa.length; i++) {
			if (pa[i] != null) {
				if (red == true)
					pa[i].draws(g);
				else
					pa[i].draw(g);

			}
		}
	}

	//終了
	protected void jgcGameFinish() {
	}

	public static void main(String args[]) {
		JsGameCanvas.jgcStart("Javaパー", new JgcParticleTest02(), JsGameCanvas.SCREEN_WINDOW);

	}
}
