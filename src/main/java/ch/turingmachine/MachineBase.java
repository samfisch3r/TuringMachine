package ch.turingmachine;

import com.googlecode.lanterna.screen.ScreenWriter;

public abstract class MachineBase {
	protected int stepCount = 0;
	
	public MachineBase() {
		
	}
	
	public void printTapes(Tape[] tapes, State state) {
		System.out.printf("********** Step %05d: State \"%s\" **********\n", this.stepCount, state.getName());
		for (int i = 0; i < tapes.length; i++) {
			System.out.printf("#%d: %s\n", i+1, tapes[i].toString());
		}
	}
	
	public void printTapes(ScreenWriter writer, Tape[] tapes, State state, int y) {
		writer.drawString(0, y, "Step count: " + this.stepCount);
		writer.drawString(93 - 7 - state.getName().length(), y, "State: " + state.getName());
		
		for (int i = 0; i < tapes.length; i++) {
			tapes[i].printRange(writer, y + i + 1, 15);
		}
	}
}
