package io;

import java.util.*;

import model.*;
import model.Cell.*;

import java.io.*;

/*
 *		Класс для работы с файлами. Класс FileMaster используется как дополнение к классу Board.
 * Этот класс отвечает за загрузку игры в файл и последующего редактирования файла.
 * 
 *		Файлы и их формат
 * Каждая партия записывается в файл. Назание файла "game" + №(партии) ".txt". Файл находится в репозитории 
 * home/.../Checkers/game/. Формат файла такой: первые 4 строчки дополнительная информация для партии (первые 2 - кто и против кого
 * играет, последние 2 - растоновка позиций) далее идёт запись ходов партии. Ход записывается следующим образом: Порядковый номер, 
 * затем ход белых, потом ход чёрных. Пример: 4) e3:d4 e2:c4:a6[d3, b5]. В скобках съеденные шашки
 * */
public class FileMaster{
	
	private static final int EOF = -1; 
	private static final int NOEOF = 0;
	private static final int BOARD_SIZE = 8;
	
	private FileWriter currentSaveFile; //в этот файл записывается партия.
	private Integer moveCount = 1;
	private int moveEndCount = 0;
	
	public FileWriter getCurrentSaveFile() {
		return currentSaveFile;
	}
	
	public FileMaster() throws IOException{
		currentSaveFile = createNewFile();
	}
	
	/*Метод который вызывается после окончания партии. Он записывает в файл
	 * результат текущей партии*/
	public void endGame(Board.GameResult result) throws IOException {
		this.currentSaveFile.write("game result: ");
		if(result == Board.GameResult.BlackWin) {
			this.currentSaveFile.write("black win");
		}
		else if(result == Board.GameResult.WhiteWin) {
			this.currentSaveFile.write("white win");
		}
		else if(result == Board.GameResult.Draw) {
			this.currentSaveFile.write("draw");
		}
		currentSaveFile.close();
	}
	/*Создает новый файл, куда будет заисываться игра*/
	private FileWriter createNewFile() throws IOException {
		String fileDirectory = System.getProperty("user.dir") + "/src/games/";
		FileReader reader = new FileReader(fileDirectory + "info.txt");
		int gameNumber;
		StringBuffer number = new StringBuffer();
		
		char symbol = NOEOF;
		while((symbol = (char)reader.read()) != '\n') {
			number.append(Character.getNumericValue(symbol));
		}
		reader.close();
		
		gameNumber = Integer.parseInt(number.toString());
		gameNumber++;
		
		String game = "CheckerGameNumber" + gameNumber;
	
		FileWriter writeInfo = new FileWriter(fileDirectory + "info.txt");
		writeInfo.write(new String().valueOf(gameNumber));
		writeInfo.write('\n');
		writeInfo.close();
		return new FileWriter(fileDirectory + game);
	}
	
	
	public CheckerColor getFirstMoveColor() {
		return CheckerColor.White;
	}
	
	/*Начальные позиции загружаются из файла*/
	public Cell[][] loadBoardFromTextFile(String filename) throws IOException {
		FileReader fileRead = new FileReader(filename);
		Cell[][] board = new Cell[BOARD_SIZE][BOARD_SIZE];
		
		for(int horizontal = 0; horizontal < BOARD_SIZE; horizontal++) {
			for(int vertical = 0; vertical < BOARD_SIZE; vertical++) {
				board[horizontal][vertical] = new Cell();
				board[horizontal][vertical].setCoordinate(horizontal, vertical);
			}
		}
		
		char vertical;
		int horizontal;
		int verticalInt;
		
		int symbol = ' ';
		currentSaveFile.write('\n');
		currentSaveFile.write("white: ");
		
		do { 
			if(symbol == ',' || symbol == ' ' || symbol == '\n') {
				symbol = fileRead.read();
				continue;
			}	
			CheckerType type = CheckerType.NoChecker;
			vertical = (char)(symbol);
			horizontal = (int)((symbol = fileRead.read()) - '0') - 1;
			
			if(vertical >= 'a' && vertical <= 'h') {
				verticalInt = ((int)vertical - (int)'a');
				type = CheckerType.Pawn;	
			}
			else {
				verticalInt = ((int)vertical - (int)'A');
				type = CheckerType.King;
			}
			
			board[horizontal][verticalInt].setColor(CheckerColor.White);
			board[horizontal][verticalInt].setType(type);
			currentSaveFile.write(vertical);
			currentSaveFile.write(new Integer(horizontal + 1).toString());
			currentSaveFile.write(',');
			
			symbol = fileRead.read();
		
		} while(symbol != '\n');
		
		currentSaveFile.write(';');
		currentSaveFile.write('\n');
		currentSaveFile.write("black: ");
		
		do {
			if(symbol == ',' || symbol == ' ' || symbol == '\n') {
				symbol = fileRead.read();
				continue;
			
			}
			CheckerType type = CheckerType.NoChecker;
			vertical = (char)(symbol);
			horizontal = (int)((symbol = fileRead.read()) - '0') - 1;
			
			if(vertical >= 'a' && vertical <= 'h') {
				verticalInt = ((int)vertical - (int)'a');
				type = CheckerType.Pawn;	
			}
			else {
				verticalInt = ((int)vertical - (int)'A');
				type = CheckerType.King;
			}
			board[horizontal][verticalInt].setColor(CheckerColor.Black);
			board[horizontal][verticalInt].setType(type);
			currentSaveFile.write(vertical);
			currentSaveFile.write(new Integer(horizontal + 1).toString());
			currentSaveFile.write(',');

			symbol = fileRead.read();	
		} while(symbol != EOF);
		
		currentSaveFile.write(';');
		currentSaveFile.write('\n');
		return board;
	}
	
	
	/*Метод для записи хода в файл*/
	public void writeMove(String move, String delete) throws IOException {
		
		if(++moveEndCount > 1) {
			moveEndCount = 0;
			currentSaveFile.write(" ");
			currentSaveFile.write(move);
			currentSaveFile.write('[');
			currentSaveFile.write(delete);
			currentSaveFile.write(']');
			currentSaveFile.write(" ");
			if(moveCount % 8 == 0) {
				currentSaveFile.write('\n');
			}
			currentSaveFile.flush();
		}
		else {
			currentSaveFile.write(moveCount.toString());
			moveCount++;
			
			currentSaveFile.write(") ");
			currentSaveFile.write(move);
			currentSaveFile.write('[');
			currentSaveFile.write(delete);
			currentSaveFile.write(']');
			currentSaveFile.flush();
		}
	}
}
