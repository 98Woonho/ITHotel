package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
import com.whl.hotelService.domain.common.entity.AdminBoard;
import com.whl.hotelService.domain.common.entity.Comment;
import com.whl.hotelService.domain.common.entity.NoticeBoard;
import com.whl.hotelService.domain.common.entity.NoticeBoardFileInfo;
import com.whl.hotelService.domain.common.repository.AdminBoardRepository;
import com.whl.hotelService.domain.common.repository.CommentRepository;
import com.whl.hotelService.domain.common.repository.NoticeBoardFileInfoRepository;
import com.whl.hotelService.domain.common.repository.NoticeBoardRepsoitory;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminBoardService {

    @Autowired
    private AdminBoardRepository adminBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NoticeBoardRepsoitory noticeBoardRepsoitory;
    @Autowired
    private NoticeBoardFileInfoRepository noticeBoardFileInfoRepository;

    public void saveBoard(BoardWriteRequestDto boardWriteRequestDto, String userid) {

        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        AdminBoard adminBoard = AdminBoard.builder()
                .title(boardWriteRequestDto.getTitle())
                .content(boardWriteRequestDto.getContent())
                .user(user)
                .hotelname(boardWriteRequestDto.getHotelname())
                .relation((boardWriteRequestDto.getRelation()))
                .build();
        // Board 엔터티를 데이터베이스에 저장
        AdminBoard save = adminBoardRepository.save(adminBoard);
    }


    public BoardResponseDto boardDetail(Long id) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = board.getUser();
        BoardResponseDto result = BoardResponseDto.entityToDto(board, user);

        return result;
    }


    public Page<BoardResponseDto> boardList(Pageable pageable) {
        Page<AdminBoard> boards = adminBoardRepository.findAll(pageable);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public Page<CommentResponseDto> commentList(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        List<CommentResponseDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            User user = comment.getUser();
            AdminBoard board = comment.getAdminBoard();
            CommentResponseDto result = CommentResponseDto.entityToDto(comment, board, user);
            commentDtos.add(result);
        }
        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }

    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardWriteRequestDto.getTitle(), boardWriteRequestDto.getContent());
        adminBoardRepository.save(board);

        return board.getId();
    }

    public void boardRemove(Long id) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        adminBoardRepository.delete(board);
    }


    public Page<BoardResponseDto> searchingBoardList(String keyword, String type, Pageable pageable) {
        Page<AdminBoard> boards = adminBoardRepository.searchBoards(keyword, type, pageable);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public Page<BoardResponseDto> userBoardList(Pageable pageable, Authentication authentication) {
        Page<AdminBoard> adminBoards = adminBoardRepository.findByUserUserid(pageable, authentication.getName());
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : adminBoards) {
            User user = board.getUser();
            System.out.println("Authentication" + authentication.getName());
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }
        return new PageImpl<>(boardDtos, pageable, adminBoards.getTotalElements());
    }

    public void fileAttach(BoardWriteRequestDto boardWriteRequestDto) throws IOException {
        if (boardWriteRequestDto.getFile().isEmpty()) {
            boardWriteRequestDto.setFileAttached(0);
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .title(boardWriteRequestDto.getTitle())
                    .content(boardWriteRequestDto.getContent())
                    .fileAttached(boardWriteRequestDto.getFileAttached())
                    .build(); // 파일 첨부 안했을 때
            noticeBoardRepsoitory.save(noticeBoard);
        } else {
            MultipartFile file = boardWriteRequestDto.getFile(); // 파일 객체 생성
            String originalFilename = file.getOriginalFilename(); // 파일의 실제 이름
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 서버에 담길 파일 이름

            String noticeBoardUploadPath = "c:\\" + File.separator + "noticeBoardImage" + File.separator; // 서버에 파일 생성
            File noticeBoardDir = new File(noticeBoardUploadPath);
            if (!noticeBoardDir.exists())
                noticeBoardDir.mkdirs();

            String savePath = noticeBoardUploadPath + storedFileName;
            file.transferTo(new File(savePath)); // 지정된 경로로 파일 저장
            boardWriteRequestDto.setFileAttached(1);
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .title(boardWriteRequestDto.getTitle())
                    .content(boardWriteRequestDto.getContent())
                    .fileAttached(boardWriteRequestDto.getFileAttached())
                    .build();
            Long id = noticeBoardRepsoitory.save(noticeBoard).getId(); // long타입으로 저장하는 이유 : 나중에 findById 를 했을 때 부모의 primaryKey를 전달 받기 위해
            NoticeBoard notice = noticeBoardRepsoitory.findById(id).get();
            NoticeBoardFileInfo noticeBoardFileInfo = NoticeBoardFileInfo.builder()
                    .originalFileName(originalFilename)
                    .storedFileName(storedFileName)
                    .noticeBoard(notice)
                    .build();
            noticeBoardFileInfoRepository.save(noticeBoardFileInfo);
        }
    }
    public List<BoardWriteRequestDto> noticeBoardList(BoardWriteRequestDto boardWriteRequestDto){
        List<NoticeBoard> noticeBoardList = noticeBoardRepsoitory.findAll();
        List<BoardWriteRequestDto> boardWriteRequestDtos = new ArrayList<>();
        for (NoticeBoard board : noticeBoardList) {
            if (boardWriteRequestDto.getFileAttached() == 0){
                boardWriteRequestDto.setFileAttached(0);
            } else {
                boardWriteRequestDto.setFileAttached(1);
            }
            BoardWriteRequestDto result = BoardWriteRequestDto.entityToDto(board);
            boardWriteRequestDtos.add(result);
        }
        return boardWriteRequestDtos;
    }
}
