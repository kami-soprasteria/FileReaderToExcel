import Entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by Kami Hassanzadeh on 2019-02-18.
 */
public class Run extends FilterValue {

    private static ArrayList<String> list = new ArrayList<>();
    private static final String SAVE_FILE_NAME = "C:\\Users\\khassanzadeh\\Desktop\\Statisk_Logfile_230.xlsx";
    private static final String SOURCE_FILES = "D:\\Project\\SRS (Trafikförvaltning)\\LOG 229-230\\log_230";
    private static String[] ExcelColumns = {"Användare - Antal inloggning som mer än 40 gånger per dag", "Filnamn"};
    private static Sheet sheet;
    private static Workbook workbook = new XSSFWorkbook();
    private static User user = new User();

    public static void main(String args[]) throws Exception {

        File directory = new File(SOURCE_FILES);
        File[] listOfFiles = directory.listFiles();
        writeToExcel();
        int rowNum = 1;

        for (File file : listOfFiles) {
            list.clear();
            String fileNames = file.getName();
            File fileDir = new File(SOURCE_FILES + "\\" + fileNames);

            readLine(fileDir);
            filterResult();

            if (!user.getUserMap().isEmpty()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUserMap().toString());
                row.createCell(1).setCellValue(user.getFileName());

                for (int i = 0; i < ExcelColumns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
        }
        FileOutputStream fileOut = new FileOutputStream(SAVE_FILE_NAME);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private static void writeToExcel() {

        sheet = workbook.createSheet("User");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setWrapText(true);
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < ExcelColumns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(ExcelColumns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    public static void readLine(File fileDir) throws IOException {
        String line;

        Charset inputCharset = Charset.forName("ISO-8859-1");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileDir), inputCharset));

        while ((line = bufferedReader.readLine()) != null) {
            int indexFound = line.indexOf("Provar");

            if (indexFound > -1) {
                int lastWord = line.lastIndexOf(' ');
                String username = line.substring(lastWord + 1);
                list.add(username);
            }
        }
        bufferedReader.close();
        user.setFileName(fileDir.getName());
        user.setUserList(list);
    }

    public static Map<String, Long> filterResult() {
        Map<String, Long> result;
        Map<String, Long> counter = user.getUserList().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> filteredMap = filterByValue(counter, x -> (x.longValue() > 40));
        result = sortByValue(filteredMap);
        user.setUserMap(result);
        return result;
    }
}
