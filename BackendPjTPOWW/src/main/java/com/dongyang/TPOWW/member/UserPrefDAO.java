package com.dongyang.TPOWW.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dongyang.TPOWW.common.JdbcConnectUtil;

public class UserPrefDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public int registUserPref(UserPrefDTO pdto) {
		
		int dbok = 0;
		
		
		
		String sql = "insert into user_pref (un, brand, color, pcolor)" + "values(?,?,?,?)";
		try {
			con = JdbcConnectUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, pdto.getUn());
			pstmt.setString(2, pdto.getBrand());
			pstmt.setString(3, pdto.getColor());
			pstmt.setString(4, pdto.getPcolor());
			
			dbok = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.Close(con, pstmt);
		}
		return dbok;
	}
	
	public UserPrefDTO getUserPref(int un) {
        
        UserPrefDTO pdto = null;

        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "SELECT un, brand, color, pcolor FROM user_pref WHERE un = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, un);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                pdto = new UserPrefDTO();
                pdto.setUn(rs.getInt("un"));
                pdto.setBrand(rs.getString("brand"));
                pdto.setColor(rs.getString("color"));
                pdto.setPcolor(rs.getString("pcolor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt);
        }

        return pdto; // 없으면 null
    }

    public int updateUserPref(UserPrefDTO pdto) {
        int result = 0;

        try {
            con = JdbcConnectUtil.getConnection();
            String sql = "UPDATE user_pref SET brand = ?, color = ?, pcolor = ? WHERE un = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pdto.getBrand());
            pstmt.setString(2, pdto.getColor());
            pstmt.setString(3, pdto.getPcolor());
            pstmt.setInt(4, pdto.getUn());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt);
        }

        return result; // 1이면 성공
    }
}
