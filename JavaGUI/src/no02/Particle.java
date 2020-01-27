package no02;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import jgc.JsGameCanvas;

public class Particle {

	//中心点，半径，移动量，移动速度，移动角度
	private float x, y, r, dx, dy, sp, agl;
	private float gv;//重力
	private float alpha;
	public Particle(float x, float y) {
		this.x = x;
		this.y = y;
		r = 2.0f;
		sp = (float) (Math.random() * 4.0) + 3.0f;
		agl = (float) (Math.random() * 40) + 70.0f;
		dx = sp * (float) (Math.cos(Math.toRadians(agl)));
		dy = sp * -(float) (Math.sin(Math.toRadians(agl)));
		gv = 0.3f;
	}

	public void draw(Graphics g) {
		JsGameCanvas.jgcDrawAlphaOn(g, alpha);
		Random ran = new Random();
		this.alpha=ran.nextFloat();
		int rc = ran.nextInt(255);
		int gc = ran.nextInt(255);
		int b = ran.nextInt(255);
		float r=ran.nextInt(2)+2;
		Color color = new Color(rc, gc, b);
		g.setColor(color);

		g.fillOval((int) (x - r), (int) (y - r), (int) (r * 5.0f), (int) (r * 5.0f));
	}

	public void draws(Graphics s) {
		JsGameCanvas.jgcDrawAlphaOn(s, alpha);
		Random ran = new Random();
		this.alpha=ran.nextFloat();
		int rc = ran.nextInt(255);
		int gc = ran.nextInt(255);
		int b = ran.nextInt(255);
		float r=ran.nextInt(2)+1;
		Color color = new Color(rc, gc, b);
		s.setColor(color);
		s.fillOval((int) (x - r), (int) (y - r), (int) (r * 5.0f), (int) (r * 5.0f));
	}

	public void move() {
		x = x + dx;
		dy = dy + gv;
		y = y + dy;
	}

	public void update(JsGameCanvas jgc) {
		this.move();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public float getSp() {
		return sp;
	}

	public void setSp(float sp) {
		this.sp = sp;
	}

	public float getAgl() {
		return agl;
	}

	public void setAgl(float agl) {
		this.agl = agl;
	}

	public float getGv() {
		return gv;
	}

	public void setGv(float gv) {
		this.gv = gv;
	}
}
