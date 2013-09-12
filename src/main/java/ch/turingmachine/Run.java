package ch.turingmachine;

public class Run {

	public static void main(String[] args) {
		MachineFactory factory = new MachineFactory();
				
		TuringMachine machine;
		
		if(args.length == 1) {
			machine = factory.create(args[0]);
		}
		else
		{
			//machine = factory.createMultiplicationMachine();
			machine = factory.createFactorialMachine();
		}
	
		machine.initialize();
		machine.prepare();
		machine.run();
		machine.terminate();

	}
}
