package com.springboot.surveymanagerrestapi;
import com.springboot.surveymanagerrestapi.survey.SurveyService;
import com.springboot.surveymanagerrestapi.survey.Question;
import com.springboot.surveymanagerrestapi.survey.SurveyResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.skyscreamer.jsonassert.JSONAssert;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class SurveyResourceTest {

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private MockMvc mockMvc;
    //SurveyService
    //Mock -> surveyService.retrieveQuestionById(surveyId, questionId)

    //Fire a request
    ///surveys/{surveyId}/questions/{questionId}
    //http://localhost:8080/surveys/Survey1/questions/Question1 GET

    private static String SPECIFIC_QUESTION_URL="http://localhost:8080/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL="http://localhost:8080/surveys/Survey1/questions";

    @Test
    void retrieveSpecificQuestions_basicScenario() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);


        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        String expectedResponse= """
                        {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;

        when(surveyService.retrieveQuestionById("Survey1", "Question1")).thenReturn(question1);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        JSONAssert.assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString(), false);


    }

    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception {
        String requestBody= """
            {
              "description": "Your favourite language",
              "options": [
                  "java",
                  "python",
                  "js",
                  "go"
              ],
              "correctAnswer": "Java"
            }
            """;

        when(surveyService.addNewSurveyQuestion(anyString(), any())).thenReturn("SOME_ID");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GENERIC_QUESTION_URL)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .content(requestBody).contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());
        String locationHeader = mvcResult.getResponse().getHeader("Location");
        System.out.println(locationHeader);
        assertTrue(locationHeader.contains("http://localhost:8080/surveys/Survey1/questions/SOME_ID"));
    }
}
