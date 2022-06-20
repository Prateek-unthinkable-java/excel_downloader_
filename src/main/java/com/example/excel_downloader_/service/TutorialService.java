package com.example.excel_downloader_.service;

import com.example.excel_downloader_.helper.ExcelHelper;
import com.example.excel_downloader_.model.Tutorial;
import com.example.excel_downloader_.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    public ByteArrayInputStream loadTutorials(List<Tutorial> tutorials){
        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(tutorials);
        return in;
    }

    public List<Tutorial> getAllTutorials(){
        return tutorialRepository.findAll();
    }

    public Tutorial addTutorial(Tutorial tutorial){
        return tutorialRepository.save(tutorial);
    }

    public Tutorial updateTutorial(Tutorial tutorial){
        Tutorial tutorial1 = tutorialRepository.findById(tutorial.getId()).get();
        tutorial1.setId(tutorial.getId());
        tutorial1.setTitle(tutorial.getTitle());
        tutorial1.setDescription(tutorial.getDescription());
        tutorial1.setPublished(tutorial.getPublished());
        tutorialRepository.save(tutorial1);
        return tutorial;
    }

    public void deleteTutorial(Long id){
        Tutorial tutorial = tutorialRepository.findById(id).get();
        tutorialRepository.delete(tutorial);
    }
}
