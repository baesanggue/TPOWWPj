package com.dongyang.TPOWW.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dongyang.TPOWW.common.JdbcConnectUtil;





public class UserDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	public int registUser(UserDTO udto) {
		int dbok = 0;
		int un = 0;
		String sql = "insert into user (id, pw, uname, age, gender, region)"
				+ "values (?,?,?,?,?,?)";
		con =JdbcConnectUtil.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, udto.getId());
			pstmt.setString(2, udto.getPw());
			pstmt.setString(3, udto.getUname());
			pstmt.setInt(4, udto.getAge());
			pstmt.setString(5, udto.getGender());
			pstmt.setString(6, udto.getRegion());
			
			dbok = pstmt.executeUpdate();
			
			if (dbok == 1) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					un = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.Close(con, pstmt);
		}
		return un;
	}
	public UserDTO userLogin(UserDTO udto) {
		con = JdbcConnectUtil.getConnection();
		String sql = "select un, id, pw, uname, age, gender, region, role "+" from user where id = ? and pw = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, udto.getId());
			pstmt.setString(2, udto.getPw());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				udto = new UserDTO();
				udto.setUn(rs.getInt("un"));
				udto.setId(rs.getString("id"));
				udto.setPw(rs.getString("pw"));
				udto.setUname(rs.getString("uname"));
				udto.setAge(rs.getInt("age"));
				udto.setGender(rs.getString("gender"));
				udto.setRegion(rs.getString("region"));
				udto.setRole(rs.getString("role"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.Close(con, pstmt);
		}
		return udto;
	}
	public int updateUser(UserDTO udto) {
	    int result = 0;

	    try {
	        con = JdbcConnectUtil.getConnection();
	        String sql = "UPDATE user SET uname = ?, age = ?, gender = ?, region = ? " +
	                     "WHERE un = ?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, udto.getUname());
	        pstmt.setInt(2, udto.getAge());
	        pstmt.setString(3, udto.getGender());
	        pstmt.setString(4, udto.getRegion());
	        pstmt.setInt(5, udto.getUn());

	        result = pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcConnectUtil.Close(con, pstmt);
	    }

	    return result;
	}
	public int deleteUser(int un) {
		int result = 0;
		
		con = JdbcConnectUtil.getConnection();
		String sql = "delete from user where un = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, un);
			result = pstmt.executeUpdate();
			System.out.println("delete결과 = "+result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.Close(con, pstmt);
		}
		
		return result;
	}
	public List<UserWithPrefDTO> findAllusersWithPref(){
		List<UserWithPrefDTO> list = new ArrayList<>();
		con = JdbcConnectUtil.getConnection();
		String sql =
			    "SELECT u.un, u.id, u.uname, u.age, u.gender, u.region, u.role, " +
			    "       p.brand, p.color, p.pcolor " +  // ← 뒤에 공백 꼭!
			    "FROM user u " +
			    "LEFT JOIN user_pref p ON u.un = p.un " +
			    "ORDER BY u.un";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
		        UserWithPrefDTO dto = new UserWithPrefDTO();
		        dto.setUn(rs.getInt("un"));
		        dto.setId(rs.getString("id"));
		        dto.setUname(rs.getString("uname"));
		        dto.setAge(rs.getInt("age"));
		        dto.setGender(rs.getString("gender"));
		        dto.setRegion(rs.getString("region"));
		        dto.setRole(rs.getString("role"));

		        dto.setBrand(rs.getString("brand"));
		        dto.setColor(rs.getString("color"));
		        dto.setPcolor(rs.getString("pcolor"));

		        list.add(dto);
		     }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.Close(con, pstmt);
		}
		return list;
		
	}
	public UserDTO getUserByUn(int un) {
		UserDTO udto = null;
		
		con = JdbcConnectUtil.getConnection();
		String sql = "SELECT un, id, pw, uname, age, gender, region, role " +
                "FROM user WHERE un = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, un);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				udto = new UserDTO();
				udto.setUn(rs.getInt("un"));
	            udto.setId(rs.getString("id"));
	            udto.setPw(rs.getString("pw"));
	            udto.setUname(rs.getString("uname"));
	            udto.setAge(rs.getInt("age"));
	            udto.setGender(rs.getString("gender"));
	            udto.setRegion(rs.getString("region"));
	            udto.setRole(rs.getString("role"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JdbcConnectUtil.Close(con, pstmt, rs);
		}
		return udto;
	}
	
}
