package javaGUI;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import jgc.JsGameCanvas;
import no03.Particle;

public class JgcParticleTest03 extends JsGameCanvas {
	//シリアルIDの宣言と初期化
	private static final long serialVersionUID = 1L;
	public static final int PARTICLE_MAX_SIZE = 500;
	private List<Particle> pal;

	//ゲーム初期化
	protected void jgcGameInit() {
		pal = new ArrayList<Particle>(PARTICLE_MAX_SIZE);
	}

	//ゲームメイン
	protected void jgcGameMain() {

		if (this.jgcGetKeyTrigger(KeyEvent.VK_UP)) {
			while (pal.size() < PARTICLE_MAX_SIZE) {
				pal.add(new Particle(this.getWidth() / 2.0f, this.getHeight() - 10.0f));
			}
		}
		for (int i = 0; i < pal.size(); i++) {
			pal.get(i).update(this);
			if (pal.get(i).getY() > this.getHeight()) {
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
		JsGameCanvas.jgcStart("Javaパー", new JgcParticleTest03(), JsGameCanvas.SCREEN_WINDOW);

	}
}
