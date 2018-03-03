import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Test {
	public static int PUBLIC = 0x0001;
	public static int PACKAGE = 0x0002;
	public static int PROTECTED = 0x0004;
	public static int PRIVATE = 0x0008;
	public static int STATIC = 0x0010;
	public static int ABSTRACT = 0x0020;
	public static int FINAL = 0x0040;

	private int modifier = 0;

	public Test(int modifier) {
		this.modifier = modifier;
	}

	public boolean isPublic() {
		return (this.modifier & PUBLIC) != 0;
	}

	public boolean isPackage() {
		return (this.modifier & PACKAGE) != 0;
	}

	public boolean isProtected() {
		return (this.modifier & PROTECTED) != 0;
	}

	public boolean isPrivate() {
		return (this.modifier & PRIVATE) != 0;
	}

	public boolean hasModifier(int modifier) {
		return (this.modifier & modifier) == modifier;
	}

	public boolean hasnotModifier(int modifier) {
		return (this.modifier & modifier) == 0;
	}

	public static void main(String[] args) {
		int modifiers1 = PUBLIC | PROTECTED;
		int modifiers2 = PUBLIC | PROTECTED | PRIVATE;
		Test base = new Test(modifiers2); // base is [PUBLIC | PROTECTED |
											// PRIVATE]
		System.out.println(base.hasModifier(modifiers1)); // true because base
															// includes [PUBLIC
															// | PROTECTED]
		Test base2 = new Test(modifiers1); // base is [PUBLIC | PROTECTED]
		System.out.println(base2.hasModifier(modifiers2)); // fasle because
															// base2 donot
															// includes
															// [PRIVATE]
	}
}










import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Test 
{
	public static void main(String args[]) {


		String binary[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"

		};
		int a = 3; // 0 + 2 + 1 or 0011 in binary
		int b = 6; // 4 + 2 + 0 or 0110 in binary
		int c = a | b;
		int d = a & b;
		int e = a ^ b;
		int f = (~a & b) | (a & ~b);
		int g = ~a & 0x0f;


		System.out.println(" a = " + binary[a]);
		System.out.println(" b = " + binary[b]);
		System.out.println(" a|b = " + binary[c]);
		System.out.println(" a&b = " + binary[d]);
		System.out.println(" a^b = " + binary[e]);
		System.out.println("~a&b|a&~b = " + binary[f]);
		System.out.println(" ~a = " + binary[g]);


		} 
}