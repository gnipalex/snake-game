package com.hnyp.snake.interractors;

import com.hnyp.snake.components.GameComponent;
import com.hnyp.snake.components.Snake;
import com.hnyp.snake.components.Wall;
import com.hnyp.snake.ui.Game;

public class SnakeWallInterractor implements GameComponentInterractor {

	private Game game;
	
	public SnakeWallInterractor(Game game) {
		this.game = game;
	}

	@Override
	public void interract(GameComponent component, GameComponent otherComponent) {
		interract((Snake) component, (Wall) otherComponent);
	}
	
	private void interract(Snake snake, Wall wall) {
		game.endGameWithMessage("Ouch, you can not go through walls)");
	}

	@Override
	public boolean applicable(GameComponent component, GameComponent otherComponent) {
		return Snake.class.isInstance(component) && Wall.class.isInstance(otherComponent);
	}

}
