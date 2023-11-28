import java.sql.SQLException;

import utils.JDBCUtil;

public class JDBCTEST {
    
    public static void main(String[] args) {
        try{
            JDBCUtil.getConnection();
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
