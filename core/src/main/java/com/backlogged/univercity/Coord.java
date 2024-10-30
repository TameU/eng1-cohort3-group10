package com.backlogged.univercity;

public class Coord {
    private int row;
    private int column;

    public Coord(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int hashCode() {
        int result = (int) (row ^ (row >>> 32));
        result = 31 * result + Integer.hashCode(column);
        return result;
    }
    @Override 
    public boolean equals(Object other) { 
        if (other == this) { 
            return true;
        }
        if (other == null) { 
            return false;
        } 
        if (!(other instanceof Coord)) { 
            return false;
        } 
        Coord o = (Coord)other; 
        return o.getColumn() == this.column && o.getRow() == this.row;
    }
    
    public String toString() {
        return String.format("(%d, %d)", row, column);
    } 

    public int getRow() { 
        return row;
    }  

    public int getColumn() { 
        return column;
    } 
    
}
