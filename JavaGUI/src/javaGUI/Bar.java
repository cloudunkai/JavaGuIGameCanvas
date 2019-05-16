package javaGUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import jgc.JsGameCanvas;

public class Bar extends Chara {
	private Image img;

	public Bar(int x, int y, int w, int h, Image img) {
		super(x, y, w, h);
		this.img = img;
	}

	@Override
	public void update(JsGameCanvas jgc) {
		move();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(img, getLeft(), getTop(), null);
		g.setColor(Color.BLUE);
		g.drawRect(getLeft(), getTop(), getW(), getH());
	}
}
