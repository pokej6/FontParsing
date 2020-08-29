package com.unpoke.table;

import java.io.IOException;

import com.unpoke.font.FontFileReader;
import com.unpoke.font.ScalableFontVersion;

public class OffsetTable implements FontTable {	
    private final long startOffset;
    private final long endOffset;

    private final ScalableFontVersion sfntVersion;
    private final int numTables;
    private final int searchRange;
    private final int entrySelector;
    private final int rangeShift;

    public OffsetTable(final FontFileReader fontFile, long offset) throws IOException {
        startOffset = offset;

        fontFile.seek(startOffset);

        sfntVersion = ScalableFontVersion.fromInt(fontFile.readUnsignedInt32());
        if (sfntVersion != ScalableFontVersion.TTF) {
            System.err.println("Currently only supporting TrueType fonts.");
            System.exit(-1);
        }

        numTables = fontFile.readUnsignedShort();
        searchRange = fontFile.readUnsignedShort();
        entrySelector = fontFile.readUnsignedShort();
        rangeShift = fontFile.readUnsignedShort();

        endOffset = fontFile.getFilePointer();
    }

    @Override
    public long getTableStart() {
        return startOffset;
    }

    @Override
    public long getTableEnd() {
        return endOffset;
    }

    public ScalableFontVersion getSfntVersion() {
        return sfntVersion;
    }

    public int getNumTables() {
        return numTables;
    }

    public int getSearchRange() {
        return searchRange;
    }

    public int getEntrySelector() {
        return entrySelector;
    }

    public int getRangeShift() {
        return rangeShift;
    }

    @Override
    public int getTableVersion() {
        return sfntVersion.getVersion();
    }

    @Override
    public int getNumberOfSubtables() {
        return numTables;
    }

    @Override
    public FontTableType getTableType() {
        return FontTableType.OFFSET;
    }
}
