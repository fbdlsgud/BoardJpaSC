package com.himedia.sp_server.dao;

import com.himedia.sp_server.dto.Paging;
import com.himedia.sp_server.entity.Board;
import com.himedia.sp_server.entity.Reply;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardDao {

    @Autowired
    private EntityManager em;

    public List<Board> getBoardList( Paging paging ) {
        String sql = "select b from Board b order by b.id desc";
        List<Board> list
                = em.createQuery(sql, Board.class)
                .setFirstResult( paging.getStartNum() -1 )
                .setMaxResults( paging.getDisplayRow() )
                .getResultList();
        return list;
    }

    public void insertBoard(Board board) {
        em.persist(board);
    }

    public void addReadCount(int num) {
        Board updateBoard = em.find(Board.class, num);
        updateBoard.setReadcount( updateBoard.getReadcount() + 1 );
    }

    public Board getBoard(int num) {
        Board board = em.find(Board.class, num);
        return board;
    }

    public List<Reply> getReplyList(int boardnum) {
        String sql = "select r from Reply r where r.boardnum=:bnum  order by r.id desc";
        List<Reply> list
                = em.createQuery(sql, Reply.class)
                .setParameter("bnum", boardnum)
                .getResultList();
        return list;
    }

    public void addReply(Reply reply) {
        em.persist(reply);
    }

    public void deleteReply(int replynum) {
        Reply delReply = em.find(Reply.class, replynum);
        em.remove(delReply);
    }

    public void updateBoard(Board board) {
        Board updateBoard = em.find(Board.class, board.getNum());
        updateBoard.setTitle(board.getTitle());
        updateBoard.setContent(board.getContent());
        updateBoard.setImage(board.getImage());
        updateBoard.setSavefilename(board.getSavefilename());
    }

    public void deleteBoard(int boardnum) {
        Board delBoard = em.find(Board.class, boardnum);
        em.remove(delBoard);
    }

    public int getAllCount() {
        // String sql = "select count(*) from board";
        String sql = "select count(b) from Board b";
        long count = (Long)em.createQuery(sql).getSingleResult();
        return (int)count;
    }
}
