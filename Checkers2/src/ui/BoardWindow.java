package ui;

import io.*;
import model.*;
import model.Board.GameResult;
import model.Cell.*;
import ui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
/*Фрейм для игры в шашки. В нем массив кнопок 8 на 8, которые символизируют доску*/
public class BoardWindow extends JFrame {
	
	private static final int BOARD_SIZE = 8;
	private static final String fontName = "Century Gothic";
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;
	
	private static final int DIALOG_WIDTH = 100;
	private static final int DIALOG_HEIGHT = 100;
	
	private static final String TITLE = "Шашки";
	
	Board board; //логика игры
	MyButton[][] cells = new MyButton[BOARD_SIZE][BOARD_SIZE];//доска
	JPanel cellsPanel = new JPanel();
	
	ImageIcon whitePawnImg;
	ImageIcon whiteKingImg;
	ImageIcon blackPawnImg;
	ImageIcon blackKingImg;
	
	ArrayList<String> possibleMoves = null;
	
	private class MyButton extends JButton{
		int horizontal;
		int vertical;
		
		boolean isPossibleChecker = false;//это поле может быть следующей шашкой, которая будет ходить?
		boolean isPossibleMove = false;//это поле может быть следующим ходом, который сделает игрок?
		
		String path;
		
		CheckerColor color;
		
		CheckerType type;
		
