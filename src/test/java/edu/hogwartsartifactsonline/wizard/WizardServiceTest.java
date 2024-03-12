package edu.hogwartsartifactsonline.wizard;

import edu.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");

        wizards = new ArrayList<>();
        wizards.add(w1);
        wizards.add(w2);
        wizards.add(w3);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //given
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");
        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w));

        //when
        Wizard foundWizard = this.wizardService.findById(1);

        //Then
        assertThat(foundWizard.getId()).isEqualTo(w.getId());
        assertThat(foundWizard.getName()).isEqualTo(w.getName());
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        //given

        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //when
       Throwable thrown = catchThrowable(()->{
           Wizard returnWizard = this.wizardService.findById(1);
       });

        //Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find wizard with Id 1 :(");
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindAllSuccess(){
        //given
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        //when
        List<Wizard> actualWizards = this.wizardService.findAll();

        //Then
        assertThat(actualWizards.size()).isEqualTo(wizards.size());
        verify(this.wizardRepository, times(1)).findAll();
    }
    @Test
    void testSaveSuccess(){
        //Given
        Wizard newWizard = new Wizard();

        newWizard.setName("Hermione Granger");
        given(this.wizardService.save(newWizard)).willReturn(newWizard);
        //When
        Wizard returnWizard = this.wizardRepository.save(newWizard);
        //Then
        assertThat(returnWizard.getName()).isEqualTo(newWizard.getName());
        verify(this.wizardRepository, times(1)).save(newWizard);

    }

    @Test
    void testUpdateWizardSuccess(){
        //Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Albus Dumbledore-update");

        given(wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);


        //When

        Wizard updatedWizard = this.wizardService.update(1 , update);

        //Then

        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Wizard update = new Wizard();
        update.setId(1);
        update.setName("Albus Dumbledore-update");
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        //When

        assertThrows(ObjectNotFoundException.class, () ->{
            wizardService.update(1,update);
        });

        //Then
        verify(wizardRepository, times(1)).findById(1);
    }
    @Test
    void testDeleteSuccess(){
        //Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");
        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1);

        //When
        wizardService.delete(1);
        //Then
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        //Given
        given(wizardRepository.findById(10)).willReturn(Optional.empty());
        //When
        assertThrows(ObjectNotFoundException.class, () ->{
            wizardService.delete(10);
        });
        //Then
        verify(wizardRepository, times(1)).findById(10);
    }
}