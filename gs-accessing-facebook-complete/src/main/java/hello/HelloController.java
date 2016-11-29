package hello;

import java.util.List;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.EducationExperience;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }
        String [] fields = { "id", "email", "education", "first_name", "last_name", "languages" };
        User userProfile = facebook.fetchObject("me", User.class, fields);
        
       // List<EducationExperience> educationExperience = facebook.userOperations().getUserProfile().getEducation();        
        List<EducationExperience> educationExperience2 = userProfile.getEducation();
        model.addAttribute("education", educationExperience2);		
        		
        dynamic friends = fbApp.Query("SELECT uid, name, pic_square, work_history,
        		 education_history, current_location, profile_url, email FROM user WHERE uid  = me()");
        
        model.addAttribute("facebookProfile", userProfile);
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("feed", feed);
        return "hello";
    }

}
