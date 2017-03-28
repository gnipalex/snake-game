package com.hnyp.snake.components;

import java.util.ArrayList;
import java.util.List;

import com.hnyp.snake.ui.ComponentLocation;

public abstract class GameComponent {

	private List<ComponentLocation> locations = new ArrayList<>();

	public List<ComponentLocation> getLocations() {
		return locations;
	}

}
