package com.crabteam.checkers;

import java.util.ArrayList;

import javax.swing.border.Border;

import processing.core.*;

public class Checkers extends PApplet {
	
	public PImage board;

	public ArrayList<Piece> pieces = new ArrayList<>();
	public ArrayList<Piece> redPieces = new ArrayList<>();
	public ArrayList<Piece> whitePieces = new ArrayList<>();
	public ArrayList<Arrow> moves = new ArrayList<>();
	public Button skipButton;
	
	public Piece compulsoryPiece = null;
	public String turn = "red";
	
	public static void main(String args[]) {
		PApplet.main("com.crabteam.checkers.Checkers");
	}
	
	@Override
	public void settings() {
		size(740, 840);
	}

	@Override
	public void setup() {
		background(122, 122, 122);
		board = loadImage("src/com/crabteam/checkers/sprites/board.png");
		image(board, 50, 150, 640, 640);
		
		for (int i = 0; i < 4; i++) {
			redPieces.add(new Piece(this, "red", i*2+1, 0));
			redPieces.add(new Piece(this, "red", i*2, 1));
			redPieces.add(new Piece(this, "red", i*2+1, 2));
		}		
		for (int i = 0; i < 4; i++) {
			whitePieces.add(new Piece(this, "white", i*2, 5));
			whitePieces.add(new Piece(this, "white", i*2+1, 6));
			whitePieces.add(new Piece(this, "white", i*2, 7));
		}
		pieces.addAll(redPieces);
		pieces.addAll(whitePieces);
		
		for (Piece piece : pieces) { piece.show(); }
	}
	
	@Override
	public void draw() {
		fill(turn.equals("white") ? color(255, 255, 255) : color(255, 0, 0));
		stroke(0, 0, 0);
		strokeWeight(3);
		rect(50, 25, 640, 100);
		fill(0, 0, 0);
		textSize(100);
		textAlign(CENTER);
		text(turn.substring(0, 1).toUpperCase() + turn.substring(1, turn.length()) + "'s Turn", 370, 112);
		if (pieces.size() == 0)	stop();
	}
	
	@Override
	public void mouseMoved() {
		for(Arrow a : moves) { a.setSelected(a.isWithin(mouseX, mouseY)); }
		super.mouseMoved();
	}
	
	@Override
	public void mouseReleased() {
		if (mouseButton != LEFT) return;
		
		ArrayList<Arrow> temp = new ArrayList<>();
		
		
		for (Arrow a : moves) {
			if (a.isSelected()) {
				for (Arrow a1 : moves) { a1.hide(); }
				moves.clear();
				a.getPiece().move(a);
				if (a.getPiece().getY() == (a.getPiece().getColor().equals("white") ? 0 : 7)) a.getPiece().setKing();
				if (a.getBlockingPiece() != null) {
					pieces.remove(a.getBlockingPiece());
					if (a.getBlockingPiece().getColor().equals("red")) redPieces.remove(a.getBlockingPiece());
					else whitePieces.remove(a.getBlockingPiece());
					checkMoves(a.getPiece());
					for(Arrow a1 : moves) {  if (a1.getBlockingPiece() == null) temp.add(a1); }
					for (Arrow t : temp) { moves.remove(t); }
					for (Arrow a1 : moves) { a1.show(); }
					if (moves.size() == 0) {
						switchTurn();
						compulsoryPiece = null;
					}
					else compulsoryPiece = a.getPiece();
				}
				else switchTurn();
				return;
			} else if (compulsoryPiece != null && compulsoryPiece.equals(a.getPiece())) {
				
			}
		}

		if (compulsoryPiece != null) return;
		for (Piece p : pieces) {
			if (getDistance(p.getBoardLocation()[0], p.getBoardLocation()[1], mouseX, mouseY) < 32.5) {
				for (Arrow a1 : moves) { a1.hide(); }
				moves.clear();
				checkMoves(p);
				System.out.println(p.getX() + ", " + p.getY());
				for (Arrow a : moves) {
					a.show();
				}
			}
		}
	}
	
	private void switchTurn() {
		turn = turn.equals("white") ? "red" : "white";
	}
	
	private double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(Math.abs(x1-x2), 2) + Math.pow(Math.abs(y1-y2), 2));
	}
	
	private void checkMoves(Piece p) {
		
		if (!p.getColor().equals(turn)) return;
		
		ArrayList<Piece> colorList = p.getColor().equals("red") ? redPieces : whitePieces;
		ArrayList<Piece> oppList = p.getColor().equals("white") ? redPieces : whitePieces;
		
		//If it is up against a border
		boolean upLeft = (p.getX() > 0 && p.getY() > 0) && ((p.getColor().equals("red") && p.isKing()) || p.getColor().equals("white"));
		boolean upRight = (p.getX() < 7 && p.getY() > 0) && ((p.getColor().equals("red") && p.isKing()) || p.getColor().equals("white"));
		boolean downLeft = (p.getX() > 0 && p.getY() < 7) && ((p.getColor().equals("white") && p.isKing()) || p.getColor().equals("red"));
		boolean downRight = (p.getX() < 7 && p.getY() < 7) && ((p.getColor().equals("white") && p.isKing()) || p.getColor().equals("red"));
		
		//sets the blocking piece
		Piece blocking1 = null;
		Piece blocking2 = null;
		Piece blocking3 = null;
		Piece blocking4 = null;
		
		
		
		for (Piece piece : colorList) {
			//If it is being blocked by a piece of the same color
			if (p.getX()-1 == piece.getX() && p.getY()-1 == piece.getY()) upLeft = false;
			if (p.getX()+1 == piece.getX() && p.getY()-1 == piece.getY()) upRight = false;
			if (p.getX()-1 == piece.getX() && p.getY()+1 == piece.getY()) downLeft = false;
			if (p.getX()+1 == piece.getX() && p.getY()+1 == piece.getY()) downRight = false;
		}
		
		for (Piece piece : oppList) {
			//If it is being blocked by a piece of the opp. color
			if (p.getX()-1 == piece.getX() && p.getY()-1 == piece.getY()) { 
				if (!(p.getX() > 1 && p.getY() > 1)) upLeft = false;
				blocking4 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()-2 == piece1.getX() && p.getY()-2 == piece1.getY()) { upLeft = false; blocking4 = null; }
			}
			
			if (p.getX()+1 == piece.getX() && p.getY()-1 == piece.getY()) {
				if (!(p.getX() < 6 && p.getY() > 1)) upRight = false; 
				blocking1 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()+2 == piece1.getX() && p.getY()-2 == piece1.getY()) { upRight = false; blocking1 = null; }
			}
			
			if (p.getX()-1 == piece.getX() && p.getY()+1 == piece.getY()) {
				if (!(p.getX() > 1 && p.getY() < 6)) downLeft = false;
				blocking3 = piece;
				for (Piece piece1 : pieces) 
					if (p.getX()-2 == piece1.getX() && p.getY()+2 == piece1.getY()) { downLeft = false; blocking3 = null; }
			}
			
			if (p.getX()+1 == piece.getX() && p.getY()+1 == piece.getY()) {
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
