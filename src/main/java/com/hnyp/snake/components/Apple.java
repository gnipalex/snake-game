package com.hnyp.snake.components;

import com.hnyp.snake.ui.ComponentLocation;

public class Apple extends GameComponent {

	public Apple(ComponentLocation location) {
		getLocations().add(location);
	}
	
	private int value;

	public int getValue() {
		return value;
	}
	
	public void changeLocation(ComponentLocation location) {
		getLocations().clear();
		getLocations().add(location);
	}
	
}
