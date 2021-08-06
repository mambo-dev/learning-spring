package com.example.demo.dao;

import com.example.demo.bean.Smp;
import com.example.demo.util.SmpExcelParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest
class SmpDAOTests {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmpDAO smpDAO;

//    @Disabled
//    @DisplayName("SMP 추가 테스트")
//    @Test
    void testUpsert() {
        Assertions.assertDoesNotThrow(() -> {
            Smp.Type type = Smp.Type.JEJU;
            ObjectMapper objectMapper = new ObjectMapper();
            List<Smp> values = objectMapper.readValue(getSample(), new TypeReference<List<Smp>>() {
            });
            Integer result = smpDAO.upsert(type, values);
            log.info("result: {}", result);
            Assertions.assertTrue(result > 0);
        });
    }

    @Order(2)
    @DisplayName("복수 데이터 + SELECT INTO")
    @Test
    void testUploadExcel() {
        Assertions.assertDoesNotThrow(() -> {
            List<Smp> smpList = getSmpList();
            Integer result = smpDAO.upsert(Smp.Type.JEJU, smpList);
            log.info("result: {}", result);
            Assertions.assertTrue(result > 0);
        });
    }

    @Order(3)
    @DisplayName("복수 데이터 + Foreach")
    @Test
    void testUploadExcelWithForeach() {
        Assertions.assertDoesNotThrow(() -> {
            List<Smp> smpList = getSmpList();
            Integer result = smpDAO.upsertWithForeach(Smp.Type.LAND, smpList);
            log.info("result: {}", result);
            Assertions.assertTrue(result > 0);
        });
    }

    @Order(1)
    @DisplayName("복수 데이터 + 단일 데이터 추가")
    @Test
    void testUploadExcelWithFor() {
        Assertions.assertDoesNotThrow(() -> {
            List<Smp> smpList = getSmpList();
            for(Smp smp : smpList) {
                smp.setType(Smp.Type.LAND);
                Integer result = smpDAO.upsert(smp);
//                log.info("result: {}", result);
                Assertions.assertTrue(result > 0);
            }
        });
    }

    String getSample() {
        return "[{\"datetime\":1609426800000,\"value\":75.25},{\"datetime\":1609430400000,\"value\":75.25},{\"datetime\":1609434000000,\"value\":73.91},{\"datetime\":1609437600000,\"value\":73.91},{\"datetime\":1609441200000,\"value\":75.25},{\"datetime\":1609444800000,\"value\":75.25},{\"datetime\":1609448400000,\"value\":75.25},{\"datetime\":1609452000000,\"value\":74.42},{\"datetime\":1609455600000,\"value\":75.12},{\"datetime\":1609459200000,\"value\":75.12},{\"datetime\":1609462800000,\"value\":75.12},{\"datetime\":1609466400000,\"value\":70.78},{\"datetime\":1609470000000,\"value\":63.31},{\"datetime\":1609473600000,\"value\":64.52},{\"datetime\":1609477200000,\"value\":74.61}]";
    }

    List<Smp> getSmpList() throws IOException {
        String[] filenames = new String[]{"sample/smp_land_2018.xls", "sample/smp_land_2019.xls", "sample/smp_land_2020.xls", "sample/smp_land_2021.xls"};
        List<Smp> smpList = new ArrayList<>();
        for(String filename : filenames) {
            ClassPathResource resource = new ClassPathResource(filename);
            smpList.addAll(SmpExcelParser.parse(resource.getInputStream()));
        }
        return smpList;
    }
}
