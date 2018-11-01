package com.unpoke.font;

public interface FontTable {
    FontTableType getTableType();
    int getTableVersion();
    int getNumberOfSubtables();
    long getTableStart();
    long getTableEnd();
}
