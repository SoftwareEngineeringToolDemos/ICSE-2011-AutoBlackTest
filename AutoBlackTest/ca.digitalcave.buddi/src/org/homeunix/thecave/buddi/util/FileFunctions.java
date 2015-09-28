/*
 * Created on Aug 28, 2005 by wyatt
 */
package org.homeunix.thecave.buddi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Common file-based functions for which there is no good support in Java.
 * 
 * @author wyatt
 *
 */
public class FileFunctions {
	
	private FileFunctions(){}
	
	/**
	 * Copies a given file from Source to Destination
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(File source, File dest) throws IOException {
		FileChannel in = null, out = null;
		try {          
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();
			
			in.transferTo( 0, in.size(), out);
			
		}
		catch (FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}
		finally{
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/**
	 * Reads a String from a given InputStream.  Useful for reading text 
	 * files from Jars, etc.  Thanks to JST for helping find and fix a bug 
	 * in this code which would append garbage characters to the output.  
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static String readTextStream(InputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));			
		char[] chars = new char[1024];
		int charsRead;
		while((charsRead = reader.read(chars)) > -1){
			sb.append(String.valueOf(chars).substring(0, charsRead));
		}

		reader.close();
		return sb.toString();		
	}
	
	/**
	 * Simple test to see if a folder is writable.  File.canWrite()
	 * does not work on all Windows folders, as the folder may
	 * be set RO but permissions allow it to be written.
	 * This is in response to Buddi bug #1716654, "Error creating
	 * a new file in My Documents folder".
	 * @param folder
	 * @return
	 */
	public static boolean isFolderWritable(File folder){
		if (!folder.isDirectory())
			folder = folder.getParentFile();
		
		//If the folder is set to writable, it is fine...
		if (folder.canWrite())
			return true;
		
		//... the problem is when the folder is mistakenly set to read only.
		// The test is simple - we try to write to a new file in the given 
		// folder.  If it succeeds, we remove it and return true.  If it
		// fails, we return false.
		File testFile = null;
		final int MAX_CHECKS = 25;
		for (int i = 0; i <= MAX_CHECKS && (testFile == null || testFile.exists()); i++){
			testFile = new File(folder.getAbsolutePath() + File.separator + "." + Math.random());
			if (i == MAX_CHECKS)
				return false; //Could not test file - all tests already failed
		}

		try {
			testFile.createNewFile();
			if (testFile.exists()){
				testFile.delete();
				return true;
			}
		}
		catch (IOException ioe){}

		return false;
	}
	
	/**
	 * Returns a list of all File objects which descend from the given
	 * root.  If root is a file, this list will contain just that file.
	 * It root if a directory, this list will contain that file, plus
	 * all directories and files beneath it. 
	 * @param root
	 * @return
	 */
	public static List<File> getFileTree(File root){
		List<File> files = new LinkedList<File>();
		files.add(root);

		if (root.isDirectory()){
			for (File file : root.listFiles()) {
				files.addAll(getFileTree(file));
			}
		}

		return files;
	}
	
	public static FilenameFilter getDirectoryFilter(final boolean includeHiddenDirectories){
		return new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return (includeHiddenDirectories || !name.startsWith(".")) && new File(dir.getAbsoluteFile() + File.separator + name).isDirectory();
			}
		};
	}
	
	public static FilenameFilter getExtensionFilter(final boolean includeHiddenDirectories, final String... extensions){
		return new FilenameFilter(){
			public boolean accept(File dir, String name) {
				for (String string : extensions) {
					if (name.toLowerCase().endsWith(string.toLowerCase()))
						return (includeHiddenDirectories || !name.startsWith(".")) && new File(dir.getAbsoluteFile() + File.separator + name).isFile();						
				}
				return false;
			}
		};
	}
}
