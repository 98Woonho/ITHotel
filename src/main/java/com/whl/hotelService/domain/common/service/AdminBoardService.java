package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardDto;
import com.whl.hotelService.domain.common.dto.BoardFileDto;
import com.whl.hotelService.domain.common.dto.CommentDto;
import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.repository.*;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private NoticeImageRepository noticeImageRepository;

    public void saveBoard(BoardDto boardDto, String userid) {

        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        AdminBoard adminBoard = AdminBoard.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .user(user)
                .hotelname(boardDto.getHotelname())
                .relation((boardDto.getRelation()))
                .build();
        // Board 엔터티를 데이터베이스에 저장
        AdminBoard save = adminBoardRepository.save(adminBoard);
    }


    public BoardDto boardDetail(Long id) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = board.getUser();
        BoardDto result = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .username(user.getName())
                .email(user.getEmail())
                .userid(user.getUserid())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .hotelname(board.getHotelname())
                .relation(board.getRelation())
                .build();
        return result;
    }


    public Page<BoardDto> boardList(Pageable pageable) {
        Page<AdminBoard> boards = adminBoardRepository.findAll(pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardDto result = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .username(user.getName())
                    .email(user.getEmail())
                    .userid(user.getUserid())
                    .createdTime(board.getCreatedTime())
                    .updatedTime(board.getUpdatedTime())
                    .hotelname(board.getHotelname())
                    .relation(board.getRelation())
                    .build();
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public Page<CommentDto> commentList(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            User user = comment.getUser();
            AdminBoard board = comment.getAdminBoard();
            CommentDto result = CommentDto.entityToDto(comment, board, user);
            commentDtos.add(result);
        }
        return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
    }

//    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto) {
//        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
//        board.update(boardWriteRequestDto.getTitle(), boardWriteRequestDto.getContent());
//        adminBoardRepository.save(board);
//
//        return board.getId();
//    }

    public void boardRemove(Long id) {
        AdminBoard board = adminBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        adminBoardRepository.delete(board);
    }


    public Page<BoardDto> searchingBoardList(String keyword, String type, Pageable pageable) {
        Page<AdminBoard> boards = adminBoardRepository.searchBoards(keyword, type, pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : boards) {
            User user = board.getUser();
            BoardDto result = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .username(user.getName())
                    .email(user.getEmail())
                    .userid(user.getUserid())
                    .createdTime(board.getCreatedTime())
                    .updatedTime(board.getUpdatedTime())
                    .hotelname(board.getHotelname())
                    .relation(board.getRelation())
                    .build();
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public Page<BoardDto> userBoardList(Pageable pageable, Authentication authentication) {
        Page<AdminBoard> adminBoards = adminBoardRepository.findByUserUserid(pageable, authentication.getName());
        List<BoardDto> boardDtos = new ArrayList<>();
        for (AdminBoard board : adminBoards) {
            User user = board.getUser();
            System.out.println("Authentication" + authentication.getName());
            BoardDto result = BoardDto.
                    builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .username(user.getName())
                    .email(user.getEmail())
                    .userid(user.getUserid())
                    .createdTime(board.getCreatedTime())
                    .updatedTime(board.getUpdatedTime())
                    .hotelname(board.getHotelname())
                    .relation(board.getRelation())
                    .build();
            boardDtos.add(result);
        }
        return new PageImpl<>(boardDtos, pageable, adminBoards.getTotalElements());
    }
    @Transactional(rollbackFor = Exception.class)
    public void fileAttach(BoardFileDto boardFileDto, String userid) throws IOException {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        if (boardFileDto.getFile() == null) {
            boardFileDto.setFileAttached(0);
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .user(user)
                    .title(boardFileDto.getTitle())
                    .content(boardFileDto.getContent())
                    .fileAttached(boardFileDto.getFileAttached())
                    .build(); // 파일 첨부 안했을 때
            Long id = noticeBoardRepsoitory.save(noticeBoard).getId();
//            List<NoticeImage> noticeBoardId = noticeImageRepository.findByNoticeBoardId(id);
//            for (NoticeImage noticeImage : noticeBoardId) {
//                noticeImageRepository.save(noticeImage);
//            }

        } else {
            MultipartFile file = boardFileDto.getFile(); // 파일 객체 생성
            String originalFilename = file.getOriginalFilename(); // 파일의 실제 이름
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 서버에 담길 파일 이름

            String noticeBoardUploadPath = "c:\\" + File.separator + "noticeBoardImage" + File.separator; // 서버에 파일 생성
            File noticeBoardDir = new File(noticeBoardUploadPath);
            if (!noticeBoardDir.exists())
                noticeBoardDir.mkdirs();

            String savePath = noticeBoardUploadPath + storedFileName;
            file.transferTo(new File(savePath)); // 지정된 경로로 파일 저장
            boardFileDto.setFileAttached(1);
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .user(user)
                    .title(boardFileDto.getTitle())
                    .content(boardFileDto.getContent())
                    .fileAttached(boardFileDto.getFileAttached())
                    .build();
            Long id = noticeBoardRepsoitory.save(noticeBoard).getId();// long타입으로 저장하는 이유 : 나중에 findById 를 했을 때 부모의 primaryKey를 전달 받기 위해
//            List<NoticeImage> noticeBoardId = noticeImageRepository.findByNoticeBoardId(id);
//            for (NoticeImage noticeImage : noticeBoardId) {
//                noticeImageRepository.save(noticeImage);
//            }
            NoticeBoard notice = noticeBoardRepsoitory.findById(id).get();
            NoticeBoardFileInfo noticeBoardFileInfo = NoticeBoardFileInfo.builder()
                    .originalFileName(originalFilename)
                    .storedFileName(storedFileName)
                    .noticeBoard(notice)
                    .build();
            noticeBoardFileInfoRepository.save(noticeBoardFileInfo);
        }
    }
    public NoticeImage getImage(Long id){
        NoticeImage noticeImage = noticeImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다."));
        return noticeImage;
    }
    public String uploadImage(NoticeImage noticeImage){

        noticeImageRepository.save(noticeImage);

        return "SUCCESS";
    }

    public List<BoardFileDto> noticeBoardList(BoardFileDto boardFileDto){
        List<NoticeBoard> noticeBoardList = noticeBoardRepsoitory.findAll();
        List<BoardFileDto> boardDtos = new ArrayList<>();
        for (NoticeBoard board : noticeBoardList) {
            if (boardFileDto.getFileAttached() == null){
                boardFileDto.setFileAttached(0);
            } else {
                boardFileDto.setFileAttached(1);
            }
            BoardFileDto result = BoardFileDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdTime(board.getCreatedTime())
                    .updatedTime(board.getUpdatedTime())
                    .fileAttached(board.getFileAttached())
                    .build();
            boardDtos.add(result);
        }
        return boardDtos;
    }

    public String deleteNotice(Long id){
        noticeBoardRepsoitory.deleteById(id);

        return "SUCCESS";
    }
}
