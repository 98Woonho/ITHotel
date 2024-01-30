package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.RoomDto;
import com.whl.hotelService.domain.common.entity.Hotel;
import com.whl.hotelService.domain.common.entity.HotelFileInfo;
import com.whl.hotelService.domain.common.entity.Room;
import com.whl.hotelService.domain.common.entity.RoomFileInfo;
import com.whl.hotelService.domain.common.repository.HotelRepository;
import com.whl.hotelService.domain.common.repository.RoomFileInfoRepository;
import com.whl.hotelService.domain.common.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RoomService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomFileInfoRepository roomFileInfoRepository;

    public String confirmKind(String kind, String hotelName) {
        List<String> kindList = roomRepository.findHotelsRoomKind(hotelName);

        for (String existingKind : kindList) {
            if (Objects.equals(existingKind, kind)) {
                return "FAILURE_DUPLICATED_KIND";
            }
        }
        return "SUCCESS";
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addRoom(RoomDto roomDto) throws IOException {
        Hotel hotel = hotelRepository.findById(roomDto.getHotelName()).get();

        Room room = Room.builder()
                .checkinTime(roomDto.getCheckinTime())
                .checkoutTime(roomDto.getCheckoutTime())
                .count(roomDto.getCount())
                .kind(roomDto.getKind())
                .standardPeople(roomDto.getStandardPeople())
                .maximumPeople(roomDto.getMaximumPeople())
                .weekdayPrice(roomDto.getWeekdayPrice())
                .fridayPrice(roomDto.getFridayPrice())
                .saturdayPrice(roomDto.getSaturdayPrice())
                .hotel(hotel)
                .build();

        roomRepository.save(room);

        //저장 폴더 지정()
        String uploadPath = "c:\\" + File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();


        // 추가 이미지
        for (String fileName : roomDto.getFileNames()) {
            for (MultipartFile file : roomDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if (!fileobj.exists()) {
                        // DB에 파일경로 저장
                        RoomFileInfo roomFileInfo = new RoomFileInfo();
                        roomFileInfo.setRoom(room);
                        String dirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind() + File.separator;
                        roomFileInfo.setDir(dirPath);
                        roomFileInfo.setFileName(file.getOriginalFilename());
                        roomFileInfoRepository.save(roomFileInfo);
                    }

                    file.transferTo(fileobj);   //저장
                }
            }
        }

        // 대표 이미지
        for (MultipartFile file : roomDto.getMainFiles()) {
            if (Objects.equals(roomDto.getMainFileName(), file.getOriginalFilename())) {

                File fileobj = new File(dir, file.getOriginalFilename()); //파일객체생성

                if (!fileobj.exists()) {
                    RoomFileInfo mainFileInfo = new RoomFileInfo();
                    mainFileInfo.setRoom(room);
                    String mainDirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind() + File.separator;
                    mainFileInfo.setDir(mainDirPath);
                    mainFileInfo.setFileName(file.getOriginalFilename());
                    mainFileInfo.setMainImage(true);
                    roomFileInfoRepository.save(mainFileInfo);
                }

                file.transferTo(fileobj); // 저장
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean modifyRoom(RoomDto roomDto) throws IOException {
        Hotel hotel = hotelRepository.findById(roomDto.getHotelName()).get();

        Room room = Room.builder()
                .id(roomDto.getId())
                .checkinTime(roomDto.getCheckinTime())
                .checkoutTime(roomDto.getCheckoutTime())
                .count(roomDto.getCount())
                .kind(roomDto.getKind())
                .standardPeople(roomDto.getStandardPeople())
                .maximumPeople(roomDto.getMaximumPeople())
                .weekdayPrice(roomDto.getWeekdayPrice())
                .fridayPrice(roomDto.getFridayPrice())
                .saturdayPrice(roomDto.getSaturdayPrice())
                .hotel(hotel)
                .build();

        roomRepository.save(room);

        String uploadPath = "c:\\" + File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind();

        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        String[] existingFileNameArray = roomDto.getExistingFileNames();

        System.out.println(Arrays.toString(existingFileNameArray));

        // 객실 종류가 바뀌지 않았다면 기존 파일 리스트와 수정된 파일 리스트를 비교해서 제거/추가
        if (Objects.equals(roomDto.getExistingKind(), roomDto.getKind())) {

            File[] files = dir.listFiles();

            for (File file : files) {
                if (!Arrays.asList(existingFileNameArray).contains(file.getName())) {
                    roomFileInfoRepository.deleteByFileNameAndRoomId(file.getName(), roomDto.getId());
                    file.delete();
                }
            }
            // 객실 종류가 바꼈으면 기존 파일 리스트와 수정된 파일 리스트를 비교해서 제거/추가 후 객실 파일을 새 dir로 옮김
        } else {

            String existingUploadPath = "c:\\" + File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getExistingKind();

            File existingDir = new File(existingUploadPath);
            if (!existingDir.exists())
                existingDir.mkdirs();

            File[] existingFiles = existingDir.listFiles();

            for (File file : existingFiles) {
                if (!Arrays.asList(existingFileNameArray).contains(file.getName())) {
                    roomFileInfoRepository.deleteByFileNameAndRoomId(file.getName(), roomDto.getId());
                    file.delete();
                }
            }

            for (String existingFileName : existingFileNameArray) {
                Path existingFile = Paths.get(existingUploadPath + File.separator + existingFileName);
                Path newFile = Paths.get(uploadPath + File.separator + existingFileName);

                Files.move(existingFile, newFile, StandardCopyOption.REPLACE_EXISTING);

                RoomFileInfo roomFileInfo = roomFileInfoRepository.findByFileNameAndRoomId(existingFileName, roomDto.getId());

                String newDirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind() + File.separator;

                roomFileInfo.setDir(newDirPath);

                roomFileInfoRepository.save(roomFileInfo);
            }

            for (File file : existingFiles) {
                file.delete();
            }
            existingDir.delete();

        }

        // 추가 이미지
        for (String fileName : roomDto.getFileNames()) {
            for (MultipartFile file : roomDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if (!fileobj.exists()) {
                        // DB에 파일경로 저장
                        RoomFileInfo roomFileInfo = new RoomFileInfo();
                        roomFileInfo.setRoom(room);
                        String dirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind() + File.separator;
                        roomFileInfo.setDir(dirPath);
                        roomFileInfo.setFileName(file.getOriginalFilename());
                        roomFileInfoRepository.save(roomFileInfo);
                    }

                    file.transferTo(fileobj);   //저장
                }
            }
        }

        // 대표 이미지
        if (roomDto.getMainFiles() != null) {
            for (MultipartFile file : roomDto.getMainFiles()) {
                if (Objects.equals(roomDto.getMainFileName(), file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename()); //파일객체생성

                    if (!fileobj.exists()) {
                        RoomFileInfo mainFileInfo = new RoomFileInfo();
                        mainFileInfo.setRoom(room);
                        String mainDirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelName() + File.separator + roomDto.getKind() + File.separator;
                        mainFileInfo.setDir(mainDirPath);
                        mainFileInfo.setFileName(file.getOriginalFilename());
                        mainFileInfo.setMainImage(true);
                        roomFileInfoRepository.save(mainFileInfo);
                    }

                    file.transferTo(fileobj); // 저장
                }
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(String hotelName, String kind) {
        roomRepository.deleteByHotelHotelNameAndKind(hotelName, kind);

        String uploadPath = "c:\\" + File.separator + "roomimage" + File.separator + hotelName + File.separator + kind;

        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        File[] files = dir.listFiles();

        for(File file : files) {
            file.delete();
        }

        dir.delete();
    }
}
