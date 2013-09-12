package ch.turingmachine;

public class Transition
{
	private char[] input;
	private char[] output;
	private Direction[] direction;
	private State targetState;
	
	
	public Transition(int dimension) {
		input = new char[dimension];
		output = new char[dimension];
		direction = new Direction[dimension];
	}
	
	public Transition(String input, String output, String directions, State targetState) {
		if(targetState == null) {
			//throw new NullPointerException(); 
		}
		
		// We take the length of the first string as the dimension
		int dimension = input.length();
		
		this.input = input.toCharArray();
		this.output = output.toCharArray();
		
		this.direction = new Direction[dimension];
		for (int i = 0; i < directions.length(); i++) {
			char d = directions.charAt(i);
			
			try {
				this.direction[i] = Utilities.parseDirection(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.targetState = targetState;
	}

	public char[] getOutput()
	{
		return output;
	}

	public void setOutput(char[] output)
	{
		this.output = output;
	}

	public char[] getInput()
	{
		return input;
	}

	public void setInput(char[] input)
	{
		this.input = input;
	}

	public Direction[] getDirection()
	{
		return direction;
	}

	public void setDirection(Direction[] directions)
	{
		this.direction = directions;
	}

	public State getTargetState() {
		return targetState;
	}

	public void setTargetState(State targetState) {
		this.targetState = targetState;
	}
}