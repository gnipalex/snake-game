package com.hnyp.snake.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import com.hnyp.snake.components.GameComponent;

public class Field extends JPanel {

	private int rows;
	private int columns;
	private int blockPixels;
	private List<GameComponent> components = new ArrayList<>();
	private List<ComponentLocation> allLocations;
	
	public Field(int rows, int columns, int blockPixels) {
		this.rows = rows;
		this.columns = columns;
		this.blockPixels = blockPixels;
		this.setSize(columns * blockPixels, rows * blockPixels);
		initAllLocations();
	}
	
	private void initAllLocations() {
		this.allLocations = new ArrayList<>(rows * columns);
		for (int r=0; r<rows; r++) {
			for (int c=0; c<columns; c++) {
				allLocations.add(new ComponentLocation(r, c));
			}
		}
	}
	
	private List<ComponentLocation> getFreeLocations() {
		List<ComponentLocation> freeLocations = new ArrayList<>(allLocations);
		freeLocations.removeAll(getOccupiedLocations());
		return freeLocations;
	}
	
	private List<ComponentLocation> getOccupiedLocations() {
		return components.stream().map(GameComponent::getLocations).flatMap(List::stream).collect(Collectors.toList());
	}
	
	public boolean isLocationFree(ComponentLocation location) {
		//validateLocation(location);
		boolean locationIsFree = components.stream().flatMap(component -> component.getLocations().stream()).noneMatch(location::equals);
		return locationIsFree;// && isLocationInsideField(location);
	}
	
	public boolean isLocationInsideField(ComponentLocation location) {
		boolean verticallyInside = location.getRow() >= 0 && location.getRow() < rows;
		boolean horizontallyInside = location.getColumn() >= 0 && location.getColumn() < columns;
		return verticallyInside && horizontallyInside;
	}
	
	public void add(GameComponent component) {
		checkComponentsLocations(component);
		components.add(component);
	}

	private void validateLocation(ComponentLocation location) {
		if (location.getRow() < 0 || location.getRow() >= rows 
				|| location.getColumn() < 0 || location.getColumn() >= columns) {
			throw new IllegalArgumentException("bad location " + location.getRow() + ", " + location.getColumn());
		}
	}

	private void checkComponentsLocations(GameComponent component) {
		component.getLocations().forEach(this::validateLocation);
	}
	
	private Map<ComponentLocation, List<GameComponent>> getComponentsMap() {
		Map<ComponentLocation, List<GameComponent>> componentsMap = new HashMap<>();
		components.forEach(component -> {
			component.getLocations().forEach(location -> append(componentsMap, location, component));
		});
		return componentsMap;
	}
	
	public List<GameComponent> getComponentsByLocation(ComponentLocation location) {
		List<GameComponent> componentsByLocation = getComponentsMap().get(location);
		return Optional.ofNullable(componentsByLocation).orElse(Collections.emptyList());
	}
	
	private void append(Map<ComponentLocation, List<GameComponent>> componentsMap, ComponentLocation location, GameComponent component) {
		List<GameComponent> componentsByLocation = componentsMap.get(location);
		if (componentsByLocation == null) {
			componentsByLocation = new ArrayList<>();
			componentsMap.put(location, componentsByLocation);
		}
		componentsByLocation.add(component);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawGameGrid(g);
		drawGameComponents(g);
	}

	private void drawGameGrid(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLUE);
		for (int r=0; r<rows; r++) {
			for (int c=0; c<columns; c++) {
				g.drawRect(c * blockPixels, r * blockPixels, getBlockSizeInPosition(c, columns), getBlockSizeInPosition(r, rows));
			}
		}
	}
	
	private int getBlockSizeInPosition(int currentPosition, int maxPosition) {
		if (currentPosition < maxPosition - 1) {
			return blockPixels;
		} else {
			return blockPixels - 1;
		}
	}
	
	private void drawGameComponents(Graphics g) {
		components.forEach(component -> {
			component.getLocations().forEach(location -> drawLocation(g, location));
		});
	}
	
	private void drawLocation(Graphics g, ComponentLocation location) {
		g.setColor(location.getColor());
		int x = location.getColumn() * blockPixels;
		int y = location.getRow() * blockPixels;
		int padding = 2;
		int border = 1;
		g.fillRect(x + padding, y + padding, blockPixels - padding - border, blockPixels - padding - border);
	}
	
	public ComponentLocation generateRandomLocation() {
		List<ComponentLocation> freeLocations = getFreeLocations();
		if (freeLocations.isEmpty()) {
			return null;
		}
		return getRandomElement(freeLocations);
	}
	
	private <E> E getRandomElement(List<E> list) {
		return list.get(getRandom(list.size() - 1));
	}
	
	private int getRandom(int max) {
		return (int)Math.round(Math.random() * max);
	}
	
	public boolean areThereFreeLocations() {
		return !getFreeLocations().isEmpty();
	}
		
}
