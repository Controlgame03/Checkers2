package model;

import java.util.ArrayList;

import model.Board.Move;
import model.Cell.*;

/*Класс возвращающий все возможные ходы для текущей доски и текущего цвета*/
public class BoardPossibleMoves {
	private static final int BOARD_SIZE = 8;

	private Cell board[][];

	private ArrayList<String> possibleMoves = new ArrayList<String>();
	
	/*главный метод. единственный public*/
	public ArrayList<String> possibleMove(CheckerColor col, Cell[][] board){
		this.board = board;
		possibleMoves.clear();
		Move moveType = Move.SimpleMove;
		for(int horizontal = 0; horizontal < BOARD_SIZE; horizontal++) {
			for(int vertical = 0; vertical < BOARD_SIZE; vertical++) {
				if((horizontal % 2) != (vertical % 2)) {
					continue;
				}
				Cell current = new Cell(board[horizontal][vertical]);
				if(current.getType() != CheckerType.NoChecker && current.getColor() == col) {
					board[horizontal][vertical].setColor(CheckerColor.NoColor);
					board[horizontal][vertical].setType(CheckerType.NoChecker);
					moveType = possibleMove(current, new StringBuffer(current.getStringCoordinate()), moveType);
					
					board[horizontal][vertical] = current;
				}
			}
		}

		return possibleMoves;
	}
	/*возвращает возможные ходы для одной фигры*/
	private Move possibleMove(Cell cell, StringBuffer path, Move move){	
		if(cell.getType() == CheckerType.Pawn) {
			
			if(move == Move.SimpleMove) {
				move = pawnSimpleMove(cell, path.toString());
				if(move == Move.EatMove) possibleMoves.clear();
			}
			
			if(move == Move.EatMove) {
			
				move = pawnEatMove(cell, path, move);
			}
			
		}
		if(cell.getType() == CheckerType.King) {
			if(move == Move.SimpleMove) {
				move = kingSimpleMove(cell, path.toString());
				if(move == Move.EatMove) possibleMoves.clear();
			}
			if(move == Move.EatMove) {
				move = kingEatMove(cell, path);
			}
		}
		return move;
	}
	/*если это ход пешкой с поеданием других фигур*/
	private Move pawnEatMove(Cell cell, StringBuffer path, Move move) {
		//CheckerColor enemyColor = cell.getColor() == (CheckerColor.White) ? CheckerColor.Black : CheckerColor.White;
		CheckerColor enemyColor;
		if(cell.getColor() == CheckerColor.White) {
			enemyColor = CheckerColor.Black;
		}
		else {
			enemyColor = CheckerColor.White;
		}
		boolean finshPosition = true;
		
		
		
		if(cell.getHorizontal() + 2 < BOARD_SIZE && cell.getVertical() + 2 < BOARD_SIZE 
				&& board[cell.getHorizontal() + 1][cell.getVertical() + 1].getColor() == enemyColor
				&& board[cell.getHorizontal() + 2][cell.getVertical() + 2].getType() == CheckerType.NoChecker
				&& board[cell.getHorizontal() + 2][cell.getVertical() + 2].getColor() == CheckerColor.NoColor
				&& !isDeleteChecker(board[cell.getHorizontal() + 1][cell.getVertical() + 1], path)) {
			
			finshPosition = false;
			path.append(':');
			path.append(board[cell.getHorizontal() + 2][cell.getVertical() + 2].getStringCoordinate());
			if(enemyColor == CheckerColor.Black && (cell.getHorizontal() + 2) == BOARD_SIZE - 1) {
				move = possibleMove(new Cell(cell.getHorizontal() + 2, cell.getVertical() + 2, cell.getColor(), CheckerType.King), path, Move.EatMove);
			}
			else {
				move = possibleMove(new Cell(cell.getHorizontal() + 2, cell.getVertical() + 2, cell.getColor(), cell.getType()), path, Move.EatMove);
			}
		}
		if(cell.getHorizontal() + 2 < BOARD_SIZE && cell.getVertical() - 2 >= 0 
				&& board[cell.getHorizontal() + 1][cell.getVertical() - 1].getColor() == enemyColor
				&& board[cell.getHorizontal() + 2][cell.getVertical() - 2].getType() == CheckerType.NoChecker
				&& board[cell.getHorizontal() + 2][cell.getVertical() - 2].getColor() == CheckerColor.NoColor
				&& !isDeleteChecker(board[cell.getHorizontal() + 1][cell.getVertical() - 1], path)) {
			finshPosition = false;						
			path.append(':');
			path.append(board[cell.getHorizontal() + 2][cell.getVertical() - 2].getStringCoordinate());
			if(enemyColor == CheckerColor.Black && (cell.getHorizontal() + 2) == BOARD_SIZE - 1) {
				move = possibleMove(new Cell(cell.getHorizontal() + 2, cell.getVertical() - 2, cell.getColor(), CheckerType.King), path, Move.EatMove);
			}
			else {
				move = possibleMove(new Cell(cell.getHorizontal() + 2, cell.getVertical() - 2, cell.getColor(), cell.getType()), path, Move.EatMove);
			}
		}
		if(cell.getHorizontal() - 2 >= 0 && cell.getVertical() + 2 < BOARD_SIZE 
			&& board[cell.getHorizontal() - 1][cell.getVertical() + 1].getColor() == enemyColor
			&& board[cell.getHorizontal() - 2][cell.getVertical() + 2].getType() == CheckerType.NoChecker
			&& board[cell.getHorizontal() - 2][cell.getVertical() + 2].getColor() == CheckerColor.NoColor
			&& !isDeleteChecker(board[cell.getHorizontal() - 1][cell.getVertical() + 1], path)) {
			finshPosition = false;
			path.append(':');
			path.append(board[cell.getHorizontal() - 2][cell.getVertical() + 2].getStringCoordinate());
			if(enemyColor == CheckerColor.White && (cell.getHorizontal() - 2) == 0) {
				move = possibleMove(new Cell(cell.getHorizontal() - 2, cell.getVertical() + 2, cell.getColor(), CheckerType.King), path, Move.EatMove);
			}
			else {
				move = possibleMove(new Cell(cell.getHorizontal() - 2, cell.getVertical() + 2, cell.getColor(), cell.getType()), path, Move.EatMove);					
			}
		}
		if(cell.getHorizontal() - 2 >= 0 && cell.getVertical() - 2 >= 0					
				&& board[cell.getHorizontal() - 1][cell.getVertical() - 1].getColor() == enemyColor
				&& board[cell.getHorizontal() - 2][cell.getVertical() - 2].getType() == CheckerType.NoChecker
				&& board[cell.getHorizontal() - 2][cell.getVertical() - 2].getColor() == CheckerColor.NoColor
				&& !isDeleteChecker(board[cell.getHorizontal() - 1][cell.getVertical() - 1], path)) {
			finshPosition = false;
			path.append(':');
			path.append(board[cell.getHorizontal() - 2][cell.getVertical() - 2].getStringCoordinate());
			if(enemyColor == CheckerColor.White && (cell.getHorizontal() - 2) == 0) {
				move = possibleMove(new Cell(cell.getHorizontal() - 2, cell.getVertical() - 2, cell.getColor(), CheckerType.King), path, Move.EatMove);
			}
			else {
				move = possibleMove(new Cell(cell.getHorizontal() - 2, cell.getVertical() - 2, cell.getColor(), cell.getType()), path, Move.EatMove);
			}
		}
			
								
		if(finshPosition && path.length() > 2) {
			possibleMoves.add(path.toString());
			
			int index = path.length() - 1; // запись про последий ход
			path.deleteCharAt(index--); //delete(':')
			path.deleteCharAt(index--); //delete('vertical')
			path.deleteCharAt(index--); //delete('horizontal')
		}
		else if(!finshPosition && path.length() > 2) {
			int index = path.length() - 1; // запись про последий ход
			path.deleteCharAt(index--); //delete(':')
			path.deleteCharAt(index--); //delete('vertical')
			path.deleteCharAt(index--); //delete('horizontal')
		}
		/*
		else {
			return move;
		}*/
		
		return Move.EatMove;
	}
	/*если это ход пешкой без поедания*/
	private Move pawnSimpleMove(Cell cell, String path) {
		
		CheckerColor enemyColor;
		if(cell.getColor() == CheckerColor.White) {
			enemyColor = CheckerColor.Black;
		}
		else {
			enemyColor = CheckerColor.White;
		}
		Cell rightCell;
		Cell leftCell;
		
		if(cell.getVertical() != BOARD_SIZE - 1) {
			if(cell.getColor() == CheckerColor.White) {
				rightCell = board[cell.getHorizontal() + 1][cell.getVertical() + 1];
			}
			else {
				rightCell = board[cell.getHorizontal() - 1][cell.getVertical() + 1];
			}
		}
		else {
			rightCell = cell;
		}
		
		if(cell.getVertical() != 0) {
			if(cell.getColor() == CheckerColor.White) {
				leftCell = board[cell.getHorizontal() + 1][cell.getVertical() - 1];
			}
			else {
				leftCell = board[cell.getHorizontal() - 1][cell.getVertical() - 1];
			}
		}
		else {
			leftCell = cell;
		}
		
		if(rightCell.getColor() == CheckerColor.NoColor) {
			StringBuffer buf = new StringBuffer(path);
			buf.append(':');
			buf.append(rightCell.getStringCoordinate());
			possibleMoves.add(buf.toString());
		}
		else {
			if(rightCell.getColor() == enemyColor 
					&& rightCell.getHorizontal() != BOARD_SIZE - 1 && rightCell.getHorizontal() != 0
					&& rightCell.getVertical() != BOARD_SIZE - 1 && rightCell.getVertical() != 0
					&& board[rightCell.getHorizontal() + rightCell.getHorizontal() - cell.getHorizontal()]
							[rightCell.getVertical() + rightCell.getVertical() - cell.getVertical()].getColor() == CheckerColor.NoColor){
				return Move.EatMove;
			}
		}
		
		if(leftCell.getColor() == CheckerColor.NoColor) {
			StringBuffer buf = new StringBuffer(path);
			buf.append(':');
			buf.append(leftCell.getStringCoordinate());
			possibleMoves.add(buf.toString());
		}
		else {
			if(leftCell.getColor() == enemyColor
					&& leftCell.getHorizontal() != BOARD_SIZE - 1 && leftCell.getHorizontal() != 0
					&& leftCell.getVertical() != BOARD_SIZE - 1 && leftCell.getVertical() != 0
					&& board[leftCell.getHorizontal() + leftCell.getHorizontal() - cell.getHorizontal()]
							[leftCell.getVertical() + leftCell.getVertical() - cell.getVertical()].getColor() == CheckerColor.NoColor) {
				return Move.EatMove;
			}
		}
		
		if(cell.getColor() == CheckerColor.White
				&& cell.getHorizontal() - 2 >= 0) {
			if(cell.getVertical() + 2 < BOARD_SIZE 
					&& board[cell.getHorizontal() - 1][cell.getVertical() + 1].getColor() == enemyColor
					&& board[cell.getHorizontal() - 2][cell.getVertical() + 2].getColor() == CheckerColor.NoColor) {
				return Move.EatMove;
			}
			if(cell.getVertical() - 2 >= 0 
					&& board[cell.getHorizontal() - 1][cell.getVertical() - 1].getColor() == enemyColor
					&& board[cell.getHorizontal() - 2][cell.getVertical() - 2].getColor() == CheckerColor.NoColor) {
				return Move.EatMove;
			}
		}
		else if(cell.getColor() == CheckerColor.Black
				&& cell.getHorizontal() + 2 < BOARD_SIZE) {
			if(cell.getVertical() + 2 < BOARD_SIZE 
					&& board[cell.getHorizontal() + 1][cell.getVertical() + 1].getColor() == enemyColor
					&& board[cell.getHorizontal() + 2][cell.getVertical() + 2].getColor() == CheckerColor.NoColor) {
				return Move.EatMove;
			}
			if(cell.getVertical() - 2 >= 0 
					&& board[cell.getHorizontal() + 1][cell.getVertical() - 1].getColor() == enemyColor
					&& board[cell.getHorizontal() + 2][cell.getVertical() - 2].getColor() == CheckerColor.NoColor) {
				return Move.EatMove;
			}
		}
		return Move.SimpleMove;
	}
	/*если это ход дамкой с поеданием фигур*/
	private Move kingEatMove(Cell cell, StringBuffer path) {
		CheckerColor enemyColor;
		if(cell.getColor() == CheckerColor.White) {
			enemyColor = CheckerColor.Black;
		}
		else {
			enemyColor = CheckerColor.White;
		}
		boolean finishPosition = true;
		int horizontal = cell.getHorizontal();
		int vertical = cell.getVertical();
	
			
		for(horizontal = cell.getHorizontal(), vertical = cell.getVertical(); 
				horizontal + 2 < BOARD_SIZE && vertical + 2 < BOARD_SIZE; horizontal++, vertical++) {
			if(board[horizontal + 1][vertical + 1].getColor() == cell.getColor()) break;
			if(board[horizontal + 1][vertical + 1].getColor() == enemyColor
					&& board[horizontal + 2][vertical + 2].getType() == CheckerType.NoChecker
					&& board[horizontal + 2][vertical + 2].getColor() == CheckerColor.NoColor
					&& board[horizontal][vertical].getType() == CheckerType.NoChecker) {
				if(isDeleteChecker(board[horizontal + 1][vertical + 1], path)) break;
				
				while(horizontal + 2 < BOARD_SIZE && vertical + 2 < BOARD_SIZE) {
					if(board[horizontal + 2][vertical + 2].getColor() == cell.getColor()) break;
					if(board[horizontal + 2][vertical + 2].getColor() == enemyColor 
							&& (horizontal + 2 == BOARD_SIZE || vertical + 2 == BOARD_SIZE)) break;
					finishPosition = false;
					path.append(':');
					path.append(board[horizontal + 2][vertical + 2].getStringCoordinate());
					possibleMove(new Cell(horizontal + 2, vertical + 2, cell.getColor(), cell.getType()), path, Move.EatMove);
					horizontal++;
					vertical++;
					if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
				}
				if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
			}
			else {
				if(board[horizontal + 1][vertical + 1].getColor() == enemyColor
						&& (board[horizontal + 2][vertical + 2].getType() != CheckerType.NoChecker
						|| board[horizontal + 2][vertical + 2].getColor() != CheckerColor.NoColor)
						&& board[horizontal][vertical].getType() == CheckerType.NoChecker) break;
			}
			
		}
		
		for(horizontal = cell.getHorizontal(), vertical = cell.getVertical(); 
				horizontal + 2 < BOARD_SIZE  && vertical - 2 >= 0; horizontal++, vertical--) {
			if(board[horizontal + 1][vertical - 1].getColor() == cell.getColor()) break;
			if(board[horizontal + 1][vertical - 1].getColor() == enemyColor
					&& board[horizontal + 2][vertical - 2].getType() == CheckerType.NoChecker
					&& board[horizontal + 2][vertical - 2].getColor() == CheckerColor.NoColor
					&& board[horizontal][vertical].getType() == CheckerType.NoChecker) {
				if(isDeleteChecker(board[horizontal + 1][vertical - 1], path)) break;
				
				while(horizontal + 2 < BOARD_SIZE && vertical - 2 >= 0) {
					if(board[horizontal + 2][vertical - 2].getColor() == cell.getColor()) break;
					if(board[horizontal + 2][vertical - 2].getColor() == enemyColor 
							&& (horizontal + 2 == BOARD_SIZE || vertical - 2 == 0)) break;
					finishPosition = false;
					path.append(':');
					path.append(board[horizontal + 2][vertical - 2].getStringCoordinate());
					possibleMove(new Cell(horizontal + 2, vertical - 2, cell.getColor(), cell.getType()), path, Move.EatMove);
					horizontal++;
					vertical--;
					if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
				}
				if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
			}
			if(board[horizontal + 1][vertical - 1].getColor() == enemyColor
					&& (board[horizontal + 2][vertical - 2].getType() != CheckerType.NoChecker
					|| board[horizontal + 2][vertical - 2].getColor() != CheckerColor.NoColor)
					&& board[horizontal][vertical].getType() == CheckerType.NoChecker) break;
			
		}
			
		for(horizontal = cell.getHorizontal(), vertical = cell.getVertical(); 
				horizontal - 2 >= 0 && vertical + 2 < BOARD_SIZE; horizontal--, vertical++) {
			if(board[horizontal - 1][vertical + 1].getColor() == cell.getColor()) break;
			if(board[horizontal - 1][vertical + 1].getColor() == enemyColor
					&& board[horizontal - 2][vertical + 2].getType() == CheckerType.NoChecker
					&& board[horizontal - 2][vertical + 2].getColor() == CheckerColor.NoColor
					&& board[horizontal][vertical].getType() == CheckerType.NoChecker) {
				if(isDeleteChecker(board[horizontal - 1][vertical + 1], path)) break;
				
				while(horizontal - 2 >= 0 && vertical + 2 < BOARD_SIZE) {
					if(board[horizontal - 2][vertical + 2].getColor() == cell.getColor()) break;
					if(board[horizontal - 2][vertical + 2].getColor() == enemyColor 
							&& (horizontal - 2 == 0 || vertical + 2 == BOARD_SIZE)) break;
					finishPosition = false;
					path.append(':');
					path.append(board[horizontal - 2][vertical + 2].getStringCoordinate());
					possibleMove(new Cell(horizontal - 2, vertical + 2, cell.getColor(), cell.getType()), path, Move.EatMove);
					horizontal--;
					vertical++;
					
					if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
				}
				if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
			}
			else {
				if(board[horizontal - 1][vertical + 1].getColor() == enemyColor
						&& (board[horizontal - 2][vertical + 2].getType() != CheckerType.NoChecker
						|| board[horizontal - 2][vertical + 2].getColor() != CheckerColor.NoColor)
						&& board[horizontal][vertical].getType() == CheckerType.NoChecker) break;
			}
		}
		
		for(horizontal = cell.getHorizontal(), vertical = cell.getVertical(); 
				horizontal - 2 >= 0 && vertical - 2 >= 0; horizontal--, vertical--) {
			if(board[horizontal - 1][vertical - 1].getColor() == cell.getColor()) break;
			if(board[horizontal - 1][vertical - 1].getColor() == enemyColor
					&& board[horizontal - 2][vertical - 2].getType() == CheckerType.NoChecker
					&& board[horizontal - 2][vertical - 2].getColor() == CheckerColor.NoColor
					&& board[horizontal][vertical].getType() == CheckerType.NoChecker) {
				if(isDeleteChecker(board[horizontal - 1][vertical - 1], path)) break;
				
				while(horizontal - 2 >= 0 && vertical - 2 >= 0) {
					if(board[horizontal - 2][vertical - 2].getColor() == cell.getColor()) break;
					if(board[horizontal - 2][vertical - 2].getColor() == enemyColor 
							&& (horizontal - 2 == 0 || vertical - 2 == 0)) break;
					finishPosition = false;
					path.append(':');
					path.append(board[horizontal - 2][vertical - 2].getStringCoordinate());
					possibleMove(new Cell(horizontal - 2, vertical - 2, cell.getColor(), cell.getType()), path, Move.EatMove);
					horizontal--;
					vertical--;
					if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
				}
				if(board[horizontal][vertical].getType() != CheckerType.NoChecker) break;
			}
			if(board[horizontal - 1][vertical - 1].getColor() == enemyColor
					&& (board[horizontal - 2][vertical - 2].getType() != CheckerType.NoChecker
					|| board[horizontal - 2][vertical - 2].getColor() != CheckerColor.NoColor)
					&& board[horizontal][vertical].getType() == CheckerType.NoChecker) break;
				
		}
			
		if(finishPosition && path.length() > 2) {
			
			String[] pathArray = path.toString().split(":");
			
			int index = path.length() - 1; // запись про последий ход
			path.deleteCharAt(index--); //delete(':')
			path.deleteCharAt(index--); //delete('vertical')
			path.deleteCharAt(index--); //delete('horizontal')
			
			StringBuffer buf = new StringBuffer();
			int i;
			for( i = 0; i < pathArray.length - 1; i++) {
				buf.append(pathArray[i]);
				buf.append(':');
			}
			i--;
			int startVertical = (int)(pathArray[i].charAt(0) - '0');
			int startHorizontal = (int)(pathArray[i++].charAt(1) - '0');
			int finishVertical = (int)(pathArray[i].charAt(0) - '0');
			int finishHorizontal = (int)(pathArray[i].charAt(1) - '0');
			
			int verticalDifference = (finishVertical - startVertical) / Math.abs(finishVertical - startVertical);
			int horizontalDifference = (finishHorizontal - startHorizontal) / Math.abs(finishHorizontal - startHorizontal);
			
			buf.append(pathArray[i]);
			buf.append(":");
			for(startVertical = finishVertical, startHorizontal = finishHorizontal;
					Board.checkCorrectCoordinate(startHorizontal, startVertical);
					startVertical += verticalDifference, startHorizontal += horizontalDifference) {
				if(board[startHorizontal][startVertical].getType() != CheckerType.NoChecker) break;
				path.append(":");
				path.append(Integer.toString(startVertical));
				path.append(Integer.toString(startHorizontal));
				
				
				if(path.length() > 2 && !deleteBadMoves(path.toString())) {
					possibleMoves.add(path.toString());
				}
				index = path.length() - 1; // запись про последий ход
				path.deleteCharAt(index--); //delete(':')
				path.deleteCharAt(index--); //delete('vertical')
				path.deleteCharAt(index--); //delete('horizontal')
			}
		}
		if(!finishPosition && path.length() > 2) {
			int index = path.length() - 1; // запись про последий ход
			path.deleteCharAt(index--); //delete(':')
			path.deleteCharAt(index--); //delete('vertical')
			path.deleteCharAt(index--); //delete('horizontal')
		}
		return 	Move.EatMove;
	}
	/*если это ход дамкой без поедания фигур*/
	private Move kingSimpleMove(Cell cell, String path) {
		CheckerColor enemyColor;
		if(cell.getColor() == CheckerColor.White) {
			enemyColor = CheckerColor.Black;
		}
		else {
			enemyColor = CheckerColor.White;
		}
		for(int horizontal = cell.getHorizontal() + 1, vertical = cell.getVertical() + 1;
				horizontal < BOARD_SIZE && horizontal >= 0 && vertical < BOARD_SIZE && vertical >= 0 
				&& board[horizontal][vertical].getColor() != cell.getColor(); horizontal++, vertical++) {
			if(board[horizontal][vertical].getColor() == CheckerColor.NoColor) {
				StringBuffer buf = new StringBuffer(path);
				buf.append(':');
				buf.append(board[horizontal][vertical].getStringCoordinate());
				possibleMoves.add(buf.toString());
			}
			else {
				if(board[horizontal][vertical].getColor() == enemyColor
						&& horizontal + 1 < BOARD_SIZE && vertical + 1 < BOARD_SIZE
						&& board[horizontal + 1][vertical + 1].getType() == CheckerType.NoChecker
						&& board[horizontal + 1][vertical + 1].getColor() == CheckerColor.NoColor){
					return Move.EatMove;
				}
				break;
			}
		}
		for(int horizontal = cell.getHorizontal() + 1, vertical = cell.getVertical() - 1;
				horizontal < BOARD_SIZE && horizontal >= 0 && vertical < BOARD_SIZE && vertical >= 0 
				&& board[horizontal][vertical].getColor() != cell.getColor(); horizontal++, vertical--) {
			if(board[horizontal][vertical].getColor() == CheckerColor.NoColor) {
				StringBuffer buf = new StringBuffer(path);
				buf.append(':');
				buf.append(board[horizontal][vertical].getStringCoordinate());
				possibleMoves.add(buf.toString());
			}
			else {
				if(board[horizontal][vertical].getColor() == enemyColor
						&& horizontal + 1 < BOARD_SIZE && vertical - 1 >= 0
						&& board[horizontal + 1][vertical - 1].getType() == CheckerType.NoChecker
						&& board[horizontal + 1][vertical - 1].getColor() == CheckerColor.NoColor){
					return Move.EatMove;
				}
				break;
			}
		}
		for(int horizontal = cell.getHorizontal() - 1, vertical = cell.getVertical() + 1;
				horizontal < BOARD_SIZE && horizontal >= 0 && vertical < BOARD_SIZE && vertical >= 0 
				&& board[horizontal][vertical].getColor() != cell.getColor(); horizontal--, vertical++) {
			if(board[horizontal][vertical].getColor() == CheckerColor.NoColor) {
				StringBuffer buf = new StringBuffer(path);
				buf.append(':');
				buf.append(board[horizontal][vertical].getStringCoordinate());
				possibleMoves.add(buf.toString());
			}
			else {
				if(board[horizontal][vertical].getColor() == enemyColor
						&& horizontal - 1 >= 0 && vertical + 1 < BOARD_SIZE
						&& board[horizontal - 1][vertical + 1].getType() == CheckerType.NoChecker
						&& board[horizontal - 1][vertical + 1].getColor() == CheckerColor.NoColor){
					return Move.EatMove;
				}
				break;
			}
			
		}
		for(int horizontal = cell.getHorizontal() - 1, vertical = cell.getVertical() - 1;
				horizontal < BOARD_SIZE && horizontal >= 0 && vertical < BOARD_SIZE && vertical >= 0 
				&& board[horizontal][vertical].getColor() != cell.getColor(); horizontal--, vertical--) {
			if(board[horizontal][vertical].getColor() == CheckerColor.NoColor) {
				StringBuffer buf = new StringBuffer(path);
				buf.append(':');
				buf.append(board[horizontal][vertical].getStringCoordinate());
				possibleMoves.add(buf.toString());
			}
			else {
				if(board[horizontal][vertical].getColor() == enemyColor
						&& horizontal - 1 >= 0 && vertical - 1 >= 0
						&& board[horizontal - 1][vertical - 1].getType() == CheckerType.NoChecker
						&& board[horizontal - 1][vertical - 1].getColor() == CheckerColor.NoColor){
					return Move.EatMove;
				}
				break;
			}
		}
		return Move.SimpleMove;
	}
	
