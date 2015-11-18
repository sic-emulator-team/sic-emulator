

public class SICmulator implements Runnable {

    private Memory mem;

    public SICmulator() {
        mem = new Memory();
    }
    
    public SICmulator(String filename) {
        mem = new Memory();
        loadProgram(filename);
    }

    public void loadProgram(String filename) {
        // load file into the memory at the specified location.
        // Should also change the value of PC so it executes from loaded program
    }

    public void run() {
        while (true) {
            // Get the next word in memory at the PC
            int nextWord = mem.getWord(mem.PC, false);
            mem.PC += 3; // increment PC before executing the instruction

            // Decode the next word
            int opcode = nextWord >> 16;
            int address = nextWord & 0x7FFF;
            boolean indexed = (nextWord & 0x8000) != 0;

            // Execute the instruction
            boolean error = execute(opcode, address, indexed);

            // So we can see contents of memory when error occurred
            if (error) {
                mem.hexDump("data.txt");
                return;
            }
        }
    }

    public boolean execute(int opcode, int address, boolean indexed) {
        boolean error; 

        // A function pointer would have been nice here... 
        switch (opcode) {
            case ALU.OP_ADD:
                error = ALU.add(mem, address, indexed);
                break;
            case ALU.OP_AND:
                error = ALU.and(mem, address, indexed);
                break;
            case ALU.OP_COMP:
                error = ALU.comp(mem, address, indexed);
                break;
            case ALU.OP_DIV:
                error = ALU.div(mem, address, indexed);
                break;
            case ALU.OP_J:
                error = ALU.j(mem, address, indexed);
                break;
            case ALU.OP_JEQ:
                error = ALU.jeq(mem, address, indexed);
                break;
            case ALU.OP_JGT:
                error = ALU.jgt(mem, address, indexed);
                break;
            case ALU.OP_JLT:
                error = ALU.jlt(mem, address, indexed);
                break;
            case ALU.OP_JSUB:
                error = ALU.jsub(mem, address, indexed);
                break;
            case ALU.OP_LDA:
                error = ALU.lda(mem, address, indexed);
                break;
            case ALU.OP_LDCH:
                error = ALU.ldch(mem, address, indexed);
                break; 
            case ALU.OP_LDL:
                error = ALU.ldl(mem, address, indexed);
                break; 
            case ALU.OP_LDX:
                error = ALU.ldx(mem, address, indexed);
                break; 
            case ALU.OP_MUL:
                error = ALU.mul(mem, address, indexed);
                break;
            case ALU.OP_OR:
                error = ALU.or(mem, address, indexed);
                break;
            case ALU.OP_RD:
                error = ALU.rd(mem, address, indexed);
                break;
            case ALU.OP_RSUB:
                error = ALU.rsub(mem, address, indexed);
                break;
            case ALU.OP_STA:
                error = ALU.sta(mem, address, indexed);
                break;
            case ALU.OP_STCH:
                error = ALU.stch(mem, address, indexed);
                break;
            case ALU.OP_STL:
                error = ALU.stl(mem, address, indexed);
                break;
            case ALU.OP_STSW:
                error = ALU.stsw(mem, address, indexed);
                break;
            case ALU.OP_STX:
                error = ALU.stx(mem, address, indexed);
                break;
            case ALU.OP_SUB:
                error = ALU.sub(mem, address, indexed);
                break;
            case ALU.OP_WD:
                error = ALU.wd(mem, address, indexed);
                break;

            default:
                System.err.printf("Encountered invalid opcode: %2x\n", opcode);
                error = true; // opcode was invalid...
        }   
        return error;
    }

    public static void main(String[] args) {

        SICmulator sim1 = new SICmulator("HelloWorld.obj");
        sim1.run();

        // OR... we can use threads to have multiple simulations going at the same time
        SICmulator sim2 = new SICmulator("FizzBuzz.obj");
        Thread t1 = new Thread(sim2);
        t1.start();  // we can continue while it simulates on its own thread

        Thread t2 = new Thread(sim1);
        t2.start();
    }
}
