/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.util.FileFunctions;

/**
 * A small class which defines an HTML page.  There are two parts to this: the HTML text,
 * and a map of images.  The HTML text is constructed manually (optionally using HTMLHelper
 * to create the standard report look and layout).  If you are going to add images, include
 * them in the HTML text normally (with no path - just the image name), and map that name
 * to a buffered image.  The buffered images will be stored to disk, to the file name
 * given in the map.
 * 
 * For an example on how to do this, please refer to some of the built in reports in 
 * the Buddi SVN repository.
 * 
 * @author wyatt
 *
 */
public class HtmlPage {
	private String html;
	private Map<String, BufferedImage> images;

	/**
	 * Creates a new HtmlPage, with the given text and images.
	 * @param html
	 * @param images
	 */
	public HtmlPage(String html, Map<String, BufferedImage> images) {
		this.html = html;
		this.images = images;
	}
	public String getHtml() {
		return html;
	}
	public Map<String, BufferedImage> getImages() {
		return images;
	}
	
	/**
	 * Saves the HTML and associated images to disk.  Returns the
	 * file object of the associated HTML index on success, or null
	 * on failure. 
	 */
	public File createHTML(String name){
		final int countMax = 100;

		File dataFolder = Buddi.getReportsFolder();
		File htmlFolder = null;
		int counter = 0;

		//Do some sanity checks, logging results
		if (dataFolder == null)
			Logger.getLogger(this.getClass().getName()).warning("Data folder is null (HtmlPage.createHTML()).");
		if (html == null)
			Logger.getLogger(this.getClass().getName()).warning("HTML is null (HtmlPage.createHTML()).");
		if (name == null)
			Logger.getLogger(this.getClass().getName()).warning("Name is null (HtmlPage.createHTML()).");

		//Get a unique folder name which has not yet been used.
		while (counter < countMax && (htmlFolder == null || htmlFolder.exists())){
			htmlFolder = new File(dataFolder.getAbsolutePath() + File.separator + name + (counter > 0 ? "_" + counter : ""));
			counter++;
		}
		//We could not create a folder, after 1000 tries.  Exit.
		if (counter == countMax){
			Logger.getLogger(this.getClass().getName()).warning("Could not find a folder to use in the data folder, after 1000 tries.  Cancelling HTML export.  Please verify if you really have this many folders in your data drectory, and if so, clean up a bit.");

			return null;
		}

		if (!FileFunctions.isFolderWritable(htmlFolder.getParentFile())){
			Logger.getLogger(this.getClass().getName()).warning("Cannot write to '" + htmlFolder.getParentFile().getAbsolutePath() + "'.  This may cause problems shortly...");
		}

		//Try to create a folder.  If this doesn't work, we return with an error.
		if (!htmlFolder.mkdirs()){
			Logger.getLogger(this.getClass().getName()).warning("Could not create folder '" + htmlFolder.getAbsolutePath() + "'.  Please check that you have write permission on this folder and its sub folders.");
			return null;
		}

		File htmlFile = new File(htmlFolder.getAbsolutePath() + File.separator + "index.html");
		htmlFolder.deleteOnExit();
		htmlFile.deleteOnExit();
		try{
			OutputStreamWriter out = new OutputStreamWriter(
					new BufferedOutputStream(
							new FileOutputStream(htmlFile)), "UTF-8");
			out.write(this.getHtml());
			out.close();
//			BufferedWriter bw = new BufferedWriter(
//			new FileWriter(htmlFile));
//			bw.write(html.getHtml());
//			bw.close();
		}
		catch (IOException ioe){
			Logger.getLogger(this.getClass().getName()).warning("Could not write HTML file.");
			return null;
		}

		if (this.getImages() != null){
			for (String imgName : this.getImages().keySet()) {
				if (imgName != null && this.getImages().get(imgName) != null){
					File f = new File(htmlFolder.getAbsolutePath() + File.separator + imgName);
					try {
						ImageIO.write(this.getImages().get(imgName), "png", f);
						f.deleteOnExit();
					}
					catch (IOException ioe){
						Logger.getLogger(this.getClass().getName()).warning("Error writing file " + f.getAbsolutePath());
					}
				}
			}
		}

		return htmlFile;
	}
}