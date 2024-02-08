package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.dto.CommentResponseDto;
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
import java.util.stream.Collectors;

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

    public Long saveBoard(BoardWriteRequestDto boardWriteRequestDto, String id) {
        // 유저 아이디로 유저 찾기
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));

        // 연관된 유저와 함께 새로운 Board 엔터티 생성
        Board result = Board.builder()
                .title(boardWriteRequestDto.getTitle())
                .content(boardWriteRequestDto.getContent())
                .user(user)
                .build();

        // Board 엔터티를 데이터베이스에 저장
        boardRepository.save(result);

        return result.getId();
    }


    public BoardResponseDto boardDetail(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = board.getUser();
        BoardResponseDto result = BoardResponseDto.entityToDto(board, user);

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

    public Page<BoardResponseDto> boardList(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }


    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardWriteRequestDto.getTitle(), boardWriteRequestDto.getContent());
        boardRepository.save(board);

        return board.getId();
    }

    public void boardRemove(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        boardRepository.delete(board);
    }


    public Page<BoardResponseDto> searchingBoardList(String keyword, String type, Pageable pageable) {
        Page<Board> boards = boardRepository.searchBoards(keyword, type, pageable);
        List<BoardResponseDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            User user = board.getUser();
            BoardResponseDto result = BoardResponseDto.entityToDto(board, user);
            boardDtos.add(result);
        }

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    public List<String> searchHotelname(){
        List<Hotel> hotels = hotelRepository.findAll();
        List<String> hotelNames = new ArrayList<>();
        for(Hotel hotel : hotels){
            HotelDto hotelDto = HotelDto.entityToDto(hotel);
            hotelNames.add(hotelDto.getHotelName());
        }
        return hotelNames;
    }
}
