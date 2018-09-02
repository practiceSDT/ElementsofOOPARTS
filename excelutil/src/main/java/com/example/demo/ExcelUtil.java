package com.example.demo;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** エクセルユーティリティ */
public class ExcelUtil implements AutoCloseable{
    private OutputStream outputStream;
    private Workbook book;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ここでは入力からWorkbookと出力となるOutputStreamの設定を行います.
     * 入力のWorkbookは設定必須ですが、出力しない場合はOutputStreamの設定は任意です.
     */
    public static class Builder{
        private Workbook book;
        private OutputStream outputStream;

        //入力エクセルの設定
        public Builder fromFile(String origin) throws EncryptedDocumentException, InvalidFormatException, IOException {
            InputStream is = new FileInputStream(origin);
            this.book = WorkbookFactory.create(is);
            return this;
        }

        public Builder fromClassPath(String origin) throws EncryptedDocumentException, InvalidFormatException, IOException {
            InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream(origin);
            this.book = WorkbookFactory.create(is);
            return this;
        }

        public Builder fromBook(Workbook book) {
            this.book = book;
            return this;
        }

        public Builder fromClassPath(Class<?> clazz, String origin) throws EncryptedDocumentException, InvalidFormatException, IOException {
            InputStream is = clazz.getClassLoader().getResourceAsStream(origin);
            this.book = WorkbookFactory.create(is);
            return this;
        }

        //出力エクセルの設定
        public Builder output(String fileName) throws FileNotFoundException {
            FileOutputStream os = new FileOutputStream(fileName);
            this.outputStream = os;
            return this;
        }
        public Builder output(OutputStream os) {
            this.outputStream = os;
            return this;
        }
        public ExcelUtil build() {
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.book = this.book;
            excelUtil.outputStream = this.outputStream;
            return excelUtil;
        }
    }

    private Sheet getSheetAt(int sheetNo ) {
        Sheet sheet = book.getSheetAt(sheetNo);
        if( null == sheet ){
            throw new RuntimeException("インデックスが`"+sheetNo+"`のシートをお探しのようですが、無いです");
        }
        return sheet;
    }

    private Sheet getSheet(String sheetName ) {
        Sheet sheet = book.getSheet(sheetName);
        if( null == sheet ){
            sheet = book.createSheet(sheetName);
        }
        return sheet;
    }

    /** A1形式セル指定の正規表現 */
    final static private Pattern CELL_PATTERN = Pattern.compile("([A-Z]+)([0-9]+)", Pattern.CASE_INSENSITIVE);

    /**
     * シート情報に対しての操作を行います.
     */
    public class UtilSheet{
        private Sheet poiSheet;
        public UtilSheet( Sheet poiSheet){
            this.poiSheet = poiSheet;
        }

        public String get(String cell) {
            Matcher m = CELL_PATTERN.matcher(cell);
            if( m.find() ) {
                String colNum = m.group(1);
                int rowIndex = Integer.parseInt(m.group(2));
                return get(colNum, rowIndex);
            }else {
                throw new RuntimeException("`"+cell+"`と指定していますが、A1形式の指定じゃないようです");
            }
        }

        public String get(String colNum, int rowIndex) {
            int cellIndex = convToIntFromA1Style(colNum);
            rowIndex--;//1はじまり指定のため

            Iterator<Row> rows = poiSheet.rowIterator();
            int _rowIndex = 0;
            while (rows.hasNext()){
                if( _rowIndex == rowIndex ) {
                    Row row = rows.next();
                    Iterator<Cell> cells = row.cellIterator();
                    int _cellIndex = 0;
                    while (cells.hasNext()){
                        Cell cell = cells.next();
                        if( _cellIndex == cellIndex ) {
                            return cell.getStringCellValue();
                        }
                        _cellIndex++;
                    }
                }
                _rowIndex++;
            }
            return null;
        }

        /**
         * 値を指定された位置へセットします.
         * @param colNum カラム番号
         * @param rowIndex 行番号
         * @param cellValue セルの値
         * @throws IOException
         */
        public UtilSheet put(String cell, Object cellValue) {
            Matcher m = CELL_PATTERN.matcher(cell);
            if( m.find() ) {
                String colNum = m.group(1);
                int rowIndex = Integer.parseInt(m.group(2));
                put(colNum, rowIndex, cellValue);
                return this;
            }else {
                throw new RuntimeException("`"+cell+"`と指定していますが、A1形式の指定じゃないようです");
            }
        }

        public void put(String colNum, int rowIndex, Object cellValue) {
            actualPut( convToIntFromA1Style(colNum), rowIndex-1, cellValue);
        }

