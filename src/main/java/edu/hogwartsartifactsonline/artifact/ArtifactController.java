package edu.hogwartsartifactsonline.artifact;


import edu.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import edu.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import edu.hogwartsartifactsonline.system.Result;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
       Artifact foundArtifact = this.artifactService.findById(artifactId);
        //ArtifactDto artifactDto = this.artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", this.artifactToArtifactDtoConverter.convert(foundArtifact));
    }

    @GetMapping
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        List<ArtifactDto> ArtifactDtos = foundArtifacts.stream().map(foundArtifact -> this.artifactToArtifactDtoConverter.convert(foundArtifact)).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", ArtifactDtos);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        //Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        //this.artifactService.save(newArtifact);

        Artifact savedArtifact = this.artifactService.save(this.artifactDtoToArtifactConverter.convert(artifactDto));
        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto){
        Artifact updatedArtifact = this.artifactService.update(artifactId, Objects.requireNonNull(this.artifactDtoToArtifactConverter.convert(artifactDto)));
        ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }
    
}
