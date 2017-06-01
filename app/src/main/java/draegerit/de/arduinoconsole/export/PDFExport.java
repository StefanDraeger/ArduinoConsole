package draegerit.de.arduinoconsole.export;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import draegerit.de.arduinoconsole.R;

public class PDFExport extends AbstractExport {

    private static final String PDF_SUFFIX = ".pdf";

    private static final String EMPTY = "";

    private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD | Font.UNDERLINE, BaseColor.GRAY);
    private Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

    private Document document;

    private Context context;
    private Object logoImage;

    @Override
    public void doExport(Context ctx) {
        this.context = ctx;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        setExportFilename(path.getAbsolutePath() + File.pathSeparator + String.valueOf(System.currentTimeMillis()) + PDF_SUFFIX);
        try {
            initDocument();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        startExportIntent(new File(getExportFilename()), ctx);
    }

    private void initDocument() throws IOException, DocumentException {
        this.document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(getExportFilename()));
        this.document.open();
        addMetaData();
        addFrontPage();
        this.document.close();
    }

    private void addFrontPage() throws DocumentException, IOException {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidths(new float[]{1f});

        PdfPCell cell;

        //headerTable.addCell(getTblHeaderTitleCell(getLogoImage(), this.context.getResources().getString(R.string.pdfExportTitle));

        headerTable.addCell(getTblHeaderTitleCell(this.context.getResources().getString(R.string.pdfExportTitle)));
        headerTable.addCell(getTblHeaderSubTitleCell(this.context.getResources().getString(R.string.pdfExportSubTitle)));
        headerTable.addCell(getTblHeaderCell("      "));

        Image img = Image.getInstance(getChart());
        img.scalePercent(45f);
        cell = new PdfPCell(img);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        headerTable.addCell(cell);

        this.document.add(headerTable);
        this.document.newPage();

        PdfPTable valueTable = new PdfPTable(3);
        valueTable.setWidths(new float[]{1f, 1f, 1f});
        addValueTable(valueTable);
        this.document.add(valueTable);
        this.document.newPage();

        PdfPTable parameterTable = new PdfPTable(2);
        parameterTable.setWidths(new float[]{1f, 1f});
        addParameterTable(parameterTable);
        this.document.add(parameterTable);

    }

    private PdfPCell getTblHeaderTitleCell(String value) {
        return getTblHeaderCustomCell(value, this.titleFont);
    }

    private PdfPCell getTblHeaderSubTitleCell(String value) {
        return getTblHeaderCustomCell(value, this.catFont);
    }

    private PdfPCell getTblHeaderCustomCell(String value, Font font) {
        Phrase phrase = new Phrase(value, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        return cell;
    }


    private void addParameterTable(PdfPTable parameterTable) {
        parameterTable.addCell(getTblHeaderCell(""));
        parameterTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportParameterTableColDescription)));
    }

    private void addValueTable(PdfPTable valueTable) {
        valueTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportValueTableColID)));
        valueTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportValueTableColTimestamp)));
        valueTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportValueTableColValue)));
    }

    private PdfPCell getTblHeaderCell(String value) {
        Phrase phrase = new Phrase(value, this.smallBold);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        return cell;
    }

    private byte[] getChart() throws IOException {
        File chart = getChartImage();
        if (chart.exists()) {
            return FileUtils.readFileToByteArray(chart);
        } else {
            return null;
        }
    }

    private void addMetaData() {
        this.document.addTitle(this.context.getResources().getString(R.string.pdfMetaDataTitle));
        this.document.addSubject(this.context.getResources().getString(R.string.pdfMetaDataSubject));
        this.document.addKeywords(EMPTY);
        this.document.addAuthor(this.context.getResources().getString(R.string.pdfMetaDataAuthor));
        this.document.addCreator(this.context.getResources().getString(R.string.pdfMetaDataCreator));
    }

    @Override
    public String getMimeType() {
        return MIMETYPE_PDF;
    }

    public Object getLogoImage() {
        return logoImage;
    }
}
