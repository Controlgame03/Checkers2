package model;

import io.*;
import model.Cell.*;
import ui.*;
import java.util.*;
import java.io.*;
import java.lang.Math;


/*Главный класс который отвечает за работу с доской.
 * Инициализация доски, ходы и прочее происходит здесь
 * Полезный метод для отладки = toString*/
public class Board {

	public enum GameResult{
		BlackWin,
		WhiteWin,
		Draw,
		Continue;
	}
	
	public enum Move{
		SimpleMove, //простой ход
		EatMove; //ход с поеданием вражеской шашки
	}
	
	private static final int BOARD_SIZE = 8;
	private static final int NULL = 0;
	private static final int ONE_CHECKER_SIZE = 2;
	private static final CheckerColor DEFAULT_FIRST_MOVE_COLOR = CheckerColor.White;
	
	private Cell board[][];
	
	private FileMaster fMaster; // для работы с файлами
	private BoardPossibleMoves possibleMovesClass = new BoardPossibleMoves(); //класс вычисляющий возможные ходы
	private CheckerColor nextMoveColor = CheckerColor.NoColor; //шашка какого цвета будет ходить следующей
	private ArrayList<String> possibleMoves = null; //массив где храняться все дальнейшие возможжные пути
	
	public Board() throws IOException{
		nextMoveColor = DEFAULT_FIRST_MOVE_COLOR;
		startNewGame();
	}
	
	public Board(CheckerColor color) throws IOException{
		nextMoveColor = color;
		startNewGame();
	}

	private void startNewGame() throws IOException{
		fMaster = new FileMaster();
		board = fMaster.loadBoardFromTextFile(System.getProperty("user.dir") + "/src/model/BasicCheckersPosition.txt");
	}
	
