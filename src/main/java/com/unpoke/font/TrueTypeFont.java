package com.unpoke.font;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrueTypeFont implements Font {
    private final Map<FontTableType, TableRecord> tableRecords;
    private final Map<FontTableType, FontTable> tables;

    public TrueTypeFont(final File fontFile) throws IOException {
        final FontFileReader fontReader = new FontFileReader(fontFile, "r");
        tableRecords = new HashMap<>();
        tables = new HashMap<>();

        final FontTable offsetTable = new OffsetTable(fontReader, 0);
        tables.put(FontTableType.OFFSET, offsetTable);

        TableRecord currentRecord;
        for (int i = 0;i < offsetTable.getNumberOfSubtables();i++) {
            currentRecord = new TableRecord(fontReader);
            tableRecords.put(currentRecord.getTableType(), currentRecord);
        }
    }

    public Map<FontTableType, TableRecord> getTableRecords() {
        return new HashMap<>(tableRecords);
    }
}
