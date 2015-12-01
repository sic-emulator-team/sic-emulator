import java.io.IOException;

// Arithmetic Logic Unit. Contains the implementation of each operation
// Defines static methods that operate on instances of Memory...

public class ALU {

    public static final int OP_ADD  = 0x18;
    public static final int OP_AND  = 0x40;
    public static final int OP_COMP = 0x28;
    public static final int OP_DIV  = 0x24;
    public static final int OP_J    = 0x3C;
    public static final int OP_JEQ  = 0x30;
    public static final int OP_JGT  = 0x34;
    public static final int OP_JLT  = 0x38;
    public static final int OP_JSUB = 0x48;
    public static final int OP_LDA  = 0x00;
    public static final int OP_LDCH = 0x50;
    public static final int OP_LDL  = 0x08;
    public static final int OP_LDX  = 0x04;
    public static final int OP_MUL  = 0x20;
    public static final int OP_OR   = 0x44;
    public static final int OP_RD   = 0xD8;
    public static final int OP_RSUB = 0x4C;
    public static final int OP_STA  = 0x0C;
    public static final int OP_STCH = 0x54;
    public static final int OP_STL  = 0x14;
    public static final int OP_STSW = 0xE8;
    public static final int OP_STX  = 0x10;
    public static final int OP_SUB  = 0x1C;
    public static final int OP_TD   = 0xE0;
    public static final int OP_TIX  = 0x2C;
    public static final int OP_WD   = 0xDC;

    public static boolean stdinOpen = true;

    // return true if there was an error, else false
    public static boolean add(Memory mem, int addr, boolean indexed) {
        // Not sure if this will actually work... I hope it does :o
        mem.A += mem.getWord(addr, indexed);
        return false;
    }

    public static boolean and(Memory mem, int addr, boolean indexed) {
        mem.A &= mem.getWord(addr, indexed);
        return false;
    }

    public static boolean comp(Memory mem, int addr, boolean indexed) {
        int data = mem.getWord(addr, indexed);
        if (mem.A == data) {
            mem.SW = '=';
            //set Conditional Code to '=' for JEQ instruction
        } else if (mem.A < data) {
            mem.SW = '<';
            //set Conditional Code to '<' for JLT instruction
        } else {
            mem.SW = '>';
            //set Conditional Code to '>' for JGT instruction
        }
        return false;
    }

    public static boolean div(Memory mem, int addr, boolean indexed) {
        mem.A /= mem.getWord(addr, indexed);
        return false;
    }

    public static boolean j(Memory mem, int addr, boolean indexed) {
        mem.PC = addr;
        //set PC to the address to jump to
        return false;
    }

    public static boolean jeq(Memory mem, int addr, boolean indexed) {
        if (mem.SW == '=') {
            mem.PC = addr;
        }
        //set PC to the address to jump to? if SW from comp is set to '='
        return false;
    }

    public static boolean jgt(Memory mem, int addr, boolean indexed) {
        if (mem.SW == '>')
            mem.PC = addr;
        //set PC to address to jump to if SW from comp is set to ">"
        return false;
    }

    public static boolean jlt(Memory mem, int addr, boolean indexed) {
        if (mem.SW == '<')
            mem.PC = addr;
        //set PC to the address to jump to if SW from COMP is set to "<"
        return false;
    }

    public static boolean jsub(Memory mem, int addr, boolean indexed) {
        mem.L = mem.PC;
        //store return address in L
        mem.PC = addr;
        // set PC to address of subroutine to jump to
        return false;
    }

    public static boolean lda(Memory mem, int addr, boolean indexed) {
        mem.A = mem.getWord(addr, indexed);
        return false;
    }

    public static boolean ldch(Memory mem, int addr, boolean indexed) {
        mem.A &= 0xffff00;
        mem.A |= (byte) mem.getByte(addr, indexed);
        //loads a byte from memory into the rightmost byte of A. The rest of A remains the same
        return false;
    }

    public static boolean ldl (Memory mem, int addr, boolean indexed) {
        mem.L = mem.getWord(addr, indexed);
        return false;
    }


    public static boolean ldx(Memory mem, int addr, boolean indexed) {
        mem.X = mem.getWord(addr, indexed);
        return false;
    }

    public static boolean mul(Memory mem, int addr, boolean indexed) {
        mem.A *= mem.getWord(addr, indexed);
        return false;
    }

    public static boolean or(Memory mem, int addr, boolean indexed) {
        mem.A |= mem.getWord(addr, indexed);
        return false;
    }

    public static boolean rd(Memory mem, int addr, boolean indexed) {
        int data = mem.getByte(addr, indexed);

        // Read from stdin if device code is 00
        if (data == 0) {
            mem.A &= 0xffff00; // Clear the lowest byte of A
            try {
                // Input goes into lowest byte of A
                int input = System.in.read();
                if (!stdinOpen || input == -1) {
                    stdinOpen = false;
                    input = 0;
                }
                mem.A |= input;
            }
            catch(IOException e) {
                return true;
            }
        }
        return false;
    }

    public static boolean rsub(Memory mem, int addr, boolean indexed) {
        mem.PC = mem.L;
        return false;
    }

    public static boolean sta(Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.A, addr, indexed);
        return false;
    }

    public static boolean stch(Memory mem, int addr, boolean indexed) {
        mem.setByte(mem.A, addr, indexed);
        return false;
        //stores a character from the rightmost byte of A into memory.
    }

    public static boolean stl (Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.L, addr, indexed);
        return false;
    }

    public static boolean stsw(Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.SW, addr, indexed);
        return false;
    }

    public static boolean stx (Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.X, addr, indexed);
        return false;
    }

    public static boolean sub (Memory mem, int addr, boolean indexed) {
        mem.A -= mem.getWord(addr, indexed);
        return false;
    }

    public static boolean td(Memory mem, int addr, boolean indexed) {
        int data = mem.getByte(addr, indexed);

        // 0 - STDIN, 1 - STDOUT, 2 - STDERR
        if (data >= 0 && data < 3) {
            mem.SW = '<'; // '<' signals ready for some reason in SIC
        }
        else {
            mem.SW = '=';
        }
        return false;
    }

    public static boolean tix(Memory mem, int addr, boolean indexed) {
        int data = mem.getWord(addr, indexed);
        mem.X++;

        if (mem.X > data) {
            mem.SW = '>';
        }
        else if (mem.X < data) {
            mem.SW = '<';
        }
        else {
            mem.SW = '=';
        }
        return false;
    }

    public static boolean wd(Memory mem, int addr, boolean indexed) {
        int data = mem.getByte(addr, indexed);

        char toWrite = (char) (mem.A & 0xff);
        if (data == 1) {
            System.out.print(toWrite);
        }
        else if (data == 2) {
            System.err.print(toWrite);
        }
        return false;
    }

}
