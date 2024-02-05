package com.whl.hotelService.domain.common.service;

import com.whl.hotelService.domain.common.dto.HotelDto;
import com.whl.hotelService.domain.common.dto.PaymentDto;
import com.whl.hotelService.domain.common.dto.ReservationDto;
import com.whl.hotelService.domain.common.entity.*;
import com.whl.hotelService.domain.common.repository.*;
import com.whl.hotelService.domain.user.entity.User;
import com.whl.hotelService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

import static java.awt.geom.Path2D.contains;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelFileInfoRepository hotelFileInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> getDistinctRegion() {
        return hotelRepository.findDistinctRegion();
    }

    public List<String> getHotelName() {
        return hotelRepository.findAllHotelName();
    }

    public List<HotelFileInfo> getHotelMainFileInfoList(boolean isMainImage) {
        return hotelFileInfoRepository.findByIsMainImage(isMainImage);
    }

    public String confirmHotelName(String hotelName) {
        List<String> hotelNameList = hotelRepository.findAllHotelName();

        for (String existingHotelName : hotelNameList) {
            if (Objects.equals(existingHotelName, hotelName)) {
                return "FAILURE_DUPLICATED_HOTEL_NAME";
            }
        }

        return "SUCCESS";
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addHotel(HotelDto hotelDto) throws IOException {
        Hotel hotel = Hotel.builder()
                .hotelName(hotelDto.getHotelName())
                .region(hotelDto.getRegion())
                .addr1(hotelDto.getAddr1())
                .addr2(hotelDto.getAddr2())
                .zipcode(hotelDto.getZipcode())
                .contactInfo(hotelDto.getContactInfo())
                .hotelDetails(hotelDto.getHotelDetails())
                .build();

        hotelRepository.save(hotel);

        //저장 폴더 지정()
        String uploadPath = "c:\\" + File.separator + "hotelimage" + File.separator + hotelDto.getHotelName();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        // 추가 이미지
        for (String fileName : hotelDto.getFileNames()) {
            for (MultipartFile file : hotelDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if (!fileobj.exists()) {
                        // DB에 파일경로 저장
                        HotelFileInfo hotelFileInfo = new HotelFileInfo();
                        hotelFileInfo.setHotel(hotel);
                        String dirPath = File.separator + "hotelimage" + File.separator + hotelDto.getHotelName() + File.separator;
                        hotelFileInfo.setDir(dirPath);
                        hotelFileInfo.setFileName(file.getOriginalFilename());
                        hotelFileInfoRepository.save(hotelFileInfo);
                    }
                    file.transferTo(fileobj);   //저장
                }
            }
        }

        // 대표 이미지
        for (MultipartFile file : hotelDto.getMainFiles()) {
            if (Objects.equals(hotelDto.getMainFileName(), file.getOriginalFilename())) {

                File fileobj = new File(dir, file.getOriginalFilename()); //파일객체생성

                if (!fileobj.exists()) {
                    HotelFileInfo mainFileInfo = new HotelFileInfo();
                    mainFileInfo.setHotel(hotel);
                    String mainDirPath = File.separator + "hotelimage" + File.separator + hotelDto.getHotelName() + File.separator;
                    mainFileInfo.setDir(mainDirPath);
                    mainFileInfo.setFileName(file.getOriginalFilename());
                    mainFileInfo.setMainImage(true);
                    hotelFileInfoRepository.save(mainFileInfo);
                }

                file.transferTo(fileobj); // 저장
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean modifyHotel(HotelDto hotelDto) throws IOException {
        Hotel hotel = Hotel.builder()
                .hotelName(hotelDto.getHotelName())
                .region(hotelDto.getRegion())
                .addr1(hotelDto.getAddr1())
                .addr2(hotelDto.getAddr2())
                .zipcode(hotelDto.getZipcode())
                .contactInfo(hotelDto.getContactInfo())
                .hotelDetails(hotelDto.getHotelDetails())
                .build();

        hotelRepository.save(hotel);

        String uploadPath = "c:\\" + File.separator + "hotelimage" + File.separator + hotelDto.getHotelName();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        // 기존 파일 삭제
        File[] files = dir.listFiles();

        String[] existingFileNameArray = hotelDto.getExistingFileNames();

        for (File file : files) {
            if (!Arrays.asList(existingFileNameArray).contains(file.getName())) {
                hotelFileInfoRepository.deleteByFileNameAndHotelHotelName(file.getName(), hotelDto.getHotelName());
                file.delete();
            }
        }

        // 추가 이미지
        for (String fileName : hotelDto.getFileNames()) {
            for (MultipartFile file : hotelDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {
                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if (!fileobj.exists()) {
                        // DB에 파일경로 저장
                        HotelFileInfo hotelFileInfo = new HotelFileInfo();
                        hotelFileInfo.setHotel(hotel);
                        String dirPath = File.separator + "hotelimage" + File.separator + hotelDto.getHotelName() + File.separator;
                        hotelFileInfo.setDir(dirPath);
                        hotelFileInfo.setFileName(file.getOriginalFilename());
                        hotelFileInfoRepository.save(hotelFileInfo);
                    }
                    file.transferTo(fileobj);   //저장
                }
            }
        }

        // 대표 이미지
        if (hotelDto.getMainFiles() != null) {
            for (MultipartFile file : hotelDto.getMainFiles()) {
                if (Objects.equals(hotelDto.getMainFileName(), file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename()); //파일객체생성

                    if (!fileobj.exists()) {
                        HotelFileInfo mainFileInfo = new HotelFileInfo();
                        mainFileInfo.setHotel(hotel);
                        String mainDirPath = File.separator + "hotelimage" + File.separator + hotelDto.getHotelName() + File.separator;
                        mainFileInfo.setDir(mainDirPath);
                        mainFileInfo.setFileName(file.getOriginalFilename());
                        mainFileInfo.setMainImage(true);
                        hotelFileInfoRepository.save(mainFileInfo);
                    }

                    file.transferTo(fileobj); // 저장
                }
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteHotel(String hotelName) {
        hotelRepository.deleteById(hotelName);
        roomRepository.deleteByHotelHotelName(hotelName);

        String hotelUploadPath = "c:\\" + File.separator + "hotelimage" + File.separator + hotelName;
        File hotelDir = new File(hotelUploadPath);
        if (!hotelDir.exists())
            hotelDir.mkdirs();

        File[] hotelFiles = hotelDir.listFiles();

        for(File file : hotelFiles) {
            file.delete();
        }

        hotelDir.delete();

        String roomUploadPath = "c:\\" + File.separator + "roomimage" + File.separator + hotelName;

        Path path = Paths.get(roomUploadPath);

        try {
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Ignore if the file doesn't exist
                    if (exc instanceof NoSuchFileException) {
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}