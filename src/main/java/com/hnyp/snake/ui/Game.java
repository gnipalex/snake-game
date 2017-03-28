package com.hnyp.snake.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.hnyp.snake.components.Snake;

public class Game {
	
	private Field field;
	private Snake snake;
	
	private int score;
	
	private volatile Direction currentDirection = Direction.random();
	
	private int fieldRepaintTimeout;

	private volatile GameState gameState = GameState.CREATED;
	private volatile boolean controllWasPressed = false;
	
	private Consumer<String> endGameHandler;
	
	private Consumer<Game> gameStatusHandler;
	
	public void incrementScore() {
		score++;
	}
	
	public void endGameWithMessage(String message) {
		end();
		endGameHandler.accept(message);
	}
	
	public void start() {
		if (GameState.IN_PROGRESS.equals(gameState)) {
			// do nothing
		} else if (GameState.CREATED.equals(gameState)) {
			initKeyListeners();
			gameState = GameState.IN_PROGRESS;
			gameLoop();
		} else if (GameState.PAUSED.equals(gameState)) {
			gameState = GameState.IN_PROGRESS;
		} else {
			throw new IllegalStateException("game is in state " + gameState + ", it can not be started");
		}
	}
	
	private void gameLoop() {
		new GameLoopThread().start();
	}
	
	private class GameLoopThread extends Thread {
		
		public GameLoopThread() {
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				while(!hasState(GameState.STOPPED)) {
					boolean skipUpdate = getAndResetControllWasPressed();
					if (!hasState(GameState.PAUSED) && !skipUpdate) {
						moveSnakeAndUpdateField();
					}
					Thread.sleep(fieldRepaintTimeout);
				}
			} catch (InterruptedException e) {
				Game.this.gameState = GameState.STOPPED;
				e.printStackTrace();
			}
		}
	}
	
	private boolean hasState(GameState state) {
		return Objects.equals(gameState, state);
	}
	
	private void moveSnakeAndUpdateField() {
		snake.move(currentDirection);
		field.repaint();
		gameStatusHandler.accept(this);
	}
	
	private boolean getAndResetControllWasPressed() {
		if (controllWasPressed) {
			controllWasPressed = false;
			return true;
		}
		return false;
	}
	
	public void pause() {
		gameState = GameState.PAUSED;
	}
	
	public void end() {
		resetKeyListeners();
		gameState = GameState.STOPPED;
	}
	
	private void initKeyListeners() {
		InputMap fieldInputMap = getField().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		fieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "field left typed");
		fieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "field right typed");
		fieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "field up typed");
		fieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "field down typed");
		
		ActionMap fieldActionMap =  getField().getActionMap();
		fieldActionMap.put("field left typed", getArrowActionHandler(Direction.LEFT));
		fieldActionMap.put("field right typed", getArrowActionHandler(Direction.RIGHT));
		fieldActionMap.put("field up typed", getArrowActionHandler(Direction.UP));
		fieldActionMap.put("field down typed", getArrowActionHandler(Direction.DOWN));
	}
	
	private boolean isNotOppositeDirection(Direction direction) {
		Direction headDirection = snake.getHeadDirection();
		return !Objects.equals(headDirection.getOppositeDirection(), direction);
	}
	
	private Action getArrowActionHandler(Direction direction) {
		
		return new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Game.this.hasState(GameState.IN_PROGRESS) 
						&& Game.this.isNotOppositeDirection(direction)) {
					Game.this.controllWasPressed = true;
					Game.this.currentDirection = direction;
					Game.this.moveSnakeAndUpdateField();
				}
			}
		};
		
	}
	
	private void resetKeyListeners() {
		getField().getActionMap().clear();
	}
	
	public int getScore() {
		return score;
	}

	public Field getField() {
		return field;
	}

	public Snake getSnake() {
		return snake;
	}

	public int getFieldRepaintTimeout() {
		return fieldRepaintTimeout;
	}

	public void setFieldRepaintTimeout(int fieldRepaintTimeout) {
		this.fieldRepaintTimeout = fieldRepaintTimeout;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void setSnake(Snake snake) {
		this.snake = snake;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	public void setEndGameHandler(Consumer<String> endGameHandler) {
		this.endGameHandler = endGameHandler;
	}

	public Consumer<Game> getGameStatusHandler() {
		return gameStatusHandler;
	}

	public void setGameStatusHandler(Consumer<Game> gameStatusHandler) {
		this.gameStatusHandler = gameStatusHandler;
	}

	public GameState getGameState() {
		return gameState;
	}
	
}
