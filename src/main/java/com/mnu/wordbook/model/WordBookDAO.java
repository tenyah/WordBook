package com.mnu.wordbook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mnu.wordbook.util.DBManager;

public class WordBookDAO {
	private WordBookDAO() {}

	private static WordBookDAO instance = new WordBookDAO();

	public static WordBookDAO getInstnace() {
		return instance;
	}

	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	// 새 단어장 등록 메소드
	public int newWordbook(String name) {
		int result = 0;
		String sql = "INSERT INTO wordbook (name) VALUES (?)";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		return result;
	}
	
	//목록조회 리스트
	public List<WordBookListDTO> getWordbookList() {
	    List<WordBookListDTO> list = new ArrayList<>();

	    String sql = "SELECT wb.id, wb.name, COUNT(ww.word_id) AS cnt "
	               + "FROM wordbook wb "
	               + "LEFT JOIN word_wordbook ww ON wb.id = ww.wordbook_id "
	               + "GROUP BY wb.id, wb.name "
	               + "ORDER BY wb.id"; 

	    try {
	    	conn = DBManager.getConnection();
			pstmt= conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();  

	        while (rs.next()) {
	            WordBookListDTO lDTO = new WordBookListDTO();
	            lDTO.setId(rs.getInt("id"));
	            lDTO.setName(rs.getString("name"));
	            lDTO.setWordcount(rs.getInt("cnt"));
	            list.add(lDTO);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);  
	    }

	    return list;
	}
	
	//단어등록 메소드
	public int wordInsert(int wordId, int bookId) {
	    int result = 0;
	    String sql = "INSERT INTO word_wordbook (wordbook_id, word_id) VALUES (?, ?)";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, bookId);
	        pstmt.setInt(2, wordId);
	        result = pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt);
	    }

	    return result;
	}
	
	//단어장 안에 단어 조회
	public WordBookListDTO wordlist(int wbId) {
	    WordBookListDTO wDTO = null;

	    String sql = "SELECT wb.id, wb.name, COUNT(ww.word_id) AS cnt "
	               + "FROM wordbook wb "
	               + "LEFT JOIN word_wordbook ww ON wb.id = ww.wordbook_id "
	               + "WHERE wb.id = ? "
	               + "GROUP BY wb.id, wb.name";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, wbId);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            wDTO = new WordBookListDTO();
	            wDTO.setId(rs.getInt("id"));
	            wDTO.setName(rs.getString("name"));
	            wDTO.setWordcount(rs.getInt("cnt"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }

	    return wDTO;
	}
	
	public int wordInsert(int wordId, int bookId, String wordType) {
        int result = 0;
        String sql = "INSERT INTO word_wordbook (wordbook_id, word_id, word_type) VALUES (?, ?, ?)";

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, wordId);
            pstmt.setString(3, wordType);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(conn, pstmt);
        }

        return result;
    }
	
	//단어 삭제 메소드
	public int deleteWords(int wbId, String[] wordItems) {
	    int result = 0;
	    String deleteLinkSql = "DELETE FROM word_wordbook WHERE wordbook_id = ? AND word_id = ? AND word_type = ?";
	    String checkSql = "SELECT COUNT(*) FROM word_wordbook WHERE word_id = ? AND word_type = 'C'";
	    String deleteCustomSql = "DELETE FROM custom_word WHERE id = ?";

	    try {
	        conn = DBManager.getConnection();

	        for (String item : wordItems) {
	            String[] parts = item.split(":");
	            int wordId = Integer.parseInt(parts[0]);
	            String wordType = parts[1];

	            // 1. 연결 끊기 (기존 로직)
	            pstmt = conn.prepareStatement(deleteLinkSql);
	            pstmt.setInt(1, wbId);
	            pstmt.setInt(2, wordId);
	            pstmt.setString(3, wordType);
	            result = result + pstmt.executeUpdate();

	            // 2. custom_word인 경우, 다른 단어장에도 없으면 원본까지 삭제
	            if ("C".equals(wordType)) {
	                pstmt = conn.prepareStatement(checkSql);
	                pstmt.setInt(1, wordId);
	                rs = pstmt.executeQuery();

	                if (rs.next() && rs.getInt(1) == 0) {   // 어디에도 안 남아있으면 삭제
	                    pstmt = conn.prepareStatement(deleteCustomSql);
	                    pstmt.setInt(1, wordId);
	                    pstmt.executeUpdate();
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }

	    return result;
	}
	
	// 단어장 이름 변경
	public int updateWordbookName(int wbId, String newName) {
	    int result = 0;
	    String sql = "UPDATE wordbook SET name = ? WHERE id = ?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, newName);
	        pstmt.setInt(2, wbId);
	        result = pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt);
	    }

	    return result;
	}

	// 단어장 삭제 (연결된 데이터까지 정리)
	public int deleteWordbook(int wbId) {
	    int result = 0;

	    String selectCustomIdsSql = "SELECT word_id FROM word_wordbook WHERE wordbook_id = ? AND word_type = 'C'";
	    String deleteLinksSql = "DELETE FROM word_wordbook WHERE wordbook_id = ?";
	    String checkOtherSql = "SELECT COUNT(*) FROM word_wordbook WHERE word_id = ? AND word_type = 'C'";
	    String deleteCustomSql = "DELETE FROM custom_word WHERE id = ?";
	    String deleteWordbookSql = "DELETE FROM wordbook WHERE id = ?";

	    List<Integer> customWordIds = new ArrayList<>();

	    try {
	        conn = DBManager.getConnection();

	        pstmt = conn.prepareStatement(selectCustomIdsSql);
	        pstmt.setInt(1, wbId);
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            customWordIds.add(rs.getInt("word_id"));
	        }

	        pstmt = conn.prepareStatement(deleteLinksSql);
	        pstmt.setInt(1, wbId);
	        pstmt.executeUpdate();


	        for (int wordId : customWordIds) {
	            pstmt = conn.prepareStatement(checkOtherSql);
	            pstmt.setInt(1, wordId);
	            rs = pstmt.executeQuery();

	            if (rs.next() && rs.getInt(1) == 0) {
	                pstmt = conn.prepareStatement(deleteCustomSql);
	                pstmt.setInt(1, wordId);
	                pstmt.executeUpdate();
	            }
	        }

	        pstmt = conn.prepareStatement(deleteWordbookSql);
	        pstmt.setInt(1, wbId);
	        result = pstmt.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }

	    return result;
	}

}
