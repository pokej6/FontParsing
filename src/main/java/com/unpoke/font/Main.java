package com.unpoke.font;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Main {
    private final static Logger LOG = Logger.getLogger(Main.class);
    
    public static void main(final String[] args) throws IOException {
        if (args.length < 1) {
            LOG.error("Usage: java -jar FontParser.jar <font_filename>");
            System.exit(-1);
        }

        final String fontFilename = args[0];
        final File fontFile = new File(fontFilename);
        final TrueTypeFont font = new TrueTypeFont(fontFile);
        LOG.info(font.getTableRecords());
    }
}
