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

    @Transactional(rollbackFor = Exception.class)
    public boolean addRoom(RoomDto roomDto) throws IOException {
        Hotel hotel = hotelRepository.findById(roomDto.getHotelname()).get();

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
        String uploadPath = "c:\\" + File.separator + "roomimage" + File.separator + roomDto.getHotelname() + File.separator + roomDto.getKind();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();


        // 추가 이미지
        for (String fileName : roomDto.getFileNames()) {
            for (MultipartFile file : roomDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if(!fileobj.exists()) {
                        // DB에 파일경로 저장
                        RoomFileInfo roomFileInfo = new RoomFileInfo();
                        roomFileInfo.setRoom(room);
                        String dirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelname() + File.separator + roomDto.getKind() + File.separator;
                        roomFileInfo.setDir(dirPath);
                        roomFileInfo.setFilename(file.getOriginalFilename());
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

                if(!fileobj.exists()) {
                    RoomFileInfo mainFileInfo = new RoomFileInfo();
                    mainFileInfo.setRoom(room);
                    String mainDirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelname() + File.separator + roomDto.getKind() + File.separator;
                    mainFileInfo.setDir(mainDirPath);
                    mainFileInfo.setFilename(file.getOriginalFilename());
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
        Hotel hotel = hotelRepository.findById(roomDto.getHotelname()).get();

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

        String uploadPath = "c:\\" + File.separator + "roomimage" + File.separator + roomDto.getHotelname() + File.separator + roomDto.getKind();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        // 기존 파일 삭제
        File[] files = dir.listFiles();

        String[] existingFilenameArray = roomDto.getExistingFileNames();

        System.out.println(Arrays.toString(existingFilenameArray));

        for (File file : files) {
            if (!Arrays.asList(existingFilenameArray).contains(file.getName())) {
                roomFileInfoRepository.deleteByFilenameAndRoomHotelHotelname(file.getName(), roomDto.getHotelname());
                file.delete();
            }
        }

        // 추가 이미지
        for (String fileName : roomDto.getFileNames()) {
            for (MultipartFile file : roomDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if(!fileobj.exists()) {
                        // DB에 파일경로 저장
                        RoomFileInfo roomFileInfo = new RoomFileInfo();
                        roomFileInfo.setRoom(room);
                        String dirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelname() + File.separator + roomDto.getKind() + File.separator;
                        roomFileInfo.setDir(dirPath);
                        roomFileInfo.setFilename(file.getOriginalFilename());
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

                if(!fileobj.exists()) {
                    RoomFileInfo mainFileInfo = new RoomFileInfo();
                    mainFileInfo.setRoom(room);
                    String mainDirPath = File.separator + "roomimage" + File.separator + roomDto.getHotelname() + File.separator + roomDto.getKind() + File.separator;
                    mainFileInfo.setDir(mainDirPath);
                    mainFileInfo.setFilename(file.getOriginalFilename());
                    mainFileInfo.setMainImage(true);
                    roomFileInfoRepository.save(mainFileInfo);
                }

                file.transferTo(fileobj); // 저장
            }
        }

        return true;
    }
}
