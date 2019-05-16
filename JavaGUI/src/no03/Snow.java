package no03;

import java.awt.Color;
import java.awt.Graphics;

import jgc.JsGameCanvas;

public class Snow {

	public static final int STATE_WAITING = 0;
	public static final int STATE_MOVE = 1;

	//中心点，半径，移动量，移动速度，移动角度
	private float x, y, r, dx, dy, sp, agl;
	private float gv;//重力
	private int timer;
	private int state;
	private float alpha;

	public Snow(float x, float y) {
		this.x = x;
		this.y = y;
		this.alpha = 1.0f;
		r = 2.0f;
		sp = (float) (Math.random() * 2.0) + 1.0f;
		agl = (float) (Math.random() * 40) + 70.0f;
		dx = sp * (float) (Math.cos(Math.toRadians(agl)));
		dy = sp * (float) (Math.sin(Math.toRadians(agl)));
		gv = 0.0f;
		state = Snow.STATE_WAITING;
		timer = (int) (Math.random() * 300.0f);
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void waiting() {
		this.timer = this.timer - 1;
		if (this.timer <= 0) {
			this.state = Snow.STATE_MOVE;
		}
	}

	public void draw(Graphics g) {
		JsGameCanvas.jgcDrawAlphaOn(g, alpha);
		g.setColor(Color.WHITE);
		g.fillOval((int) (x - r), (int) (y - r), (int) (r * 5.0f), (int) (r * 5.0f));
		JsGameCanvas.jgcDrawAlphaOff(g);
	}

	public void move() {
		x = x + dx;
		dy = dy + gv;
		y = y + dy;

		if (dy > 0) {
			this.alpha = this.alpha - (float) (Math.random() * 0.005f);
			if (this.alpha < 0.0f) {
				this.alpha = 0.0f;
			}
		}
	}

	public void update(JsGameCanvas jgc) {
		if (this.state == Snow.STATE_WAITING) {
			this.waiting();
		} else if (this.state == Snow.STATE_MOVE) {
			this.move();
		}
	}

}
