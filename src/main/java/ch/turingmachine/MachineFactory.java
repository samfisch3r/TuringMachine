package ch.turingmachine;

public class MachineFactory {

	public MachineFactory() {
		
	}

	
	public TuringMachine create(String name) {
		switch(name) {
			case "multiplication":
				return createMultiplicationMachine();
			case "factorial":
				return createFactorialMachine();
			default:
				return null;
		}
	}
	
	public TuringMachine createMultiplicationMachine() {
		State[] states = new State[] {
				new State("State 1"),
				new State("State 2") };
		
		states[0].addTransition(new Transition("11B", "111", "NRR", states[0]));
		states[0].addTransition(new Transition("1BB", "BBB", "RLN", states[1]));
		states[1].addTransition(new Transition("11B", "111", "NLR", states[1]));
		states[1].addTransition(new Transition("1BB", "BBB", "RRN", states[0]));

		Tape[] tapes = new Tape[] {
				new Tape("B"),
				new Tape("B"),
				new Tape("B") 
				};
		return new TuringMachine("Multiplication Machine", states, states[0], tapes);
	}
	
	public TuringMachine createFactorialMachine() {
		State[] states = new State[] {
				new State("State 1"),
				new State("State 2"),
				new State("State 3"),
				new State("State 4"),
				new State("State 5"),
				new State("State 6")
		};
		
		states[0].addTransition(new Transition("1BB", "11B", "RRN", states[0]));
		states[0].addTransition(new Transition("BBB", "BB1", "NLN", states[0]));
		states[0].addTransition(new Transition("B11", "BBB", "LLN", states[1]));
		states[1].addTransition(new Transition("1BB", "BB1", "NNN", states[1]));
		states[1].addTransition(new Transition("11B", "11B", "NNN", states[2]));
		states[2].addTransition(new Transition("11B", "111", "NLR", states[2]));
		states[2].addTransition(new Transition("1BB", "BBB", "LRN", states[3]));
		states[2].addTransition(new Transition("B1B", "BBB", "NLL", states[4]));
		states[3].addTransition(new Transition("11B", "111", "NRR", states[3]));
		states[3].addTransition(new Transition("1BB", "BBB", "LLN", states[2]));
		states[3].addTransition(new Transition("B1B", "BBB", "NRL", states[5]));
		states[4].addTransition(new Transition("B11", "11B", "RNL", states[4]));
		states[4].addTransition(new Transition("B1B", "B1B", "LNN", states[2]));
		states[5].addTransition(new Transition("B11", "11B", "RNL", states[5]));
		states[5].addTransition(new Transition("B1B", "B1B", "LNN", states[3]));
		
		Tape[] tapes = new Tape[] {
				new Tape("B"),
				new Tape("B"),
				new Tape("B")
		};
		
		return new TuringMachine("Factorial Machine", states, states[0], tapes);
	}
}
