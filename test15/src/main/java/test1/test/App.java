import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{


    static String url = "jdbc:mysql://rm-j6c2sl8r7ky0wica8yo.mysql.rds.aliyuncs.com:3306/hurong_web";
    static String username = "cpct_yf_rd";
    static String password ="ac3)u1L*Kh9YoOcm";


    public static void main( String[] args ) {

        System.out.print("请输入手机号 18487278206,17749995146 , ','号分隔 ,输入 quit 退出：");
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextLine()){
            String inputString = scanner.nextLine();
            if(inputString==null||"".equals(inputString)||"quit".equals(inputString)){
                break;
            }
            String mobiles[] = inputString.split(",");
            for (int j = 0; j < mobiles.length; j++) {
                String mobile = mobiles[j];

                List<String> coinCodesList  =new ArrayList<>();
                String s2 = accountInfo(mobile, coinCodesList);
                String[] split = s2.split("=");
                Long customId =Long.parseLong(split[0].trim());
                String trueName=split[1].trim();
                System.out.println(coinCodesList);
                for (int i = 0; i < coinCodesList.size(); i++) {
                    String s = coinCodesList.get(i).trim();
                    System.out.println("正在导出==>"+ s + " " +trueName + " " +customId);
                    hotExcel(s,customId,trueName);
                    System.out.println("hotExcel导出完成");
                    coldExcel(s,customId,trueName);
                    System.out.println("coldExcel导出完成");
                    System.out.println(customId);
                }
            }
            //System.out.println(inputString);
        }


       /* ArrayList<String> coinCodes = new ArrayList<>();
        coinCodes.add("USDT  ");
        coinCodes.add("BTC   ");
        coinCodes.add("ETH   ");
        coinCodes.add("OMG   ");
        coinCodes.add("ZEC   ");
        coinCodes.add("DASH  ");
        coinCodes.add("LTC   ");
        coinCodes.add("BCH   ");
        coinCodes.add("CPCT  ");
        coinCodes.add("TYT   ");
        coinCodes.add("MOAC  ");
        coinCodes.add("LEND  ");
        coinCodes.add("GOT   ");
        coinCodes.add("NTK   ");
        coinCodes.add("FUEL  ");
        coinCodes.add("CDT   ");
        coinCodes.add("SNT   ");
        coinCodes.add("HT    ");
        coinCodes.add("VOLLAR ");*/

       /* ArrayList<Integer> customIds = new ArrayList<>();
       *//* customIds.add(18);
        customIds.add(2);*//*
        customIds.add(202256);
        customIds.add(202273);
        customIds.add(202294);
        customIds.add(1360);
        customIds.add(34183);
        customIds.add(200539);

        ArrayList<String> customNames = new ArrayList<>();
        *//*customNames.add("张三");
        customNames.add("李四");*//*
        customNames.add("张安其");
        customNames.add("张陆郎");
        customNames.add("熊孝见");
        customNames.add("谭其燕");
        customNames.add("刘瑞宝");
        customNames.add("张洋");*/

    }

    static void hotExcel(String coinCode, Long customerId,String userName){
        List<String> list  =new ArrayList<>();

        String sql = "-- hot\n" +
                "select '"+coinCode+" 可用账户',-- c.id,\n" +
                "c.remark '操作类型',c.transactionmoney '交易金额',balancemoney '交易前可用余额',\n" +
                "CASE WHEN c.`recordtype`=1 then c.balancemoney+c.transactionmoney WHEN c.`recordtype`=2 THEN c.balancemoney-c.transactionmoney   ELSE 'null' END \n" +
                "AS  '交易后可用余额' ,\n" +
                "c.created '交易时间',c.transactionNum '交易单号'-- ,c.customerId '客户ID'\n" +
                ",c.coinCode '账户流水币种',\n" +
                "CASE WHEN e.type=1 \n" +
                "then '买入' \n" +
                "WHEN e.type=2 \n" +
                "THEN '卖出'   \n" +
                "ELSE '' END '方向'\n" +
                ",\n" +
                "e.coinCode '交易币种',e.fixPriceCoinCode '定价币种' ,\n" +
                "e.entrustPrice 'price',e.status '委托表状态'\n" +
                "-- ,e.gangShengOrderNo, i.mobilePhone '客户手机号',recordType,\"-->\",e.`status` as 'ex_entrust_status',\n" +
                "-- e.entrustNum,e.created,e.type,e.entrustWay,e.coinCode,e.fixPriceCoinCode,e.`status`,e.surplusEntrustCount,e.entrustPrice,e.entrustCount,e.entrustSum,e.transactionFee,e.processedPrice,e.*\n" +
                "-- ex_entrust 0未成交　1部分成交　2已完成　 3部分成交已撤销 4已撤销   7队列中 \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "from ex_dm_hot_account_record c \n" +
                "left join app_person_info i on c.customerId=i.customerId\n" +
                "left join ex_entrust e on c.TRANSACTIONnum = e.entrustNum\n" +
                "where 1=1 -- c.customerId=186\n" +
                "   and c.customerId="+customerId+"          \n" +
                "-- i.mobilePhone=15396370089\n" +
                "\t -- and e.`status` !=4 and c.`recordtype`=2\n" +
                "       and  c.coincode = '"+coinCode+"' \n" +
                "-- and remark like '撤销%' and (e.`status`=0 or e.`status`=1 or e.`status`=3 )\n" +
                "-- and transactionMoney = 280.77422642\n" +
                " -- and c.balanceMoney = 2000.2225355554\n" +
                "   -- and c.created >= '2019-03-27'\n" +
                "  -- and (c.balancemoney-c.transactionmoney <0 and c.`recordtype`=2 )\n" +
                "-- transactionmoney=3517\n" +
                "-- and c.`recordtype`=2\n" +
                " -- and balancemoney <0\n" +
                " -- and transactionNum like '%190526195956001618%'\n" +
                "-- GROUP BY c.coinCode\n" +
                "  order by c.id desc -- limit 0,20000;\n" +
                "\n" +
                "\n" +
                "-- `status` smallint(2) NOT NULL DEFAULT '1' COMMENT '0未成交　1部分成交　2已完成　 3部分成交已撤销 4已撤销   7队列中 ',\n" +
                "\n" +
                "\n" +
                "-- select * from ex_entrust where entrustNum='190112175239001726';\n" +
                "\n";
        Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {


            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            List<Map> coinList =null;
            //新建excel报表
            HSSFWorkbook excel = new HSSFWorkbook();
            //添加一个sheet，
            HSSFSheet hssfSheet = excel.createSheet("数据表");
            //往excel表格创建一行，excel的行号是从0开始的
            HSSFRow hssfRow = hssfSheet.createRow(0);
            //第一行创建第一个单元格 GOT 可用账户	操作类型	交易金额	交易前可用余额	交易后可用余额
            // 交易时间	交易单号	账户流水币种	方向	交易币种	定价币种
            int z=0;
            hssfRow.createCell(z).setCellValue(coinCode+"可用账户");z++;
            hssfRow.createCell(z).setCellValue("操作类型");z++;
            hssfRow.createCell(z).setCellValue("交易金额");z++;
            hssfRow.createCell(z).setCellValue("交易前可用余额");z++;
            hssfRow.createCell(z).setCellValue("交易后可用余额");z++;
            hssfRow.createCell(z).setCellValue("交易时间");z++;
            hssfRow.createCell(z).setCellValue("交易单号");z++;
            hssfRow.createCell(z).setCellValue("账户流水币种");z++;
            hssfRow.createCell(z).setCellValue("方向");z++;
            hssfRow.createCell(z).setCellValue("交易币种");z++;
            hssfRow.createCell(z).setCellValue("定价币种");z++;
            hssfRow.createCell(z).setCellValue("价格");z++;
            hssfRow.createCell(z).setCellValue("委托表状态");z++;
            int j=0;
            boolean result=false;
            while (rs.next()) {
                result=true;
                hssfRow = hssfSheet.createRow((int)j+1);
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    hssfRow.createCell(i-1).setCellValue(rs.getString(i));
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
                j++;
            }

            if(result){
                String name=userName+" "+coinCode+" 可用账户数据表.xls";

                FileOutputStream fout = null;
                try{
                    //用流将其写到D盘
                    fout = new FileOutputStream(name);
                    excel.write(fout);
                    fout.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println("============================");
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(rs,pstmt,conn);
        }
    }


    static void coldExcel(String coinCode, Long customerId,String userName){
        List<String> list  =new ArrayList<>();
        String sql = "-- cold\n" +
                "select '"+coinCode+" 冻结账户',-- i.mobilePhone,\n" +
                "c.remark '操作类型',c.transactionmoney '交易金额',balancemoney '交易前冻结余额',\n" +
                "CASE WHEN c.`recordtype`=1 \n" +
                "then c.balancemoney+c.transactionmoney \n" +
                "WHEN c.`recordtype`=2 \n" +
                "THEN c.balancemoney-c.transactionmoney   \n" +
                "ELSE 'null' END AS '交易后冻结余额',\n" +
                "c.created '交易时间',c.transactionNum '交易单号',-- c.customerId '客户ID' , \n" +
                "-- recordtype,\n" +
                "c.coinCode '账户流水币种',\n" +
                "CASE WHEN e.type=1 \n" +
                "then '买入' \n" +
                "WHEN e.type=2 \n" +
                "THEN '卖出'   \n" +
                "ELSE '' END '方向'\n" +
                ",\n" +
                "e.coinCode '交易币种',e.fixPriceCoinCode '定价币种',\n" +
                " e.`status` '委托表状态'\n" +
                "-- ,c.customerId ,i.mobilePhone '客户手机号',e.type,e.entrustWay,e.coinCode,e.fixPriceCoinCode,e.surplusEntrustCount,e.*\n" +
                "\n" +
                "from ex_dm_cold_account_record c left join app_person_info i on c.customerId=i.customerId\n" +
                "left join ex_entrust e on c.TRANSACTIONnum = e.entrustNum\n" +
                "where 1=1\n" +
                "  and c.customerId="+customerId+"\n" +
                "-- i.mobilePhone=15396370089\n" +
                "      and  c.coincode = '"+coinCode+"' \n" +
                "-- and balancemoney <0\n" +
                "  -- and c.created >= '2019-03-27' \n" +
                "-- and balancemoney- transactionmoney= -0.0000000001\n" +
                " -- and TRANSACTIONnum like '181025161239001379%'\n" +
                "  -- and (balancemoney- transactionmoney<0 and c.`recordtype`=2  )\n" +
                "-- and balancemoney <0\n" +
                "-- and recordType =1\n" +
                "-- GROUP BY c.customerId \n" +
                "\n" +
                "order by c.id desc -- limit 0,20000\n" +
                "\n" +
                " \n";
        Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            List<Map> coinList =null;
            //新建excel报表
            HSSFWorkbook excel = new HSSFWorkbook();
            //添加一个sheet，
            HSSFSheet hssfSheet = excel.createSheet("数据表");
            //往excel表格创建一行，excel的行号是从0开始的
            HSSFRow hssfRow = hssfSheet.createRow(0);
            //第一行创建第一个单元格 GOT 可用账户	操作类型	交易金额	交易前可用余额	交易后可用余额
            // 交易时间	交易单号	账户流水币种	方向	交易币种	定价币种
            int z=0;
            hssfRow.createCell(z).setCellValue(coinCode+"冻结账户");z++;
            hssfRow.createCell(z).setCellValue("操作类型");z++;
            hssfRow.createCell(z).setCellValue("交易金额");z++;
            hssfRow.createCell(z).setCellValue("交易前冻结余额");z++;
            hssfRow.createCell(z).setCellValue("交易后冻结余额");z++;
            hssfRow.createCell(z).setCellValue("交易时间");z++;
            hssfRow.createCell(z).setCellValue("交易单号");z++;
            hssfRow.createCell(z).setCellValue("账户流水币种");z++;
            hssfRow.createCell(z).setCellValue("方向");z++;
            hssfRow.createCell(z).setCellValue("交易币种");z++;
            hssfRow.createCell(z).setCellValue("定价币种");z++;
            hssfRow.createCell(z).setCellValue("委托表状态");z++;
            int j=0;
            boolean result=false;
            while (rs.next()) {
                result=true;
                hssfRow = hssfSheet.createRow((int)j+1);
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    hssfRow.createCell(i-1).setCellValue(rs.getString(i));
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
                j++;
            }


            if(result){
                String name=userName+" "+coinCode+" 冻结账户数据表.xls";

                FileOutputStream fout = null;
                try{
                    //用流将其写到D盘
                    fout = new FileOutputStream(name);
                    excel.write(fout);
                    fout.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println("============================");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(rs,pstmt,conn);
        }
    }


    static String accountInfo(String mobilePhone,List<String> coinCodesList){

        String sql = "-- ex_digitalmoney_account\n" +
                "\n" +
                "SELECT -- a.modified,p.country,c.phoneState,c.phone,c.googleState,-- ==1\n" +
                "\tconcat(\"id= \",a.id),a.coinCode,p.customerId customerId,concat(p.surname,p.trueName),p.mobilePhone,\n" +
                "\t'-------------',concat(\"hotMoney= \",a.hotMoney),concat(\"coldMoney= \",a.coldMoney)\n" +
                "\t\n" +
                "FROM\n" +
                "\tex_digitalmoney_account a\n" +
                "join app_person_info p on a.customerId=p.customerId\n" +
                "join app_customer c on c.id =p.customerId\n" +
                "-- join (select * from ex_digitalmoney_account b where b.coinCode='CPCT') d on d.customer13452604699Id=a.customerId\n" +
                "WHERE 1=1 \n" +
                "\t          -- and  a.customerId = 1376\n" +
                " \n" +
                "-- and  a.hotMoney<0 \n" +
                "-- ORDER BY a.modified desc \n" +
                "     and  p.mobilePhone ="+mobilePhone+" \n" +
                "\n" +
                "   -- and  p.mobilePhone =15908155681\t\t\t\t \t\t\t\t\n" +
                "    -- and p.email='cpctnpc4@cpct.pro'\n" +
                "-- and concat(p.surname,p.surname) like '%麟%'\n" +
                " \n" +
                " -- and  a.publicKey='0x6db7da10409d11cc19226020cd5498915c767d8b'\n" +
                "\n" +
                "-- 17749995146 \n" +
                "\n" +
                "-- 15396370089 陈文农\n" +
                "-- 15396370988 刘瑞宝\n" +
                "\n" +
                "-- #机构账号\n" +
                "-- app.jigou=cpctnpc2@cpct.pro,cpctnpc1@cpct.pro,cpctnpc3@cpct.pro,cpctnpc4@cpct.pro\n" +
                "\n" +
                "#系统账号\n" +
                "-- app.xitong=15817460827,12@qq.com\n" +
                "\n" +
                "\n" +
                "-- 18307236420 余凡\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "-- a.coldmoney <0 order by modified desc";
        Connection conn = getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String customId = null;
        String trueName = null;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            List<Map> coinList =null;
            int z=0;
            int j=0;
            boolean result=false;
            while (rs.next()) {
                result=true;
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    if(i==2){
                        coinCodesList.add(rs.getString(i));
                    }
                    if(i==3){
                        if(customId==null||"".equals(customId.trim())){
                            customId=rs.getString(i);
                        }

                    }
                    if(i==4){
                        if(trueName==null||"".equals(trueName.trim())){
                            trueName=rs.getString(i);
                        }
                    }
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
                j++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeAll(rs,pstmt,conn);
        }
        return customId+"="+trueName;
    }

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        //String url = "jdbc:mysql://instance-cpct.c6kzigybqduh.ap-southeast-1.rds.amazonaws.com:3306/change3.1.1";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //释放资源
    public static void closeAll(ResultSet rs, Statement st, Connection con){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }finally{
                if(st!=null){
                    try {
                        st.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }finally{
                        if(con!=null){
                            try {
                                con.close();
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        if(st!=null){
            try {
                st.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }finally{
                if(con!=null){
                    try {
                        con.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}
