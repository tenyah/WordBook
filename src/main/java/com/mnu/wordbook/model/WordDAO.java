package com.mnu.wordbook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mnu.wordbook.util.DBManager;



public class WordDAO {
	private WordDAO() {}
	private static WordDAO instnace = new WordDAO();
	public static WordDAO getInstnace() {
		return instnace;
	}
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	//검색 메소드
	public List<WordDTO> wordSearch(String keyword) {
        List<WordDTO> list = new ArrayList<>();

        String sql = "SELECT id, word, huri, mean, kanji, kormean, korsound "
                   + "FROM word "
                   + "WHERE word LIKE ? OR huri LIKE ? OR mean LIKE ? OR kanji LIKE ? "
                   + "ORDER BY id";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            String likeKeyword = "%" + keyword + "%";
            pstmt.setString(1, likeKeyword);
            pstmt.setString(2, likeKeyword);
            pstmt.setString(3, likeKeyword);
            pstmt.setString(4, likeKeyword);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                WordDTO wDTO = new WordDTO();
                wDTO.setId(rs.getInt("id"));
                wDTO.setWord(rs.getString("word"));
                wDTO.setHuri(rs.getString("huri"));
                wDTO.setMean(rs.getString("mean"));
                wDTO.setKanji(rs.getString("kanji"));
                wDTO.setKormean(rs.getString("kormean"));
                wDTO.setKorsound(rs.getString("korsound"));
                list.add(wDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(conn, pstmt, rs);
        }

        return list;
    }
	
	public List<WordDTO> wordBookData(int wbId) {
	    List<WordDTO> list = new ArrayList<>();

	    String sql = "SELECT w.id, w.word, w.huri, w.mean, w.kanji, w.kormean, w.korsound, 'W' AS word_type "
	               + "FROM word w "
	               + "JOIN word_wordbook ww ON w.id = ww.word_id "
	               + "WHERE ww.wordbook_id = ? AND ww.word_type = 'W' "
	               + "UNION ALL "
	               + "SELECT c.id, c.word, c.huri, c.mean, c.kanji, c.kormean, c.korsound, 'C' AS word_type "
	               + "FROM custom_word c "
	               + "JOIN word_wordbook ww ON c.id = ww.word_id "
	               + "WHERE ww.wordbook_id = ? AND ww.word_type = 'C' "
	               + "ORDER BY id";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, wbId);
	        pstmt.setInt(2, wbId);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            WordDTO wDTO = new WordDTO();
	            wDTO.setId(rs.getInt("id"));
	            wDTO.setWord(rs.getString("word"));
	            wDTO.setHuri(rs.getString("huri"));
	            wDTO.setMean(rs.getString("mean"));
	            wDTO.setKanji(rs.getString("kanji"));
	            wDTO.setKormean(rs.getString("kormean"));
	            wDTO.setKorsound(rs.getString("korsound"));
	            wDTO.setWordType(rs.getString("word_type"));
	            list.add(wDTO);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }

	    return list;
	}
	
	 public int insertCustomWord(WordDTO wDTO) {
	        int newId = 0;
	        String insertSql = "INSERT INTO custom_word (word, huri, mean, kanji, kormean, korsound) "
	                          + "VALUES (?, ?, ?, ?, ?, ?)";

	        try {
	            conn = DBManager.getConnection();
	            pstmt = conn.prepareStatement(insertSql, new String[]{"id"});
	            pstmt.setString(1, wDTO.getWord());
	            pstmt.setString(2, wDTO.getHuri());
	            pstmt.setString(3, wDTO.getMean());
	            pstmt.setString(4, wDTO.getKanji());
	            pstmt.setString(5, wDTO.getKormean());
	            pstmt.setString(6, wDTO.getKorsound());
	            pstmt.executeUpdate();

	            rs = pstmt.getGeneratedKeys();
	            if (rs.next()) {
	                newId = rs.getInt(1);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            DBManager.close(conn, pstmt, rs);
	        }

	        return newId;
	    }
	
	 
	
}
