package com.unmatched.controller;

import com.unmatched.common.exception.BaseRootException;
import com.unmatched.pojo.User;
import com.unmatched.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/userLogin")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    /**
    * Description:（很重要一点需要注意，Errors参数要紧 跟在带有@Valid注解的参数后面，
     * @Valid注解所标注的就是要检验的参数。）
    * @date: 2019/12/15
    */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String register(MultipartFile multipartFile, @Valid User user, Errors errors){
        if(multipartFile!=null){
            String originalFilename = multipartFile.getOriginalFilename();
            System.out.println("上传的文件名："+originalFilename);
        }
        if(errors.hasErrors()){
            String errorMessage="";
            List<ObjectError> allErrors = errors.getAllErrors();
            for (ObjectError objectError:allErrors){
                errorMessage=errorMessage.valueOf(objectError.getDefaultMessage());
            }
            return errorMessage;
        }
        userService.insert(user);
        return user.toString();
    }

    @RequestMapping("/testException")
    public void testException(){
        throw new BaseRootException("出错了！");
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public String selectById(@PathVariable int id){
        User user=userService.selectByPrimaryKey(id);
        return user.toString();
    }
}
