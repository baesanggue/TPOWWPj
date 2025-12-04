package com.dongyang.TPOWW.tpo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dongyang.TPOWW.common.JdbcConnectUtil;

public class TpoHistoryDAO {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // 히스토리 저장
    public int insertHistory(TpoHistoryDTO dto) {
        int result = 0;
        String sql = "INSERT INTO tpo_history (un, request_date, request_time, what, weather_summary, ai_recommend) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            con = JdbcConnectUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, dto.getUn());
            pstmt.setString(2, dto.getRequestDate());
            pstmt.setString(3, dto.getRequestTime());
            pstmt.setString(4, dto.getWhat());
            pstmt.setString(5, dto.getWeatherSummary());
            pstmt.setString(6, dto.getAiRecommend());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt);
        }
        return result;
    }

    // 특정 유저의 히스토리 조회 (최신순)
    public List<TpoHistoryDTO> getHistoryByUn(int un) {
        List<TpoHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tpo_history WHERE un = ? ORDER BY h_id DESC";

        try {
            con = JdbcConnectUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, un);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                TpoHistoryDTO dto = new TpoHistoryDTO();
                dto.sethId(rs.getInt("h_id"));
                dto.setUn(rs.getInt("un"));
                dto.setRequestDate(rs.getString("request_date"));
                dto.setRequestTime(rs.getString("request_time"));
                dto.setWhat(rs.getString("what"));
                dto.setWeatherSummary(rs.getString("weather_summary"));
                dto.setAiRecommend(rs.getString("ai_recommend"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt, rs);
        }
        return list;
    }
}
