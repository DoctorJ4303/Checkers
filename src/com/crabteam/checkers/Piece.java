package com.crabteam.checkers;

import processing.core.*;

public class Piece{
	
	private PApplet p;
	private String color;
	private int x;
	private int y;
	private PImage sprite;
	
	public Piece(PApplet p, String c, int xPos, int yPos) {
		this.p = p;
		this.color = c;
		this.x = xPos;
		this.y = yPos;
		
		if (color.equals("red")) sprite = p.loadImage("src/com/crabteam/checkers/sprites/red_piece.png");
		else if (color.equals("white")) sprite =  p.loadImage("src/com/crabteam/checkers/sprites/white_piece.png");
	}
	
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	public String getColor() { return color; }
	
	public void hide() {
		p.fill(0, 0, 0);
		p.circle(this.getBoardLocation()[0], this.getBoardLocation()[1], 79);
	}
	
	public void show() {
		p.imageMode(PConstants.CENTER);
		p.image(sprite, this.getBoardLocation()[0], this.getBoardLocation()[1], 80, 80);
	}
	
	public int[] getBoardLocation() {
		return new int[] {this.x*80+40, this.y*80+140};
	}
	
	public void move(Arrow arrow) {
		hide();
		if (arrow.getBlockingPiece() != null) {
			x += (arrow.getLocation() <= 2 ? 2 : -2);
			y += (arrow.getLocation() == 1 || arrow.getLocation() == 4 ? -2 : 2);
			arrow.getBlockingPiece().hide();
		} else {
			x += (arrow.getLocation() <= 2 ? 1 : -1);
			y += (arrow.getLocation() == 1 || arrow.getLocation() == 4 ? -1 : 1);
		}
		show();
	}
}