	/*некоторые ходы которые возвращие possibleMove неверны. Этот метод проверяет эти ходы*/
	
	private boolean checkBadMove(String path, String betaPath) {
		int index = betaPath.length() - 1;
		
		int finishHorizontal = (int)(betaPath.charAt(index--) - '0');	
		int finishVertical = (int)(betaPath.charAt(index--) - '0');
		index--;
		int startHorizontal = (int)(betaPath.charAt(index--) - '0');
		int startVertical = (int)(betaPath.charAt(index--) - '0');
		
		StringBuffer pathBuff = new StringBuffer(betaPath);
		pathBuff.deleteCharAt(pathBuff.length() - 1);
		pathBuff.deleteCharAt(pathBuff.length() - 1);
			
		int verticalDiff = (finishVertical - startVertical) / Math.abs(finishVertical - startVertical);
		int horizontalDiff = (finishHorizontal - startHorizontal) / Math.abs(finishHorizontal - startHorizontal);
			
		for(int verticalIter = finishVertical, horizontalIter = finishHorizontal;
				Board.checkCorrectCoordinate(verticalIter, horizontalIter); verticalIter += verticalDiff, horizontalIter += horizontalDiff) {
			StringBuffer buf = new StringBuffer(pathBuff);
			buf.append(verticalIter);
			buf.append(horizontalIter);
			if(path.contains(buf.toString()) && path.length() > buf.toString().length()) {
				return true;
			}	
		}
			
		verticalDiff *= (-1);
		horizontalDiff *= (-1);
		
		for(int verticalIter = finishVertical, horizontalIter = finishHorizontal;
				Board.checkCorrectCoordinate(verticalIter, horizontalIter) && board[horizontalIter][verticalIter].getType() == CheckerType.NoChecker; 
				verticalIter += verticalDiff, horizontalIter += horizontalDiff) {
			StringBuffer buf = new StringBuffer(pathBuff);
			buf.append(verticalIter);
			buf.append(horizontalIter);
			if(path.contains(buf.toString()) && path.length() > buf.toString().length()) {
				return true;
			}	
		}
		return false;
	}
	
