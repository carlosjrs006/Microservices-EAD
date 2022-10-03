package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecficationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    private final ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable("moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDto lessonDto){

        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if (moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto,lessonModel);
        lessonModel.setDescription(lessonDto.getDescription());
        lessonModel.setModule(moduleModelOptional.get());
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setVideoUrl(lessonDto.getVideoUrl());
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }


    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteCourse( @PathVariable(value = "moduleId") UUID moduleId,
                                                @PathVariable(value = "lessonId") UUID lessonId){

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,lessonId);

        if (lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this module");
        }

        lessonService.delete(lessonModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted sucessfully.");
    }


    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto){

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,lessonId);

        if (lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this module");
        }

        var lessonModel = lessonModelOptional.get();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setDescription(lessonDto.getDescription());
        lessonModel.setVideoUrl(lessonDto.getVideoUrl());

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable("moduleId") UUID moduleId,
                                                           SpecficationTemplate.LessonSpec spec,
                                                           @PageableDefault(page = 0,size = 10,sort = "lessonId",direction = Sort.Direction.ASC)
                                                                       Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(lessonService.findAllByModules(SpecficationTemplate.lessonModuleId(moduleId).and(spec),pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId,lessonId);

        if (lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this module");
        }

        return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
    }

}
