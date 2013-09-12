package ch.turingmachine;

public final class Utilities {

	public static final Direction parseDirection(char direction) throws Exception {
		switch(direction) {
			case 'L':
			case '<':
				return Direction.LEFT;
			case 'R':
			case '>':
				return Direction.RIGHT;
			case 'N':
				return Direction.NEUTRAL;
		}
		
		throw new Exception("Invalid direction.");
	}
}
