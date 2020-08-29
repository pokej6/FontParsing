package com.unpoke.font;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.unpoke.table.FontTable;
import com.unpoke.table.FontTableType;
import com.unpoke.table.OffsetTable;

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
        long currentRecordOffset = offsetTable.getTableEnd(); 
        for (int i = 0;i < offsetTable.getNumberOfSubtables();i++) {
            currentRecord = new TableRecord(fontReader, currentRecordOffset);
            tableRecords.put(currentRecord.getTableType(), currentRecord);
            currentRecordOffset += 16;
        }
    }

    public Map<FontTableType, TableRecord> getTableRecords() {
        return new HashMap<>(tableRecords);
    }
}
