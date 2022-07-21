package tests;

import io.*;
import model.*;
import model.Board.GameResult;
import model.Cell.CheckerColor;
import ui.*;

import java.util.*;
import java.lang.Math;

public class BoardTest {
	
	//public GameResult isWin()
	//public ArrayList<String> move(String movePath)
	//public void loadBoardFromTextFile(String filename)
	//public ArrayList<String> possibleMove(CheckerColor col, Cell[][] board)
	//testGame
	
	private static boolean checkIsWin() {
		try {
			Board b = new Board();
			int testPassed = 0;
			if(b.isWin() == Board.GameResult.Continue) {
				testPassed++;
			}
			
			b.loadBoardFromTextFile(System.getProperty("user.dir") + "/src/tests/CheckIsWin.txt");
			
			if(b.isWin() == Board.GameResult.WhiteWin) {
				testPassed++;
			}
			
			if(testPassed == 2) return true;
			else return false;
			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	private static boolean checkMove() {
		try {
			Board b = new Board();
			
			int testPassed = 0;
			
			String correctMove = "42:33"; //e3:d4
			String uncorrectMove = "42:43"; //e3:e4

			if(!b.move(correctMove).isEmpty()) {
				testPassed++;
			}
			
			if(b.move(uncorrectMove).isEmpty()) {
				testPassed++;
			}
			
			
			if(testPassed == 2) return true;
			else return false;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	private static boolean checkLoadBoardFromTextFile(){
		try {
			Board b = new Board();
			String ideal = new String(
					"b b b b \n" + 
					" b b b b\n" + 
					"b b b b \n" + 
					" # # # #\n" + 
					"# # # # \n" + 
					" w w w w\n" + 
					"w w w w \n" + 
					" w w w w");
			b.loadBoardFromTextFile(System.getProperty("user.dir") 
					+ "/src/model/ReverseBasicCheckersPosition.txt");
			if((b.toString()).contains(ideal)) {
				return true;
			}
			else return false;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private static boolean checkPossibleMove(){
		try {
			Board b = new Board();
			ArrayList<String> ideal = new ArrayList<String>();
			
			ideal.add("02:13");
			ideal.add("22:33");
			ideal.add("22:13");
			ideal.add("42:53");
			ideal.add("42:33");
			ideal.add("62:73");
			ideal.add("62:53");
			
			ArrayList<String> test = b.getPossibleMoves();
			if(test.equals(ideal)) {
				return true;
			}
			else return false;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

	}
	
	private static boolean testGame(){
		try {
			Board b = new Board();
			return true;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static void main(String[] argv) {
		try {
			if(checkIsWin()) System.out.println("isWin() true");
			else System.out.println("isWin() false");
			
			if(checkMove()) System.out.println("move() true");
			else System.out.println("move() false");
			
			if(checkLoadBoardFromTextFile()) System.out.println("loadBoardFromTextFile() true");
			else System.out.println("loadBoardFromTextFile() false");
			
			if(checkPossibleMove()) System.out.println("possibleMove() true");
			else System.out.println("possibleMove() false");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}