        private void actualPut(int colNum, int rowIndex, Object cellValue) {
            Row row = getNewRow(rowIndex);
            Cell cell = getNewCell(colNum, row);
            if( cellValue instanceof Boolean ) {
                cell.setCellValue((Boolean)cellValue);
            }else if( cellValue instanceof Calendar ) {
                cell.setCellValue((Calendar)cellValue);
            }else if( cellValue instanceof java.util.Date ) {
                cell.setCellValue((java.util.Date)cellValue);
            }else if( cellValue instanceof Double ) {
                cell.setCellValue((Double)cellValue);
            }else if( cellValue instanceof RichTextString) {
                cell.setCellValue((RichTextString)cellValue);
            }else if( cellValue instanceof String ) {
                cell.setCellValue((String)cellValue);
            }else {
                cell.setCellValue(""+cellValue);
                Class<?> clazz = cellValue == null ? null : cellValue.getClass();
                logger.warn("`{}`という対応していないオブジェクトをセットしようとしていますが、とりあえず文字列として書き込んでおきました.", clazz );
            }
        }

        private Cell getNewCell(int colNum, Row row) {
            Cell cell = row.getCell(colNum);
            if( null == cell ){
                cell = row.createCell(colNum);
            }
            return cell;
        }

        private Row getNewRow(int rowIndex) {
            Row row = poiSheet.getRow(rowIndex);
            if( null == row ){
                row = poiSheet.createRow(rowIndex);
            }
            return row;
        }

        /**
         * 現在のシートの内容を取得する.
         * @return List<List<String>>形式のシート内容
         */
        public List<List<String>> toListList() {
            List<List<String>> resultListList = new ArrayList<>();

            Iterator<Row> rows = poiSheet.rowIterator();
            while (rows.hasNext()){
                Row row= rows.next();
                Iterator<Cell> cells = row.cellIterator();

                List<String> resultRow = new ArrayList<>();
                while (cells.hasNext()){
                    Cell cell = cells.next();
                    resultRow.add(cell+"");
                }
                resultListList.add(resultRow);
            }
            return resultListList;
        }

        /**
         * 項目名で指定されたリフレクション用フィールドを取得.
         * @param clazz 探す対象のビーンクラス
         * @param label 項目名
         * @return リフレクション用フィールド
         */
        private Field findReflectField(Class<?> clazz, String label) {
            Field[] fields = clazz.getDeclaredFields();
            for( Field f: fields ) {
                f.setAccessible(true);
                Column column = f.getAnnotation(Column.class);
                if( label.equals(column.value()) ){
                    return f;
                }
            }
            return null;
        }

        /**
         * ビーンリストへ変換する.
         * @param clazz ビーンクラス
         * @return ビーンリスト
         * @throws IllegalAccessException
         * @throws InstantiationException
         */
        public <E> List<E> toBeanList(Class<E> clazz) throws InstantiationException, IllegalAccessException {
            List<E> list = new ArrayList<>();
            List<List<String>> listList = toListList();

            ListIterator<List<String>> iter = listList.listIterator();
            List<String> header = iter.next();

            List<Field> fieldList = new ArrayList<>();
            for( String columnName : header ) {
                fieldList.add(findReflectField(clazz, columnName));
            }

            while(iter.hasNext()) {
                List<String> xlsxList = iter.next();
                E bean = clazz.newInstance();
                ListIterator<String> xlsxListItr = xlsxList.listIterator();
                ListIterator<Field> fieldListItr  = fieldList.listIterator();
                while( xlsxListItr.hasNext() && fieldListItr.hasNext() ) {
                    String val = xlsxListItr.next();
                    Field f = fieldListItr.next();
                    f.set(bean, val);
                }
                list.add(bean);
            }
            return list;
        }

        /**
         * BeanListから書き込みを行う.
         * @param beanList ビーンリスト
         * @param <E> 型情報※特に意識しないでよい
         * @return UtilSheet、シートに対して続けて操作が可能.
         * @throws InstantiationException
         * @throws IllegalAccessException
         */
        public <E> UtilSheet setBeanList(List<E> beanList) throws IllegalAccessException {
            Iterator<Row> rows = poiSheet.rowIterator();
            List<String> headers = new ArrayList<>();

            //書き込み位置
            int beginColumnIndex = 0;
            int beginRowIndex = 1;

            //1行目取得
            if (rows.hasNext()) {
                Row row = rows.next();
                beginRowIndex += row.getRowNum();
                Iterator<Cell> cells = row.cellIterator();

                //1列目取得
                if (cells.hasNext()){
                    Cell cell = cells.next();
                    beginColumnIndex = cell.getColumnIndex();
                    headers.add(cell+"");
                }
                while (cells.hasNext()){
                    Cell cell = cells.next();
                    headers.add(cell+"");
                }
            }

            //書き込み
            write(beanList, headers, beginColumnIndex, beginRowIndex);

            return this;
        }


