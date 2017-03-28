package com.hnyp.snake.ui;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;
	
	public Direction getOppositeDirection() {
		if (UP.equals(this)) {
			return DOWN;
		} else if (DOWN.equals(this)) {
			return UP;
		} else if (LEFT.equals(this)) {
			return RIGHT;
		} else if (RIGHT.equals(this)) {
			return LEFT;
		}
		throw new IllegalArgumentException("cannot get opposite direction for " + this);
	}
	
	public static Direction random() {
		Direction[] values = Direction.values();
		int randomIndex = (int) Math.round(Math.random() * (values.length - 1));
		return values[randomIndex];
	}
}