	/*проверка на выигрыш одной из сторон*/
	public GameResult isWin() {
		try {
			if(possibleMovesClass.possibleMove(CheckerColor.White, board).isEmpty() 
					&& possibleMovesClass.possibleMove(CheckerColor.Black, board).isEmpty()) {
				return GameResult.Draw;
			}
			else if(possibleMovesClass.possibleMove(CheckerColor.White, board).isEmpty()) {
				return GameResult.BlackWin;
			}
			else if(possibleMovesClass.possibleMove(CheckerColor.Black, board).isEmpty()) {
				return GameResult.WhiteWin;
			}
			else {
				return GameResult.Continue;
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public GameResult endGame() throws IOException {
		fMaster.endGame(isWin());
		return isWin();
	}
	
	/*метод делающий ход с проверкой на корректность и записью в файл*/
	public ArrayList<String> move(String movePath) throws IOException{
		if(possibleMoves != null) possibleMoves.clear();
		
		String[] checker = movePath.split(":");
		StringBuffer prevChecker = new StringBuffer();
		StringBuffer deleteCheckers = new StringBuffer();
		
		for(String currentChecker : checker) {
			if(prevChecker.length() == NULL) { 
				prevChecker = new StringBuffer(currentChecker); 
				prevChecker.append(':');
				continue; 
			}
			
			if(deleteCheckers.length() != 0) deleteCheckers.append(',');
			
			boolean flag = isCorrectMove(new StringBuffer(prevChecker).append(currentChecker));
			if(flag) 
				deleteCheckers.append(makeMove(new StringBuffer(prevChecker).append(currentChecker)));
			else return new ArrayList<String>();
			
			
			prevChecker = new StringBuffer(currentChecker);
			prevChecker.append(':');
		}
		
		fMaster.writeMove(movePath, deleteCheckers.toString());
		nextMoveColor = Cell.enemyColor(nextMoveColor);
		possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		
		return possibleMoves;
	}
	/*проверка корректности хода*/
	private boolean isCorrectMove(StringBuffer path) {
		Cell startPosition;
		Cell finishPosition;

		int index = 0;
		int endIndex = path.length();
		
		int vertical = (int)(path.charAt(index++) - '0');
		int horizontal = (int)(path.charAt(index++) - '0');
		
		index++;//пропуск символа ':'
		//Проверка координат начала хода на выход за пределы доски и на цвет клетки
		if(!Board.checkCorrectCoordinate(horizontal, vertical)) {
			return false;
		}
		
		startPosition = board[horizontal][vertical];
		
		//Проверка на наличие ходящей шашки
		if(startPosition.getColor() == CheckerColor.NoColor 
				|| startPosition.getType() == CheckerType.NoChecker) {
			return false;
		}
		
		vertical = (int)(path.charAt(index++) - '0');
		horizontal = (int)(path.charAt(index++) - '0');
		
		//Проверка координат конца хода на выход за пределы доски и на цвет клетки
		if(!Board.checkCorrectCoordinate(horizontal, vertical)) {
			return false;
		}
			
		finishPosition = board[horizontal][vertical];
			
		//Проверка на пустоту конечной клетки
		if(finishPosition.getColor() != CheckerColor.NoColor && 
				finishPosition.getType() != CheckerType.NoChecker) {
			return false;
		}
		
		int verticalDifference = finishPosition.getVertical() - startPosition.getVertical();
		int horizontalDifference = finishPosition.getHorizontal() - startPosition.getHorizontal();
		
		//Расположены ли начальная и конечные клетки на одной диагонали?
		if(Math.abs(verticalDifference) != Math.abs(horizontalDifference)) {
			return false;
		}
		
		//Eсли я хочу ходить пешкой
		if(startPosition.getType() == CheckerType.Pawn) {
			
			//если пешка идёт на соседнюю клетку, т.е. просто ходит
			if(Math.abs(verticalDifference) == 1) {
				
				//Проверка правильности направления пешки
				if(horizontalDifference < 0 && startPosition.getColor() == CheckerColor.White
						|| horizontalDifference > 0 && startPosition.getColor() == CheckerColor.Black) {
					return false;
				}
				return true;
			}
			
			//если пешка сьедает вражескую фигуру
			else if(Math.abs(verticalDifference) == 2){
				
				int middleHorizontal = startPosition.getHorizontal() + 
						horizontalDifference / Math.abs(horizontalDifference);
				int middleVertical = startPosition.getVertical() + 
						verticalDifference / Math.abs(verticalDifference);
				
				//Проверка различности цветов сьеденной пешки и сьедающей пешки
				if((startPosition.getColor() == CheckerColor.White && board[middleHorizontal][middleVertical].getColor() == CheckerColor.Black)
						|| (startPosition.getColor() == CheckerColor.Black && board[middleHorizontal][middleVertical].getColor() == CheckerColor.White)){
					return true;
				}
				return false;
			}
			
			return false;
		}
		
		//Если ходят дамкой	
		else if(startPosition.getType() == CheckerType.King) {
			
			int diagonalHorizontal = startPosition.getHorizontal() + horizontalDifference / Math.abs(horizontalDifference);
			int diagonalVertical = startPosition.getVertical() + verticalDifference / Math.abs(verticalDifference);
			
			int checkersCount = 0; //счётчик вподряд идущих шашек
			
			while(diagonalHorizontal != finishPosition.getHorizontal() 
					&& diagonalVertical != finishPosition.getVertical()) {
				
				//если на пути дамки встретилась союзная фигура
				if(startPosition.getColor() == board[diagonalHorizontal][diagonalVertical].getColor()) {
					return false;
				}
				
				//проверка на чередующиеся фигруры
				if(board[diagonalHorizontal][diagonalVertical].getType() != CheckerType.NoChecker) {
					checkersCount++;
					if(checkersCount > 1) {
						return false;
					}
				}
				else {
					checkersCount = 0;
				}
				
				diagonalHorizontal += horizontalDifference / Math.abs(horizontalDifference);
				diagonalVertical += verticalDifference / Math.abs(verticalDifference);
				
			}
			return true;
		}
		return false;
	}
	
	/*Функция которая передвигает шашки на доске*/
	private String makeMove(StringBuffer path) {

		Cell startPosition;
		Cell finishPosition;
		
		StringBuffer deleteCheckers = new StringBuffer();
		StringBuffer delete = new StringBuffer();
		
		int index = 0;
		int endIndex = path.length();
		
		int vertical = (int)(path.charAt(index++) - '0');
		int horizontal = (int)(path.charAt(index++) - '0');
		
		index++;
		
		startPosition = board[horizontal][vertical];
		
		vertical = (int)(path.charAt(index++) - '0');
		horizontal = (int)(path.charAt(index++) - '0');
		
		finishPosition = board[horizontal][vertical];
		
		int verticalDifference = finishPosition.getVertical() - startPosition.getVertical();
		int horizontalDifference = finishPosition.getHorizontal() - startPosition.getHorizontal();
		
		int currentHorizontal = startPosition.getHorizontal() + horizontalDifference / Math.abs(horizontalDifference);
		int currentVertical = startPosition.getVertical() + verticalDifference / Math.abs(verticalDifference);
			
		while(currentHorizontal != finishPosition.getHorizontal() 
				&& currentVertical != finishPosition.getVertical()) {
				
			if(board[currentHorizontal][currentVertical].getType() != CheckerType.NoChecker) {
				if(board[currentHorizontal][currentVertical].getType() == CheckerType.King) {
					delete.append((char)('A' + currentVertical));	
				}
				if(board[currentHorizontal][currentVertical].getType() == CheckerType.Pawn) {
					delete.append((char)('a' + currentVertical));
				}
				delete.append((char)('1' + currentHorizontal));
				
				deleteCheckers.append(currentVertical);
				deleteCheckers.append(currentHorizontal);
				deleteCheckers.append(',');
			}
					
			currentHorizontal += horizontalDifference / Math.abs(horizontalDifference);
			currentVertical += verticalDifference / Math.abs(verticalDifference);		
		}
			
		if(finishPosition.getHorizontal() == BOARD_SIZE - 1 && startPosition.getColor() == CheckerColor.White
				|| finishPosition.getHorizontal() == NULL && startPosition.getColor() == CheckerColor.Black) {
			finishPosition.setType(CheckerType.King);
		}
		else finishPosition.setType(startPosition.getType());
		
		finishPosition.setColor(startPosition.getColor());
		startPosition.setType(CheckerType.NoChecker);
		startPosition.setColor(CheckerColor.NoColor);
		
		String[] currentDeleteCheckers = deleteCheckers.toString().split(",");
		
		for(String currentChecker: currentDeleteCheckers) {
			if(currentChecker.length() == NULL) continue;
			int currentIndex = 0;
			
			int deleteVertical = currentChecker.charAt(currentIndex++) - '0';
			int deleteHorizontal = currentChecker.charAt(currentIndex++) - '0';
			
			board[deleteHorizontal][deleteVertical].setType(CheckerType.NoChecker);
			board[deleteHorizontal][deleteVertical].setColor(CheckerColor.NoColor);
		}
		return delete.toString();
	}
	
	/*возвращает все возможные текушие ходы*/
	public ArrayList<String> getPossibleMoves(){
		if(possibleMoves == null) {
			possibleMoves = possibleMovesClass.possibleMove(nextMoveColor, board);
		}
		return possibleMoves;
	}
	
	static public boolean checkCorrectCoordinate(int horizontal, int vertical) {
		final int BOARD_SIZE = 8;
		return (horizontal >= 0 && horizontal < BOARD_SIZE 
				&& vertical >= 0 && vertical < BOARD_SIZE
				&& horizontal % 2 == vertical % 2);
	}
		
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				buf.append(board[i][j].toString());
			}
			buf.append('\n');
		}
		return buf.toString();
	}
	
	public void loadBoardFromTextFile(String filename) throws IOException {
		board = fMaster.loadBoardFromTextFile(filename);	
	}
}
