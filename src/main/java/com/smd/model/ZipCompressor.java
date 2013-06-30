package com.smd.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

public class ZipCompressor {
	private OutputStream zipOutput;
	private ArchiveOutputStream logicalZip;
	
	public ZipCompressor() {
						
	}
	
	//Cria arquivo zip chamado "zipName", caso o retorno seja verdadeiro;;
	public boolean createZip (String zipName) {
		try {
			zipOutput = new FileOutputStream(new File(zipName));
			logicalZip = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ArchiveException e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	//Adiciona arquivo ao arquivo zip, caso o retorno seja verdadeiro;
	public boolean addFile(String fileName) {
		try {
			logicalZip.putArchiveEntry(new ZipArchiveEntry(fileName));
			IOUtils.copy(new FileInputStream(new File(fileName)), logicalZip);
			logicalZip.closeArchiveEntry();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Salva arquivo zip no disco rígido, caso o retorno seta verdadeiro;
	public boolean generateZip() {
		try {
			logicalZip.finish();
			zipOutput.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
	}
}