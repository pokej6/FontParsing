package com.unpoke.font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FontFileReader extends RandomAccessFile {	
    public FontFileReader(final File file, final String mode) throws FileNotFoundException {
        super(file, mode);
    }

    public int readUnsignedInt32() throws IOException {
        return (readUnsignedShort() << 16) | readUnsignedShort();
    }
}
