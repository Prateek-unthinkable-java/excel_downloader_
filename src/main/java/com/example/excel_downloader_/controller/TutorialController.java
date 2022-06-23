package com.example.excel_downloader_.controller;

import com.example.excel_downloader_.model.Tutorial;
import com.example.excel_downloader_.service.TutorialService;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class TutorialController {

    static String fileDownloadUri;

    @Autowired
    private TutorialService tutorialService;

//    @GetMapping("/")
//    public ModelAndView method(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return new ModelAndView("/excel/downloadTutorials");
//    }

    @GetMapping("/downloadTutorials")
    public ResponseEntity<Resource> getExcelFile(HttpServletResponse httpServletResponse) throws IOException {
        List<Tutorial> tutorials = tutorialService.getAllTutorials();
        String fileName = "tutorials.xlsx";
        Path path = Paths.get(fileName);
        InputStreamResource file = new InputStreamResource(tutorialService.loadTutorials(tutorials));
        fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileName)
                .toUriString();

        ResponseEntity url = responseUrl();
        System.out.println(url);

        //redirection with path changing reflects in browser
        //httpServletResponse.sendRedirect(String.valueOf(url));
        //httpResponse.addHeader("Link:", String.valueOf(url));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    public static ResponseEntity responseUrl(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(fileDownloadUri+"_"+dtf.format(now));
    }

    @PostMapping("/addTutorial")
    public Tutorial addTutorial(@RequestBody Tutorial tutorial){
        return tutorialService.addTutorial(tutorial);
    }

    @PutMapping("/updateTutorial")
    public Tutorial updateTutorialById(@RequestBody Tutorial tutorial){
        return tutorialService.updateTutorial(tutorial);
    }

    @DeleteMapping("/deleteTutorial/{id}")
    public ResponseEntity<Tutorial> deleteTutorialById(@PathVariable("id") Long id){
        try{
            tutorialService.deleteTutorial(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
