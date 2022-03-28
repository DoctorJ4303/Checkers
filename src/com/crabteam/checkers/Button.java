package com.crabteam.checkers;

import processing.core.*;

public class Button {
	
	
	private PApplet p;
	private int x;
	private int y;
	private int length;
	private int height;
	private PImage sprite;
	
	public Button (PApplet p, int x, int y, int length, int height, String type) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.length = length;
		this.height = height;
		
		sprite = p.loadImage("src/com/crabteam/checkers/sprites/" + type + ".png");
	}
	
	public void show() {
		p.image(sprite, x, y, length, height);
	}
	
	public void hide() {
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

	public int getLength() {
		return length;
	}

	
	public int getHeight() {
		return height;
	}
	
	public boolean isWithin(int mouseX, int mouseY) {
		return (mouseX > this.x && mouseX < this.x+this.length && mouseY > this.y && mouseY < this.y+this.height);
	}
}
