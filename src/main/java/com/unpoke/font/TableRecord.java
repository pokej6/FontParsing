package com.unpoke.font;

import java.io.IOException;

public class TableRecord {
    private final FontTableType fontTableType;
    private final long startOffset;
    private final long endOffset;
    private final int checksum;

    public TableRecord(final FontFileReader fontFile) throws IOException {
        final StringBuilder tableName = new StringBuilder();
        for (int i = 0;i < 4;i++) {
            tableName.append((char)fontFile.readUnsignedByte());
        }
        fontTableType = FontTableType.fromString(tableName.toString());

        //TODO: check this
        checksum = fontFile.readUnsignedInt32();
        startOffset = fontFile.readUnsignedInt32();
        endOffset = startOffset + fontFile.readUnsignedInt32();
    }

    public FontTableType getTableType() {
        return fontTableType;
    }

    public long getTableStart() {
        return startOffset;
    }

    public long getTableEnd() {
        return endOffset;
    }
}