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

    // return true if there was an error, else false
    public static boolean add(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }

        // Not sure if this will actually work... I hope it does :o
        mem.A += data;

        return false;
    }

    public static boolean and(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        mem.A &= data;
        return false;
    }

    public static boolean comp(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
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
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        mem.A /= data;
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
        //set PC to the address to jump to if SW from comp is set to '='
        return false;
    }
    
    public static boolean jgt(Memory mem, int addr, boolean indexed) {
        if (mem.SW == ">") {
            mem.PC = addr;
        }
        //set PC to the address to jump to if SW from comp is set to ">"
        return false;
    }
    
    public static boolean jlt(Memory mem, int addr, boolean indexed) {
        if (mem.SW == "<") {
            mem.PC = addr;
        }
        //set PC to the address to jump to if SW from comp is set to "<"
        return false;
    }
    
    public static boolean jsub(Memory mem, int addr, boolean indexed) {
        mem.L = mem.PC;
        //store return address in L
        mem.PC = addr;
        //set PC to address of subroutine to jump to
    }
    
    public static boolean lda(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        
        mem.A = data;
        return false;
    }
    
    public static boolean ldch(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getByte(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        mem.A = data;
        return false;
        //loads a byte from memory into the rightmost byte of A? may need to be double-checked
    }
    
    public static boolean ldl(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }

        mem.L = data;
        return false;
    }

    public static boolean ldx(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }

        mem.X = data;
        return false;
    } 

    public static boolean mul(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }

        mem.A *= data;
        return false;
    }

    public static boolean or(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }

        mem.A |= data;
        return false;
    }

    public static boolean rd(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            // Only read the first 2 bytes
            data = mem.getWord(addr, indexed) >> 8;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        // Read from stdin if device code is 00
        if (data == 0) {
            mem.A &= 0xffff00; // Clear the lowest byte of A
            try {
                // Input goes into lowest byte of A
                mem.A |= (byte) System.in.read(); 
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
        //stores a character from the rightmost byte of A into memory? may need to be double-checked
    }
    
    public static boolean stl(Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.L, addr, indexed);
        return false;
    }
    
    public static boolean stsw(Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.SW, addr, indexed);
        return false;
    }
    
    public static boolean stx(Memory mem, int addr, boolean indexed) {
        mem.setWord(mem.X, addr, indexed);
        return false;
    }
    
    public static boolean sub(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        mem.A -= data;
        return false;
    }

    public static boolean td(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            // Only read the first 2 bytes
            data = mem.getWord(addr, indexed) >> 8;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        // 0 - STDIN, 1 - STDOUT, 2 - STDERR
        if (data >= 0 && data < 3) {
            mem.SW = '<'; // '<' signals ready for some reason in SIC
        }
        return false;
    }

    public static boolean tix(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        } 
        mem.X++;

        if (mem.X > data) {
            mem.SW = '>';
        }
        else if (mem.X < data) {
            mem.SW = '<';
        }
        else {
            mem.X = '=';
        }
        return false;
    }


}

