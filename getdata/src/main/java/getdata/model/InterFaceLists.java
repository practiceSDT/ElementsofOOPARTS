package getdata.model;

import java.util.ArrayList;
import java.util.List;

import com.github.crab2died.annotation.ExcelField;

import getdata.converter.IncludeCammaCell2List;
import getdata.converter.IncludeReturnCell2List;
import lombok.Data;

@Data
public class InterFaceLists {
	
    @ExcelField(title = "IF名", order = 3)
    private String name;

    @ExcelField(title = "改行", order = 1, readConverter = IncludeReturnCell2List.class)
    private ArrayList<String> kaigyo;

    @ExcelField(title = "カンマ", order = 2, readConverter = IncludeCammaCell2List.class)
    private ArrayList<String> camma;


}
