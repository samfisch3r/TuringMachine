package ch.turingmachine;

import java.text.NumberFormat;
import java.util.Locale;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class TuringMachine {
	public final char unarySymbol = '1'; 
	
	// Terminal stuff
	private SwingTerminal terminal;
	private Screen screen;
	private final int offsetTop = 6;
	
	// What makes the Turing machine
	private String name;
	private State[] states;
	private State currentState;
	private Tape[] tapes;
	
	// Some statistics
	private int stepCount = 0;
	private long startTime = 0;
	private long endTime = 0;

	
	public TuringMachine(String name, State[] states, State initialState, Tape[] tapes) {
		this.name = name;
		this.states = states;
		this.currentState = initialState;
		this.tapes = tapes;
	}

	public void initialize() {
		// Initialize terminal
		//this.terminal = new SwingTerminal();
		//this.screen = new Screen(this.terminal);
		
		this.screen = TerminalFacade.createScreen();
		this.terminal = (SwingTerminal)this.screen.getTerminal();
		
		this.screen.startScreen();
		this.terminal.getJFrame().setLocation(0, 0);
		
		this.terminal.getJFrame().addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent evt) {
		        screen.stopScreen();
		        System.exit(0);
		    }
		});
		
		this.terminal.setCursorVisible(false);
		
		printHeader();
	}

	public void prepare() {
		ScreenWriter writer = new ScreenWriter(screen);
		
		writer.drawString(0, offsetTop, "Please enter the initial values for the " + this.tapes.length + " tapes:");
		writer.drawString(0, offsetTop + 1, "(Press enter for an empty tape)");
		terminal.applyBackgroundColor(Color.WHITE);
		terminal.applyForegroundColor(Color.BLACK);
		this.screen.refresh();
		
		for (int i = 0; i < tapes.length; i++) {
			this.screen.putString(0, offsetTop + i + 2, String.format("Tape %d: ", i + 1), Color.WHITE, Color.BLACK);
			this.screen.setCursorPosition(8, offsetTop + i + 2);
			this.terminal.setCursorVisible(true);
			this.screen.refresh();
			
			Key key = null;
			StringBuilder builder = new StringBuilder();

			while(key == null) {
				key = screen.readInput();
				
				if(key == null)
					continue;
				
				if(key.getKind() == Kind.NormalKey) {
					terminal.putCharacter(key.getCharacter());
					builder.append(key.getCharacter());
				}
				/*
				else if(key.getKind() == Kind.Backspace) {
					terminal.putCharacter(key.getCharacter());
					builder.delete(builder.length() - 2, 1);
					
				}
				*/
				else if(key.getKind() == Kind.Enter) {
					// Store value on tape
					
					// Try to parse as integer
					try {
					    Integer number = new Integer(builder.toString());
					    
					    // Parse succeeded, treat as unary number
					    this.tapes[i].initializeAsUnary(number, unarySymbol);
					}
					catch(NumberFormatException ex) {
						// Parse failed, treat a pure character input
						if(builder.length() == 0) builder.append(Tape.BLANK);
						
						this.tapes[i].initialize(builder.toString().toCharArray());
					}
					
					// Exit the while loop, go to the next tape
					break;
				}

				key = null;
			}
		}
		
		this.terminal.setCursorVisible(false);
		this.screen.setCursorPosition(0, 0);
		this.screen.refresh();
	}
	
	public void prepare(int[] args) {
		// Validate arguments
		if (args.length != this.tapes.length) {
			throw new IllegalArgumentException();
		}

		// Convert arguments to input strings
		for (int i = 0; i < args.length; i++) {
			if(args[i] == 0) {
				this.tapes[i].initialize("B".toCharArray());	
			}
			else {
				this.tapes[i].initializeAsUnary(args[i], unarySymbol);
			}
		}		
	}

	
	public void run() {
		// Expecting tapes to be initialized
		this.screen.getTerminal().setCursorVisible(false);
		
		printHeader();
		
		ScreenWriter writer = new ScreenWriter(screen);
		
		StateFrame stateFrame = new StateFrame(this.name, this.currentState.getName());
		stateFrame.setLocation(0, this.terminal.getJFrame().getHeight());
		stateFrame.setVisible(true);
		this.terminal.getJFrame().requestFocus();

		printTapes(writer, tapes, this.currentState, this.offsetTop);
		screen.refresh();
		
		Key key = null;
		boolean runThrough = false;
		
		// Wait for user to start
		do {
			key = screen.readInput();
		} while (key == null);
		
		if(key.getKind() == Kind.Escape) {
			return;
		}
		else if(key.getKind() == Kind.Enter) {
			runThrough = true;
			stateFrame.setVisible(false);
		}
		
		// Let's go
		this.startTime = System.nanoTime();
		
		while (key == null || key.getKind() != Kind.Escape || runThrough) {
			this.stepCount++;

			char[] values = new char[3];

			// Get current values on all tapes
			for (int i = 0; i < tapes.length; i++) {
				values[i] = tapes[i].read();
			}

			Transition matchingTransition = this.currentState.findTransition(values);

			if (matchingTransition == null) {
				// No further transition found, were done!
				
				// Calculate execution time (including breaks between steps in manual mode)
				this.endTime = System.nanoTime();
				NumberFormat formatter = NumberFormat.getInstance(new Locale("en"));
				String duration = formatter.format(endTime - startTime);
				
				
				printTapes(writer, tapes, this.currentState, this.offsetTop);
				screen.refresh();

				int offset = this.offsetTop + this.tapes.length + 2;
				
				Color color = writer.getForegroundColor();
				writer.setForegroundColor(Color.GREEN);
				writer.drawString(0, offset, "FINISHED in " + duration + " nano seconds");
				writer.setForegroundColor(color);
				
				printTapesAsUnaryNumber(writer, offset + 1);
				
				screen.refresh();
				
				// Wait until user presses a key to terminate the application
				while (screen.readInput() == null) {

				}

				return;
			}

			for (int i = 0; i < tapes.length; i++) {
				tapes[i].write(matchingTransition.getOutput()[i], matchingTransition.getDirection()[i]);
			}

			this.currentState = matchingTransition.getTargetState();

			// Comment out for max performance :)
			//printTapes(tapes, this.currentState);
			printTapes(writer, tapes, this.currentState, this.offsetTop);
			screen.refresh();
			
			// Update state
			if(!runThrough) {
				stateFrame.setStateImage(this.currentState.getName());
			}

			do {
				key = screen.readInput();
			} while (!runThrough && key == null);

			if (key != null) {
				if (key.getKind() == Kind.Enter) {
					// Run through
					runThrough = true;
					stateFrame.setVisible(false);
				}
				// Else: next step
			}
		}
	}

	public void terminate() {
		this.screen.stopScreen();
		System.exit(0);
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
	
	public void printTapesAsUnaryNumber(ScreenWriter writer, int y) {
		for (int i = 0; i < tapes.length; i++) {
			try {
				writer.drawString(0, y + i, "Tape #" + (i + 1) + " as unary number: " + tapes[i].readAsUnary(unarySymbol));
			} catch (Exception ex) {
				writer.drawString(0, y + i, "Tape #" + (i + 1) + " as unary number: " + ex.getMessage());
			}
		}
	}
	
	public void printHeader() {
		this.screen.clear();
		this.screen.getTerminal().clearScreen();
		
		this.screen.refresh();
		
		ScreenWriter writer = new ScreenWriter(screen);
		Color color = writer.getForegroundColor();

		writer.drawString(0, 0, "╔═══════════════════════════════════════════════════════════════════════════════════════════╗");
		writer.drawString(0, 1, "║   Turing machine                                        by Marc-André, Marco und Samuel   ║");
		writer.drawString(0, 2, "╚═══════════════════════════════════════════════════════════════════════════════════════════╝");
		writer.drawString(0, 3, "    Control: [ENTER] = Run to the end                         Any other key for next step    ");
		writer.drawString(0, 4, "");
		writer.setForegroundColor(Color.GREEN);
		writer.drawString(Math.round((93 - this.name.length()) / 2), 5, this.name, ScreenCharacterStyle.Bold);
		writer.setForegroundColor(color);
		
		// Clear the screen below the header
		String emptyLine = new String(new char[screen.getTerminalSize().getColumns()]).replace("\0", " ");
		for (int i = 0; i < 15; i++) {
			writer.drawString(0, this.offsetTop + i, emptyLine);
		}
	}
}
