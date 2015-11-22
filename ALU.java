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
        //set PC to the address to jump to?
    }

    public static boolean jeq(Memory mem, int addr, boolean indexed) {
        if (mem.SW == '=') {
            mem.PC = addr;
        }
        //set PC to the address to jump to? if SW from comp is set to '='
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
            data = mem.getWord(addr, indexed);
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
            } catch(IOException e) {
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

    public static boolean td(Memory mem, int addr, boolean indexed) {
        int data = 0;
        try {
            data = mem.getWord(addr, indexed);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        mem.SW = '<'; // '<' signals ready for some reason in SIC
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

