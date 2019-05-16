//package no03;
//
//import java.awt.Graphics;
//
//import jgc.JsGameCanvas;
//
//public class SParticle extends Particle {
//
//	private float alpha;
//
//	public SParticle(float x, float y) {
//		super(x, y);
//		this.alpha = 1.0f;
//	}
//
//	public void draw(Graphics g) {
//		JsGameCanvas.jgcDrawAlphaOn(g, alpha);
//		super.draw(g);
//		JsGameCanvas.jgcDrawAlphaOff(g);
//	}
//
//	public void update(JsGameCanvas jgc) {
//		super.update(jgc);
//		if (this.getState() == Particle.STATE_MOVE) {
//			if (this.getDy() > 0) {
//				this.alpha = this.alpha - (float) (Math.random() * 0.025f);
//				if (this.alpha < 0.0f) {
//					this.alpha = 0.0f;
//				}
//			}
//		}
//	}
//}
