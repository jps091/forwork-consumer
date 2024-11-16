package forwork.forwork_consumer.api;

import forwork.forwork_consumer.api.service.ResumeQuantityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
    private final ResumeQuantityService resumeQuantityService;

    @RequestMapping(method = RequestMethod.POST, value = "/pes")
    public ResponseEntity<String> pessimistic(
    ){
        resumeQuantityService.increaseSalesQuantityPessimistic(List.of(1L));
        return new ResponseEntity<>("confirm", HttpStatus.OK);
    }
}
