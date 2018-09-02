package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.ExcelUtil.Column;
import com.example.demo.ExcelUtil.UtilSheet;

//@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) throws Exception{         
	
		
/*		
		ExcelUtil util = new ExcelUtil.Builder()
                .fromFile("C:\\Users\\water\\Documents\\Book1.xls")//入力のエクセル
                .output( "C:\\Users\\water\\Documents\\aaa.xls")//出力のエクセル。読み込みだけだったら無くてOK。
                .build();
        List<List<String>> lsitList = util.sheet(0).toListList();//左から1シート目の内容を取得
        System.out.println(lsitList);

        util.sheet(0).put( "G", 1, "hello");//左から1シート目のG1セルに書き込み
        util.sheet(1).put("BG", 6, "helloWorld");//左から2シート目のBG6セルに書き込まれる
        util.sheet("new").put("B1", 1);//newという名前のシートに書き込まれる
        util.sheet("new").put("AA2", true);//newという名前のシートに書き込まれる
        int rowCnt = 1;
        util.sheet("new").put("A", rowCnt++, util.timeOnly(new Date(1000*60*60*3)));//Date型から時間のみへ削る関数も用意

		util = new ExcelUtil.Builder()
            .fromClassPath("C:\\Users\\water\\Documents\\Book1.xls")//入力のエクセル※クラスパスから指定
            .build();//今回は読み込みだけなので出力は指定なし。
    //SampleBeanで指定したビーンクラスでエクセルを読み込むよ
    UtilSheet sheet = util.sheet("シート");
    List<SampleBean> beanList = sheet.toBeanList(SampleBean.class);
    System.out.println(beanList);//読み込んだ値を表示

    List<SampleBean> beanList2 = new ArrayList<>();
    SampleBean bean1 = new SampleBean();
    bean1.koumoku1 = "aaa";
    bean1.koumoku2 = "bbb";
    bean1.koumoku3 = "ccc";
    beanList2.add(bean1);

    SampleBean bean2 = new SampleBean();
    bean2.koumoku1 = "aa2";
    bean2.koumoku2 = "bb2";
    bean2.koumoku3 = "cc2";
    beanList2.add(bean2);

    sheet.addBeanList(beanList2);//追記書き込みもできるよ.setBeanListを使うと、上書きします。
    util.close();

*/	
	
	}


public static class SampleBean{
    @Column("こうもく１")//エクセルのヘッダーの項目を指定する
    String koumoku1;

    @Column("こうもく２")
    String koumoku2;

    @Column("こうもく３")
    String koumoku3;

    @Override
    public String toString() {
        return "SampleBean [koumoku1=" + koumoku1 + ", koumoku2=" + koumoku2 + ", koumoku3=" + koumoku3 + "]";
    	}
	}
}
