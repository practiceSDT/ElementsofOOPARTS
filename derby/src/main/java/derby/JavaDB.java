package derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JavaDB {
        
        /** データベース接続用 */
        Connection con;
        
        /** メイン */
        public static void main(String[] args) {
                System.out.println("javaDB test program...");
                JavaDB obj = new JavaDB();
                
                try {
                        //DBに接続
                        obj.connectDB();
                        //データを表示する
                        obj.showTable();
                }catch(SQLException e) {
                        e.printStackTrace();
                }catch(ClassNotFoundException e) {
                        e.printStackTrace();
                }
                finally {
                        //DBから接続解除
                        obj.disconnectDB();
                }
                
                System.out.println("finish...");
        }
        
        /** データベースに接続 */
        private void connectDB() throws SQLException,ClassNotFoundException {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                System.out.println("ドライバ読み込みＯＫ");
                
                Properties prop = new Properties();
                prop.put("create" ,"true");             //指定したデータベースが存在しない場合、作成する
                //ユーザＩＤとパスワードは設定が施していある場合、指定する。
                //prop.put("user" ,"admin");            //DBに接続する際のユーザ名を指定
                //prop.put("password" ,"admin");        //パスワードを指定
                
                con = DriverManager.getConnection("jdbc:derby:testDB1", prop);
                System.out.println("接続ＯＫ");
        }
        
        /** データを表示する */
        private void showTable() throws SQLException {
                Statement st = con.createStatement();
                //ＤＢの新規作成時はテーブルがないので作成し、データの挿入を行う。
                try {
                        //st.executeUpdate("CREATE TABLE MessageTBL(id integer,message varchar(100))");
                        st.executeUpdate("INSERT INTO MessageTBL VALUES(1 ,'てすとですーーーーーーーｗｗ')");
                } catch(SQLException e){}
                
                //データを取得し、表示する。
                ResultSet res = st.executeQuery("SELECT * FROM MessageTBL");
                while(res.next()) {
                        System.out.println("id : "+res.getInt("id")+" / Message : "+res.getString("message"));
                }
                
                //それとなくリソース開放。
                try{    res.close();    }catch(SQLException e){}
                try{    st.close();     }catch(SQLException e){}
        }
        
        /** データベースから接続を解除 */
        private void disconnectDB(){
                if( con != null ) {
                        //普通の接続のクローズ処理
                        try {   con.close();    }
                        catch(SQLException e){  e.printStackTrace();    }
                        con = null;
                        
                        //DB全体を停止する処理
                        try {
                                DriverManager.getConnection("jdbc:derby:;shutdown=true");
                        } catch(SQLException e) {
                                //しっかりと終了できた場合、XJ015という例外が発せられるのでそれを確認する。
                                if( e.getSQLState().equals("XJ015") ) {
                                        System.out.println("DBのシャットダウンが完了しました。");
                                } else {
                                        //XJ015以外は普通の例外なので、とりあえずprintStacTraceする。
                                        e.printStackTrace();
                                }
                        }
                }
                System.out.println("開放処理終了");
        }

}