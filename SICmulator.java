import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.Integer;


public class SICmulator implements Runnable {

    private Memory mem;

    public SICmulator() {
        mem = new Memory();
    }

    public SICmulator(String filename) {
        mem = new Memory();
        loadProgram(filename);
    }

    // Load file into the memory at the specified location.
    // Should also change the value of PC so it executes from loaded program
    public static void loadProgram(String filename, Memory mem) {
        String progName = "";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {

            // This code pretty gross but it works :/
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if ((line.charAt(0) == 'H') && (line.length() == 20)) {
                    progName = line.substring(1, 7);
                    mem.PC = Integer.parseInt(line.substring(7, 13), 16);
                    System.out.printf("Loading \"%s\" at %04x ...\n", progName, startAddr);
                }
                else if (line.charAt(0) == 'T' && (line.length >= 9)) {
                    int addr = Integer.parseInt(line.substring(1, 7), 16);
                    int len = 2 * Integer.parseInt(line.substring(7, 9), 16);
                    for (int i = 0; i < len; i += 2) {
                        mem.setByte(Integer.parseInt(line.substring(9 + i, 11 + i), 16), addr + (i / 2), false);
                    }
                }
                else if (line.charAt(0) == 'E') {
                    System.out.printf("Loaded %s\n", progName);
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void run() {
        boolean error = false;
        while (!error) {
            // Fetch the next word in memory at the PC
            int nextWord = mem.getWord(mem.PC, false);
            mem.PC += 3; // increment PC before executing the instruction

            // Decode the next word
            int opcode = nextWord >> 16;
            int address = nextWord & 0x7FFF;
            boolean indexed = (nextWord & 0x8000) != 0;

            // Execute the instruction
            error = execute(opcode, address, indexed);
        }
        // So we can see contents of memory when error occurred
        mem.hexDump("data.txt");
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
            case ALU.OP_TD:
                error = ALU.td(mem, address, indexed);
                break;
            case ALU.OP_TIX:
                error = ALU.tix(mem, address, indexed);
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
