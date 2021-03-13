package com.example.demo.handler;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("파일 업로드 핸들러 테스트")
@ActiveProfiles({"test"})
@SpringBootTest
@AutoConfigureMockMvc
class FileUploadHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("이미지 파일 리소스 업로드")
    @Test
    void testUploadFile() {
        Assertions.assertDoesNotThrow(() -> {
            ClassPathResource resource = new ClassPathResource("mambo-dev.jpg");
            if (!resource.exists()) {
                throw new Exception("파일을 찾을 수 없음");
            }

            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                    .multipart("/files")
                    .file("file", resource.getInputStream().readAllBytes()));

            resultActions
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("STATUS").value(true));
        });
    }
}
