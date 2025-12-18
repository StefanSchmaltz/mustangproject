package org.mustangproject.ZUGFeRD;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;

public class XRechnungImporter extends ZUGFeRDImporter {

	public XRechnungImporter(byte[] rawXml) {
		this(new RandomAccessReadBuffer(rawXml));
	}

	public XRechnungImporter(RandomAccessRead rawXml) {
		super();

		try {
			setRawXML(rawXml);
			containsMeta = true;
		} catch (final IOException e) {
			Logger.getLogger(ZUGFeRDImporter.class.getName()).log(Level.SEVERE, null, e);
			throw new ZUGFeRDExportException(e);
		}
	}

	public XRechnungImporter(String filename) {
		super();

		try {
			setRawXML(new RandomAccessReadBufferedFile(Paths.get(filename)));
			containsMeta = true;
		} catch (final IOException e) {
			Logger.getLogger(ZUGFeRDImporter.class.getName()).log(Level.SEVERE, null, e);
			throw new ZUGFeRDExportException(e);
		}

	}
	public XRechnungImporter(InputStream fileinput) {
		super();

		try {
			setRawXML(new RandomAccessReadBuffer(fileinput));
			containsMeta = true;
		} catch (final IOException e) {
			Logger.getLogger(ZUGFeRDImporter.class.getName()).log(Level.SEVERE, null, e);
			throw new ZUGFeRDExportException(e);
		}


	}


}
