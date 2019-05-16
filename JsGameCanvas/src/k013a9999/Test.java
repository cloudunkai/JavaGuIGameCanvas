package k013a9999;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import jgc.JsGameCanvas;

public class Test extends JsGameCanvas {

	private Image bi;
	private float x;
	private float y;

	private float agl;
	private float a;
	private Random r = new Random();

	@Override
	protected void jgcGameInit() {

		r = new Random();

		bi = this.jgcLoadImage("img/bozz.gif", this);
		x = this.getWidth()/2 - bi.getWidth(null)/2;
		y = this.getHeight()/2 - bi.getHeight(null)/2;

		agl = 0.0f;
		a = 10; //100～200


	}

	@Override
	protected void jgcGameMain() {

		agl = agl - 0.02f;
		a = a + 0.1f;

		//this.getGraphics().getFont().getSize();


	}

	@Override
	protected void jgcGameDraw(Graphics g) {
		g.drawImage(bi, (int)(x+Math.cos(agl)*a), (int)(y+Math.sin(agl)*a), null);

		g.setColor(Color.WHITE);
		g.drawString("FONTSIZE" + this.getGraphics().getFont().getSize(), 10, 10);
		g.drawString("FONTSIZE" + g.getFont().getSize(), 10, 30);

	}

	@Override
	protected void jgcGameFinish() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JsGameCanvas.jgcStart("TEST", new Test(), JsGameCanvas.SCREEN_WINDOW);

	}

}
