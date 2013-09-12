package ch.turingmachine;

import java.util.ArrayList;
import java.util.Arrays;

public class State {

	private ArrayList<Transition> transitions;
	private String name;

	public State() {
		transitions = new ArrayList<Transition>();
	}

	public State(String name) {
		this();
		this.name = name;
	}

	/**
	 * Find a transition that matches the input (current position) values   
	 * @param input
	 * @return Matching transition or null if no match
	 */
	public Transition findTransition(char[] input) {
		// Return the first (and hopefully only) matching transition
		for (Transition t : transitions) {
			if(Arrays.equals(input, t.getInput())) {
				return t;
			}
		}
		// return null if no state is found
		return null;
	}

	public void addTransition(Transition transition) {
		transitions.add(transition);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
