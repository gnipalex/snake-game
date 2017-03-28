package com.hnyp.snake.ui;

import java.awt.Color;

public class ComponentLocation {

	private Color color;
	private int row;
	private int column;

	public ComponentLocation(Color color, int row, int column) {
		this(row, column);
		this.color = color;
	}

	public ComponentLocation(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentLocation other = (ComponentLocation) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