		public MyButton(){
			super();
			ActionListener action = new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	try {
				    	if(isPossibleMove) {
				    		possibleMoves = board.move(path);
				    			
				    		cellsDefaultOptions(path);
				    		getPossibleMoves();
				    		showBoard();
				    		if(possibleMoves.isEmpty()) {
				    			endGame(board.isWin());
					    	}
				    	}
				    	else if(isPossibleChecker) showPossibleMoves(horizontal, vertical);
			    	}
			    	catch(Exception exep) {
			    		System.out.println(exep.getMessage());
			    	}
			    }
			};
			this.addActionListener(action);
		}
	}
	
	
	public BoardWindow() throws IOException{
		super(TITLE);
		this.setLayout(new BorderLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(dim.width/2 - WIDTH/2, dim.height/2 - HEIGHT/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setLayout(new BorderLayout());
		
		
		
		board = new Board();
		
		Font font = new Font(fontName, Font.BOLD, 20);
        this.add(Box.createRigidArea(new Dimension(40,40)));
        
        initializeIcons();
		initializeBoard();
		
		this.setBackground(Color.YELLOW);
		
		showBoard();
		
		startGame();	
		
		setVisible(true);
	}
	
	public Board getBoard() {
		return board;
	}
	
	private void initializeBoard(){
		this.add(cellsPanel, BorderLayout.CENTER);
		
		cellsPanel.setLayout(new GridLayout(8, 8));
		
		initializeCells();
		
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				cellsPanel.add(cells[i][j]);
			}
		}
		
		
	}
	
	private void getPossibleMoves(){
		if(possibleMoves == null) {
			possibleMoves = board.getPossibleMoves();
		}
		for(int i = 0; i < possibleMoves.size(); i++) {
			int index = 0;
			
			int vertical = (int)(possibleMoves.get(i).charAt(index++) - '0');
			int horizontal = (int)(possibleMoves.get(i).charAt(index++) - '0');
			
			cells[horizontal][vertical].isPossibleChecker = true;
		}
	}
	
	private void startGame() {
		try {
			getPossibleMoves();
			showBoard();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void initializeCells() {
		for(int horizontal = 0; horizontal < BOARD_SIZE; horizontal++) {
			for(int vertical = 0; vertical < BOARD_SIZE; vertical++) {
				cells[horizontal][vertical] = new MyButton();
				cells[horizontal][vertical].horizontal = horizontal;
				cells[horizontal][vertical].vertical = vertical;
				cells[horizontal][vertical].setBorderPainted(false);
				cells[horizontal][vertical].setOpaque(true);
			}
		}
	}
	
	private void initializeIcons() {
		whitePawnImg =new ImageIcon(getClass().getResource("/ui/img/whitePawn.png"));
	    Image image = whitePawnImg.getImage();
	    Image newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);  
	    whitePawnImg = new ImageIcon(newimg);
	    
	    whiteKingImg = new ImageIcon(getClass().getResource("/ui/img/whiteKing.png"));
	    image = whiteKingImg.getImage();
	    newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);  
	    whiteKingImg = new ImageIcon(newimg);
	    
	    blackPawnImg = new ImageIcon(getClass().getResource("/ui/img/blackPawn.png"));
	    image = blackPawnImg.getImage();
	    newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);  
	    blackPawnImg = new ImageIcon(newimg);
	    
	    blackKingImg = new ImageIcon(getClass().getResource("/ui/img/blackKing.png"));
	    image = blackKingImg.getImage();
	    newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);  
	    blackKingImg = new ImageIcon(newimg);
	}
	/*создает диалоговое меню для вывода информации о победителе*/
	private void endGame(GameResult res) {
		JDialog gameEnd = new JDialog(new JFrame(),"Game End", true);
		
		gameEnd.setLayout(new BorderLayout());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		gameEnd.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		gameEnd.setLocation(dim.width/2 - DIALOG_WIDTH/2, dim.height/2 - DIALOG_HEIGHT/2);
		gameEnd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JLabel label;
		
		if(res == Board.GameResult.BlackWin) {
			label = new JLabel("BLACK WIN");
		}
		else {
			label = new JLabel("WHITE WIN");
		}
		
		
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font(fontName, Font.BOLD, 15));
        label.setOpaque(true);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.YELLOW);
		
        gameEnd.getContentPane().add(label, BorderLayout.CENTER);
        
		gameEnd.setVisible(true);
	}
	
	public void showBoard() {
		String currentBoard = board.toString();
		int horizontal = 0;
		int vertical = 0;
		
		try {
		    
			for(int index = 0; index < currentBoard.length(); index++) {
				switch(currentBoard.charAt(index)) {
				case(' '):
					cells[horizontal][vertical].type = CheckerType.NoChecker;
					cells[horizontal][vertical].color = CheckerColor.NoColor;
					cells[horizontal][vertical].setBackground(new Color(240, 240, 240));
					vertical++;
					break;
				case('#'):
					cells[horizontal][vertical].type = CheckerType.NoChecker;
					cells[horizontal][vertical].color = CheckerColor.NoColor;
					cells[horizontal][vertical].setIcon(null);
					if(cells[horizontal][vertical].isPossibleMove) 
						cells[horizontal][vertical].setBackground(Color.YELLOW);
					else
						cells[horizontal][vertical].setBackground(new Color(150, 150, 100));
					vertical++;
					break;
				case('w'):
					cells[horizontal][vertical].type = CheckerType.Pawn;
					cells[horizontal][vertical].color = CheckerColor.White;
					cells[horizontal][vertical].setIcon(whitePawnImg);
					if(cells[horizontal][vertical].isPossibleChecker)
						cells[horizontal][vertical].setBackground(Color.GREEN);
					else
						cells[horizontal][vertical].setBackground(new Color(150, 150, 100));
					vertical++;
					break;
				case('W'):
					cells[horizontal][vertical].type = CheckerType.King;
					cells[horizontal][vertical].color = CheckerColor.White;
					cells[horizontal][vertical].setIcon(whiteKingImg);
					if(cells[horizontal][vertical].isPossibleChecker)
						cells[horizontal][vertical].setBackground(Color.GREEN);
					else
						cells[horizontal][vertical].setBackground(new Color(150, 150, 100));
					vertical++;
					break;
				case('b'):
					cells[horizontal][vertical].type = CheckerType.Pawn;
					cells[horizontal][vertical].color = CheckerColor.Black;
					cells[horizontal][vertical].setIcon(blackPawnImg);
					if(cells[horizontal][vertical].isPossibleChecker)
						cells[horizontal][vertical].setBackground(Color.GREEN);
					else
						cells[horizontal][vertical].setBackground(new Color(150, 150, 100));
					vertical++;
					break;
				case('B'):
					cells[horizontal][vertical].type = CheckerType.King;
					cells[horizontal][vertical].color = CheckerColor.Black;
					cells[horizontal][vertical].setIcon(blackKingImg);
					if(cells[horizontal][vertical].isPossibleChecker)
						cells[horizontal][vertical].setBackground(Color.GREEN);
					else
						cells[horizontal][vertical].setBackground(new Color(150, 150, 100));
					vertical++;
					break;
				default:
					break;
				}
				
				if(vertical >= BOARD_SIZE) {
					vertical = 0;
					horizontal++;
				}
				if(horizontal >= BOARD_SIZE) {
					break;
				}
				
			}
		
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void cellsDefaultOptions(String path) {
		
		int index = 0;
		int startVertical = (int)(path.charAt(index++) - '0');
		int startHorizontal = (int)(path.charAt(index++) - '0');
		cells[startHorizontal][startVertical].setIcon(null);
		
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				cells[i][j].isPossibleChecker = false;
				cells[i][j].isPossibleMove = false;
			}
		}
	}
	
	private static int reverse(int a) {
		return BOARD_SIZE - a - 1;
	}
	
	
	
	private void showPossibleMoves(int horizontal, int vertical) {
		MyButton button = cells[horizontal][vertical];
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				cells[i][j].isPossibleMove = false;
			}
		}
		for(int i = 0; i < possibleMoves.size(); i++) {
			int index = 0;
			
			String current = possibleMoves.get(i);
			
			int startVertical = (int)(current.charAt(index++) - '0');
			int startHorizontal = (int)(current.charAt(index++) - '0');
			
			if(startVertical == button.vertical && startHorizontal == button.horizontal) {
				index = current.length() - 2;
				int finishVertical = (int)(current.charAt(index++) - '0');
				int finishHorizontal = (int)(current.charAt(index) - '0');
				cells[finishHorizontal][finishVertical].isPossibleMove = true;
				cells[finishHorizontal][finishVertical].path = current;
				
			}
		}
		
		showBoard();
	}
}
