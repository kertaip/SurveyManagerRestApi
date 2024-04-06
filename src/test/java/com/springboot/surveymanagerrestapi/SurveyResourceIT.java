package com.springboot.surveymanagerrestapi;


import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {


    @Autowired
    private TestRestTemplate template;

    String str= """
            {
                "id": "Question1",
                "description": "Most Popular Cloud Platform Today",
                "options": [
                    "AWS",
                    "Azure",
                    "Google Cloud",
                    "Oracle Cloud"
                ],
                "correctAnswer": "AWS"
            }
            """;

    private static String SPECIFIC_QUESTION_URL="/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTION_URL="/surveys/Survey1/questions";
    private static String GENERIC_SURVEY_URL="/surveys/";
    private static  String SPECIFIC_SURVEY_URL = "/surveys/Survey1";


    @Test
    void retrieveSpecificQuestions_basicScenario() throws JSONException {

        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();

        //admin+password--> YWRtaW46cGFzc3dvcmQ=

        HttpEntity<String> httpEntity= new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);
        //ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);

        String expectedResponse=
                """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }
    @Test
    void retrieveAllQuestions_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();

        //admin+password--> YWRtaW46cGFzc3dvcmQ=

        HttpEntity<String> httpEntity= new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);


        //ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_QUESTION_URL, String.class);

        String expectedResponse=
                """
                    [
                        {
                            "id": "Question1"           
                        },
                        {
                            "id": "Question2"
                        },
                        {
                            "id": "Question3"
                        } 
                    ]
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    @Test
    void retrieveAllSurveys_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();

        //admin+password--> YWRtaW46cGFzc3dvcmQ=

        HttpEntity<String> httpEntity= new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange(GENERIC_SURVEY_URL, HttpMethod.GET, httpEntity, String.class);
        //ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_SURVEY_URL, String.class);

        String expectedResponse= """
                [
                    {
                    "id": "Survey1"
                    }
                ]
                
                """;
        System.out.println(responseEntity.getBody());

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }

    @Test
    void retrieveSpecificSurvey_basicScenario() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();

        //admin+password--> YWRtaW46cGFzc3dvcmQ=

        HttpEntity<String> httpEntity= new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_SURVEY_URL, HttpMethod.GET, httpEntity, String.class);
        //ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_SURVEY_URL, String.class);

        String expectedResponse=
                """
                {
                "title": "My Favorite Survey"}
               """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);

    }
    //http://localhost:8080/surveys/Survey1/questions
    //Post
    //Content-Type: application/json





    @Test
    void addNewSurveyQuestion_basicScenario(){
        String body= """
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
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();

        //admin+password--> YWRtaW46cGFzc3dvcmQ=

        HttpEntity<String> httpEntity= new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTION_URL, HttpMethod.POST, httpEntity, String.class);

        System.out.println(responseEntity.getHeaders());
        //[Location:"http://localhost:57214/surveys/Survey1/questions/508172674", Content-Length:"0", Date:"Wed, 19 Oct 2022 19:14:38 GMT", Keep-Alive:"timeout=60", Connection:"keep-alive"]
        //201
        //Location http://localhost:8080/surveys/Survey1/questions/598187508
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue( locationHeader.contains("surveys/Survey1/questions"));

        //DELETE
        //locationHeader
        ResponseEntity<String> responseEntityDelete = template.exchange(locationHeader, HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());

        //template.delete(locationHeader);
    }

    private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic "+performBasicAuthEncoding("admin", "password"));
        return headers;
    }

    String performBasicAuthEncoding(String user, String password){
        String combined= user+":"+password;
        //Base64 encoding => Bytes
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        //Bytes => String

        return new String(encodedBytes);
    }
}
