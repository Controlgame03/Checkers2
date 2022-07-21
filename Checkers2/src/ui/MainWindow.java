package ui;

import io.*;
import model.*;
import model.Cell.*;
import ui.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.util.*;
import java.io.*;

/*Главный фрейм. На котором расположены одна надпись и одна кнопка*/
public class MainWindow extends JFrame{
	private static final int MAIN_WIDTH = 300;
	private static final int MAIN_HEIGHT = 150;
	
	private static final String TITLE = "Шашки";
	
	private static final String PLAY_NEW_GAME = "Начать новую игру";
	
	private static final String fontName = "Century Gothic";
	
	public MainWindow() throws IOException {
		super(TITLE);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(MAIN_WIDTH, MAIN_HEIGHT);
		this.setLocation(dim.width/2 - MAIN_WIDTH/2, dim.height/2 - MAIN_HEIGHT/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addButtonAndLetter();
		
		this.getContentPane().setBackground(Color.YELLOW);
		
		setVisible(true);
		
	}
	
	/*добавляет кнопку и надпись на фрейм*/
	private void addButtonAndLetter() throws IOException{
		
		JButton buttonNewGame = new JButton(PLAY_NEW_GAME);
		
		buttonNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		ActionListener action = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	try {
			        JButton btn = (JButton)e.getSource();
			        BoardWindow newGame = new BoardWindow();
	
		    	}
	        	catch(Exception exep) {
	        		System.out.println(exep.getMessage());
	        	}
		        
		    }
		};
		
		buttonNewGame.addActionListener(action);
		buttonNewGame.setBorderPainted(false);
		buttonNewGame.setOpaque(true);
		buttonNewGame.setBackground(Color.RED);
		Container container = new Container();
		Dimension size = new Dimension(20, 20);
		
		Font font = new Font(fontName, Font.BOLD, 50);
        
        JLabel label = new JLabel("ШАШКИ");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(font);
        label.setOpaque(true);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.RED);
        
		container.add(label);
		container.add(Box.createRigidArea(new Dimension(5, 5)));
        container.add(buttonNewGame);
        container.add(Box.createRigidArea(size));
    
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
       
        getContentPane().add(container);        

        setVisible(true);    
	}
}
