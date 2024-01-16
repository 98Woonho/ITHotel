package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.BoardResponseDto;
import com.whl.hotelService.domain.common.dto.BoardWriteRequestDto;
import com.whl.hotelService.domain.common.entity.Board;
import com.whl.hotelService.domain.common.repository.BoardRepository;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// DTO -> Entity변환 작업은 (Entity class)컨트롤러가 서비스로 데이터를 넘겨줄 땐 DTO 객체를 반환해야함 반대로 서비스에서 컨트롤러에 데이터를 넘겨줄 땐 DTO 객체를 반환
// Entity -> DTO변환 작업은 (DTO class)서비스가 레파지토리로 데이터를 넘겨줄 땐 Entity 객체를 반환해야함 반대로 레파지토리에서 서비스로 데이터를 넘겨줄때도 Enitiy 객체를 반환
@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Long saveBoard(BoardWriteRequestDto boardWriteRequestDto, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("유저 아이디가 존재하지 않습니다."));
        Board result = Board.builder()
                .title(boardWriteRequestDto.getTitle())
                .content(boardWriteRequestDto.getContent())
                .user(user)
                .build();
        boardRepository.save(result);

        return result.getId();
    }
    @Override
    public BoardResponseDto boardDetail(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        BoardResponseDto result = BoardResponseDto.builder()
                .board(board)
                .build();

        return result;
    }

    @Override
    public List<BoardResponseDto> boardList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponseDto> boardDtos = new ArrayList<>();

        for (Board board : boards) {
            BoardResponseDto result = BoardResponseDto.builder()
                    .board(board)
                    .build();
            boardDtos.add(result);
        }

        return boardDtos;
    }

    @Override
    public Long boardUpdate(Long id, BoardWriteRequestDto boardWriteRequestDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardWriteRequestDto.getTitle(), boardWriteRequestDto.getContent());
        boardRepository.save(board);

        return board.getId();
    }
    @Override
    public void boardRemove(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        boardRepository.delete(board);
    }
}






//    @Transactional(rollbackFor = Exception.class)
//    public Board save(BoardDto boardDto) throws Exception{
//        // Contoller에서 받은 Dto객체를 엔티티로 변환해서 boardRepositoydp 저장해야함
//        Board board = Board.DtoToEntity(boardDto);
//        if (boardDto.getBoardWriter() == null ||
//                boardDto.getBoardPassword() == null ||
//                boardDto.getBoardTitle() == null ||
//                boardDto.getBoardContents() == null){
//            System.out.println("SAVE!!!!!!!!!!!!!!null!!!!!!!!!!!");
//            return null;
//        }else {
//            System.out.println("boardDto.getBoardWriter() : " + boardDto.getBoardWriter() );
//            System.out.println("boardDto.getBoardPassword() : " + boardDto.getBoardPassword() );
//            System.out.println("boardDto.getBoardTitle() : " + boardDto.getBoardTitle() );
//            System.out.println("boardDto.getBoardContents() : " + boardDto.getBoardContents() );
//            System.out.println("SAVE!!!!!!!!!!!!!!!!!!!!!!");
//            return boardRepository.save(board);
//        }
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public Page<BoardDto> findAll(Pageable pageable) throws Exception {
//        // boardRepositoy로부터 Entity로 넘어온 객체를 Dto로 담아서 컨트롤러에 전달해야함.
//        Page<Board> boardEntities = boardRepository.findAll(pageable);
//        Page<BoardDto> boardDtos = boardEntities.map(board -> new BoardDto(board.getId(),
//                board.getBoardWriter(),
//                board.getBoardTitle(),
//                board.getBoardHits(),
//                board.getCreatedTime())); //Entity를 DTO로 변환 시키는 작업
//        return boardDtos;
//    }
//    @Transactional(rollbackFor = Exception.class)
//    public Page<BoardDto> findByBoardTitleContainingOrBoardWriterContaining(String boardTitle, String boardWriter, Pageable pageable) throws Exception {
//        Page<Board> boardEntities = boardRepository.findByBoardTitleContainingOrBoardWriterContaining(boardTitle, boardWriter,  pageable);
//        Page<BoardDto> boardDtos = boardEntities.map(board -> new BoardDto(board.getId(),
//                board.getBoardWriter(),
//                board.getBoardTitle(),
//                board.getBoardHits(),
//                board.getCreatedTime()));
//        return boardDtos;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void updateHits(Long id) throws Exception {
//        boardRepository.updateHits(id);
//    }
//    @Transactional(rollbackFor = Exception.class)
//    public BoardDto findById(Long id) throws Exception{
//        Optional<Board> optionalBoardEntity = boardRepository.findById(id); //Id 로 조회를 해서
//        if (optionalBoardEntity.isPresent()){ //만약 있으면
//            Board board = optionalBoardEntity.get(); //반환하고
//
//            BoardDto boardDto = BoardDto.EntityToDto(board); //엔티티를 Dto로 변환해서 컨트롤러에 리턴
//            return boardDto;
//        } else {
//            return null;
//        }
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public BoardDto update(BoardDto boardDto) throws Exception{
//        Board board = Board.DtoToUpdateEntity(boardDto);
//        boardRepository.save(board);
//        return findById(boardDto.getId());
//    }
//    @Transactional(rollbackFor = Exception.class)
//    public void delete(Long id) throws Exception{
//        boardRepository.deleteById(id);
//    }
//
//}

