package com.crabteam.checkers;


import processing.core.*;

public class Arrow{
	
	private int loc;
	private Piece piece;
	private PApplet p;
	private boolean selected;
	public PImage unselectedArrow;
	public PImage selectedArrow;
	private PImage preload;
	public Piece blockingPiece;
	private int near[];
	private int far[];
	
	public Arrow(PApplet p, Piece piece, int l, Piece blockingPiece, PImage preload) {
		this.p = p;
		this.piece = piece;
		this.loc = l;
		this.preload = preload;
		this.blockingPiece = blockingPiece;
		
		this.near = new int[]{((loc*2-5) > 0 ? -1 : 1)*30+piece.getBoardLocation()[0], piece.getBoardLocation()[1]};
		this.far = new int[]{(loc*2-5 > 0 ? -1 : 1)*45+piece.getBoardLocation()[0], (loc == 2 || loc == 3 ? 1 : -1)*75+piece.getBoardLocation()[1]};
		
		unselectedArrow = this.p.loadImage("src/com/crabteam/checkers/sprites/unselected_arrow.png");
		selectedArrow = this.p.loadImage("src/com/crabteam/checkers/sprites/selected_arrow.png");
	}
	
	public int getLocation() { return loc; }
	public boolean isSelected() { return selected; }
	public Piece getPiece() { return piece; }
	public Piece getBlockingPiece() { return blockingPiece; }
	
	public void setSelected(boolean selected) {
		show();
		this.selected = selected; 
	}
	
	public void show() {
		p.translate(((loc*2-5) > 0 ? -1 : 1)*10+piece.getBoardLocation()[0], (loc == 2 || loc == 3 ? 1 : -1)*10+piece.getBoardLocation()[1]);
		p.rotate(PApplet.radians(90*loc-45));
		p.image(selected ? selectedArrow : unselectedArrow, 0, -40, 60, 60);
		p.rotate(PApplet.radians(-(90*loc-45)));
		p.translate(-(((loc*2-5) > 0 ? -1 : 1)*10+piece.getBoardLocation()[0]), -((loc == 2 || loc == 3 ? 1 : -1)*10+piece.getBoardLocation()[1]));
	}

	public void hide() {
		p.image(preload, 370, 420);
	}

	public boolean isWithin(int x, int y) {
		return (((loc == 1 || loc == 4) ? (y-far[1] > x-far[0]) : (y-far[1] < x-far[0])) && ((loc == 1 || loc == 4) ? (y-near[1] < x-near[0]) : (y-near[1] > x-near[0]))
				&& ((loc == 1 || loc == 4) ? ((y+x)-(near[0]+near[1]) < 0) : ((y+x)-(near[0]+near[1]) > 0)) && ((loc == 1 || loc == 4) ? (y+x-(far[0]+far[1]) > 0) : (y+x-(far[0]+far[1]) < 0)));
	}
}
