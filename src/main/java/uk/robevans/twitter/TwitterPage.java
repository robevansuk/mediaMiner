package uk.robevans.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TwitterPage  {
    @Autowired
    ApplicationContext ctx;

    @GetMapping("tweets")
    public ModelAndView showTweetsPage(){

        ModelAndView modelAndView = new ModelAndView();

        List<String> beans = Arrays.stream(ctx.getBeanDefinitionNames()).collect(Collectors.toList());

        modelAndView.addObject("beans", beans);

        return modelAndView;

    }

}
