package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardDto;
import com.whl.hotelService.domain.common.dto.BoardFileDto;
import com.whl.hotelService.domain.common.dto.CommentDto;
import com.whl.hotelService.domain.common.dto.HotelDto;
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
import java.util.*;
import java.util.stream.Collectors;

// DTO -> Entity변환 작업은 (Entity class)컨트롤러가 서비스로 데이터를 넘겨줄 땐 DTO 객체를 반환해야함 반대로 서비스에서 컨트롤러에 데이터를 넘겨줄 땐 DTO 객체를 반환
// Entity -> DTO변환 작업은 (DTO class)서비스가 레파지토리로 데이터를 넘겨줄 땐 Entity 객체를 반환해야함 반대로 레파지토리에서 서비스로 데이터를 넘겨줄때도 Enitiy 객체를 반환
@Service
public class BoardService {
    @Autowired
    private QuestionBoardRepository questionBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private NoticeBoardRepsoitory noticeBoardRepsoitory;
    @Autowired
    private NoticeBoardFileInfoRepository noticeBoardFileInfoRepository;
    @Autowired
    private InquiryBoardRepository inquiryBoardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NoticeImageRepository noticeImageRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long saveQuestionBoard(BoardDto boardDto, String id) {
        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        QuestionBoard result = QuestionBoard.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .user(user)
                .build();
        // QuestionBoard 엔터티를 데이터베이스에 저장
        questionBoardRepository.save(result);

        return result.getId();
    }


