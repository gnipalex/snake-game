package com.hnyp.snake.interractors;

import com.hnyp.snake.components.GameComponent;

public interface GameComponentInterractor {

	void interract(GameComponent component, GameComponent otherComponent);
	
	boolean applicable(GameComponent component, GameComponent otherComponent);
	
}
