package com.unpoke.table;

import java.util.function.BiFunction;

import org.apache.log4j.Logger;

import com.unpoke.font.FontFileReader;

public enum FontTableType {    
    CMAP("cmap"),
    CVT("cvt "),
    DSIG("DSIG"),
    EBDT("EBDT"),
    EBLC("EBLC"),
    FPGM("fpgm"),
    GASP("gasp"),
    GDEF("GDEF"),
    GLYF("glyf"),
    GPOS("GPOS"),
    GSUB("GSUB"),
    HEAD("head"),
    HMTX("hmtx"),
    HHEA("hhea"),
    KERN("kern", KernTable::new),
    LOCA("loca"),
    MAXP("maxp"),
    META("meta"),
    NAME("name"), 
    OFFSET("offset"),
    OS2("OS/2"),
    POST("post"),
    PREP("prep"),
    UNKNOWN("unknown");

    private static Logger LOG = Logger.getLogger(FontTableType.class);
    
    private final String tableName;
    private final BiFunction<FontFileReader, Long, FontTable> constructor;

    private FontTableType(final String tableName) {
        this.tableName = tableName;
        constructor = null;
    }
    
    private FontTableType(final String tableName, final BiFunction<FontFileReader, Long, FontTable> constructor) {
        this.tableName = tableName;
        this.constructor = constructor;
    }

    public String getTableName() {
        return tableName;
    }
    
    public FontTable parseTable(final FontFileReader fontFile, final long offset) {
        if (constructor == null) {
            return new FontTable() {

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
                
            };
        }
        return constructor.apply(fontFile, offset);
    }

    public static FontTableType fromString(final String tableName) {
        for (final FontTableType fontTableType : FontTableType.values()) {
            if (fontTableType.getTableName().equals(tableName)) {
                return fontTableType;
            }
        }

        LOG.debug("Unknown table type: " + tableName);
        return FontTableType.UNKNOWN;
    }
}
