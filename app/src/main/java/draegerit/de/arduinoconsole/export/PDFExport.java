package draegerit.de.arduinoconsole.export;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.awt.geom.CubicCurve2D;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import draegerit.de.arduinoconsole.Model;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.Message;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_LEFT;

public class PDFExport extends AbstractExport {

    private static final String PDF_SUFFIX = ".pdf";

    private static final String EMPTY = "";

    private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
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

        headerTable.addCell(getTblHeaderTitleCell(this.context.getResources().getString(R.string.pdfExportTitle)));
        headerTable.addCell(getTblHeaderSubTitleCell(this.context.getResources().getString(R.string.pdfExportSubTitle)));
        headerTable.addCell(getTblHeaderSubTitleCell("     "));

        Image img = Image.getInstance(getChart());
        img.scalePercent(45f);
        cell = new PdfPCell(img);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        headerTable.addCell(cell);

        Model model = Model.getInstance();

        List<Message> messages = new ArrayList<>(model.getMessages());
        Message firstMsg = null;
        Message lastMsg = null;

        double maxValue = 0d;
        double minValue = 0d;
        double averagePeak = 0d;

        for (Message msg : messages) {
            if (firstMsg == null && msg.getType() == Message.Type.FROM) {
                firstMsg = msg;
            } else if (lastMsg == null && msg.getType() == Message.Type.FROM) {
                lastMsg = msg;
            }

            try {
                minValue = Math.min(minValue, Double.parseDouble(msg.getValue()));
            } catch (NumberFormatException ex) {
                Log.e(TAG, ex.getMessage());
            }

            try {
                maxValue = Math.max(maxValue, Double.parseDouble(msg.getValue()));
            } catch (NumberFormatException ex) {
                Log.e(TAG, ex.getMessage());
            }

            try {
                averagePeak += Double.parseDouble(msg.getValue());
            } catch (NumberFormatException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }

        averagePeak /= messages.size();
        averagePeak = Math.round(averagePeak);

        if (firstMsg != null && lastMsg != null) {
            headerTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportChartParameter), ALIGN_LEFT, PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportChartFrom, formatTimestamp(firstMsg.getTimestamp())), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportChartTo, formatTimestamp(lastMsg.getTimestamp())), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportChartMinValue, String.valueOf(minValue)), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportChartMaxValue, String.valueOf(maxValue)), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportChartAveragePeak, String.valueOf(averagePeak)), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblHeaderSubTitleCell("     "));
        }

        if (model.getPort() != null) {
            headerTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportConnectionParameter), ALIGN_LEFT, PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportDevice, model.getPort().getDriver().getDevice().getDeviceName()), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportBaudrate, String.valueOf(model.getBaudrate())), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportDatabits, String.valueOf(model.getDatabits())), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportStopbits, String.valueOf(model.getStopbits())), PdfPCell.NO_BORDER));
            headerTable.addCell(getTblCell(this.context.getResources().getString(R.string.pdfExportParity, String.valueOf(model.getParity())), PdfPCell.NO_BORDER));
        }

        this.document.add(headerTable);
        this.document.newPage();

        PdfPTable valueTable = new PdfPTable(3);
        valueTable.setWidths(new float[]{1f, 1f, 1f});
        valueTable.setHeaderRows(1);
        addValueTable(valueTable, messages);
        this.document.add(valueTable);
    }

    private String formatTimestamp(long timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(new Date(timestamp));
    }

    private PdfPCell getTblCell(String value, int border) {
        Phrase phrase = new Phrase(value, this.normal);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(border);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        return cell;
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
        parameterTable.addCell(getTblHeaderCell("", ALIGN_LEFT, PdfPCell.NO_BORDER));
        parameterTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportParameterTableColDescription), ALIGN_LEFT, PdfPCell.NO_BORDER));
    }

    private void addValueTable(PdfPTable valueTable, List<Message> messages) {
        valueTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportValueTableColID), ALIGN_CENTER, PdfPCell.RECTANGLE));
        valueTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportValueTableColTimestamp), ALIGN_CENTER, PdfPCell.RECTANGLE));
        valueTable.addCell(getTblHeaderCell(this.context.getResources().getString(R.string.pdfExportValueTableColValue), ALIGN_CENTER, PdfPCell.RECTANGLE));

        int counter = 1;
        for (Message msg : messages) {
            valueTable.addCell(getTblCell(String.valueOf(counter++), PdfPCell.RECTANGLE));
            valueTable.addCell(getTblCell(formatTimestamp(msg.getTimestamp()), PdfPCell.RECTANGLE));
            valueTable.addCell(getTblCell(String.valueOf(msg.getValue()), PdfPCell.RECTANGLE));
        }

    }

    private PdfPCell getTblHeaderCell(String value, int align, int border) {
        Phrase phrase = new Phrase(value, this.smallBold);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(border);
        cell.setHorizontalAlignment(align);
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
