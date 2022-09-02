package com.example.miniproject2team3.presentation;

import com.example.miniproject2team3.service.Question;
import com.example.miniproject2team3.service.QuizGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class GameController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/quiz")
    public String questions(Model model, HttpSession session) {
        QuizGenerator generator = (QuizGenerator) session.getAttribute("gen");
        if (generator == null) {
            generator = new QuizGenerator();
        }
        Question q = generator.currentQuestion();
        int id = generator.idIncrease();
        model.addAttribute("question", q);
        model.addAttribute("questionid", id);
        session.setAttribute("gen", generator);
        return "gamepage";

    }

    @PostMapping("/answer")
    public String answer(Model model, HttpSession session, @RequestParam int answerNumber) {
        QuizGenerator generator = (QuizGenerator) session.getAttribute("gen");
        Question q = generator.currentQuestion();

        if (q.getCorrectAnswer() == answerNumber) {
            int score = generator.scoreIncrease();
            model.addAttribute("response", "Correct answer! score= " + score);
        } else {
            model.addAttribute("response", "Wrong answer! score= " + generator.getScore()
                    + "\n The correct answer is: " + q.getAnswers()[q.getCorrectAnswer()]);
        }

        if (generator.nextQuestion()) {
            if (generator.getScore() >= 5) {
                model.addAttribute("finalscore", generator.getScore());
                session.invalidate();
                return "endofgamepass";
            } else {
                model.addAttribute("finalscore", generator.getScore());
                session.invalidate();
                return "endofgamefailed";
            }
        }  return "result";
    }
}
