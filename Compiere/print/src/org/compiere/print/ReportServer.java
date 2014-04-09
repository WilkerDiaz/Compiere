package org.compiere.print;

import java.io.File;

import org.compiere.process.ProcessInfo;
import org.compiere.util.Ctx;

/**
 * Report Server Interface.
 * 
 * @author Saurabh Raval
 */
public interface ReportServer {
	/**
	 *	Report Server returns the file to the client
	 *  @param Ctx context
	 *  @param ProcessInfo process info
	 *  @param IsDirectPrint Direcly print to the printer
	 */
	public File startReport (Ctx ctx, ProcessInfo pi, boolean IsDirectPrint);
}
