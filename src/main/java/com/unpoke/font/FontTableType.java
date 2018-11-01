package com.unpoke.font;

import org.apache.log4j.Logger;

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
    KERN("kern"),
    LOCA("loca"),
    MAXP("maxp"),
    NAME("name"), 
    OFFSET("offset"),
    OS2("OS/2"),
    POST("post"),
    PREP("prep"),
    UNKNOWN("unknown");

    private static Logger LOG = Logger.getLogger(FontTableType.class);
    
    private final String tableName;

    private FontTableType(final String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
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
