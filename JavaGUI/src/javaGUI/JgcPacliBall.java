package javaGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.Random;

import jgc.JsGameCanvas;

public class JgcPacliBall extends JsGameCanvas {

	public static final int SCENE_OPENING = 0;
	public static final int SCENE_PLAYING = 1;
	public static final int SCENE_ENDING = 2;

	private int scene;
	private long startTime;
	private long playTime;
	private Image bgImg;
	private Image tlImg;
	private int score;

	private Chara bar;
	private Image barImg;
	private Chara ball;
	private Image ballImg;
	private Random rnd;

	protected void jgcGameInit() {
		scene = SCENE_OPENING;
		startTime = 0;
		playTime = 0;
		score = 0;
		bgImg = jgcLoadImage("img/bg.jpg", this);
		tlImg = jgcLoadImage("img/title.gif", this);
		barImg = jgcLoadImage("img/bar.gif", this);
		bar = new Bar(getWidth() / 2, getHeight() - 6, barImg.getWidth(this), barImg.getHeight(this), barImg);
		ballImg = jgcLoadImage("img/ball.gif", this);
		ball = new Ball(67, 65, ballImg.getWidth(this) / 2, ballImg);
		rnd = new Random();
	}

	protected void jgcGameMain() {
		if (scene == SCENE_OPENING) {
			if (jgcGetMouseTrigger(MouseEvent.BUTTON1)) {
				startTime = System.currentTimeMillis();
				scene = SCENE_PLAYING;
				score = 0;
				bar.setX(getWidth() / 2);
				bar.setY(getHeight() - 20);
				ball.setX(67);
				ball.setY(165);
				ball.setD(rnd.nextInt(5) + 2, rnd.nextInt(5) + 2);
			}
		} else if (scene == SCENE_PLAYING) {

			if (ball.isCollision(bar) && (ball.getDy() > 0)) {
				ball.setDy(-(rnd.nextInt(5) + 2));
				score = score + 10;
			}
			if (ball.getTop() < 0 && (ball.getDy() < 0)) {
				ball.setDy(rnd.nextInt(5) + 2);
			}
			if (ball.getLeft() < 0 || (getWidth() < ball.getRight())) {
				ball.setDx(-ball.getDx());
			}
			ball.update(this);
			bar.setD(0, 0);
			if((jgcGetMouseCursorX()<bar.getX())&&(0<bar.getLeft()))
			{
				bar.setDx(-5);
			}
			if((bar.getX()<jgcGetMouseCursorX())&&(bar.getRight()<getWidth()))
			{
				bar.setDx(5);
			}
			bar.update(this);

			if (ball.getTop() > getHeight()) {
				scene = SCENE_ENDING;
			}
			playTime = System.currentTimeMillis() - startTime;
		} else if (scene == SCENE_ENDING) {
			if (jgcGetMouseTrigger(MouseEvent.BUTTON1)) {
				scene = SCENE_OPENING;
				playTime = 0;
			}
		}
	}

	protected void jgcGameDraw(Graphics g) {
		g.drawImage(bgImg, 0, 0, null);
		g.setColor(Color.WHITE);
		g.drawString("SCORE:" + score, 10, 15);
		g.drawString("TIME:" + (playTime / 1000), 130, 15);
		if (scene == SCENE_OPENING) {
			g.drawImage(tlImg, 0, 0, null);
			g.setColor(Color.WHITE);
			g.drawString("start", getWidth()/2, 320);


		} else if (scene == SCENE_PLAYING) {
			bar.draw(g);
			ball.draw(g);
		} else if (scene == SCENE_ENDING) {
			g.setColor(Color.white);
			g.drawString("gameover", 240, 320);
			g.drawString("return", 240, 360);
		}
	}

	protected void jgcGameFinish() {

	}

	public static void main(String[] args) {
		JsGameCanvas.jgcStart("nau", new JgcPacliBall(), JsGameCanvas.SCREEN_WINDOW);
	}

}
