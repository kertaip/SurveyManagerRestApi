package com.springboot.surveymanagerrestapi.survey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@CrossOrigin(origins="*")
public class SurveyResource {

    private SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping("/surveys/")
    public List<Survey> retrieveAllSurveys(){
        return surveyService.retrieveAllSurveys();
    }
    @RequestMapping("/surveys/{surveyId}")
    public Survey retrieveSpecificSurvey(@PathVariable String surveyId){
        Survey survey = surveyService.retrieveSurveyById(surveyId);
        if(survey==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return survey;
    }

    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveAllQuestions(@PathVariable String surveyId){
        List<Question> questions = surveyService.retrieveAllSurveyQuestions(surveyId);
        if(questions==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return questions;
    }

    @RequestMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveSpecificQuestions(@PathVariable String surveyId, @PathVariable String questionId){
        Question question= surveyService.retrieveQuestionById(surveyId, questionId);
        if(question==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return question;
    }

    @RequestMapping(value="/surveys/{surveyId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId,
                                                       @RequestBody Question question){


        String questionId = surveyService.addNewSurveyQuestion(surveyId, question);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{questionId}").buildAndExpand(questionId).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteSpecificQuestions(@PathVariable String surveyId, @PathVariable String questionId)
    {
        String result = surveyService.deleteSurveyQuestion(surveyId, questionId);
        if(result ==null) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSpecificQuestions(@PathVariable String surveyId,
                                                          @PathVariable String questionId,
                                                          @RequestBody Question question)
    {

        surveyService.updateSurveyQuestion(surveyId, questionId, question);
        return ResponseEntity.noContent().build();
    }
}
