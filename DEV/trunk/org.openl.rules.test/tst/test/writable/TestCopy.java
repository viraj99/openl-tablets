/**
 * Created Feb 15, 2007
 */
package test.writable;

import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openl.rules.lang.xls.XlsSheetSourceCodeModule;
import org.openl.rules.lang.xls.XlsWorkbookSourceCodeModule;
import org.openl.rules.table.GridSplitter;
import org.openl.rules.table.IGridTable;
import org.openl.rules.table.IWritableGrid;
import org.openl.rules.table.xls.XlsSheetGridModel;
import org.openl.syntax.impl.FileSourceCodeModule;

/**
 * @author snshor
 *
 */
public class TestCopy extends TestCase
{
	
	public void testInsert() throws Exception
	{
		String testXls = "tst/test/writable/TestCopy.xls";

		FileSourceCodeModule source = new FileSourceCodeModule(testXls, null);
		
		XlsWorkbookSourceCodeModule wbSrc = new XlsWorkbookSourceCodeModule(source);

		Workbook wb = wbSrc.getWorkbook();
		
		int nsheets = wb.getNumberOfSheets();

		for (int i = 0; i < nsheets; i++)
		{
			Sheet sheet = wb.getSheetAt(i);
			String name = wb.getSheetName(i);
			XlsSheetSourceCodeModule sheetSrc = new XlsSheetSourceCodeModule(sheet, name, wbSrc);
			
			XlsSheetGridModel xsGrid =
				new XlsSheetGridModel(sheetSrc);

			IGridTable[] tables = new GridSplitter(xsGrid).split();
			
			for (int j = 0; j < tables.length; j++)
			{
				IWritableGrid.Tool.insertColumns(1, 1, tables[j].getRegion(), (IWritableGrid)tables[j].getGrid());
			}
		}	
		
    // Write the output to a file
    FileOutputStream fileOut = new FileOutputStream("workbook.xls");
    wb.write(fileOut);
    fileOut.close();
		
	}	

	
	
	


	public static void main(String[] args) throws Exception
	{
		new TestCopy().testInsert();
	}
	

}
