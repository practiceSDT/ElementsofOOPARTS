package getdata.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.subtlelib.poi.api.workbook.WorkbookContext;
import org.subtlelib.poi.impl.workbook.WorkbookContextFactory;

import com.github.crab2died.ExcelUtils;
import com.google.common.io.Files;

public class InterFaceListsTest {
	
	@Test
	public void testInterFaceLists() {
		
        String path = "src/test/resources/Book1.xlsx";
        try {
            
            // 1)
            // 不基于注解,将Excel内容读至List<List<String>>对象内
            List<List<String>> lists = ExcelUtils.getInstance().readExcel2List(path, 0);
            System.out.println("String Lists：");
            
            lists.forEach(System.out::println);

            // 2)
            // 基于注解,将Excel内容读至List<Student2>对象内
            // 验证读取转换函数Student2ExpelConverter 
            // 注解 `@ExcelField(title = "是否开除", order = 5, readConverter =  Student2ExpelConverter.class)`
            List<InterFaceLists> interFaceLists = 
            		ExcelUtils.getInstance().readExcel2Objects(path, InterFaceLists.class, 0, 0);
            System.out.println("Object Lists：");
            
            interFaceLists.forEach(System.out::println);
            
            SimpleReportView view = new SimpleReportView(WorkbookContextFactory.useXlsx());
            WorkbookContext workbook = view.render(interFaceLists);
            Files.write(workbook.toNativeBytes(), new File("./simple_example.xls"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