    @Transactional(rollbackFor = Exception.class)
    public BoardDto questionBoardDetail(Long id) {
        QuestionBoard questionBoard = questionBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = questionBoard.getUser();
        BoardDto result = BoardDto.builder()
                .id(questionBoard.getId())
                .title(questionBoard.getTitle())
                .content(questionBoard.getContent())
                .username(user.getName())
                .email(user.getEmail())
                .userid(user.getUserid())
                .createdTime(questionBoard.getCreatedTime())
                .updatedTime(questionBoard.getUpdatedTime())
                .build();
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public NoticeBoardFileInfo noticeBoardFileDetail(Long id) {
        NoticeBoardFileInfo noticeBoardFileInfo = noticeBoardFileInfoRepository.findByNoticeBoardId(id);

        return noticeBoardFileInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public NoticeBoard noticeBoard(Long id) {
        NoticeBoard noticeBoard = noticeBoardRepsoitory.findById(id).orElseThrow();
        return noticeBoard;
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<BoardDto> questionBoardList(Pageable pageable) {
        Page<QuestionBoard> boards = questionBoardRepository.findAll(pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (QuestionBoard questionBoard : boards) {
            User user = questionBoard.getUser();
            BoardDto result = BoardDto.builder()
                    .id(questionBoard.getId())
                    .title(questionBoard.getTitle())
                    .content(questionBoard.getContent())
                    .username(user.getName())
                    .email(user.getEmail())
                    .userid(user.getUserid())
                    .createdTime(questionBoard.getCreatedTime())
                    .updatedTime(questionBoard.getUpdatedTime())
                    .build();
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<BoardDto> searchingQuestionBoardList(String keyword, String type, Pageable pageable) {
        Page<QuestionBoard> boards = questionBoardRepository.searchBoards(keyword, type, pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (QuestionBoard questionBoard : boards) {
            User user = questionBoard.getUser();
            BoardDto result = BoardDto.builder()
                    .id(questionBoard.getId())
                    .title(questionBoard.getTitle())
                    .content(questionBoard.getContent())
                    .username(user.getName())
                    .email(user.getEmail())
                    .userid(user.getUserid())
                    .createdTime(questionBoard.getCreatedTime())
                    .updatedTime(questionBoard.getUpdatedTime())
                    .build();
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }


    @Transactional(rollbackFor = Exception.class)
    public Long questionBoardUpdate(Long id, BoardDto boardDto) {
        QuestionBoard questionBoard = questionBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        questionBoard.update(boardDto.getTitle(), boardDto.getContent());
        questionBoardRepository.save(questionBoard);

        return questionBoard.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void questionBoardRemove(Long id) {
        QuestionBoard questionBoard = questionBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        questionBoardRepository.delete(questionBoard);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> searchHotelname(){
        List<Hotel> hotels = hotelRepository.findAll();
        List<String> hotelNames = new ArrayList<>();
        for(Hotel hotel : hotels){
            HotelDto hotelDto = HotelDto.builder()
                    .hotelName(hotel.getHotelName())
                    .region(hotel.getRegion())
                    .addr1(hotel.getAddr1())
                    .addr2(hotel.getAddr2())
                    .contactInfo(hotel.getContactInfo())
                    .build();
            hotelNames.add(hotelDto.getHotelName());
        }
        return hotelNames;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveInquiryBoard(BoardDto boardDto, String userid) {

        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        InquiryBoard inquiryboard = InquiryBoard.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .user(user)
                .hotelname(boardDto.getHotelname())
                .relation((boardDto.getRelation()))
                .build();
        // Board 엔터티를 데이터베이스에 저장
        InquiryBoard save = inquiryBoardRepository.save(inquiryboard);
    }

    @Transactional(rollbackFor = Exception.class)
    public BoardDto inquiryBoardDetail(Long id) {
        InquiryBoard board = inquiryBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
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

    @Transactional(rollbackFor = Exception.class)
    public Page<BoardDto> inquiryBoardList(Pageable pageable) {
        Page<InquiryBoard> boards = inquiryBoardRepository.findAll(pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (InquiryBoard board : boards) {
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

    @Transactional(rollbackFor = Exception.class)
    public Page<CommentDto> commentList(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            User user = comment.getUser();
            InquiryBoard board = comment.getInquiryboard();
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

    @Transactional(rollbackFor = Exception.class)
    public void inquiryBoardRemove(Long id) {
        InquiryBoard board = inquiryBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        inquiryBoardRepository.delete(board);
    }


    @Transactional(rollbackFor = Exception.class)
    public Page<BoardDto> searchingInquiryBoardList(String keyword, String type, Pageable pageable) {
        Page<InquiryBoard> boards = inquiryBoardRepository.searchBoards(keyword, type, pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (InquiryBoard board : boards) {
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

    @Transactional(rollbackFor = Exception.class)
    public Page<BoardDto> userBoardList(Pageable pageable, Authentication authentication) {
        Page<InquiryBoard> inquiryBoards = inquiryBoardRepository.findByUserUserid(pageable, authentication.getName());
        List<BoardDto> boardDtos = new ArrayList<>();
        for (InquiryBoard board : inquiryBoards) {
            User user = board.getUser();
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
        return new PageImpl<>(boardDtos, pageable, inquiryBoards.getTotalElements());
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
//            NoticeBoard notice = noticeBoardRepsoitory.findById(id).get(); // 해결해야함,,,,
//            NoticeImage noticeImage = NoticeImage.builder()
//                    .noticeBoard(notice)
//                    .build();
//            noticeImageRepository.save(noticeImage);
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
            Long id = noticeBoardRepsoitory.save(noticeBoard).getId();// long타입으로 저장하는 이유 : 나중에 findById 를 했을 때
            NoticeBoard notice = noticeBoardRepsoitory.findById(id).get();
            NoticeBoardFileInfo noticeBoardFileInfo = NoticeBoardFileInfo.builder()
                    .originalFileName(originalFilename)
                    .storedFileName(storedFileName)
                    .noticeBoard(notice)
                    .build();
            noticeBoardFileInfoRepository.save(noticeBoardFileInfo);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void modifyNotice(Long id, BoardFileDto boardFileDto, String userid) throws IOException {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        if (boardFileDto.getFile() == null) {
            boardFileDto.setFileAttached(0);
            NoticeBoard noticeBoard = NoticeBoard.builder()
                    .id(id)
                    .user(user)
                    .title(boardFileDto.getTitle())
                    .content(boardFileDto.getContent())
                    .fileAttached(boardFileDto.getFileAttached())
                    .build();
            noticeBoardRepsoitory.save(noticeBoard);
        }else {
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
                    .id(id)
                    .user(user)
                    .title(boardFileDto.getTitle())
                    .content(boardFileDto.getContent())
                    .fileAttached(boardFileDto.getFileAttached())
                    .build();
            noticeBoardRepsoitory.save(noticeBoard);// long타입으로 저장하는 이유 : 나중에 findById 를 했을 때
            NoticeBoardFileInfo noticeBoardId = noticeBoardFileInfoRepository.findByNoticeBoardId(id);
            if (noticeBoardId != null){
                NoticeBoard notice = noticeBoardRepsoitory.findById(id).get();
                NoticeBoardFileInfo noticeBoardFileInfo = NoticeBoardFileInfo.builder()
                        .id(noticeBoardId.getId())
                        .originalFileName(originalFilename)
                        .storedFileName(storedFileName)
                        .noticeBoard(notice)
                        .build();
                noticeBoardFileInfoRepository.save(noticeBoardFileInfo);
            } else {
                NoticeBoard notice = noticeBoardRepsoitory.findById(id).get();
                NoticeBoardFileInfo noticeBoardFileInfo = NoticeBoardFileInfo.builder()
                        .originalFileName(originalFilename)
                        .storedFileName(storedFileName)
                        .noticeBoard(notice)
                        .build();
                noticeBoardFileInfoRepository.save(noticeBoardFileInfo);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String uploadImage(NoticeBoardImage noticeBoardImage){

        noticeImageRepository.save(noticeBoardImage);


        return "SUCCESS";
    }

    @Transactional(rollbackFor = Exception.class)
    public NoticeBoardImage getImage(Long id){
        NoticeBoardImage noticeBoardImage = noticeImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다."));
        return noticeBoardImage;
    }



    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public String deleteNotice(Long id){
        noticeBoardRepsoitory.deleteById(id);

        return "SUCCESS";
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateComment(String content, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comment.update(content);
        commentRepository.save(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long writeComment(Long boardId, String content, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        InquiryBoard board = inquiryBoardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        Comment result = Comment.builder()
                .content(content)
                .inquiryboard(board)
                .user(user)
                .build();
        commentRepository.save(result);

        return result.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CommentDto> commentList(Long id) {
        InquiryBoard inquiryBoard = inquiryBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByInquiryboard(inquiryBoard);

        return comments.stream()
                .map(comment -> CommentDto.entityToDto(comment, comment.getInquiryboard(), comment.getUser()))
                .collect(Collectors.toList());
    }

}
