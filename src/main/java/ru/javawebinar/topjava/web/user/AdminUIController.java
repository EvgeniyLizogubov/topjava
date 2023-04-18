package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.getErrorMsg;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {

//    @Autowired
//    ExceptionInfoHandler exceptionInfoHandler;
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ErrorInfo duplicateEmailException(HttpServletRequest req, DataIntegrityViolationException e) {
//        return exceptionInfoHandler.validationError(req, e);
//    }

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@Valid UserTo userTo, BindingResult result, HttpServletRequest req) {
        if (result.hasErrors()) {
            throw new IllegalRequestDataException(getErrorMsg(result));
        } else {
//            try {
                if (userTo.isNew()) {
                    super.create(userTo);
                } else {
                    super.update(userTo, userTo.id());
                }
//            } catch (DataIntegrityViolationException e) {
//                exceptionInfoHandler.validationError(req, e);
//            }
        }
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}