	/*метод для удаления неверных ходов*/
	private boolean deleteBadMoves(String path) {
		int iter = 0;
		for(String string: possibleMoves) {
			if (checkBadMove(string, path)) {
				return true;
            }
			iter++;
		}
		return false;
	}
	/*проверка на то, была ли съеденна до этого фигура*/
	private boolean isDeleteChecker(Cell cell, StringBuffer path) {
		int index = 0;
		
		int prevVertical = (int)(path.charAt(index++) - '0');
		
		int prevHorizontal = (int)(path.charAt(index++) - '0');
		
		int nextVertical;
		int nextHorizontal;
		
		int verticalDifference;
		int horizontalDifference;
		for(index++; index < path.length();index++) {
			
			nextVertical = (int)(path.charAt(index++) - '0');
			nextHorizontal = (int)(path.charAt(index++) - '0');
			
			verticalDifference = (nextVertical - prevVertical) / Math.abs((nextVertical - prevVertical));
			horizontalDifference = (nextHorizontal - prevHorizontal) / Math.abs((nextHorizontal - prevHorizontal));
			
			prevVertical += verticalDifference;
			prevHorizontal += horizontalDifference;
			
			while(prevVertical != nextVertical && prevHorizontal != nextHorizontal) {
				if(prevVertical == cell.getVertical() && prevHorizontal == cell.getHorizontal()) {
					return true;
				}
				prevVertical += verticalDifference;
				prevHorizontal += horizontalDifference;
			}
			
		}
		return false;
	}
}

