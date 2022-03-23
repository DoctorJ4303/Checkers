package com.crabteam.checkers;

import java.util.ArrayList;

import processing.core.*;

public class Checkers extends PApplet {
	
	public PImage board;
	
	public ArrayList<Piece> pieces = new ArrayList<>();
	public ArrayList<Arrow> moves = new ArrayList<>();
	
	public static void main(String args[]) {
		PApplet.main("com.crabteam.checkers.Checkers");
		
	}
	
	@Override
	public void settings() {
		size(640, 740);
	}

	@Override
	public void setup() {
		background(100, 100, 100);
		board = loadImage("src/com/crabteam/checkers/sprites/board.png");
		image(board, 0, 100, 640, 640);
		
		pieces.add(new Piece(this, "red", 0, 0));
		pieces.add(new Piece(this, "white", 1, 1));
		for (Piece piece : pieces) {
			piece.show();
		}
	}
	
	@Override
	public void draw() {
		for(Arrow a : moves) {
			if (a.isWithin(mouseX, mouseY)) a.setSelected(true);
			else a.setSelected(false);
		}
	}
	
	@Override
	public void mouseReleased() {
		if (mouseButton != LEFT) return;
		
		for (Arrow a : moves) {
			a.hide();
		}
		
		for (Arrow a : moves) {
			if (a.isSelected()) {
				if (a.getBlockingPiece() != null) {
					moves.clear();
					a.getPiece().move(a);
					for (int i = 0; i < pieces.size(); i++) {
						System.out.println(pieces.get(i));
						System.out.println(a.getBlockingPiece());
						if (pieces.get(i).equals(a.getBlockingPiece())) {
							pieces.get(i).hide();
							pieces.remove(i);
						}
					}
				} else {
					moves.clear();
					a.getPiece().move(a);
				}
				return;
			}
		}
		
		moves.clear();

		for (Piece p : pieces) {
			if (getDistance(p.getBoardLocation()[0], p.getBoardLocation()[1], mouseX, mouseY) < 32.5) {
				checkMoves(p);
				System.out.println(p.getX() + ", " + p.getY());
				for (Arrow a : moves) {
					a.show();
				}
			}
		}
	}
	
	private double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(Math.abs(x1-x2), 2) + Math.pow(Math.abs(y1-y2), 2));
	}
	
	private void checkMoves(Piece p) {
		//If it is up against a border
		boolean upLeft = (p.getX() > 0 && p.getY() > 0);
		boolean upRight = (p.getX() < 7 && p.getY() > 0);
		boolean downLeft = (p.getX() > 0 && p.getY() < 7);
		boolean downRight = (p.getX() < 7 && p.getY() < 7);
		Piece blocking1 = null;
		Piece blocking2 = null;
		Piece blocking3 = null;
		Piece blocking4 = null;
		for (Piece piece : pieces) {
			//If it is being blocked by a piece of the same color
			if (p.getX()-1 == piece.getX() && p.getY()-1 == piece.getY() && p.getColor().equals(piece.getColor())) upLeft = false;
			if (p.getX()+1 == piece.getX() && p.getY()-1 == piece.getY() && p.getColor().equals(piece.getColor())) upRight = false;
			if (p.getX()-1 == piece.getX() && p.getY()+1 == piece.getY() && p.getColor().equals(piece.getColor())) downLeft = false;
			if (p.getX()+1 == piece.getX() && p.getY()+1 == piece.getY() && p.getColor().equals(piece.getColor())) downRight = false;
			
			//If it is being blocked by a piece of the opp. color
			if (p.getX()-1 == piece.getX() && p.getY()-1 == piece.getY() && !p.getColor().equals(piece.getColor())) { 
				if (!(p.getX() > 1 && p.getY() > 1)) upLeft = false;
				blocking4 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()-2 == piece1.getX() && p.getY()-2 == piece1.getY()) { upLeft = false; blocking4 = null; }
			}
			
			if (p.getX()+1 == piece.getX() && p.getY()-1 == piece.getY() && !p.getColor().equals(piece.getColor())) {
				if (!(p.getX() < 6 && p.getY() > 1)) upRight = false; 
				blocking1 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()+2 == piece1.getX() && p.getY()-2 == piece1.getY()) { upRight = false; blocking1 = null; }
			}
			
			if (p.getX()-1 == piece.getX() && p.getY()+1 == piece.getY() && !p.getColor().equals(piece.getColor())) {
				if (!(p.getX() > 1 && p.getY() < 6)) downLeft = false;
				blocking3 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()-2 == piece1.getX() && p.getY()+2 == piece1.getY()) { downLeft = false; blocking3 = null; }
			}
			
			if (p.getX()+1 == piece.getX() && p.getY()+1 == piece.getY() && !p.getColor().equals(piece.getColor())) {
				if (!(p.getX() < 6 && p.getY() < 6)) downRight = false;
				blocking2 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()+2 == piece1.getX() && p.getY()+2 == piece1.getY()) { downRight = false; blocking2 = null; }
			}
		}
		for (Arrow a : moves) {
			a.hide();
		}
		moves.clear();
		if (upRight) moves.add(new Arrow(this, p, 1, blocking1, get()));
		if (downRight) moves.add(new Arrow(this, p, 2, blocking2, get()));
		if (downLeft) moves.add(new Arrow(this, p, 3, blocking3, get()));
		if (upLeft) moves.add(new Arrow(this, p, 4, blocking4, get()));
	}
}
