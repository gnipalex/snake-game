package com.hnyp.snake.interractors;

import java.awt.Color;

import com.hnyp.snake.components.Apple;
import com.hnyp.snake.components.GameComponent;
import com.hnyp.snake.components.Snake;
import com.hnyp.snake.ui.ComponentLocation;
import com.hnyp.snake.ui.Field;
import com.hnyp.snake.ui.Game;

public class SnakeAppleInterractor implements GameComponentInterractor {

	private Field field;
	
	private Game game;
	
	public SnakeAppleInterractor(Game game, Field field) {
		this.game = game;
		this.field = field;
	}

	@Override
	public void interract(GameComponent component, GameComponent otherComponent) {
		Snake snake = (Snake) component;
		Apple apple = (Apple) otherComponent;
		snake.grow();
		game.incrementScore();
		ComponentLocation newAppleLocation = field.generateRandomLocation();
		if (newAppleLocation == null) {
			game.endGameWithMessage("Congradulations! You win!");
		} else {
			newAppleLocation.setColor(Color.RED);
			apple.changeLocation(newAppleLocation);
		}
	}

	@Override
	public boolean applicable(GameComponent component, GameComponent otherComponent) {
		return Snake.class.isInstance(component) && Apple.class.isInstance(otherComponent);
	}

}
