package javaGUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import jgc.JsGameCanvas;

public class Ball extends Chara {

	private Image img;

	public Ball(int x, int y, int r, Image img) {
		super(x, y, r * 2, r * 2);
		this.img = img;
	}

	@Override
	public void update(JsGameCanvas jgc) {
		move();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, getLeft(), getTop(), null);
		g.setColor(Color.RED);
		g.drawOval(getLeft(), getTop(), getW(), getH());
	}

}
