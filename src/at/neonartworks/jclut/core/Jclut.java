package at.neonartworks.jclut.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <h1>JCLUT</h1>
 * 
 * <p>
 * LUT means Look-Up-Table. A 3D LUT file consists as the name implies of a 3D
 * table/array (R, G, B). Jclut is a project to create 3D C-LUT (.cube) files
 * from a hald image.
 * <hr>
 * <h2>Public Methods</h2>
 * <ul>
 * <li>public void convert(BufferedImage in, String output) throws
 * IOException</li>
 * </ul>
 * <hr>
 * <p2>Basic How To:
 * <ul>
 * <li>1.) Download a hald image from the Internet.</li>
 * <li>2.) Apply any color-correction on the image.</li>
 * <li>3.) Save the resulting image as a <b>.png</b> file.</li>
 * <li>4.) Use the previously saved image with this library.</li>
 * <li>4.) You now have a LUT (Look Up Table) with those exact corrections.</li>
 * </ul>
 * </p2>
 * 
 * </p>
 * 
 * @author Florian Wagner
 * @version 0.14
 *
 */
public class Jclut {

	/**
	 * Constructor
	 */
	public Jclut() {
	}

	/**
	 * <h1>public void convert({@link BufferedImage}, {@link String}) throws
	 * IOException</h1>
	 * <hr>
	 * <p>
	 * This method computes the 3D-Lut (.cube) file from the input file given
	 * with the first parameter. The resulting .cube file will be stored at the
	 * <b>path </b> given with the second parameter. If the file could not be
	 * saved, or if the path does not exist, an IOException will be thrown.
	 * 
	 * </p>
	 * 
	 * @param in
	 *            This is the image you want to generate the LUT from.
	 * @param output
	 *            It defines the <b>file-path</b> of the resulting .cube file.
	 * 
	 * @throws IOException
	 */
	public void convert(BufferedImage in, String output) throws IOException {

		File outputFile = new File(output);
		FileWriter fw = new FileWriter(outputFile, true); // the true will
															// append the new
															// data, not
															// override it
		// init used variables
		int nx = in.getWidth(); // get width of input image
		int ny = in.getHeight(); // geth height input image
		int rgb;
		double r, g, b;
		if (nx != ny) {
			System.err.println("Image width and height MUST BE the same length!");
		}

		int steps = (int) Math.round(Math.pow(nx, 0.333333334)); // steps is
																	// derived
																	// from the
																	// width (or
																	// height)
																	// of the
																	// image.

		double sstep = steps * steps; // this is the grid size, the resulting
										// data point size will be 'ssteps^3';

		// writing file header
		fw.append("#*------------------------------------------------*" + System.lineSeparator());
		fw.append("#Created with: " + "JClut-Hald2Lut Generator" + System.lineSeparator());
		fw.append("#Copyrigh: " + "NeonArtworks - www.neonartworks.at" + System.lineSeparator());
		fw.append("#*------------------------------------------------*" + System.lineSeparator());
		fw.append(System.lineSeparator());

		// THESE header informations MUST be in the .cube file.
		fw.append("#LUT size" + System.lineSeparator());
		fw.append("LUT_3D_SIZE " + ((int) sstep) + System.lineSeparator());

		// compute the lut data points, and write them into a table
		// first iterate through all pixels in the image
		// Note: The first for-loop starts with the y axis.
		for (int y = 0; y < ny; y++) {
			for (int x = 0; x < nx; x++) {
				rgb = in.getRGB(x, y); // returns the rgb-value of the current
										// pixel.
				
				// the following computations return values between 0 and 255.
				
				b = (rgb >> 0) & 0xFF; // computes the blue value
				g = (rgb >> 8) & 0xFF;// computes the green value
				r = (rgb >> 16) & 0xFF;// computes the red value

				// since cube files use values between 0 and 1 we divide the
				// resulting numbers by 255.
				fw.append(round(r / 255d) + " " + round(g / 255d) + " " + round(b / 255d) + System.lineSeparator());
			}
		}
		fw.close(); // close the filewrite when done
	}

	/**
	 * Simple method to round the values to a maximum of 6 comma digits.
	 * 
	 * @param value
	 *            is the original double precision value
	 * @return the rounded double value
	 */
	private static double round(double value) {
		double round;
		round = (long) (value * (1e6));
		round = round / 1e6;
		return round;
	}
}
