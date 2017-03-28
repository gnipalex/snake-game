package com.hnyp.snake.components;

import java.util.List;

import com.hnyp.snake.ui.ComponentLocation;

public class Wall extends GameComponent {

	public Wall(List<ComponentLocation> locations) {
		getLocations().addAll(locations);
	}
	
}
