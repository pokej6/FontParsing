package com.unpoke.font;

public enum ScalableFontVersion {
    UNKNOWN(0), TTF(65536), OTTO(1330926671);

    private final int version;

    private ScalableFontVersion(final int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public String getVersionHex() {
        return String.format("0x%08X", version);
    }

    public static ScalableFontVersion fromInt(final int version) {
        for (final ScalableFontVersion sfnt : ScalableFontVersion.values()) {
            if (sfnt.getVersion() == version) {
                return sfnt;
            }
        }

        return UNKNOWN;
    }
}
