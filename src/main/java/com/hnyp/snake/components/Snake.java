package com.hnyp.snake.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hnyp.snake.interractors.GameComponentInterractor;
import com.hnyp.snake.ui.ComponentLocation;
import com.hnyp.snake.ui.Direction;
import com.hnyp.snake.ui.Field;
import com.hnyp.snake.ui.Game;

public class Snake extends GameComponent {

	private Field field;
	private Game game;
	private List<Direction> directions = new ArrayList<>();
	private List<GameComponentInterractor> componentInterractors = new ArrayList<>();
	
	public Snake(ComponentLocation location, Direction initialDirection) {
		getLocations().add(location);
		directions.add(initialDirection);
	}
	
	public void move(Direction direction) {
		// should this logic be here ?
//		Direction headDirection = directions.get(0);
//		if (Objects.equals(headDirection.getOppositeDirection(), direction)) {
//			// cannot go back
//			return;
//		}
		ComponentLocation newHeadLocation = prepareNewHeadLocation(direction);
		// extract this logic
		if (!field.isLocationFree(newHeadLocation)) {
			List<GameComponent> otherComponentsInLocation = field.getComponentsByLocation(newHeadLocation);
			interractWithOtherComponents(otherComponentsInLocation);
		} else if (!field.isLocationInsideField(newHeadLocation)) {
			game.endGameWithMessage("Oups! You can not go out of field!!");
		}
		shiftPath(direction);
		recalcultateSnakeBody();
	}
	
	public Direction getHeadDirection() {
		return directions.get(0);
	}
	
	private void recalcultateSnakeBody() {
		// need to change all body parts points according to path
		for (int i=0; i<getLocations().size(); i++) {
			ComponentLocation location = getLocations().get(i);
			Direction direction = directions.get(i);
			
			int c = location.getColumn();
			int r = location.getRow();
			
			if (Direction.UP.equals(direction)) {
				r--;
			} else if (Direction.DOWN.equals(direction)) {
				r++;
			} else if (Direction.LEFT.equals(direction)) {
				c--;
			} else if (Direction.RIGHT.equals(direction)) {
				c++;
			} else {
				throw new IllegalStateException("not supported direction " + direction);
			}
			
			location.setColumn(c);
			location.setRow(r);
		}
	}
	
	private void shiftPath(Direction direction) {
		directions.add(0, direction);
		directions.remove(directions.size() - 1);
	}
	
	public void grow() {
		ComponentLocation tailLocation = getLocations().get(getLocations().size() - 1);
		Direction tailDirection = directions.get(directions.size() - 1);
		int row = tailLocation.getRow();
		int column = tailLocation.getColumn();
		
		if (Direction.UP.equals(tailDirection)) {
			row++;
		} else if (Direction.DOWN.equals(tailDirection)) {
			row--;
		} else if (Direction.LEFT.equals(tailDirection)) {
			column++;
		} else if (Direction.RIGHT.equals(tailDirection)) {
			column--;
		} else {
			throw new IllegalStateException("direction " + tailDirection + " is not supported");
		}
		
		directions.add(tailDirection);
		getLocations().add(new ComponentLocation(tailLocation.getColor(), row, column));
	}
	
	private void interractWithOtherComponents(List<GameComponent> otherComponents) {
		otherComponents.forEach(component -> {
			componentInterractors.stream()
				.filter(interractor -> interractor.applicable(this, component))
				.forEach(interractor -> interractor.interract(this, component));
		});
	}
	
	private ComponentLocation prepareNewHeadLocation(Direction direction) {
		int column = getLocations().get(0).getColumn();
		int row = getLocations().get(0).getRow();
		if (Direction.UP.equals(direction)) {
			row--;
		} else if (Direction.DOWN.equals(direction)) {
			row++;
		} else if (Direction.LEFT.equals(direction)) {
			column--;
		} else if (Direction.RIGHT.equals(direction)) {
			column++;
		} else {
			throw new IllegalStateException("not supported direction " + direction);
		}
		return new ComponentLocation(row, column);
	}
	
	

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<GameComponentInterractor> getComponentInterractors() {
		return componentInterractors;
	}

	public void setComponentInterractors(List<GameComponentInterractor> componentInterractors) {
		this.componentInterractors = componentInterractors;
	}
	
}
