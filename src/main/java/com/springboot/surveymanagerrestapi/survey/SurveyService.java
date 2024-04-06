package com.springboot.surveymanagerrestapi.survey;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SurveyService {

    private static List<Survey> surveys=new ArrayList<>();

    static{
        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey",
                "Description of the Survey", questions);

        surveys.add(survey);
    }


    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    public Survey retrieveSurveyById(String surveyId) {

        return surveys.stream().filter(survey -> survey.getId().equals(surveyId)).findFirst().orElse(null);
    }

    public List<Question> retrieveAllSurveyQuestions(String surveyId) {
        Survey survey = retrieveSurveyById(surveyId);
        if(survey==null) return null;
        return survey.getQuestions();

    }

    public Question retrieveQuestionById(String surveyId, String questionId) {
        Question question = retrieveAllSurveyQuestions(surveyId).stream()
                .filter(currentQuestion -> currentQuestion.getId().equals(questionId))
                .findFirst()
                .orElse(null);
        return question;
    }

    public String addNewSurveyQuestion(String surveyId, Question question) {
        List<Question> questions= retrieveAllSurveyQuestions(surveyId);
        String randomId = generateRandomId();
        question.setId(randomId);
        questions.add(question);

        return randomId;

    }

    private String generateRandomId() {
        SecureRandom secureRandom= new SecureRandom();
        String randomId = new BigInteger(32, secureRandom).toString();
        return randomId;
    }

    public String deleteSurveyQuestion(String surveyId, String questionId) {
        List<Question> surveyQuestions=retrieveAllSurveyQuestions(surveyId);

        boolean removed = surveyQuestions.removeIf(q -> q.getId().equals(questionId));

        return (!removed ? null : questionId);
    }

    public void updateSurveyQuestion(String surveyId, String questionId, Question question)
    {
        List<Question> surveyQuestions=retrieveAllSurveyQuestions(surveyId);
        surveyQuestions.removeIf(q -> q.getId().equals(questionId));
        surveyQuestions.add(question);
    }
}
