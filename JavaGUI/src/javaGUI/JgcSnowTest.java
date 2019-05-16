package javaGUI;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import jgc.JsGameCanvas;
import no03.Snow;

public class JgcSnowTest extends JsGameCanvas {
	//シリアルIDの宣言と初期化
	private static final long serialVersionUID = 1L;
	public static final int PARTICLE_MAX_SIZE = 1;
	private List<Snow> pal;

	//ゲーム初期化
	protected void jgcGameInit() {
		pal = new ArrayList<Snow>(PARTICLE_MAX_SIZE);
	}

	//ゲームメイン
	protected void jgcGameMain() {

		//		if (this.jgcGetKeyTrigger(KeyEvent.VK_UP)) {
		while (pal.size() < 550) {
			for (int i = 0; i < 680; i++) {
				pal.add(new Snow(i * 1.0f, -10.0f));
			}
		}
		//REMOVE
		for (int i = 0; i < pal.size(); i++) {
			pal.get(i).update(this);
			if (pal.get(i).getY() > this.getHeight()-10) {
				pal.remove(i);
				i--;
			}
		}

	}

	//描画
	protected void jgcGameDraw(Graphics g) {

		for (int i = 0; i < pal.size(); i++) {
			pal.get(i).draw(g);
		}
	}

	//終了
	protected void jgcGameFinish() {
	}

	public static void main(String args[]) {
		JsGameCanvas.jgcStart("SNOW", new JgcSnowTest(), JsGameCanvas.SCREEN_WINDOW);

	}
}
