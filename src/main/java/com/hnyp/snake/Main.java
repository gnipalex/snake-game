package com.hnyp.snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.hnyp.snake.components.Apple;
import com.hnyp.snake.components.Snake;
import com.hnyp.snake.components.Wall;
import com.hnyp.snake.interractors.SnakeAppleInterractor;
import com.hnyp.snake.interractors.SnakeWallInterractor;
import com.hnyp.snake.ui.ComponentLocation;
import com.hnyp.snake.ui.Direction;
import com.hnyp.snake.ui.Field;
import com.hnyp.snake.ui.Game;
import com.hnyp.snake.ui.GameState;

public class Main extends JFrame {
	
	public static final int blockSize = 25;
	public static final int rows = 20;
	public static final int columns = 20;
	public static final int indentBottom = 100;
	public static final int indentTop = 50;
	public static final int indentLeftRight = 50;
	
	private Game game;
	
	private JButton pauseButton;
	private JLabel gameStatus;
	private JLabel scoreLabel;
	
	public Main() {
		game = createGame();
		
		createInfoPanel();
		
		setUpGameHandlers();
		
		this.add(game.getField(), BorderLayout.SOUTH);
		
		this.setTitle("snake game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		
		game.start();
	}
	
	
	private void setUpGameHandlers() {
		game.setGameStatusHandler(game -> {
			gameStatus.setText("Game status : " + game.getGameState().toString());
			scoreLabel.setText("Your score is : " + game.getScore());
		});
		
		game.setEndGameHandler(message -> {
			JOptionPane.showMessageDialog(game.getField(), message);
			game.end();
			pauseButton.setText("restart");
		});
	}


	private void createInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pauseButton = new JButton("pause");
		gameStatus = new JLabel("game status : ");
		scoreLabel = new JLabel("your score is : ");
		infoPanel.add(scoreLabel);
		infoPanel.add(gameStatus);
		infoPanel.add(pauseButton);
		infoPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.add(infoPanel, BorderLayout.NORTH);
		
		pauseButton.addActionListener(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (GameState.IN_PROGRESS.equals(game.getGameState())) {
					game.pause();
					pauseButton.setText("resume");
				} else if (GameState.PAUSED.equals(game.getGameState())) {
					game.start();
					pauseButton.setText("pause");
				} else if (GameState.STOPPED.equals(game.getGameState())) {
					recreateGame();
					pauseButton.setText("pause");
				}
				
			}
		});
	}
	
	public void recreateGame() {
		game.end();
		this.remove(game.getField());
		game = createGame();
		setUpGameHandlers();
		this.add(game.getField(), BorderLayout.SOUTH);
		this.validate();
		game.start();
	}
	
	
	// extract this to factory
	private Game createGame() {
		Game game = new Game();
		
		Field gameField = new Field(rows, columns, blockSize);
		
		gameField.setLocation(indentLeftRight, indentTop);
		Dimension gameDimension = new Dimension(columns * blockSize, rows * blockSize);
		gameField.setPreferredSize(gameDimension);
		
		ComponentLocation snakeLocation = new ComponentLocation(Color.GREEN, 10, 10);
		Snake snake = new Snake(snakeLocation, Direction.random());
		snake.setField(gameField);
		snake.setGame(game);
		snake.setComponentInterractors(Arrays.asList(
			new SnakeAppleInterractor(game, gameField),
			new SnakeWallInterractor(game)
		));
		
		ComponentLocation appleLocation = new ComponentLocation(Color.RED, 17, 14);
		Apple apple = new Apple(appleLocation);
		
		Wall wall1 = new Wall(Arrays.asList(
			new ComponentLocation(Color.GRAY, 7, 7),
			new ComponentLocation(Color.GRAY, 8, 7),
			new ComponentLocation(Color.GRAY, 8, 8)
		)); 
		
		Wall wall2 = new Wall(Arrays.asList(
			new ComponentLocation(Color.GRAY, 15, 17),
			new ComponentLocation(Color.GRAY, 16, 17)
		)); 
		
		gameField.add(apple);
		gameField.add(wall1);
		gameField.add(wall2);
		gameField.add(snake);
		
		game.setField(gameField);
		game.setSnake(snake);
		
		game.setFieldRepaintTimeout(700);
		
		return game;
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
