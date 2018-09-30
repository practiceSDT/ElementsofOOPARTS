package getdata.model;

import java.util.Collection;

import org.subtlelib.poi.api.sheet.SheetContext;
import org.subtlelib.poi.api.totals.ColumnTotalsDataRange;
import org.subtlelib.poi.api.totals.Formula;
import org.subtlelib.poi.api.workbook.WorkbookContext;
import org.subtlelib.poi.impl.workbook.WorkbookContextFactory;

public class SimpleReportView {
    private final WorkbookContextFactory ctxFactory;

    public SimpleReportView(WorkbookContextFactory ctxFactory) {
        this.ctxFactory = ctxFactory;
    }

    public WorkbookContext render(Collection<InterFaceLists> interFaceLists) {
        WorkbookContext workbookCtx = ctxFactory.createWorkbook();
        SheetContext sheetCtx = workbookCtx.createSheet("Payments");

        // heading
        sheetCtx
            .nextRow()
                .skipCell()
                .header("Amount")
                .header("Currency")
                .header("Beneficiary").setColumnWidth(25)
                .header("Payee bank").setColumnWidth(35);

        // data
        interFaceLists.forEach(i -> 
        sheetCtx.nextRow()
        .text(i.getName())
        		);

        return workbookCtx;
    }
}