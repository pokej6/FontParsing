package com.unpoke.table;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.unpoke.font.FontFileReader;

public class KernTable implements FontTable {
    private final long startOffset;
    private  long endOffset;
    private final int version;
    private final int numSubTables;

    // Update FontFileReader to read the file into a byte[] and then provide read functions on that
    public KernTable(final FontFileReader fontFile, long offset) throws IOException {
        fontFile.seek(offset);
        startOffset = offset;
        version = fontFile.readUnsignedShort();
        numSubTables = fontFile.readUnsignedShort();
        
        
    }

    @Override
    public int getTableVersion() {
        return version;
    }

    @Override
    public int getNumberOfSubtables() {
        return numSubTables;
    }

    @Override
    public FontTableType getTableType() {
        return FontTableType.KERN;
    }

    @Override
    public long getTableStart() {
        return startOffset;
    }

    @Override
    public long getTableEnd() {
        return endOffset;
    }

    static class SubTable implements FontTable {
        private static final Logger LOG = Logger.getLogger(KernTable.SubTable.class);
        
        private final long startOffset;
        private  long endOffset;
        private final int version;
        private final int coverage;
        private final boolean horizontal;
        private final boolean minimum;
        private final boolean crossStream;
        private final boolean override;
        private final int reserved;
        private final int format;
        
        public SubTable(final FontFileReader fontFile, long offset) throws IOException {
            fontFile.seek(offset);
            startOffset = offset;
            version = fontFile.readUnsignedShort();
            coverage = fontFile.readUnsignedShort();
            
            horizontal  = (coverage >> 0 & 0x1) == 1;
            minimum     = (coverage >> 1 & 0x1) == 1;
            crossStream = (coverage >> 2 & 0x1) == 1;
            override    = (coverage >> 3 & 0x1) == 1;
            reserved    =  coverage >> 4 & 0xF;
            format      =  coverage >> 8;
            
            if (reserved != 0) {
                LOG.warn("Kern Subtable had non-zero data in reserved space.");
            }
            
            if (format != 0 && format != 2) {
                LOG.warn("Kern Subtable had undefined format: " + format);
            }
        }
        
        @Override
        public FontTableType getTableType() {
            // TODO Auto-generated method stub
            return null;
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
}
