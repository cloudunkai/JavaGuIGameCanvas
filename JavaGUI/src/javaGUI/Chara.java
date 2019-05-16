package javaGUI;
/** �Q�[���p�L�������`�������ۃN���XChara  @version 1.0 H24/02/01  @author HATADA Toshinobu */
import java.awt.Graphics;

import jgc.JsGameCanvas;

//JsGameCanvas���g�����߂̃C���|�[�g
//�`�揈���Ȃǂ��s�����߂̃C���|�[�g

public abstract class Chara {
	private int x, y, w, h, dx, dy; // �L�����̒��S�_���W�ix�Cy�j�A������w�ch�A�ړ��ʂ̉�����dx�c����dy
	private boolean life;// �L�����̐����̔���t���O

	/** �R���X�g���N�^�F�L�����N���X�̃t�B�[���h���������B�L�����̒��S�_�Ɖ��c�̒����͈����Ŏw��B */
	public Chara(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.dx = this.dy = 0;
		this.life = true;
	}

	// �A�N�Z�X���\�b�h�e��
	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return this.x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return this.y;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getW() {
		return this.w;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getH() {
		return this.h;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDx() {
		return this.dx;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public int getDy() {
		return this.dy;
	}

	/** isAlive���\�b�h �L�����̐����̐^�U��Ԃ��B */
	public boolean isAlive() {
		return this.life;
	}

	/** die���\�b�h �L�����̏�Ԃ����ifalse�j�ɂ���B */
	public void die() {
		this.life = false;
	}

	/** setD���\�b�h �ړ���dx, dy���Z�b�g����B */
	public void setD(int dx, int dy) {
		setDx(dx);
		setDy(dy);
	}

	/** �ړ����\�b�h�@�ݒ肳�ꂽ�ړ��ʕ��ړ����� **/
	public void move() {
		x = x + dx;
		y = y + dy;
	}

	/** getLeft���\�b�h �L�����̍��[��x���W��Ԃ��B */
	public int getLeft() {
		return x - (w / 2);
	}

	/** getRight���\�b�h �L�����̉E�[��x���W��Ԃ��B */
	public int getRight() {
		return x + (w / 2);
	}

	/** getTop���\�b�h �L�����̏�[��y���W��Ԃ��B */
	public int getTop() {
		return y - (h / 2);
	}

	/** getBottom���\�b�h �L�����̉��[��y���W��Ԃ��B */
	public int getBottom() {
		return y + (h / 2);
	}

	/** �Փ˔��胁�\�b�h �w�肵���L�����uc�v�ƏՓ˂��Ă��邩�ǂ�����Ԃ��B */
	public boolean isCollision(Chara c) {
		if ((c.getLeft() < this.getRight()) && (this.getLeft() < c.getRight())) {
			if ((c.getTop() < this.getBottom()) && (this.getTop() < c.getBottom())) {
				return true;
			}
		}
		return false;
	}

	/** �X�V���\�b�h �X�V�i�ړ��Ȃǁj�̋�̓I�����̓L�����ɂ���ĈقȂ�̂Œ��ۃ��\�b�h�Ƃ���B */
	public abstract void update(JsGameCanvas jgc);

	/** �`�惁�\�b�h �`��̋�̓I�����̓L�����ɂ���ĈقȂ�̂Œ��ۃ��\�b�h�Ƃ���B */
	public abstract void draw(Graphics g);
}
