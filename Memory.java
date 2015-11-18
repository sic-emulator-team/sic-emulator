public class Memory {

    private byte[] memory;
    public int A, X, L, SW, PC;

    public Memory() {
        memory = new byte[32768];
    }

    // Get the word in memory at addr; most significant byte is zeroed.
    public int getWord(int addr, boolean indexed) {
        int offset = addr;
        if (indexed)
            offset += X;
        return (memory[offset] << 16) | (memory[offset + 1] << 8) | memory[offset + 2];
    }

    public byte getByte(int addr, boolean indexed) {
        if (indexed)
            return memory[addr + X];
        return memory[addr];
    }

    // Sets the word in memory at addr to the first 24 bits of val
    public void setWord(int val, int addr, boolean indexed) {
        int offset = addr;
        if (indexed) 
            offset += X;
        memory[offset]     = (byte) (val >> 16);
        memory[offset + 1] = (byte) (val >> 8);
        memory[offset + 2] = (byte) val;
    }

    public void setByte(int val, int addr, boolean indexed) {
        if (indexed)
            memory[addr + X] = (byte) val;
        else 
            memory[addr] = (byte) val;
    }
    
    // Save the memory contents to a file so we can see the state of memory
    public void hexDump(String filename) { 

    }

}


