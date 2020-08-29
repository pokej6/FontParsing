package com.unpoke.font;

public class KernTable implements FontTable {
    private int version;

    public KernTable(final byte[] tableBytes) {

    }

    @Override
    public int getTableVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumberOfSubtables() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public FontTableType getTableType() {
        return FontTableType.KERN;
    }

    @Override
    public long getTableStart() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTableEnd() {
        // TODO Auto-generated method stub
        return 0;
    }

}