        /**
         * BeanListから書き込みを行う.
         * @param beanList ビーンリスト
         * @param <E> 型情報※特に意識しないでよい
         * @return UtilSheet、シートに対して続けて操作が可能.
         * @throws InstantiationException
         * @throws IllegalAccessException
         */
        public <E> UtilSheet addBeanList(List<E> beanList) throws IllegalAccessException {
            Iterator<Row> rows = poiSheet.rowIterator();
            List<String> headers = new ArrayList<>();

            //書き込み位置
            int beginColumnIndex = 0;
            int beginRowIndex = 1;

            boolean setedBeginColumnIndex = false;
            boolean setedHeaders = false;

            //最後尾取得
            while (rows.hasNext()) {
                Row row = rows.next();
                beginRowIndex = row.getRowNum();
                Iterator<Cell> cells = row.cellIterator();

                //1列目取得
                if (cells.hasNext() && !setedBeginColumnIndex){
                    Cell cell = cells.next();
                    beginColumnIndex = cell.getColumnIndex();
                    headers.add(cell+"");
                    setedBeginColumnIndex = true;
                }

                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    if( !setedHeaders ) {
                        headers.add(cell + "");
                    }
                }
                setedHeaders = true;
            }
            beginRowIndex++;

            //書き込み
            write(beanList, headers, beginColumnIndex, beginRowIndex);
            return this;
        }

        private <E> void write(List<E> beanList, List<String> headers, int beginColumnIndex, int beginRowIndex) throws IllegalAccessException {
            int rowCnt = 0;
            for (E e : beanList) {
                for (String header : headers) {
                    Field[] fields = e.getClass().getDeclaredFields();
                    int columnCnt = 0;
                    for (Field f : fields) {
                        f.setAccessible(true);
                        Column column = f.getAnnotation(Column.class);
                        if (header.equals(column.value())) {
                            Object val = f.get(e);
                            actualPut( beginColumnIndex + columnCnt, beginRowIndex + rowCnt, val);
                        }
                        columnCnt++;
                    }
                }
                rowCnt++;
            }
        }

    }

    public UtilSheet sheet(String sheetName) {
        return new UtilSheet(getSheet(sheetName));
    }

    public UtilSheet sheet(int sheetNo) {
        return new UtilSheet(getSheetAt(sheetNo));
    }

    /**
     * 列ナンバーからcolumn indexを取得する
     * @param a1StyleColumn A!形式の列ナンバー
     * @return poiでそのまま使用可能なcolumn index
     */
    private static int convToIntFromA1Style(String a1StyleColumn) {
        char[] reverseChars = a1StyleColumn.toUpperCase().toCharArray();
        int sinsu = 'Z'-'A'+1;
        int cnt = 0;
        int result = 0;
        for( int i = reverseChars.length-1; i >= 0; i-- ) {
            int a = reverseChars[i] - 'A';
            if( cnt > 0 ) {//エクセルの26進数は一番右の桁以外はAは0ではなく1という意味のため(Zの次はBAではなくAA)
                a++;
            }
            result += a * Math.pow(sinsu, cnt);
            cnt++;
        }
        return result;
    }

    /**
     * A1形式の表記を配列へ変換.
     * @param a1Style A1形式の列と行の指定
     * @return 要素0を列名、要素1を行数としてsplitしたもの
     */
    private static String[] a1StyleToArr(String a1Style) {
        char[] chars = a1Style.toCharArray();

        int startDigitPos = 0;
        for( int i = 0; i < chars.length; i++ ){
            if( '0' <= chars[i] &&  '9' >= chars[i] ){
                startDigitPos = i;
                break;
            }
        }
        String[] ret = {
                a1Style.substring(0, startDigitPos),
                a1Style.substring(startDigitPos, a1Style.length()),
        };
        return ret;
    }

    /**
     * 書き込んでクローズします.
     * あえてワークブックを閉じていません。
     * bookをflyweightパターンにしたい場合などを考慮してこのつくりです。
     * @throws IOException
     */
    @Override
    public void close() throws IOException{
        book.getCreationHelper().createFormulaEvaluator().evaluateAll();
        if( book instanceof XSSFWorkbook) {
            @SuppressWarnings("resource")
            SXSSFWorkbook sxss = new SXSSFWorkbook((XSSFWorkbook)book);
            sxss.write(outputStream);
        }else {
            logger.warn("SXSSFWorkbookを使用していないため、書き込みに時間がかかります.");
            book.write(outputStream);
        }
        outputStream.flush();
        outputStream.close();
    }

    public Workbook book() {
        return book;
    }

    /**
     * Date型から時間のみの形式にする.
     * @param date
     * @return
     */
    public static Double timeOnly(java.util.Date date) {
        if( date == null ) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int min = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Double time = (double)0;
        time += (double)1/24 * hour;
        time += (double)1/24/60 * min;
        return time;
    }

    /**
     * カラムアノテーション.
     * <p>
     * valueにエクセル項目名を設定してください
     * </p>
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Column {
        String value();
    }

}
