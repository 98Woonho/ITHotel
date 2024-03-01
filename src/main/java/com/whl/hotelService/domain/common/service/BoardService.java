package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardDto;
import com.whl.hotelService.domain.common.dto.HotelDto;
import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.repository.BoardRepository;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.repository.NoticeBoardFileInfoRepository;
import com.whl.hotelService.domain.common.repository.NoticeBoardRepsoitory;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

// DTO -> Entity변환 작업은 (Entity class)컨트롤러가 서비스로 데이터를 넘겨줄 땐 DTO 객체를 반환해야함 반대로 서비스에서 컨트롤러에 데이터를 넘겨줄 땐 DTO 객체를 반환
// Entity -> DTO변환 작업은 (DTO class)서비스가 레파지토리로 데이터를 넘겨줄 땐 Entity 객체를 반환해야함 반대로 레파지토리에서 서비스로 데이터를 넘겨줄때도 Enitiy 객체를 반환
@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private NoticeBoardRepsoitory noticeBoardRepsoitory;
    @Autowired
    private NoticeBoardFileInfoRepository noticeBoardFileInfoRepository;

    public Long saveBoard(BoardDto boardDto, String id) {
        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        Board result = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .user(user)
                .build();
        // Board 엔터티를 데이터베이스에 저장
        boardRepository.save(result);

        return result.getId();
    }


    public BoardDto boardDetail(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
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
                .build();
        return result;
    }

    public NoticeBoardFileInfo noticeBoardFileDetail(Long id) {
        NoticeBoardFileInfo noticeBoardFileInfo = noticeBoardFileInfoRepository.findByNoticeBoardId(id);

        return noticeBoardFileInfo;
    }
    public NoticeBoard noticeBoard(Long id) {
        NoticeBoard noticeBoard = noticeBoardRepsoitory.findById(id).orElseThrow();
        return noticeBoard;
    }

    public Page<BoardDto> boardList(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
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
                    .build();
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }


    public Long boardUpdate(Long id, BoardDto boardDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardDto.getTitle(), boardDto.getContent());
        boardRepository.save(board);

        return board.getId();
    }

    public void boardRemove(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        boardRepository.delete(board);
    }


    public Page<BoardDto> searchingBoardList(String keyword, String type, Pageable pageable) {
        Page<Board> boards = boardRepository.searchBoards(keyword, type, pageable);
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
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
                    .build();
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

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
}
