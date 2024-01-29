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
    private HotelFileInfoRepository hotelFileInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> getDistinctRegion() {
        return hotelRepository.findDistinctRegion();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addHotel(HotelDto hotelDto) throws IOException {
        Hotel hotel = Hotel.builder()
                .hotelname(hotelDto.getHotelname())
                .region(hotelDto.getRegion())
                .addr1(hotelDto.getAddr1())
                .addr2(hotelDto.getAddr2())
                .zipcode(hotelDto.getZipcode())
                .contactInfo(hotelDto.getContactInfo())
                .hotelDetails(hotelDto.getHotelDetails())
                .build();

        hotelRepository.save(hotel);

        //저장 폴더 지정()
        String uploadPath = "c:\\" + File.separator + "hotelimage" + File.separator + hotelDto.getHotelname();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        for (String fileName : hotelDto.getFileNames()) {
            for (MultipartFile file : hotelDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if (!fileobj.exists()) {
                        // DB에 파일경로 저장
                        HotelFileInfo hotelFileInfo = new HotelFileInfo();
                        hotelFileInfo.setHotel(hotel);
                        String dirPath = File.separator + "hotelimage" + File.separator + hotelDto.getHotelname() + File.separator;
                        hotelFileInfo.setDir(dirPath);
                        hotelFileInfo.setFilename(file.getOriginalFilename());
                        hotelFileInfoRepository.save(hotelFileInfo);
                    }

                    file.transferTo(fileobj);   //저장
                }
            }
        }
        return true;

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean modifyHotel(HotelDto hotelDto) throws IOException {
        Hotel hotel = Hotel.builder()
                .hotelname(hotelDto.getHotelname())
                .region(hotelDto.getRegion())
                .addr1(hotelDto.getAddr1())
                .addr2(hotelDto.getAddr2())
                .zipcode(hotelDto.getZipcode())
                .contactInfo(hotelDto.getContactInfo())
                .hotelDetails(hotelDto.getHotelDetails())
                .build();

        hotelRepository.save(hotel);

        String uploadPath = "c:\\" + File.separator + "hotelimage" + File.separator + hotelDto.getHotelname();
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        // 기존 파일 삭제
        File[] files = dir.listFiles();

        String[] existingFilenameArray = hotelDto.getExistingFileNames();

        System.out.println(Arrays.toString(existingFilenameArray));

        for (File file : files) {
            if (!Arrays.asList(existingFilenameArray).contains(file.getName())) {
                hotelFileInfoRepository.deleteByFilenameAndHotelHotelname(file.getName(), hotelDto.getHotelname());
                file.delete();
            }
        }

        for (String fileName : hotelDto.getFileNames()) {
            for (MultipartFile file : hotelDto.getFiles()) {
                if (Objects.equals(fileName, file.getOriginalFilename())) {

                    File fileobj = new File(dir, file.getOriginalFilename());    //파일객체생성

                    if (!fileobj.exists()) {
                        // DB에 파일경로 저장
                        HotelFileInfo hotelFileInfo = new HotelFileInfo();
                        hotelFileInfo.setHotel(hotel);
                        String dirPath = File.separator + "hotelimage" + File.separator + hotelDto.getHotelname() + File.separator;
                        hotelFileInfo.setDir(dirPath);
                        hotelFileInfo.setFilename(file.getOriginalFilename());
                        hotelFileInfoRepository.save(hotelFileInfo);
                    }
                    file.transferTo(fileobj);   //저장
                }
            }
        }
        return true;
    }

}
