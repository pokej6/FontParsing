import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fonts {
	public static void main(String[]args) throws Exception {
		RandomAccessFile f = new RandomAccessFile(new File("calibri.ttf"), "r");
		// 0x00010000 is TrueType
		System.out.println("snftVersion: " + String.format("0x%08X", readUint32(f)));
		int numTables = f.readUnsignedShort();
		System.out.println("numTables: " + numTables);
		System.out.println("searchRange: " + f.readUnsignedShort());
		System.out.println("entrySelector: " + f.readUnsignedShort());
		System.out.println("rangeShift: " + f.readUnsignedShort());
		System.out.println();

		int offsetTableStart = 12;
		
		String tableName;
		int tableOffset;
		int tableLength;
		
		do {
			tableName = "";
			for (int i = 0;i < 4;i++) {
				tableName+=(char)f.readUnsignedByte();
			}
			readUint32(f);
			tableOffset = readUint32(f);
			tableLength = readUint32(f);
		} while (!"cmap".equals(tableName));
		
		f.seek(tableOffset);
		System.out.println("version: " + f.readUnsignedShort());
		numTables = f.readUnsignedShort();
		System.out.println("numTables: " + numTables);
		boolean foundFormat4 = false;
		int segCount = 0;
		List<Integer> endCodes = new ArrayList<Integer>();
		List<Integer> startCodes = new ArrayList<Integer>();
		List<Integer> idRangeOffsets = new ArrayList<Integer>();
		List<Short> idDeltas = new ArrayList<Short>();
		List<Integer> glyphIds = new ArrayList<Integer>();
		for (int i = 0;i < numTables;i++) {
			int platformId = f.readUnsignedShort();
			System.out.println("platformID: " + platformId);
			int encodingId = f.readUnsignedShort();
			System.out.println("encodingID: " + encodingId);
			int subTableOffset = readUint32(f);
			System.out.println("offset: " + subTableOffset);
			System.out.println();

			if (platformId == 3 && encodingId == 1) {
				f.seek(tableOffset);
				f.skipBytes(subTableOffset);
				
				// format should be 4...
				System.out.println("format: " + f.readUnsignedShort());
				tableLength = f.readUnsignedShort();
				System.out.println("length: " + tableLength);
				System.out.println("language: " + f.readUnsignedShort());
				segCount = f.readUnsignedShort() / 2;
				System.out.println("segCountX2: " + segCount * 2);
				System.out.println("searchRange: " + f.readUnsignedShort());
				System.out.println("entrySelector: " + f.readUnsignedShort());
				System.out.println("rangeShift: " + f.readUnsignedShort());
				for (int j = 0;j < segCount;j++) {
					endCodes.add(f.readUnsignedShort());
				}
				System.out.println("endCount[segCount]: " + endCodes);
				System.out.println("reservedPad: " + f.readUnsignedShort());
				for (int j = 0;j < segCount;j++) {
					startCodes.add(f.readUnsignedShort());
				}
				System.out.println("startCount[segCount]: " + startCodes);
				for (int j = 0;j < segCount;j++) {
					idDeltas.add(f.readShort());
				}
				System.out.println("idDelta[segCount]: " + idDeltas);
				for (int j = 0;j < segCount;j++) {
					idRangeOffsets.add(f.readUnsignedShort());
				}
				System.out.println("idRangeOffset[segCount]: " + idRangeOffsets);
				while(f.getFilePointer() != tableOffset + subTableOffset + tableLength) {
					glyphIds.add(f.readUnsignedShort());
				}
				System.out.println("glyphIdArray[]: " + glyphIds);
				foundFormat4 = true;
				break;
			}
		}
		
		if (!foundFormat4) {
			System.err.println("Font did not contain a format 4 cmap table.");
			return;
		}
		
		Map<Integer, Character> cmap = new HashMap<Integer, Character>(); 
		for (int i = 48;i <= 122;i++) {
			int segment = 0;
			for (int j = 0;i < segCount;j++) {
				if (endCodes.get(j) >= i) {
					segment = j;
					break;
				}
			}
			
			int idRangeOffset = idRangeOffsets.get(segment);
			if (idRangeOffset == 0) {
				System.err.println("idRangeOffset of 0 not yet supported.");
				return;
			}
			
			int glyphIndex = idRangeOffset / 2 - segCount + segment + i - startCodes.get(segment);
			cmap.put(glyphIds.get(glyphIndex), (char)i);
		}
		
//		if (0==0) return;
		f.seek(offsetTableStart);
		do {
			tableName = "";
			for (int i = 0;i < 4;i++) {
				tableName+=(char)f.readUnsignedByte();
			}
			
			System.out.println("tag: " + tableName);
			System.out.println("checkSum: " + readUint32(f));
			tableOffset = readUint32(f);
			System.out.println("offset: " + tableOffset);
			tableLength = readUint32(f);
			System.out.println("length: " + tableLength);
			System.out.println();
		} while (!"kern".equals(tableName));
		
		f.seek(tableOffset);

		System.out.println("version: " + f.readUnsignedShort());
		numTables = f.readUnsignedShort();
		System.out.println("nTables: " + numTables);
		System.out.println();
		System.out.println("version: " + f.readUnsignedShort());
		System.out.println("length: " + f.readUnsignedShort());
		int coverage = f.readUnsignedShort();
		// 1 for horizontal, 0 for vertical
		System.out.println("horizontal: " + (coverage & 1));
		// 1 for minimum values, 0 for kerning values
		System.out.println("minimum: " + (coverage >> 1 & 1));
		System.out.println("cross-stream: " + (coverage >> 2 & 1));
		System.out.println("override: " + (coverage >> 3 & 1));
		System.out.println("reserved1: " + (coverage >> 4 & 15));
		int format = coverage >> 8;
		System.out.println("format: " + format);
		System.out.println();
		
		if (format != 0) {
			System.err.println("Format not supported.");
			return;
		}
		
		int numPairs = f.readUnsignedShort();
		System.out.println("nPairs: " + numPairs);
		System.out.println("searchRange: " + f.readUnsignedShort());
		System.out.println("entrySelector: " + f.readUnsignedShort());
		System.out.println("rangeShift: " + f.readUnsignedShort());
		System.out.println();
		
//		if (0==0) return;
		for(int i = 0;i < numPairs;i++) {
			int left = f.readUnsignedShort();
			int right = f.readUnsignedShort();
			int value = f.readShort();
			if (cmap.get(left) != null && cmap.get(right) != null) {
				System.out.println("l=\"" + cmap.get(left) + "\" r=\"" + cmap.get(right) + "\" v=\"" + value + "\"");
			}
		}
	}
	
	public static int readUint32(RandomAccessFile f) throws Exception {
		return (f.readUnsignedShort() << 16) | f.readUnsignedShort();
	}
}
