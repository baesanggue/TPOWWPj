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

    // 히스토리 저장 (reasonSummary 추가)
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

            // reasonSummary를 aiRecommend 앞에 추가하여 저장
            String fullRecommend = "";
            if (dto.getReasonSummary() != null && !dto.getReasonSummary().isEmpty()) {
                fullRecommend = "[추천 이유]\n" + dto.getReasonSummary() + "\n\n[추천 내용]\n" + dto.getAiRecommend();
            } else {
                fullRecommend = dto.getAiRecommend();
            }
            pstmt.setString(6, fullRecommend);

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt);
        }
        return result;
    }

    // 특정 유저의 히스토리 조회 (reasonSummary 파싱)
    public List<TpoHistoryDTO> getHistoryByUn(int un) {
        List<TpoHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tpo_history WHERE un = ? ORDER BY h_id DESC";

        try {
            con = JdbcConnectUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, un);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                TpoHistoryDTO dto = parseHistoryFromResultSet(rs);
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt, rs);
        }
        return list;
    }

    // ID로 히스토리 조회 (상세 보기용)
    public TpoHistoryDTO getHistoryById(int hId) {
        TpoHistoryDTO dto = null;
        String sql = "SELECT * FROM tpo_history WHERE h_id = ?";

        try {
            con = JdbcConnectUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, hId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = parseHistoryFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcConnectUtil.Close(con, pstmt, rs);
        }
        return dto;
    }

    // ResultSet에서 DTO 파싱 (중복 코드 제거)
    private TpoHistoryDTO parseHistoryFromResultSet(ResultSet rs) throws SQLException {
        TpoHistoryDTO dto = new TpoHistoryDTO();
        dto.sethId(rs.getInt("h_id"));
        dto.setUn(rs.getInt("un"));
        dto.setRequestDate(rs.getString("request_date"));
        dto.setRequestTime(rs.getString("request_time"));
        dto.setWhat(rs.getString("what"));
        dto.setWeatherSummary(rs.getString("weather_summary"));

        // ai_recommend에서 reasonSummary 파싱
        String aiRecommend = rs.getString("ai_recommend");
        if (aiRecommend != null && aiRecommend.contains("[추천 이유]")) {
            int reasonStart = aiRecommend.indexOf("[추천 이유]") + 8;
            int reasonEnd = aiRecommend.indexOf("[추천 내용]");
            if (reasonEnd != -1) {
                String reason = aiRecommend.substring(reasonStart, reasonEnd).trim();
                String content = aiRecommend.substring(reasonEnd + 8).trim();
                dto.setReasonSummary(reason);
                dto.setAiRecommend(content);
            } else {
                dto.setAiRecommend(aiRecommend);
            }
        } else {
            dto.setAiRecommend(aiRecommend);
        }

        dto.setCreatedAt(rs.getTimestamp("created_at"));
        return dto;
    }
}
