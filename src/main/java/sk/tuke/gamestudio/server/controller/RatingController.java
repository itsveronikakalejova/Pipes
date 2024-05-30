package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.RatingService;

import java.util.Date;

@Controller
@RequestMapping("/pipes")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class RatingController {

    @Autowired
    private final RatingService ratingService;
    @Autowired
    private UserController userController;

    public RatingController(RatingService ratingService, UserController userController) {
        this.ratingService = ratingService;
        this.userController = userController;
        String playerName = getPlayerName();
        System.out.println(playerName);
    }

    @PostMapping("/rate")
    public String rateGame(@RequestParam("rating") int ratingNum) {
        Rating rating = new Rating(getPlayerName(), "pipes", ratingNum, new Date());
        ratingService.setRating(rating);
        return "redirect:/pipes/winner";
    }

    @GetMapping("/averageRating")
    @ResponseBody
    public int getAverageRating() {
        return ratingService.getAverageRating("pipes");
    }

    private String getPlayerName() {
        if (userController.getLoggedUser() == null) {
            return "Anonymous";
        }
        return userController.getLoggedUser().getLogin();
    }


}
